var React = require('react');

var Loader = React.createClass({
    render: function () {
        if (this.props.isLoading) {
            return <div className='spinner'></div>;
        } else {
            return <div>{this.props.children}</div>;
        }
    }
});
module.exports = Loader;
