var React = require('react');
var EventStore = require('../EventStore');
var ReactRouter = require('react-router');
var EventImage = require('../EventImage');
var Timer = require('../Timer');
var Utils = require('../Utils');
var Loader = require('../Loader');
var ReactWidgets = require('react-widgets');
var Combobox = ReactWidgets.Combobox;
var ReactBootstrap = require('react-bootstrap');
var Recaptcha = require('../Recaptcha');
var Panel = ReactBootstrap.Panel;
var LocalStorageMixin = require('react-localstorage');

var Participant = React.createClass({
    render: function () {
        return <div key={this.props.participant.id}>
            <span className="col-xs-8">{this.props.participant.name}</span>
            <span className="col-xs-4"><button data-id={this.props.participant.id} data-name={this.props.participant.name} className="btn btn-danger btn-xs" onClick={this.props.unregister}>[Avmeld]</button>
            </span>
        </div>
    }
});

var ErrorPanel = React.createClass({
    render: function () {
        var errorMessage = this.props.error;
        if (!errorMessage) {
            return <span />;
        }
        return <Panel header="Feilmelding" bsStyle="danger">
            {errorMessage}
        </Panel>
    }
});

var Event = React.createClass({
    mixins: [LocalStorageMixin],
        getInitialState: function () {
        return {
        };
    },
    getDefaultProps: function () {
        return {
            stateFilterKeys: ['name', 'phoneNumber', 'email'],
            messages: {
                emptyFilter: {}
            },
            listOfCandidates: [
                'Charlie Midtlyng',
                'Thor K. Valderhaug',
                'Simen Lomås Johannessen',
                'Ole-Martin Mørk',
                'Erlend Opdahl',
                'Esben Aarseth',
                'Christoffer Marcussen',
                'Tore Myklebust',
                'Jørn Hunskaar',
                'Erlend Heimark',
                'Rasmus Bauck',
                'Marius Løkketangen',
                'Vegard Gamnes',
                'Jøran Lillesand',
                'Torstein Nicolaysen',
                'Harald Kjølner',
                'Kristoffer Liabø',
                'Jonatan Austigard',
                'Pål Moen Møst',
                'Sindre Nordbø',
                'Ole Hjalmar Herje',
                'Emil Lunde',
                'Morten Øvrebø',
                'Andreas Moe',
                'Stian Surén',
                'Simen Støa',
                'Hannes Waller',
                'Emil Staurset',
                'Nikolai Norman Andersen',
                'Severin Sverdvik',
                'Helen Le',
                'Hallvard Braaten',
                'Anne Berit Bjering',
                'Silje Kandal',
                'Kjersti Barstad Strand',
                'Morten Utengen',
                'Erlend Gjesdal',
                'Nemanja Aksic',
                'Petter Samuelsen',
                'Svein Petter Gjøby',
                'Johan Rusvik',
                'Ingar Kvalheim',
                'Endre Skogen',
                'Hallvard Braaten'

            ]
        }
    },
    componentDidMount: function () {
        this.updateEvent();
    },
    attend: function () {
        const {name, email, phoneNumber} = this.state;
        var captcha = grecaptcha.getResponse();
        if (name === '') {
            return;
        }
        this.props.registerForEvent(this.props.params.id, {name: name, phoneNumber: phoneNumber, email: email, 'g-recaptcha-response': captcha})
        grecaptcha.reset();
    },
    unregister: function (event) {
        const participantId = event.target.dataset.id,
         eventId = this.props.event.currentEvent.id,
        participantName = event.target.dataset.name;
        if (confirm("Vil du melde av " + participantName + "?")) {
            this.props.unregisterForEvent(eventId, participantId);
        }
    },
    updateEvent: function () {
        this.props.fetchEventById(this.props.params.id);
    },
    deleteEvent: function () {
        var luckyNumber = prompt("Er du helt sikker på at du vil slette denne hendelsen? \n I så fall - hvilket draktnummer har Charlie")
        if (luckyNumber && parseInt(luckyNumber) === 7) {
            EventStore.removeEvent(this.props.event.currentEvent.id);
            window.location.hash = '';
        }
    },
    sendSlackNotification: function() {
        var user = prompt("Brukernavn?");
        var pass = prompt("Passord?");
        EventStore.sendSlackNotification(this.props.event.currentEvent.id, {user: user, pass: pass});
    },
    sendMailNotification: function() {
        var user = prompt("Brukernavn?");
        var pass = prompt("Passord?");
        EventStore.sendMailNotification(this.props.event.currentEvent.id, {user: user, pass: pass});
    },
    downloadICalendar: function () {
        window.location.replace(`api/events/${this.props.event.currentEvent.id}/icalendar`);
    },
    nameChange: function (value) {
        this.setState({name: value});
    },
    emailChange: function (event) {
        this.setState({email: event.target.value});
    },
    phoneNumberChange: function (event) {
        this.setState({phoneNumber: event.target.value});
    },
    render: function () {
        var event = this.props.event.currentEvent;
        var registerProps = this.props.register;
        var participants = event.participants
            .filter( participant => participant.reserve === false)
            .map(participant => <Participant key={participant.id} participant={participant} unregister={this.unregister}/>)
        
        var reserves = event.participants
            .filter(participant => participant.reserve === true)
            .map(participant => <Participant key={participant.id} participant={participant} unregister={this.unregister}/>);

        return (
                <div>
                    <Loader isLoading={this.props.event.loading}>
                    <div className="clearfix margin-bottom-30 margin-top-50 event event-with-padding">
                        <h6 className="margin-bottom-0"><EventImage event={event} />{event.subject}</h6>
                        <h2 className='margin-top-10'><span>{event.location}</span> <span className="gray">({Utils.timeStampToDate(event.startTime)} {Utils.formatDateTime(event.startTime, 'dd. MMMM')})</span></h2>
                        <div><strong>Tid:</strong> {Utils.formatDateTime(event.startTime, 'HH:mm')} - {Utils.formatDateTime(event.endTime, 'HH:mm')}</div>
                        <div><strong>Påmelding åpner:</strong> {Utils.formatDateTime(event.regStart, 'dd. MMMM (HH:mm)')}</div>
                        <div><strong>Påmeldt:</strong> {event.participants.length} / {event.maxNumber}</div>
                        <div><button className="btn btn-default btn-xs " onClick={this.downloadICalendar}>Lagre i kalender</button></div>
                        <div>
                            <button className="btn btn-danger btn-xs" onClick={this.deleteEvent}>Slett hendelse</button> || <button className="btn btn-warning btn-xs" onClick={this.sendSlackNotification}>Send slack-melding</button> || <button className="btn btn-warning btn-xs" onClick={this.sendMailNotification}>Send epost</button>
                        </div>
                    </div>
                    <div className="event event-with-padding">
                        <p className="pre-wrap">{event.description}</p>
                    </div>
                    <div>
                        <form className="margin-top-30 margin-bottom-30">
                            <fieldset>
                                <legend>Påmelding:</legend>
                                <ErrorPanel error={registerProps.error}/>
                                <div className="form-group col-xs-12 col-sm-4 col-md-4">
                                    <label htmlFor="name">Navn*</label>
                                    <Combobox
                                            data={this.props.listOfCandidates}
                                            filter={'contains'}
                                            value={this.state.name}
                                            onChange={this.nameChange}
                                            messages={{
                                                emptyList: 'Ingen treff',
                                                emptyFilter: 'Skriv inn navnet ditt'
                                            }}
                                    />
                                </div>
                                <div className="form-group col-xs-12 col-sm-4 col-md-4">
                                    <label htmlFor="phoneNumber">Mobil</label>
                                    <input type="tel" className="form-control col-xs-8 col-md-4" placeholder="mobilnr"
                                           value={this.state.phoneNumber}
                                           onChange={this.phoneNumberChange}
                                    />
                                </div>
                                <div className="form-group col-xs-12 col-sm-4 col-md-4">
                                    <label htmlFor="email">Epost</label>
                                    <input type="email" className="form-control col-xs-8 col-md-4" placeholder="epost"
                                           value={this.state.email}
                                           onChange={this.emailChange}
                                    />
                                </div>
                            </fieldset>
                            <br/>
                            <Recaptcha
                                    sitekey="6LdhmgATAAAAADMLD50qGZ-DSaa3bVlrsQp6BTgA"
                                    theme="dark"
                                    render="explicit"/>
                            <br/>
                            <div className="col-xs-12">
                                <Timer remainingTime={event.timeToRegistration}/>
                                <button type="button" className="btn btn-primary" onClick={this.attend}>Meld på nå</button>
                            </div>
                        </form>
                        <div className="col-xs-12 col-sm-5">
                            <h3>Påmeldte</h3>
                            <div className="row">{participants}</div>
                        </div>
                        <div className="col-xs-12 col-sm-5">
                            <h3>Reserveliste</h3>
                            <div className="row">{reserves}</div>
                        </div>
                    </div>
                </Loader>
                </div>
        );
    }
});
module.exports = Event;
