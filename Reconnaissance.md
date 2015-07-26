# Requis pour l'analyse #

  * Rechercher quelles informations caractéristiques OpenCV permet d'extraire des images
  * Trier ces informations par "importance", ou plutôt par leur capacité à dissocier rapidement une image d'oeuvre d'une autre (exemple : espace colorimétrique général > Contours détectés)
  * Construire une base de donnée associant des images à ces informations pré-extraites


# Idée d'algorithme #

Comparer chaque caractéristique de l'image étudiée à celles des images en base de donnée, en partant de la plus importante, et en excluant à chaque boucle des images trop éloignées (il faut donc inclure une marge d'erreur acceptable).

  * Algorithme théoriquement lourd (dépend de la taille **n** de la base de donnée, du nombre d'image **ni** exclues à chaque boucle, et du nombre de caractéristiques **c**) :
sum(i = 0, c) : n-(ni) comparaisons.
  * La différence du temps de reconnaissance de deux images différentes peut être grande, puisque le calcul dépend du nombre d'images exclues à chaque boucle.
  * Si les premiers caractères permettent réellement d'exclure beaucoup d'images, le temps de calcul peut être très rapide.


Le principal problème est la détermination de la non-reconnaissance, et le temps de calcul dans le pire des cas.
