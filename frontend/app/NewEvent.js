/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var Router = require('react-router');
var ReactWidgets = require('react-widgets');
var DateTimePicker = ReactWidgets.DateTimePicker;
var Link = Router.Link;
var NewEvent = React.createClass({

    mixins: [Router.Navigation],
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
            creator: ''
        }
    },
    createEvent: function (event) {
        event.preventDefault();
        EventStore.addEvent(this.state, function (event) {
            this.transitionTo('event', {id: event.id});
        }.bind(this));
    },
    onValueChange: function (event, x, y) {
        var inputName = event.target.name;
        var stateValue = {};
        var value = event.target.value
        value = inputName == 'maxNumber' ? parseInt(value) : value;
        stateValue[inputName] = value;
        this.setState(stateValue);
    },
    startTimeChange: function (date, dateString) {
        this.setState({startTime: date});
    },
    endTimeChange: function (date, dateString) {
        this.setState({endTime: date});
    },
    regStartChange: function (date, dateString) {
        this.setState({regStart: date});
    },
    regEndChange: function (date, dateString) {
        this.setState({regEnd: date});
    },
    render: function () {
        return (
                <form className="form-horizontal" onSubmit={this.createEvent}>
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
                            <DateTimePicker css="btn-default" format="yyyy-MM-dd HH:mm" ref="startTime" value={this.state.startTime} onChange={this.startTimeChange} />
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="endTime" className="col-sm-2 control-label">Slutt</label>
                        <div className="col-sm-5">
                            <DateTimePicker css="btn-default" format="yyyy-MM-dd HH:mm"  ref="endTime" value={this.state.endTime} onChange={this.endTimeChange} />
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="regStart" className="col-sm-2 control-label">Registrering åpner</label>
                        <div className="col-sm-5">
                            <DateTimePicker css="btn-default" format="yyyy-MM-dd HH:mm"  ref="regStart" value={this.state.regStart} onChange={this.regStartChange} />
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="regEnd" className="col-sm-2 control-label">Registrering stenger</label>
                        <div className="col-sm-5">
                            <DateTimePicker css="btn-default" format="yyyy-MM-dd HH:mm"  ref="regEnd" value={this.state.regEnd} onChange={this.regEndChange} />
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="col-sm-offset-2 col-sm-5">
                            <button type="submit" className="btn btn-default">Lagre</button>
                        </div>
                    </div>
                </form>
        );
    }
});
module.exports = NewEvent;