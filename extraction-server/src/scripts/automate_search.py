from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.edge.service import Service
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.microsoft import EdgeChromiumDriverManager
import time
import pandas as pd  # Pour la gestion des fichiers Excel

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

def extract_logement_data(driver):
    print("entrée ici :")

    # Attendre que les éléments de logement soient présents
    WebDriverWait(driver, 20).until(
        EC.presence_of_all_elements_located((By.CLASS_NAME, "fr-card"))
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
            
    
            # Créer un dictionnaire pour les données du logement
            logement_info = {
                "titre": titre,
                "description": description,
            }
            print("Logement trouvé :", logement_info)
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

    # Attendre que la liste des suggestions soit visible
    WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.ID, "PlaceAutocompletearia-autocomplete-1-list"))
    )

    # Sélectionner la première suggestion
    first_suggestion = driver.find_element(By.XPATH, "(//li[contains(@class, 'PlaceAutocomplete__option')])[1]")
    first_suggestion.click()  # Cliquez sur la première suggestion

    # Saisie du prix maximum
    prix_input = wait_and_find_element(driver, By.ID, "SearchFormPrice")
    prix_input.clear()
    prix_input.send_keys(str(prix_max))
    
    # Clic sur le bouton de recherche
    rechercher_button = wait_and_find_element(driver, By.XPATH, "//button[contains(text(), 'Lancer une recherche')]")
    rechercher_button.click()

    # Attendre que les résultats soient chargés avant d'extraire les données
    WebDriverWait(driver, 20).until(
        EC.presence_of_all_elements_located((By.CLASS_NAME, "fr-card"))
    )

    time.sleep(10)

    # Appeler extract_logement_data pour récupérer les résultats
    logement_data = extract_logement_data(driver)
    
    return logement_data  # Retourner les données extraites

def save_to_csv(data, filename):
    if data:  # Assurez-vous que la liste n'est pas vide
        df = pd.DataFrame(data)
        df.to_csv(filename, index=False, encoding='utf-8')  # Sauvegarder dans un fichier CSV
        print(f"Données sauvegardées dans {filename}")
    else:
        print("Aucune donnée à sauvegarder.")

def main():
    driver = setup_driver()
    try:
        print("hello world")
        logements_data = search_logements(driver, "Le Bourget-du-Lac (73370)", 0)
        save_to_csv(logements_data, "logements.csv")
        print(f"Données extraites et sauvegardées dans logements.xlsx")
    except Exception as e:
        print(f"Une erreur s'est produite : {e}")
    finally:
        driver.quit()

if __name__ == "__main__":
    main()
