# airport-flight-management

## Projet Reseau
Gestion des avions dans un aéroport,

Tour de control : serveur
Avions : client

### Tours de control :
- Enregistrement des avions dans un annuaire
    - Annuaire contient l'etat des avions :
        1. active
        2. standby
        3. idel
        4. broken
- Programmation des vols
- Acheminement des avions
    - etat et posiiton de l'avions en temps réel
    - visualisation radar
    - utilisation algorithme plus court chemin pour rajectoire des avions


### classes initiales :
1. Avion : ref, reservoir, etat
2. tour de control : annuaire_avions, annuaire_station
3. station : id, nom
4. annuaire

#### nb :
- planning des vols définis au préalable ? ajouter objet schedule def par admin (tour de ctrl)
- créer une fonction tqdm pour le suivi automatique de la consomation du reservoir de l'avion selon la distance parcouru

#### a faire :
- diagramme de classe + diagramme d'activité
- répartition des classe et code

