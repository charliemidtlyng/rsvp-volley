/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var Router = require('react-router');
var Utils = require('./Utils');
var ReactWidgets = require('react-widgets');
var Combobox = ReactWidgets.Combobox;

var Participant= React.createClass({
    render: function(){
        return <div key={this.props.participant.id}>
            <span className="col-xs-8">{this.props.participant.name}</span>
            <span className="col-xs-4"><button data-id={this.props.participant.id} className="btn btn-danger btn-xs" onClick={this.props.unregister}>[Avmeld]</button>
            </span>
        </div>
    }
});

var Event = React.createClass({

    mixins: [ Router.Navigation, Router.State ],
    getInitialState: function () {
        return {
            currentEvent: {
                participants: []
            }
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
          'Sindre Nordbø'
      ]}
    },
    componentDidMount: function () {
        this.updateEvent();
    },
    attend: function(x) {
        var name = this.refs.name.state.value;
        var email = this.refs.email.getDOMNode().value.trim();
        if(name === '') {
            return;
        }
        EventStore.registerForEvent(this.getParams().id, {name: name, email: email})
                .then(this.updateEvent);
    },
    unregister: function(event){
        var participantId = event.target.dataset.id, eventId = this.state.currentEvent.id
        EventStore.unregisterForEvent(eventId, participantId).then(this.updateEvent);

    },
    updateEvent: function(){
        EventStore.getEvent(this.getParams().id)
                .then(function(event){
                    this.setState({currentEvent: event});
                }.bind(this));
    },

    render: function () {
        var event = this.state.currentEvent;
        var participants = event.participants.filter(function(participant){
            return participant.reserve === false;
        }).map(function(participant){
            return <Participant participant={participant} unregister={this.unregister} />
        }.bind(this));
        var reserves = event.participants.filter(function(participant){
            return participant.reserve === true;
        }).map(function(participant){
            return <Participant participant={participant} unregister={this.unregister} />
        }.bind(this));
        return (
                <div>
                    <h2>{event.subject}</h2>
                    <h5><strong>Start:</strong> {Utils.formatDateTime(event.startTime)}</h5>
                    <div><strong>Til:</strong> {Utils.formatDateTime(event.endTime)}</div>
                    <div><strong>Sted:</strong> {event.location}</div>
                    <div><strong>Påmelding åpner:</strong> {Utils.formatDateTime(event.regStart)}</div>
                    <div><strong>Maks antall:</strong> {event.maxNumber}</div>
                    <div><strong>Antall påmeldt:</strong> {event.participants.length}</div>
                    <form className="margin-top-30 margin-bottom-30">
                        <fieldset>
                            <legend>Påmelding:</legend>
                            <div className="form-group col-xs-12 col-sm-5 col-md-4">
                                <label htmlFor="name">Navn*</label>
                                <Combobox
                                        data={this.props.listOfCandidates}
                                        ref='name'
                                        suggest={true}
                                        filter={'contains'}
                                />
                            </div>
                            <div className="form-group col-xs-12 col-sm-5 col-md-4">
                                <label htmlFor="email">Epost</label>
                                <input type="email" className="form-control col-xs-8 col-md-4" placeholder="epost" ref="email" />
                            </div>
                        </fieldset>
                        <div className="col-xs-12"><button className="btn btn-primary" onClick={this.attend}>Meld på</button></div>
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