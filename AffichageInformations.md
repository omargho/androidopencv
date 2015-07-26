# Introduction #

Cette partie est plus ou moins mise de côté pour notre projet (la reconnaissance d'image est la priorité), pourtant, elle est indispensable pour rendre l'application réellement utilisable.

Il faut se mettre à la place des **deux utilisateurs** de l'application :
  * Le visiteur qui veut disposer des informations sur les oeuvres
  * Le responsable du musée qui doit administrer ces informations

En réfléchissant aux alternatives possibles pour satisfaire aux deux types d'utilisation, nous avons pensé que la solution idéale serait un web service, disposant d'une interface d'administration (visible depuis un navigateur sur un ordinateur) permettant d'ajouter les images en base de données, et les informations liées à ces oeuvres (pourquoi pas dans un Wiki, pour faire les choses simples ?).
De l'autre côté, les pages d'informations seraient visualisées depuis l'application mobile dans un navigateur embarqué (Android permet effectivement de le faire).


# Web Service #

Il faut une synchronisation des bases SQLite et de celle du web service (ou pourquoi pas une seule.)


# Application #

L'application se scinde du coup en deux parties distinctes :
  * Une première est l'interface d'analyse des oeuvres, comportant une activity, liée à un layout. Les différents services travaillent pour la reconnaissance d'image et une fois l'oeuvre rapportée à son bon identifiant, le relai est passé à la seconde partie de l'application.

  * Disposant du bon identifiant, la seconde activity utilise un navigateur embarqué pour rejoindre l'adresse du web service où sont disponibles les informations sur l'oeuvre.