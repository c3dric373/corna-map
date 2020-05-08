# Front-end
Description globale de fonctionnement du front-end sous Angular-Cli.
## Prérequis
Il est nécessaire d’avoir angular Cli installé sur la machine. De ce fait il est aussi nécessaire d’avoir node.js et la commande npm.
## Installation
Dans le dossier `/front-end` du projet:
- lancer la commande `npm install` dans un terminal
-  lancer la commande `ng serve` dans le terminal

## Commandes importantes
importation des modules :

	npm install

Mise en route du serveur:
```
	ng serve
```

Lancement des tests unitaires :
```
	ng test
```

Ajout de services :
```
	ng generate service nomService
```

Ajout de composants : 
```
	ng generate component nomComponent
```

## Structure
Organisation des fichiers
- corona-map
	- back-end
		- ...
	 - front-end
		- e2e
			- src
				- fichiers de test
		- src
			- app
				- app-routing : gestion des routes, comment passer à une autre page
				- components:
					- nom composant
						- nom.html : génération de HTML liée au composant
						- nom.css : style en css
						- nom.ts document typescript pour gérer dynamiquement l'HTML
						- nom.spec.ts : test unitaire
				- app.component.css
				- app.component.html
				- app.component.ts
				- app.commponent.spec.ts 
				- app.module.ts : gestion des imports globaux
				- service
					- implémentation des services propres à chaque composant
				- model
					- fichiers définissant la structure d’un objet
		- assets
			- images
				- contient les images importées
		- environments
		- index.html : première page
		- styles.css : feuille de style globale




