require('datejs');

var Utils = {
    formatDateTime: function (timestamp, formatter) {
        var formatter = formatter || 'yyyy-MM-dd kl. HH:mm:ss';
        if(timestamp) {
            return new Date(timestamp).toString(formatter);
        }
        return '';
    }
};

module.exports = Utils;
