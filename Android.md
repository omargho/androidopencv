# Contexte #

Le développement est réalisé en Java sous Eclipse Hélios : http://www.eclipse.org/downloads/
avec le SDK Google Android : http://developer.android.com/sdk/index.html

Les tests sont effectués sur un téléphone HTC Wildfire S et un SFR Starshine.
Il est possible d'utiliser un AVD (Android Virtual Device) pour développer sans se munir d'un téléphone, mais dans notre cas, pour tester les fonctionnalités des librairies mettant à profit le capteur photographique, il est préférable d'utiliser celui d'un téléphone réel.
Pour envoyer directement le programme sur le téléphone, nous employons la procédure décrite ici :
http://developer.android.com/guide/developing/device.html


# Layout #

Il y a deux manières pour créer un layout pour une application sous Android : la première, qui semblerait plus "classique" aux yeux d'un développeur Java, est dite **dynamique**,  elle se fait par appel des méthodes dans le code Java.
La seconde, propre au développement mobile, consiste en un code XML listant les différents éléments de mise en page de l'interface. Elle est dite **statique** car elle ne permet pas de générer en temps réel la mise en page avec des informations dynamiques (exemple : l'affichage d'un tableau avec un nombre de cases non connu à l'avance, la vidéo).

Il est possible de mélanger les deux layout via ce qu'on appelle un **inflater**.