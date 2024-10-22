from json import dumps
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.edge.service import Service
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.microsoft import EdgeChromiumDriverManager
from kafka import KafkaProducer
import time
import pandas as pd
import six
import sys
import re

if sys.version_info >= (3, 12, 0):
    sys.modules['kafka.vendor.six.moves'] = six.moves


def setup_driver():
    service = Service(EdgeChromiumDriverManager().install())
    options = webdriver.EdgeOptions()
    options.add_argument('--start-maximized')
    options.add_argument('--disable-extensions')
    options.add_argument('--disable-gpu')
    options.add_argument('--no-sandbox')
    options.add_argument('--ignore-certificate-errors')
    options.add_argument('--disable-popup-blocking')
    options.add_experimental_option('excludeSwitches', ['enable-logging'])
    return webdriver.Edge(service=service, options=options)


def wait_and_find_element(driver, by, value):
    return WebDriverWait(driver, 20).until(EC.visibility_of_element_located((by, value)))

def extract_zip_code(description):
    match = re.search(r'\b\d{5}\b', description)
    return match.group() if match else None

def extract_logement_data(driver):
    print("Extracting housing data:")

    WebDriverWait(driver, 20).until(
        EC.presence_of_all_elements_located((By.CLASS_NAME, "fr-card"))
    )

    logements = driver.find_elements(By.CLASS_NAME, "fr-card")
    logement_data = []

    i = 0
    for logement in logements:
        try:
            titre_element = logement.find_element(By.CLASS_NAME, "fr-card__title")
            titre = titre_element.text

            desc_element = logement.find_element(By.CLASS_NAME, "fr-card__desc")
            description = desc_element.text
            code_zip = extract_zip_code(description)
            i += 1
            logement_info = {
                "id": i,
                "titre": titre,
                "description": description,
                "codeZip": code_zip,
            }
            print("Housing found:", logement_info)
            logement_data.append(logement_info)
        except Exception as e:
            print(f"Error extracting housing data: {e}")

    return logement_data


def search_logements(driver, ville, prix_max):
    driver.get("https://trouverunlogement.lescrous.fr/")

    ville_input = wait_and_find_element(driver, By.ID, "PlaceAutocompletearia-autocomplete-1-input")
    ville_input.clear()

    if ville:
        ville_input.send_keys(ville)
        time.sleep(1)

        WebDriverWait(driver, 20).until(
            EC.presence_of_element_located((By.ID, "PlaceAutocompletearia-autocomplete-1-list"))
        )

        # Cliquer sur la première suggestion si une ville est fournie
        first_suggestion = driver.find_element(By.XPATH, "(//li[contains(@class, 'PlaceAutocomplete__option')])[1]")
        first_suggestion.click()

    prix_input = wait_and_find_element(driver, By.ID, "SearchFormPrice")
    prix_input.clear()
    prix_input.send_keys(str(prix_max))

    rechercher_button = wait_and_find_element(driver, By.XPATH, "//button[contains(text(), 'Lancer une recherche')]")
    rechercher_button.click()

    WebDriverWait(driver, 20).until(
        EC.presence_of_all_elements_located((By.CLASS_NAME, "fr-card"))
    )

    time.sleep(10)

    logement_data = extract_logement_data(driver)

    return logement_data


def save_to_csv(data, filename):
    if data:
        df = pd.DataFrame(data)
        df.to_csv(filename, index=False, encoding='utf-8')
        print(f"Data saved to {filename}")
    else:
        print("No data to save.")


def send_to_kafka(data):
    producer = KafkaProducer(
        bootstrap_servers=['localhost:9092'],
        value_serializer=lambda x: dumps(x).encode('utf-8')
    )
    for logement in data:
        try:
            producer.send(
                'housing_topic',
                value=logement,
                headers=[('__TypeId__', b'isima.crousnotifier.zzz.models.Logement')]
            )
        except Exception as e:
            print(f"Failed to send message: {e}")

    producer.flush()
    print("Data sent to Kafka topic 'housing_topic'")


def main():
    driver = setup_driver()
    try:
        print("Starting housing search...")
        logements_data = search_logements(driver, "", 0)
        save_to_csv(logements_data, "logements.csv")
        send_to_kafka(logements_data)
    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        driver.quit()


if __name__ == "__main__":
    main()