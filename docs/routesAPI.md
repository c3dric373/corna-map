# Spécifications des routes de l’API 

## Service Map
Date au format: "yyyy-MMM-dd"

### Récupérer les informations globales relatives au Coronavirus en France à la date sélectionnée :
**/map/infosFrance?date=...**
output :
```json
[  
	{  
		"id":"FRA",  
		"nom":"France",  
		"criticalCases":6027,  
		"hospitalized":31190,  
		"totalCases":109252,  
		"ephadCases":58989,  
		"ephadConfirmedCases":20272,  
		"ephadPossibleCases":38717,  
		"totalDeaths":11478,  
		"totalEphadDeaths":7203,  
		"recoveredCases":34420,  
		"totalTests":0,  
		"date":{  
			"year": ...,  
			"month": ...,  
			"day": ...  
		}  
	}  
]
```
### Récupérer les informations régionales relatives au Coronavirus à la date sélectionnée :
**/map/infosRegion?date=...**
output :
```json
[
	{
		"id":"REG-01",
		"nom":"Guadeloupe",
		"criticalCases":19,
		"totalDeaths":10,
		"hospitalized":38,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{
		"id":"REG-02",
		"nom":"Martinique",
		"criticalCases":17,
		"hospitalized":38,
		"totalDeaths":6,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{…
	}
]
```
### Récupérer les informations relatives au Coronavirus pour la région et la date sélectionnées :
**/map/infosRegion?name=...&date=....**
output :
```json
[
	{
		"id":"REG-01",
		"nom":"Guadeloupe",
		"criticalCases":19,
		"hospitalized":36,
		"totalDeaths":10,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	}
]
```
### Récupérer les informations régionales relatives au Coronavirus en France pour le département et la date sélectionnés :
**/map/infosDept?name=...&date=....**
output :
```json
[
	{
		"id":"DEP-01",
		"nom":"Ain",
		"criticalCases":24,
		"hospitalized":152,
		"totalDeaths":55,
		"recoveredCases":177,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	}
]
```
### Récupérer les informations départementales relatives au Coronavirus en France à la date sélectionnée :
**/map/infosDept?date=...**
output :
```json
[
	{
		"id":"DEP-01",
		"nom":"Ain",
		"criticalCases":24,
		"hospitalized":152,
		"totalDeaths":55,
		"recoveredCases":177,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{
		"id":"DEP-02",
		"nom":"Aisne",
		"criticalCases":44,
		"hospitalized":277,
		"totalDeaths":165,,
		"recoveredCases":345,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{ …
	}
]
```
## Service Historique
Permet d’afficher sur un graphique l’évolution des données en fonction du temps ( en jours).

### Récupérer toutes les données de la France :
**/historique?location=FRA**
```json
[  
	{  
		"id":"FRA",  
		"nom":"France",  
		"criticalCases":6027,  
		"hospitalized":31190,  
		"totalCases":109252,  
		"ephadCases":58989,  
		"ephadConfirmedCases":20272,  
		"ephadPossibleCases":38717,  
		"totalDeaths":11478,  
		"totalEphadDeaths":7203,  
		"recoveredCases":34420,  
		"totalTests":0,  
		"date":{  
			"year": ...,  
			"month": ...,  
			"day": ...  
		}  
	},
	{  
		"id":"FRA",  
		"nom":"France",  
		"criticalCases":6027,  
		"hospitalized":31190,  
		"totalCases":109252,  
		"ephadCases":58989,  
		"ephadConfirmedCases":20272,  
		"ephadPossibleCases":38717,  
		"totalDeaths":11478,  
		"totalEphadDeaths":7203,  
		"recoveredCases":34420,  
		"totalTests":0,  
		"date":{  
			"year": ...,  
			"month": ...,  
			"day": ...  
		}  
	},
	{ …
	}
]
```
 ### Récupérer toutes les données de la Région ayant pour identifiant X:
**/historique?location=REG-X**
output :
```json
[
	{
		"id":"REG-01",
		"nom":"Guadeloupe",
		"criticalCases":19,
		"hospitalized":36,
		"totalDeaths":10,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{
		"id":"REG-01",
		"nom":"Guadeloupe",
		"criticalCases":17,
		"hospitalized":38,
		"totalDeaths":6,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{…
	}
]
```
### Récupérer toutes les données du département ayant pour identifiant X:
**/historique?location=DEP-X**
output :
```json
[
	{
		"id":"DEP-01",
		"nom":"Ain",
		"criticalCases":24,
		"hospitalized":152,
		"totalDeaths":55,
		"recoveredCases":177,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{
		"id":"DEP-01",
		"nom":"Ain",
		"criticalCases":44,
		"hospitalized":277,
		"totalDeaths":165,
		"recoveredCases":345,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{ …
	}
]
```
## service Simulation
Commencer la simulation en envoyant les paramètres :
**POST /simulation/start**
format Json
Params :
```json
{
	"respectConfinement":50,
	"mask":{
		"m0_14":true,
		"m15_44":false,
		"m45_64":false,
		"m65_74":true,
		"m75":true
	},
	"conf":{
		"m0_14":true,
		"m15_44":true,
		"m45_64":true,
		"m65_74":false,
		"m75":false
	}
}
```
### Récupérer les données simulées globales relatives au Coronavirus en France à la date sélectionnée :
**/simulation/infosFrance?date=...**
output :
```json
[  
	{  
		"id":"FRA",  
		"nom":"France",  
		"criticalCases":6027,  
		"hospitalized":31190,  
		"totalCases":109252,  
		"ephadCases":58989,  
		"ephadConfirmedCases":20272,  
		"ephadPossibleCases":38717,  
		"totalDeaths":11478,  
		"totalEphadDeaths":7203,  
		"recoveredCases":34420,  
		"totalTests":0,  
		"date":{  
			"year": ...,  
			"month": ...,  
			"day": ...  
		}  
	}  
]
```
### Récupérer les données régionales simulées relatives au Coronavirus à la date sélectionnée:
**/simulation/infosRegion?date=...**
output :
```json
[
	{
		"id":"REG-01",
		"nom":"Guadeloupe",
		"criticalCases":19,
		"totalDeaths":10,
		"hospitalized":38,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{
		"id":"REG-02",
		"nom":"Martinique",
		"criticalCases":17,
		"hospitalized":38,
		"totalDeaths":6,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{…
	}
]
```
### Récupérer les données régionales simulées relatives au Coronavirus pour la région et la date sélectionnées :
**/simulation/infosRegion?date=...&name=...**
output :
```json
[
	{
		"id":"REG-01",
		"nom":"Guadeloupe",
		"criticalCases":19,
		"hospitalized":36,
		"totalDeaths":10,
		"recoveredCases":59,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	}
]
```
### Récupérer les données départementales simulées relatives au Coronavirus à la date sélectionnée :
**/simulation/infosDept?date=...**
output :
```json
[
	{
		"id":"DEP-01",
		"nom":"Ain",
		"criticalCases":24,
		"hospitalized":152,
		"totalDeaths":55,
		"recoveredCases":177,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{
		"id":"DEP-02",
		"nom":"Aisne",
		"criticalCases":44,
		"hospitalized":277,
		"totalDeaths":165,,
		"recoveredCases":345,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	},
	{ …
	}
]
```
### Récupérer les données simulées relatives au Coronavirus pour le département et la date sélectionnés :
**/simulation/infosDept?date=...&name=...**
output :
```json
[
	{
		"id":"DEP-01",
		"nom":"Ain",
		"criticalCases":24,
		"hospitalized":152,
		"totalDeaths":55,
		"recoveredCases":177,
		"date":{
			"year": ...,
			"month": ...,
			"day": ...
		},
	}
]
```