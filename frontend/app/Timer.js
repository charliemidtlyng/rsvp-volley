var React = require('react');
var ReactBootstrap = require('react-bootstrap');
var Alert = ReactBootstrap.Alert;
var Timer = React.createClass({

    getInitialState: function () {
        return {
            elapsed: 0
        };
    },
    componentWillMount: function () {
        this.timer = setInterval(this.tick, 1000);
    },
    componentWillUnmount: function () {
        clearInterval(this.timer);
    },
    tick: function () {
        if (this.diff() <= 0) {
            clearInterval(this.timer);
            this.setState({elapsed: this.props.remainingTime * 1000});
        } else {
            this.setState({elapsed: this.state.elapsed + 1});
        }
    },
    diff: function () {
        return (this.props.remainingTime / 1000).toFixed(0) - this.state.elapsed;
    },
    secondsToTime: function (remainingSeconds) {
        var returnString = '';
        var days = Math.floor(remainingSeconds / 3600 / 24);
        var hours = Math.floor(( remainingSeconds / 3600 ) % 24);
        var minutes = Math.floor(( remainingSeconds / 60 ) % 60);
        var seconds = (remainingSeconds % 60).toFixed(0);

        if (days > 0) {
            var dayString = days > 1 ? ' dager ' : ' dag ';
            returnString += days + dayString;
        }
        if (hours > 0) {
            var hourString = hours > 1 ? ' timer ' : ' time ';
            returnString += hours + hourString;
        }
        if (minutes > 0) {
            var minuteString = minutes > 1 ? ' minutter ' : ' minutt ';
            returnString += minutes + minuteString;
        }
        returnString += seconds + ' sekunder';

        return returnString;

    },
    alertWrapper: function (content, open) {
        return <Alert bsStyle={open ? 'success' : 'danger'} className="col-xs-12"><h3 className="no-margin">{content}</h3></Alert>;
    },
    render: function () {
        if (this.diff() <= 0) {
            return this.alertWrapper(<div>Påmeldingen er åpnet!</div>, true);
        }
        return this.alertWrapper(<div>{this.secondsToTime(this.diff())} til påmeldingen åpner</div>, false);
    }
});
module.exports = Timer;
