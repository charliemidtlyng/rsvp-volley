/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var Router = require('react-router');
var Utils = require('./Utils');
var ReactWidgets = require('react-widgets');
var Combobox = ReactWidgets.Combobox;
var ReactBootstrap = require('react-bootstrap');
var Recaptcha = require('./Recaptcha');
var Panel = ReactBootstrap.Panel;

var Participant= React.createClass({
    render: function(){
        return <div key={this.props.participant.id}>
            <span className="col-xs-8">{this.props.participant.name}</span>
            <span className="col-xs-4"><button data-id={this.props.participant.id} data-name={this.props.participant.name} className="btn btn-danger btn-xs" onClick={this.props.unregister}>[Avmeld]</button>
            </span>
        </div>
    }
});

var ErrorPanel = React.createClass({
   render: function(){
       var errorMessage = this.props.error;
       if(!errorMessage) {
           return <span />;
       }
       return <Panel header="Feilmelding" bsStyle="danger">
        {errorMessage}
       </Panel>
   }
});

var Event = React.createClass({

    mixins: [ Router.Navigation, Router.State ],
    getInitialState: function () {
        return {
            currentEvent: {
                participants: []
            },
            error: ""
        };
    },
    getDefaultProps: function(){
      return {listOfCandidates: [
          'Charlie Midtlyng',
          'Thor K. Valderhaug',
          'Simen Lomås Johannessen',
          'Aleksander Hindenes',
          'Ole-Martin Mørk',
          'Rune Nergård',
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
          'Magne Davidsen',
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
          'Fredrik Einarsson',
          'Emil Staurset',
          'Nikolai Norman Andersen'
      ]}
    },
    componentDidMount: function () {
        this.updateEvent();
    },
    attend: function(x) {
        var name = this.refs.name.state.value;
        var email = this.refs.email.getDOMNode().value.trim();
        var phoneNumber = this.refs.phoneNumber.getDOMNode().value.trim();
        var captcha = grecaptcha.getResponse();
        if(name === '') {
            return;
        }
        EventStore.registerForEvent(this.getParams().id, {name: name, phoneNumber: phoneNumber, email: email, 'g-recaptcha-response': captcha})
                .then(this.updateEvent, this.updateError);
        grecaptcha.reset();
    },
    unregister: function(event){
        var participantId = event.target.dataset.id, eventId = this.state.currentEvent.id;
		var participantName = event.target.dataset.name;
        if (confirm("Vil du melde av " + participantName + "?")) {
            EventStore.unregisterForEvent(eventId, participantId).then(this.updateEvent, this.updateError);
        }
    },
    updateEvent: function(){
        EventStore.getEvent(this.getParams().id)
                .then(function(event){
                    this.setState({currentEvent: event, error: ''});
                }.bind(this));
    },
    updateError: function(error){
        this.setState({error: error.message});
    },
    render: function () {
        var event = this.state.currentEvent;

        var participants = event.participants.filter(function(participant){
            return participant.reserve === false;
        }).map(function(participant){
            return <Participant key={participant.id} participant={participant} unregister={this.unregister} />
        }.bind(this));

        var reserves = event.participants.filter(function(participant){
            return participant.reserve === true;
        }).map(function(participant){
            return <Participant key={participant.id} participant={participant} unregister={this.unregister} />
        }.bind(this));

        return (
                <div className="clearfix margin-bottom-30 margin-top-30">
                    <h2>{event.subject}</h2>
                    <h4><strong>Start:</strong> {Utils.timeStampToDate(event.startTime)} - {Utils.formatDateTime(event.startTime)}</h4>
                    <div><strong>Til:</strong> {Utils.formatDateTime(event.endTime)}</div>
                    <div><strong>Sted:</strong> {event.location}</div>
                    <div><strong>Påmelding åpner:</strong> {Utils.formatDateTime(event.regStart)}</div>
                    <div><strong>Maks antall:</strong> {event.maxNumber}</div>
                    <div><strong>Antall påmeldt:</strong> {event.participants.length}</div>
                    <p className="pre">{event.description}</p>
                    <form className="margin-top-30 margin-bottom-30">
                        <fieldset>
                            <legend>Påmelding:</legend>
                            <ErrorPanel error={this.state.error} />
                            <div className="form-group col-xs-12 col-sm-4 col-md-4">
                                <label htmlFor="name">Navn*</label>
                                <Combobox
                                        data={this.props.listOfCandidates}
                                        ref='name'
                                        suggest={true}
                                        filter={'contains'}
                                        messages={emptyFilter= {}}
                                />
                            </div>
                            <div className="form-group col-xs-12 col-sm-4 col-md-4">
                                <label htmlFor="phoneNumber">Mobil</label>
                                <input type="tel" className="form-control col-xs-8 col-md-4" placeholder="mobilnr" ref="phoneNumber" />
                            </div>
                            <div className="form-group col-xs-12 col-sm-4 col-md-4">
                                <label htmlFor="email">Epost</label>
                                <input type="email" className="form-control col-xs-8 col-md-4" placeholder="epost" ref="email" />
                            </div>
                        </fieldset>
                        <br/>
                        <Recaptcha
                                sitekey="6LfB7QATAAAAAMjr-w95hNK54bNkWAYXKOzJvzt-"
                                theme="dark"
                                render="explicit" />
                        <br/>
                        <div className="col-xs-12"><button type="button" className="btn btn-primary" onClick={this.attend}>Meld på</button></div>
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
        );
    }
});
module.exports = Event;
