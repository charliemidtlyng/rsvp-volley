
var React = require('react');
var EventStore = require('./EventStore');
var ReactRouter = require('react-router');
var Utils = require('./Utils');
var ReactBootstrap = require('react-bootstrap');
var Input = ReactBootstrap.Input;
var Recaptcha = require('./Recaptcha');
var Panel = ReactBootstrap.Panel;

var Participant = React.createClass({
    render: function () {
        return <div key={this.props.participant.id}>
            <span className="col-xs-8">{this.props.participant.name}</span>
            <span className="col-xs-4">
                <Input data-id={this.props.participant.id} groupClassName='lol' wrapperClassName='lol' type="checkbox" label="Tatt ut"
                       checked={!this.props.participant.reserve} onChange={this.props.updateParticipant}/>
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

    mixins: [],
    getInitialState: function () {
        return {
            currentEvent: {
                participants: []
            },
            error: ""
        };
    },
    componentDidMount: function () {
        this.updateEvent();
    },
    saveLineup: function () {
        var participantMap = {};
        this.state.currentEvent.participants.forEach(function(participant){
            participantMap[participant.id] = Boolean(participant.reserve)
        });
        console.log(participantMap);
        EventStore.confirmLineup(this.state.currentEvent.id, participantMap).then(this.updateEvent, this.updateError);
    },
    updateParticipant: function (event) {
        var participantId = Number.parseInt(event.target.dataset.id);
        var currentEventUpdated = this.state.currentEvent
        currentEventUpdated.participants.forEach(function (participant) {
            if (participant.id === participantId) {
                participant.reserve = !participant.reserve;
            }
        });

        this.setState({
            currentEvent: currentEventUpdated
        });
    },
    updateEvent: function () {
        EventStore.getEvent(this.props.params.id)
                .then(function (event) {
                    this.setState({currentEvent: event, error: ''});
                }.bind(this));
    },
    updateError: function (error) {
        this.setState({error: error.message});
    },
    render: function () {
        var event = this.state.currentEvent;

        var participants = event.participants.map(function (participant) {
            return <Participant key={participant.id} participant={participant} unregister={this.unregister} updateParticipant={this.updateParticipant}/>
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
                    <p className="pre-wrap">{event.description}</p>
                    <div className="col-xs-12 col-sm-5">
                        <h3>Påmeldte</h3>
                        <div className="row">{participants}</div>
                    </div>
                    <div className="col-xs-12">
                        <button type="button" className="btn btn-primary" onClick={this.saveLineup}>Lagre</button>
                        <ErrorPanel error={this.state.error} />
                    </div>
                </div>
        );
    }
});
module.exports = Event;
