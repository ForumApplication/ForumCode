class Util {
    data() {
        var dataResponse = {
            status: 200,
            data: [],
            message: "",
            errorMessage: ""
        };
        return dataResponse;
    }
    idGenerator(x) {
        var timestamp = new Date;
        var id = Math.random().toString(36).substr(2, 9);
        var uniqueId = x.toString() + timestamp.getTime().toString(36) + id;
        return uniqueId;
    }
    apis() {
        var apis = {
            questionCollection: 'question',
            user: 'user',
            answerCollection: 'answer'
        };
        return apis;
    }
    onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
    }
    currentDate() {
        var today = new Date();
        var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        var dateTime = date + ' ' + time;
        return dateTime;
    }
}
module.exports = Util;
