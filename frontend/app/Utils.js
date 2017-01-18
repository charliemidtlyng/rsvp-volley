require('datejs');
require('datejs/src/i18n/nb-NO');
Date.i18n.setLanguage('nb-NO');
var Utils = {
    formatDateTime: function (timestamp, formatter) {
        var formatter = formatter || 'yyyy-MM-dd kl. HH:mm:ss';
        if (timestamp) {
            return new Date(timestamp).toString(formatter);
        }
        return '';
    },
    sortByTimestampDesc: function (eventA, eventB) {
        return eventB.startTime - eventA.startTime;
    },
    sortByTimestampAsc: function (eventA, eventB) {
        return eventA.startTime - eventB.startTime;
    },
    isOldEvent: function (event) {
        return Date.today().isAfter(new Date(event.startTime));
    },
    timeStampToDate: function (timestamp) {
        if (timestamp) {
            var dayName = new Date(timestamp).toString('dddd');
            return dayName.charAt(0).toUpperCase() + dayName.slice(1);
        }
        return '';
    }
};

module.exports = Utils;
