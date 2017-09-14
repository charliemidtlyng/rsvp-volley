
var React = require('react');
var Utils = require('../utils/Utils');
var ReactBootstrap = require('react-bootstrap');
var Tooltip = ReactBootstrap.Tooltip;
var OverlayTrigger = ReactBootstrap.OverlayTrigger;

var ImportEventsItem = React.createClass({
   render: function() {
       var event = this.props.event;
       var tipId = "id-"+event.startTime;
       var tooltip = <Tooltip id={tipId}><p className="pre-wrap">{this.props.event.description}</p></Tooltip>;
       var isSaveable = event.id === null;
       return (
               <tr>
                   <td>{Utils.timeStampToDate(event.startTime)} {Utils.formatDateTime(event.startTime, 'dd.MM.yyyy')} {Utils.formatDateTime(event.startTime, 'HH:mm')} - {Utils.formatDateTime(event.endTime, 'HH:mm')}</td>
                   <td>{event.subject} - {event.location}</td>
                   <td>{event.maxNumber}</td>
                   <td> <OverlayTrigger placement='top' overlay={tooltip}><span>info</span></OverlayTrigger></td>
                   <td>{isSaveable ? <button type="button" className="btn-default btn-danger" onClick={this.props.createEvent(event)}>Legg til kamp</button>: <span>Lagret</span>}</td>
               </tr>);
   }

});

var ImportEventsList = React.createClass({

    createEvent: function (event) {
        return function(){
            this.props.createEvent(event);
        }.bind(this)
    },
    render: function () {
        var events = this.props.events.map(function(event, i) {
            return <ImportEventsItem event={event} createEvent={this.createEvent} key={i}/>;
        }.bind(this));
        return events.length == 0 ? <div/> : (
                <div>
                    <h2 className="page-header">Genererte kamper</h2>
                    <table className="table table-striped table-bordered"><tbody>{events}</tbody></table>
                </div>
        );
    }
});
module.exports = ImportEventsList;