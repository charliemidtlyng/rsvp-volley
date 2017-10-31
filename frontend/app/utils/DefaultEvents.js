

function nextMonday(hour, minute) {
    var isMonday = Date.today().is().monday();
    var monday = isMonday ? Date.today() : Date.today().next().monday();
    monday.set({hour: hour, minute: minute, second: 0});
    return monday;
}

function nextFriday(hour, minute) {
    var isFriday = Date.today().is().friday();
    var friday = isFriday ? Date.today() : Date.today().next().friday();
    friday.set({hour: hour, minute: minute, second: 0});
    return friday;
}

var DefaultEvents = {
    training: function () {
        return {
            subject: 'Trening',
            description: 'Oppmøte 19:45!\nTrening i OBIK-hallen. Hallen ligger rett bak Oslo Skatepark. \n\n¡Avmelding!\nHvis det viser seg at du allikevel ikke \nkan komme, meld deg av så snart \ndu vet dette - og si ifra til nestemann \npå lista (evt. Slack). \n\nLegger du ved epost/mobilnr får du \nmail/sms så snart du forflytter deg \nfra reservelista til påmeldtlista!',
            location: 'OBIK-hallen',
            maxNumber: 12,
            startTime: nextMonday(20, 0),
            endTime: nextMonday(21, 0),
            regStart: nextFriday(8, 30),
            regEnd: nextMonday(21, 0),
            eventSubType: 'Training',
            creator: 'Auto'
        };
    },
    match: function () {
        return {
            subject: 'Kamp',
            description: 'Oppmøte ....!\nKamp mot ... .',
            location: 'Ekeberg',
            maxNumber: 8,
            startTime: nextMonday(20, 0),
            endTime: nextMonday(21, 0),
            regStart: Date.today(),
            regEnd: nextMonday(21, 0),
            eventSubType: 'Match',
            creator: 'Auto'
        };
    }
};
module.exports = DefaultEvents;