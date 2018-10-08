
const EventEmitter = require('events');
const userCollection = 'user';
const answerCollection = 'answer';
const Util = require('../lib/util');

class Answer extends EventEmitter {
    GetAnswer(firebase, request) {
        var Emitter = this;
        var util = new Util()
        var reqData = request;
        var questionId = reqData.params.questionId;
        var data = [];
        firebase.firestore().collection(answerCollection).where('questionId', '==', questionId).get()
            .then(function (querySnapshot) {
                querySnapshot.forEach(function (doc) {
                    data.push(doc.data());
                });
                var responseData = util.data();
                responseData.data = data;
                responseData.status = 200;
                Emitter.emit('GetAnswerEvent', responseData);
            });
    }

    SaveAnswer(firebase, request) {
        var Emitter = this;
        var util = new Util()
        var data = request;
        var userId = data.user.id;
        var user;
        var query = firebase.firestore().collection(userCollection).where('id', '==', userId);
        query.get().then(function (querySnapshot) {
            if (querySnapshot.empty) {
                var responseData = util.data();
                responseData.message = "User doesn't exist";
                Emitter.emit('AddAnswerEvent', responseData);
                return;
            } else {
                querySnapshot.forEach(function (doc) {
                    user = doc.data();
                });
            }

            var docData = {
                id: util.idGenerator("Answer"),
                createdDate: util.currentDate(),
                updatedDate: util.currentDate(),
                answer: data.answer,
                questionId: data.questionId,
                user: {
                    id: userId,
                    name: user.name
                },
                isFinal: false,
                isBlocked: false,
                media: null
            };
            firebase.firestore().collection(answerCollection).add(docData).then(function (res) {
                var responseData = util.data();
                responseData.data.push(docData);
                responseData.message = "Answer added successfully.";
                Emitter.emit('AddAnswerEvent', responseData);
            });
        });
    }
}
module.exports = Answer;

