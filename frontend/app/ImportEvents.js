var React = require('react');
var EventStore = require('./EventStore');
var ImportEventsList = require('./ImportEventsList');
var moment = require('moment');
function updateEvent(events, savedEvent) {
    return events.map(function (item) {
        if (item.startTime === savedEvent.startTime) {
            return savedEvent;
        }
        return item;
    });
}

function parseDates(event) {
    var convertedEvent = Object.assign({}, event);
    convertedEvent.startTime = moment(event.startTime).format();
    convertedEvent.endTime = moment(event.endTime).format();
    convertedEvent.regStart = moment(event.regStart).format();
    convertedEvent.regEnd = moment(event.regEnd).format();
    return convertedEvent;
}

var ImportEvents = React.createClass({

    getInitialState: function () {
        return {
            events: [],
            importDetails: {}
        }
    },
    generateFromUrl: function (event) {
        event.preventDefault()
        EventStore.importEvents(this.state.importDetails).then(function (events) {
            this.setState({events: events});
        }.bind(this));
    },
    createEvent: function (event) {
        EventStore.addEvent(parseDates(event)).then(function (savedEvent) {
            this.setState({events: updateEvent(this.state.events, savedEvent)})
        }.bind(this));
    },
    onValueChange: function (event) {
        var inputName = event.target.name;
        var stateValue = this.state.importDetails;
        var value = event.target.value;

        if (inputName === 'maxNumber' || inputName === 'teamId' || inputName === 'tournamentId') {
            value = value && !isNaN(parseInt(value)) ? parseInt(value) : value;
        }
        stateValue[inputName] = value;
        this.setState({importDetails: stateValue});
    },
    render: function () {
        return (
                <div>
                    <h2 className="page-header">Importere hendelser</h2>
                    <form className="form-horizontal">
                        <div className="form-group">
                            <label htmlFor="subject" className="col-sm-2 control-label">Tittel</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="subject" ref="subject" placeholder="tittel"
                                       value={this.state.importDetails.subject} onChange={this.onValueChange}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="tournamentId" className="col-sm-2 control-label">TournamentId</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="tournamentId" ref="tournamentId"
                                       placeholder="tournamentId" value={this.state.importDetails.tournamentId}
                                       onChange={this.onValueChange}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="teamId" className="col-sm-2 control-label">TeamId</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="teamId" ref="teamId" placeholder="teamId"
                                       value={this.state.importDetails.teamId} onChange={this.onValueChange}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="maxNumber" className="col-sm-2 control-label">Maks antall</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="maxNumber" ref="maxNumber" placeholder="maks antall"
                                       value={this.state.maxNumber} onChange={this.onValueChange}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-offset-2 col-sm-5">
                                <button type="button" className="btn btn-default" onClick={this.generateFromUrl}>Generer events</button>
                            </div>
                        </div>
                    </form>
                    <ImportEventsList events={this.state.events} createEvent={this.createEvent}/>
                </div>
        );
    }
});
module.exports = ImportEvents;