const EventEmitter = require('events');
const userCollection = 'user';
const Util = require('../lib/util');

class User extends EventEmitter {
 
    
    login(firebase, request) {
        var Emitter=this;
        var util=new Util()
        var data = request;
        var mobile = data.mobile;
        var password = data.password;
        var dataResponse = util.data();
        var query = firebase.firestore().collection(userCollection).where('mobile','==',mobile).where('password','==',password);
        query.get().then(function(querySnapshot){
            if(querySnapshot.empty){
                dataResponse.status = 400;
                dataResponse.errorMessage = "Invalid Login.";
                Emitter.emit('LoginEvent', dataResponse);

            }else{
                querySnapshot.forEach(function(doc) {
                    dataResponse.data.push(doc.data());
                    dataResponse.message = "Successfull Login.";
                }); 
                Emitter.emit('LoginEvent', dataResponse);
            }
        });
    }
    registerUser(firebase, request) {
        var Emitter=this;
        var util=new Util()

        var data = request;
        var mobile = data.mobile;
        var isExists = 1;
        var dataResponse = util.data();
        var registerfirebase=firebase;
        var query = firebase.firestore().collection(userCollection).where('mobile','==',mobile);
        query.get().then(function(querySnapshot){
            if(querySnapshot.empty){
                isExists = 0;
            }
    
            if(isExists==0){
                    var docData = {
                    id: util.idGenerator("User"),
                    mobile: data.mobile,
                    password: data.password,
                    name: data.name,
                    createdDate:util.currentDate(),
                    updatedDate:util.currentDate()
                };
                console.log(docData );
                firebase.firestore().collection(userCollection).add(docData).then(function(res){
                    dataResponse.status = 200;
                    dataResponse.data.push(docData);
                    dataResponse.message = "Successfull Registration.";
                    Emitter.emit('RegisterEvent', dataResponse);
                });
               

            }else{
                dataResponse.status = 400;
                dataResponse.errorMessage = "User already exists with this mobile number.";
                Emitter.emit('RegisterEvent', dataResponse);
            }
        });
    }
}

module.exports = User;
