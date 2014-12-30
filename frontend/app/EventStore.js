var Promise = require('promise-js');
var API = '/api/events';
var EventStore = module.exports = {

    addEvent: function (event) {
        return postJSON(API, event)
    },

    removeEvent: function (id) {
        return deleteJSON(API + '/' + id);
    },

    getEvents: function () {
        return getJSON(API);
    },

    getEvent: function (id) {
        return getJSON(API + '/' + id);
    },

    registerForEvent: function(id, participant) {
        return postJSON(API + '/' + id + '/register', participant);
    },
    unregisterForEvent: function(id, participantId) {
        return deleteJSON(API + '/' + id + '/register/' + participantId);
    }
};

function getJSON(url) {
    return new Promise(function(resolve, reject) {
        // Do the usual XHR stuff
        var req = new XMLHttpRequest();
        req.open('GET', url);

        req.onload = function() {
            if (req.status >= 200 && req.status <= 300) {
                resolve(JSON.parse(req.response));
            }
            else {
                reject(Error(req.statusText));
            }
        };

        // Handle network errors
        req.onerror = function() {
            reject(Error("Network Error"));
        };
        // Make the request
        req.send();
    });

}

function postJSON(url, obj) {
    return new Promise(function(resolve, reject) {
        var req = new XMLHttpRequest();
        req.onload = function() {
            if (req.status >= 200 && req.status <= 300) {
                resolve(JSON.parse(req.response));
            }
            else {
                reject(Error(req.statusText));
            }
        };
        req.open('POST', url);
        req.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
        req.send(JSON.stringify(obj));
    });
}

function deleteJSON(url) {
    return new Promise(function(resolve, reject) {
        var req = new XMLHttpRequest();
        req.onload = function() {
            if (req.status >= 200 && req.status <= 300) {
                resolve(req.response);
            }
            else {
                reject(Error(req.statusText));
            }
        };
        req.open('DELETE', url);
        req.send();
    });
}
