from kafka import KafkaConsumer
import json

def consume_messages(topic):
    # Créer un consommateur Kafka
    consumer = KafkaConsumer(
        topic,
        bootstrap_servers='localhost:9092',
        auto_offset_reset='earliest',  # Lire les messages depuis le début
        enable_auto_commit=True,
        group_id='housing_consumer_group',
        value_deserializer=lambda x: json.loads(x.decode('utf-8'))
    )

    # Consommer les messages
    print(f"Consommateur en écoute sur le topic '{topic}'...")
    for message in consumer:
        logement_info = message.value
        print(f"Message reçu : {logement_info}")

if __name__ == "__main__":
    consume_messages('housing_topic')
