from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.edge.service import Service
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys
from webdriver_manager.microsoft import EdgeChromiumDriverManager
import time
import csv

def setup_driver():
    service = Service(EdgeChromiumDriverManager().install())
    options = webdriver.EdgeOptions()
    options.add_argument('--start-maximized')
    return webdriver.Edge(service=service, options=options)

def wait_and_find_element(driver, by, value):
    return WebDriverWait(driver, 20).until(EC.visibility_of_element_located((by, value)))

def extract_logement_data(driver):
    # Attendre que les éléments de logement soient présents
    WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.CLASS_NAME, "fr-card"))
    )

    # Extraire les données de chaque logement
    logements = driver.find_elements(By.CLASS_NAME, "fr-card")
    logement_data = []

    for logement in logements:
        try:
            # Extraire le titre
            titre_element = logement.find_element(By.CLASS_NAME, "fr-card__title")
            titre = titre_element.text
            
            # Extraire la description
            desc_element = logement.find_element(By.CLASS_NAME, "fr-card__desc")
            description = desc_element.text
            
            # Extraire la superficie
            superficie_element = logement.find_element(By.XPATH, ".//p[contains(@class, 'fr-card__detail') and contains(text(), 'm²')]")
            superficie = superficie_element.text
            
            # Créer un dictionnaire pour les données du logement
            logement_info = {
                "titre": titre,
                "description": description,
                "superficie": superficie,
            }
            print("hello world", logement_info)
            logement_data.append(logement_info)
        except Exception as e:
            print(f"Erreur lors de l'extraction des données d'un logement : {e}")

    return logement_data


def search_logements(driver, ville, prix_max):
    driver.get("https://trouverunlogement.lescrous.fr/")
    
    # Saisie de la ville
    ville_input = wait_and_find_element(driver, By.ID, "PlaceAutocompletearia-autocomplete-1-input")
    ville_input.clear()
    ville_input.send_keys(ville)
    time.sleep(1)  # Attente pour la suggestion
    ville_input.send_keys(Keys.RETURN)
    
    # Saisie du prix maximum
    prix_input = wait_and_find_element(driver, By.ID, "SearchFormPrice")
    prix_input.clear()
    prix_input.send_keys(str(prix_max))
    
    # Clic sur le bouton de recherche
    rechercher_button = wait_and_find_element(driver, By.XPATH, "//button[contains(text(), 'Lancer une recherche')]")
    rechercher_button.click()

    # Appeler extract_logement_data pour récupérer les résultats
    logement_data = extract_logement_data(driver)
    time.sleep(20)
    return logement_data  # Retourner les données extraites

def save_to_csv(data, filename):
    with open(filename, 'w', newline='', encoding='utf-8') as file:
        writer = csv.DictWriter(file, fieldnames=["titre", "prix", "adresse"])
        writer.writeheader()
        writer.writerows(data)

def main():
    driver = setup_driver()
    try:
        search_logements(driver, "Clermont-Ferrand", 600)
        time.sleep(5)  # Attente pour le chargement complet des résultats
        logements_data = extract_logement_data(driver)
        # save_to_csv(logements_data, "logements_paris.csv")
        # print(f"Données extraites et sauvegardées dans logements_paris.csv")
    except Exception as e:
        print(f"Une erreur s'est produite : {e}")
    finally:
        driver.quit()

if __name__ == "__main__":
    main()