// From https://github.com/appleboy/react-recaptcha
// Changed to fulfill requirements of loading script in different ways
'use strict';

var React = require('react');

var Recaptcha = React.createClass({
  propTypes: {
    className: React.PropTypes.string,
    CallbackName: React.PropTypes.string,
    elementID: React.PropTypes.string,
    onloadCallback: React.PropTypes.func,
    verifyCallback: React.PropTypes.func
  },

  getDefaultProps: function() {
    return {
      elementID: 'g-recaptcha',
      onloadCallback: undefined,
      onloadCallbackName: 'onloadRecaptchaCallback',
      verifyCallback: undefined,
      render: 'onload',
      theme: 'light',
      type: 'image'
    };
  },
  renderAsyncRecaptcha: function(){
    grecaptcha.render(this.props.elementID, {
      'sitekey': this.props.sitekey,
      'callback': (this.props.verifyCallback) ? this.props.verifyCallback : undefined,
      'theme': this.props.theme,
      'type': this.props.type
    });

    if (this.props.onloadCallback) {
      this.props.onloadCallback();
    }
  },
  componentDidMount: function () {
    // Check if the grecaptcha is already loaded, if not - add a callback
    if(window.grecaptcha) {
      this.renderAsyncRecaptcha();
    } else {
      window[this.props.onloadCallbackName] = function () {
        this.renderAsyncRecaptcha()
      }.bind(this);
    }
  },
  render: function() {
      return (
        <div id={this.props.elementID}
          data-onloadcallbackname={this.props.onloadCallbackName}
          data-verifycallbackname={this.props.verifyCallbackName}
          >
        </div>
      );
  }
});

module.exports = Recaptcha;
