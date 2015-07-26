# Introduction #

Dans le cadre de notre projet de fin d’études à Polytech'lille, nous avons choisi de travailler sur le développement d'une application de visite guidée pour le musée d'art moderne de Villeneuve d'Ascq (LAM).


# Détails #

La plupart des services basés sur la localisation utilisent les données GPS pour situer un utilisateur et lui fournir des informations localisées. Toutefois le signal GPS peut être imprécis voir absent notamment en intérieur. D'autres technologies peuvent prendre le relais : bluetooth, localisation Wifi, QR code... Elles imposent toutefois un équipement des locaux qui peut être difficile dans certains cadres tels que les musées. Pour cette raison on s'intéresse ici à l'utilisation de la reconnaissance de formes comme base de l'identification des oeuvres afin de récupérer les informations pertinentes. Pour cela, il sera nécessaire d'identifier les librairies disponibles pour Android (par ex. OpenCV) et de choisir la plus appropriée afin de développer un prototype permettant de tester la qualité de la reconnaissance et les conditions limites (éclairage, angle de vue...).

# Outils utilisés #

Environnement de travail: Développement sur Android (HTC Hero), langage dépendant de la libraire choisie (java, C/C++).

# Planification du travail #

Concernant la reconnaissance d'image, nous avons établi le plan de travail suivant :
  * **Identifications des librairies existantes** : les librairies doivent évidemment être compatible avec Android.
  * **Choix d'une librairie**
  * **Prototype d'une application sous Android** : avec le téléphone HTC Hero.
  * **Test des conditions limites** : tester l'incidence de facteurs extérieurs sur la reconnaissance de l'oeuvre : éclairage, reflets dans une vitrine, ...


# Problématiques #

Différentes questions se posent quant à l'utilisation réelle de l'application dans le musée :
D'une part, la fiabilité de la reconnaissance des oeuvres par l'image qui varie en fonction des capteurs photographiques intégrés aux téléphones Android, d'autre part, de la manière dont le conservateur du musée va intégrer à l'application de nouvelles oeuvres (par exemple : prendre plusieurs photos de l'oeuvre en différentes conditions, puis les associer aux informations destinées aux visiteurs).