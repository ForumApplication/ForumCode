const EventEmitter = require('events');
var questionCollection = 'question';
const userCollection = 'user';
const Util = require('../lib/util');

class Question extends EventEmitter {

    addQuestion(firebase, request) {
        var data = request;
        var userId = data.user.id;
        var user;
        var Emitter = this;
        var util = new Util();
        var query = firebase.firestore().collection(userCollection).where('id', '==', userId);
        query.get().then(function (querySnapshot) {
            if (querySnapshot.empty) {
                var responseData = util.data();
                responseData.status = 404;
                responseData.message = "User doesn't exist";
                Emitter.emit('QuestionSaved', responseData);
            } else {
                querySnapshot.forEach(function (doc) {
                    console.log(doc.data());
                    user = doc.data();
                });

                var fileUrls = data.fileUrls;
                var files = [];
                var path = "https://s3.ap-south-1.amazonaws.com/forumapplication/userfiles/";
                fileUrls.forEach(function(url){
                    var value = path+url;
                    var keyMap = {
                        id: util.idGenerator("File"),
                        url: value
                    };
                    files.push(keyMap);
                  });
                var docData = {
                    id: util.idGenerator("Question"),
                    title: data.title,
                    description: data.description,
                    createdDate: util.currentDate(),
                    updatedDate: util.currentDate(),
                    user: {
                        id: userId,
                        name: user.name
                    },
                    group: null,
                    answers: null,
                    isPublic: true,
                    isSolved: false,
                    fileUrls:files,
                    tags: null
                };
                firebase.firestore().collection(questionCollection).add(docData).then(function (res) {
                    var responseData = util.data();
                    responseData.data.push(docData);
                    responseData.message = "Question Added Successfully.";
                    Emitter.emit('QuestionSaved', responseData);
                });
            }

        });
    }
    getQuestions(firebase, request) {
        var Emitter = this;
        var util = new Util();
        var userId = request.body.userId;
        var groupId = request.body.groupId;
        var dataResponse = util.data();
        var query = firebase.firestore().collection(questionCollection);
        if (userId) {
            query = query.where('user.id', '==', userId);
        } else if (groupId) {
            query = query.where('group', '==', groupId);
        } else {
            query = query.where('isPublic', '==', true);
        }

        query.get().then(function (querySnapshot) {
            if (querySnapshot.empty) {
                dataResponse.status = 404;
                dataResponse.message = "No questions found.";
                Emitter.emit('QuestionLoaded', dataResponse);
            } else {
                var data = [];
                querySnapshot.forEach(function (doc) {
                    data.push(doc.data());
                });
                dataResponse.data = data;
                dataResponse.status = 200;
                Emitter.emit('QuestionLoaded', dataResponse);
            }
        });
    }
}
module.exports = Question;


