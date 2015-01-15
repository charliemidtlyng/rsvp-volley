rsvp-app
==================

Må ha:

- Opprette event med registreringsdato
- Påmelding -> Reserveliste hvis fullt
- Epostutsending ved opprettelse
- Epostutsending ved ledig plass

For å slippe å skrive .gradlew overalt, og heller skrive gw ->

git clone https://github.com/dougborg/gdub.git
cd gdub
./install

Starte opp lokalt
gw bootRun -Pprofile=dev

Bygge frontend:
cd frontend && npm install && gulp deploy
