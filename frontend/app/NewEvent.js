var React = require('react');
var EventStore = require('./EventStore');
var ReactWidgets = require('react-widgets');
var DefaultEvents = require('./utils/DefaultEvents');
var DateTimePicker = ReactWidgets.DateTimePicker;
var Combobox = ReactWidgets.Combobox;
var NewEvent = React.createClass({

    mixins: [],
    getInitialState: function () {
        return {
            subject: '',
            description: '',
            location: '',
            maxNumber: 0,
            startTime: null,
            endTime: null,
            regStart: null,
            regEnd: null,
            eventType: 'Football',
            eventSubType: 'Training',
            creator: '',
            repeats: 1
        }
    },
    getDefaultProps: function(){
      return {
          listOfEventTypes: ['Football'],
          listOfEventSubTypes: ['Match', 'Training'],
          messages: {
              emptyFilter: {}
          }
      }
    },
    createEvent: function (event) {
        event.preventDefault();
        EventStore.addEvent(this.state).then(function (event) {
            window.location.hash = '/event/' + event.id;
        }.bind(this));
    },
    createMultipleEvent: function (event) {
        event.preventDefault();
        
        [...Array(this.state.repeats)].forEach((_, index) => {
            const current = {
                subject: this.state.subject,
                description: this.state.description,
                location: this.state.location,
                maxNumber: this.state.maxNumber,
                startTime: this.state.startTime.clone().addWeeks(index),
                endTime: this.state.endTime.clone().addWeeks(index),
                regStart: this.state.regStart.clone().addWeeks(index),
                regEnd: this.state.regEnd.clone().addWeeks(index),
                eventType: this.state.eventType,
                eventSubType: this.state.eventSubType,
                creator: this.state.creator
            };
            EventStore.addEvent(current);
        });
    },
    onValueChange: function (event) {
        var inputName = event.target.name;
        var stateValue = {};
        var value = event.target.value;
        if(inputName === 'maxNumber' || inputName === 'repeats') {
            value = value && !isNaN(parseInt(value)) ? parseInt(value) : value;
        }
        stateValue[inputName] = value;
        this.setState(stateValue);
    },
    startTimeChange: function (date) {
        this.setState({startTime: date});
    },
    endTimeChange: function (date) {
        this.setState({endTime: date});
    },
    regStartChange: function (date) {
        this.setState({regStart: date});
    },
    regEndChange: function (date) {
        this.setState({regEnd: date});
    },
    regEventType: function (eventType) {
        this.setState({eventType: eventType});
    },
    regEventSubType: function (eventSubType) {
        this.setState({eventSubType: eventSubType});
    },
    updateWithDefaultValue(defaultValue){
        this.setState({
            subject: defaultValue.subject,
            description: defaultValue.description,
            location: defaultValue.location,
            maxNumber: defaultValue.maxNumber,
            startTime: defaultValue.startTime,
            endTime: defaultValue.endTime,
            regStart: defaultValue.regStart,
            regEnd: defaultValue.regEnd,
            creator: defaultValue.creator,
            eventSubType: defaultValue.eventSubType
        });
    },
    defaultFootball: function() {
        var defaultValue = DefaultEvents.football();
        this.updateWithDefaultValue(defaultValue);
    },
    defaultFootballMatch: function() {
        var defaultValue = DefaultEvents.footballMatch();
        this.updateWithDefaultValue(defaultValue)
    },
    defaultFootballMatchOutdoor: function() {
        var defaultValue = DefaultEvents.footballMatchOutdoor();
        this.updateWithDefaultValue(defaultValue)
    },
    render: function () {
        return (
                <div>
                    <h2 className="page-header">Ny hendelse</h2>
                    <form className="form-horizontal">
                        <div className="form-group">
                            <span className="col-sm-offset-2 col-sm-3">
                                <button type="button" className="btn btn-default" onClick={this.defaultFootball}>Ferdigutfyll fotballtrening</button>
                            </span>
                            <span className="col-sm-3">
                                <button type="button" className="btn btn-default" onClick={this.defaultFootballMatch}>Ferdigutfyll fotballkamp (inne)</button>
                            </span>
                            <span className="col-sm-3">
                                <button type="button" className="btn btn-default" onClick={this.defaultFootballMatch}>Ferdigutfyll fotballkamp (ute)</button>
                            </span>
                        </div>
                        <div className="form-group">
                            <label htmlFor="subject" className="col-sm-2 control-label">Tittel</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="subject" ref="subject" placeholder="tittel" value={this.state.subject} onChange={this.onValueChange}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="creator" className="col-sm-2 control-label">Ansvarlig</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="creator" ref="creator" placeholder="ansvarlig" value={this.state.creator} onChange={this.onValueChange}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="description" className="col-sm-2 control-label">Beskrivelse</label>
                            <div className="col-sm-5">
                                <textarea className="form-control" type="text" name="description" ref="description" placeholder="kommentar" value={this.state.description} onChange={this.onValueChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="location" className="col-sm-2 control-label">Sted</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="location" ref="location" placeholder="sted" value={this.state.location} onChange={this.onValueChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="maxNumber" className="col-sm-2 control-label">Maks antall</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="maxNumber" ref="maxNumber" placeholder="maks antall" value={this.state.maxNumber} onChange={this.onValueChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="startTime" className="col-sm-2 control-label">Start</label>
                            <div className="col-sm-5">
                                <DateTimePicker css="btn-default" format="YYYY-MM-DD HH:mm" ref="startTime" value={this.state.startTime} onChange={this.startTimeChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="endTime" className="col-sm-2 control-label">Slutt</label>
                            <div className="col-sm-5">
                                <DateTimePicker css="btn-default" format="YYYY-MM-DD HH:mm"  ref="endTime" value={this.state.endTime} onChange={this.endTimeChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="regStart" className="col-sm-2 control-label">Registrering åpner</label>
                            <div className="col-sm-5">
                                <DateTimePicker css="btn-default" format="YYYY-MM-DD HH:mm"  ref="regStart" value={this.state.regStart} onChange={this.regStartChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="regEnd" className="col-sm-2 control-label">Registrering stenger</label>
                            <div className="col-sm-5">
                                <DateTimePicker css="btn-default" format="YYYY-MM-DD HH:mm"  ref="regEnd" value={this.state.regEnd} onChange={this.regEndChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="regEnd" className="col-sm-2 control-label">Idrett</label>
                            <div className="col-sm-5">
                                <Combobox
                                        data={this.props.listOfEventTypes}
                                        ref='name'
                                        filter={'contains'}
                                        value={this.state.eventType}
                                        onChange={this.regEventType}
                                        messages={{
                                                emptyList: 'Ingen treff',
                                                emptyFilter: 'Ingen treff'
                                            }}
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="regEnd" className="col-sm-2 control-label">Kamp/Trening</label>
                            <div className="col-sm-5">
                                <Combobox
                                        data={this.props.listOfEventSubTypes}
                                        ref='name'
                                        filter={'contains'}
                                        value={this.state.eventSubType}
                                        onChange={this.regEventSubType}
                                        messages={{
                                                emptyList: 'Ingen treff',
                                                emptyFilter: 'Ingen treff'
                                            }}
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="repeats" className="col-sm-2 control-label">Gjentas</label>
                            <div className="col-sm-5">
                                <input className="form-control" type="text" name="repeats" ref="repeats" placeholder="gjentas" value={this.state.repeats} onChange={this.onValueChange}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-offset-2 col-sm-5">
                                <button type="button" className="btn btn-default margin-right-5" onClick={this.createEvent}>Lagre</button>
                                <button type="button" className="btn btn-default" onClick={this.createMultipleEvent}>Lag ukentlig</button>
                            </div>
                        </div>
                    </form>
                </div>
        );
    }
});
module.exports = NewEvent;