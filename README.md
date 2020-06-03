# IA04 - Projet : ClearPath

Étant donné un ensemble de N agents en un espace de 2 dimensions, chaque agent se doit déplacer de sa position initiale à autre position de l’espace, en évitant les collisions entre autres agents.
Lorsque deux agents sont à une certaine distance l’un de l’autre, et qu’ils risque de rentrer en collision (la différence de leur angles est complémentaire), ils doivent corriger leurs paramètres de vitesse et d’angle jusqu’à éviter ce risque.

Pour ce faire, l'algorithme ClearPath a été étudié par Stephen J. Guy, Jur van den Berg, Ming Lin, et Dinesh Manocha de University of North Carolina at Chapel Hill.
Cet algorithme formulise l'évittement de collision en appliquant la programmation linéaire. 


## Getting Started

Ces instructions vous permettront de lancer le projet sur votre ordinateur local à des fins de développement et de test. 

### Prerequisites

* Eclipse 4.14.0
* JavaSE-8

### Importer le projet ( développement )

- Dans Eclipse, `File/ New/ Java Project`
- Décrochez `use default location`
- Choisissez le dossier dans lequel contient le projet `ClearPath` 


### Configurer 
*En cas de besoins*

**Import MASON**
- Dans le répertoire `lib-mason`, cliquer droit sur les fichiers `jar` et sélectionner Build Path 

**Import Java8**
- Cliquer droit sur le nom de projet, sélectionner `Properties`
- Dans la fenêtre ouvrant, choisir `Java Build Path/ Libraries / Modulespath` et puis `Add Library`
- Sélectionner `Installed JREs... -> Add -> Standard VM`
- Saisir le chemin du répertoire `dk-1.8.0.jdk/Contents/Home` dans le champs `JRE home`  et `Finir`

**Config source**
- Cliquer droit sur `src/ Build Path / Use as Source Folder`

### Execution
- Les scénarios de simulation sont actuellement dans la répétoire tests/, ils sont :
    + ScenarioBlockMason

- Pour exécuter, cliquer droit sur lode code source du scénario et choisir `Run As Java Application`

## Auteur
* Duong Minh Nghia
* Neglokpe Elwynn
* Nguyen Tran Khanh Linh
* Dacheux Sebastien
* Alanis Mayorga Oswaldo Aldair