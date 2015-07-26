# Librairies #

Ici toutes les informations concernant les différentes librairies de reconnaissance d'images que l'on a pu étudier / tester.

## OpenCV ##

OpenCV est une librairie gratuite, développée en C/C++, d'autres langages, et récemment en Java (une version bêta).
http://opencv.willowgarage.com/wiki/
Elle implémente plus de 500 fonctions de traitement d'images : General Image Processing Functions, Image Pyramids, Segmentations, Geometrics Descriptors, Camera Calibration / Stereo / 3D, Transforms, Features, Utilities and Data Structures, Machine Learning (Recognition, Detection), tracking, fitting.

L'installation de la version Java sous Eclipse est décrite ici : http://opencv.itseez.com/doc/tutorials/introduction/android_binary_package/android_binary_package.html

Une fois l'installation terminée, on peut directement procéder aux tests des programmes de démonstration sur le téléphone.

### Pourquoi OpenCV ? ###

Nous faisons de la reconaissance de forme 2D, pour cela on utilise des algorithmes de vision, dont le but est de réaliser les actions suivantes:
Extraction de primitives à partir des images.
Représentation des connaissances. (modèle).
Mise en correspondance image/connaissances : reconnaissance.

### Méthodes d’identifications « 2D » ###

Ces méthodes consistent à réaliser les taches suivantes :
Pré traiter, segmenter (contour, régions).
Extraire d’attributs colorimétriques, de textures et de forme.
Faire de la classification et  de la reconnaissance.

### Intérêt d'OpenCV ###

Utiliser les traitements sans forcément connaître les algorithmes et une bibliothèque optimisée, traitement temps réel.