# Contenu général #

  * Une analyse du cahier des charges
  * Mise en évidence du problème et des différentes tâches
  * Planning prévisionnel jusqu'à fin février
  * Déliverable final ?

# Détails #

> ## Problème & différentes tâches ##

Le vrai challenge de la mission est de réaliser de la reconnaissance d'image sur un petit périphérique, qui doit avoir un temps de réponse très court. Android dispose d'une adaptation  Java incomplète et sans documentation de la librairie OpenCV, il nous faut donc découvrir ce qu'elle nous permet de faire par nous même en nous aidant de la documentation de l'originale en C++.
Il nous faut penser les points suivants :
  * L'ergonomie mobile : l'application se doit d'être simple d'utilisation, rapide au démarrage, à l'analyse d'image et à l'affichage des résultats.
  * Les limites d'utilisations : pour la reconnaissance d'image, on est asservi par les conditions de vue des oeuvres à détecter.


Les différentes tâches que nous avons pu dégager de l'analyse du problème sont les suivantes :

  1. Utiliser la caméra Android via la librairie OpenCV pour utiliser les descripteurs
  1. Créer une vue statique façon Android (avec l'utilisation du XML) pour le cadrage de l'oeuvre d'art et sa capture
  1. Maitriser les descripteurs d'images pour la reconnaissance et s'en servir pour obtenir des informations sur les images
  1. Stocker en base de données les informations dégagées par les descripteurs
  1. Procéder au rapprochement de l'image à analyser avec les images en base via des algorithmes tels que KNN (K Nearest Neighbours)
  1. Stocker en base de données les informations recherchées par l'utilisateur de l'application
  1. Créer une nouvelle vue XML pour l'affichage des informations liées à l'oeuvre détectée
  1. Gérer toutes les exceptions (aucune oeuvre trouvée, etc.)
  1. Procéder à de nombreux tests dans différentes conditions, avec des oeuvres 2d (tableaux) puis 3d (sculptures)


> ## Planning prévisionnel ##


> ## Déliverable final ##

  * Une application fonctionnelle dans certaines conditions
  * Un dossier comprenant la documentation du code source, les problèmes connus (Known Bugs), les voies d'amélioration ...
