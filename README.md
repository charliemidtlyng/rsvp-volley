BEKK Volleyball
==================

### Om applikasjonen

- Opprette event med registreringsdato og påmeldingsdato
- Uttak til kamper
- Påmelding -> Reserveliste hvis fullt
- Epostutsending/Slack-melding ved opprettelse
- Epostutsending/SMS ved ledig plass
- Importere kamper fra nif sitt system (skjørt, men bør funke)


### Bygge 
For å slippe å skrive .gradlew overalt, og heller skrive gw ->
``` 
$ git clone https://github.com/dougborg/gdub.git
$ cd gdub
$ ./install
```

#### Bygge backend
```
$ gw clean build
```
#### Bygge frontend
```
$ cd frontend
$ npm install
$ gulp deploy
(´deploy´ bygger frontend og legger en kopi av js/css inn i static-mappe. Dette må sjekkes inn i git og være med i releasebygg)
```


### Kjøre lokalt

#### Backend 
```  
$ gw bootRun -Pprofile=dev
```
#### Frontend 
```  
$ cd frontend && gulp deploywatch
(´deploywatch´ bygger frontend kontinuerlig ved endringer og legger en kopi av js/css inn i static-mappe.)
```
Starter opp på `http://localhost:8080/`

### Deploy
Foreløpig kun manuel opplasting til AWS, men skal prøve å få på plass "byggeserver" og automatisk utrulling
PS: Endringer i frontend må bygges før commit:
``` 
$ cd frontend && npm install && gulp deploy
$ git commit ...
$ git push
```