# Introduction #

Nos oeuvres d'art rassemblent un certain nombre de propriétés qui nous permettent de les décrire et de les reconnaitre les une des autres.
Android fournit le support de bases de données de type SQLite. La base de données est accessible dans toutes les classes de notre application mais uniquement par notre application. La méthode conseillée pour créer une base de données est d'étendre la classe SQLiteOpenHelper et de réécrire la méthode onCreate().

Nous allons récupérer les données de notre base de données en utilisant une  classe ImageClasse que nous allons créer. Pour écrire ou lire dans une base de données, nous allons utiliser les méthodes suivantes, getWritableDatabase() et getReadableDatabase(). Ces deux méthodes retournent un objet SQLiteDatabase représentant la base de données et nous permettent d'effectuer des requêtes sur la base de données. Pour effectuer des requêtes nous disposons de deux méthodes. la méthode query() disponible dans l'objet SQLiteDatabase ou alors vous pouvez créer des requêtes complexes en utilisant l'objet SQLiteQueryBuilder. Une fois la requête effectuée, nous récupérerons un Cursor qui vous permet d'itérer sur toutes les données récupérées.

_source :_ http://www.mti.epita.fr/blogs/2010/11/17/le-stockage-sous-android/

## Caractéristiques des images ##


  * FORMAT (CHAR 4)	BMP, JPEG, TIF, GIF...
  * COULEURS (INTEGER ) 2 (noir & blanc), 256 (niveaux de gris), 1024 (couleurs)
  * LARGEUR (INTEGER)	la largeur de l'image
  * HAUTEUR (INTEGER)	la hauteur de l'image
  * NOM (VARCHAR 256)	le nom du fichier
  * TAILLE (INTEGER)	le volume des donnée
  * DESCRIPTION (VARCHAR 1024)	une description de l'image pouvant faire l'objet d'une requête LIKE
  * LEGENDE (VARCHAR 256)	légende s'inscrivant sous l'image
  * HINT (VARCHAR 256)	information apparaissant au survol de la souris
  * COPYRIGHT (VARCHAR 256)	auteur / propriétaire de l'image
  * DATE CREATION	date de création

## Modèle UML de notre BDD ##



## Les classes utiles ##