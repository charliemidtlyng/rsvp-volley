var React = require('react');

var EventImage = React.createClass({
    render: function(){
        var images = {
            general: '/css/people.png',
            trophy: '/css/sport.png'
        };
        console.log(this.props)
        var imageSrc = this.props.event.eventSubType === 'Match' ? images.trophy : images.general;
        return <img className="margin-right-5 pull-left" src={imageSrc} />
    }
});

module.exports = EventImage;