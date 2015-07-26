# Descriptif Général #

Objectif du projet :  Dans un musée, permettre l'accès à des informations à propos des oeuvres sur mobile.

Contributeurs : Romain Proyart et Olympe Kassa

Tuteur de Projet :  Peter Yvan, Martinet Jean

Contexte :  Ce projet de fin d'etudes s'inscrit dans le cadre du développement d'un prototype d'application mobile de visite de musée. L'objectif est de permettre à l'utilisateur d'obtenir des informations sur les oeuvres qu'il est en train de regarder.

# Gestion du projet #

## Analyse des besoins et faisabilité ##

**Besoins** :   On veut consulter des informations sur des œuvres d'art à partir d'un téléphone Android.
Exemple d'informations à collecter : Auteur, Date de création, courant, ...

**Faisabilité** :    On peut développer sur une plate forme Android pour certaines raisons telles que :
  * Environnement de développement accessible .
  * Code Open Source (réutilisable), langage JAVA.
  * La plate-forme Android est très populaire.
  * Etudier les outils qui permettent de faire de l'analyse d'image.

**Contraintes** :
Le musée et son environnement imposent un certains nombre de contraintes, aussi on doit les prendre en compte :
  * Ne pas rajouter des technologies ( bluetooth, localisation Wifi, QR code...).


De cette contrainte découle le fait que le seul moyen d'accéder à l'information est l'oeuvre en elle-même. On s'oriente donc vers une solution technologique utilisant de la reconnaissance d'image.

## Spécifications ##

  * Temps de réponse ( Photo & affichage des données)
  * Stockage des informations sur le téléphone (et du coup taille de l'application)
  * Adaptabilité à l'environnement et définition des conditions limites (éclairage, angle de vue...)
  * Compatibilité avec des capteurs photo de qualité différente (Autofocus ou non, résolution...)






## Technologies ##

Développement sous Android :

  * Environnement de développement : Eclipse adapté pour le développement d'application  android via le SDK que l'on peut installer facilement.
  * La librairie OpenCV développée en C/C++ ayant un wrapper JAVA qui nous permet d'utiliser des fonctions de cette librairie pour faire de la reconnaissance de forme.
  * Test et débogage sur plusieurs téléphones android possible notamment HTC Wildfire S et le Samsung Galaxy S SCL.

Une étude des fonctions utiles pour faire de la reconnaissance de forme, sera effectuée afin de comprendre comment isoler des visages, des objets , des contours, …

Un mini workflow sera aussi réalisé afin d'expliquer la méthodologie de création de notre application Android.