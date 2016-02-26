

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
    football: function () {
        return {
            subject: 'Trening i Vallhall',
            description: 'Oppmøte 19:45!\nFotballtrening i Vallhall. Banen ligger i \nmidten - lengst unna inngangsdøra. \nAnkomst: \nBuss #21 stopper på Valle. Gå derfra igjennom Vallefaret. \nT-bane #1, #2, #3 og #4 stopper på Helsfyr. Gå av mot Valle Hovin. \n\nRing 99402316 (Charlie) hvis du er i tvil. \n\n¡Avmelding!\nHvis det viser seg at du allikevel ikke \nkan komme, meld deg av så snart \ndu vet dette - og si ifra til nestemann \npå lista (evt. Socialcast). \n\nLegger du ved epost/mobilnr får du \nmail/sms så snart du forflytte...',
            location: 'Vallhall',
            maxNumber: 15,
            startTime: nextMonday(20, 0),
            endTime: nextMonday(21, 0),
            regStart: nextFriday(8, 30),
            regEnd: nextMonday(21, 0),
            eventSubType: 'Training',
            creator: 'CM'
        };
    },
    footballMatch: function () {
        return {
            subject: 'Kamp X. laget',
            description: 'Oppmøte ....!\nKamp mot ... . Husk leggskinner og svart drakt! \nAnkomst: \nT-bane #4 til Manglerud - ca. 10-15 minutter å gå.',
            location: 'Manglerudhallen',
            maxNumber: 8,
            startTime: nextMonday(20, 0),
            endTime: nextMonday(21, 0),
            regStart: Date.today(),
            regEnd: nextMonday(21, 0),
            eventSubType: 'Match',
            creator: 'CM'
        };
    }
};
module.exports = DefaultEvents;