const firebase = require('firebase-admin');
var serviceAccount = require('../google-services.json');


class FirebaseUtil{
 
    getFirebase()
    {
        if(firebase.apps.length === 0)
        {
            firebase.initializeApp({
                credential: firebase.credential.cert(serviceAccount),
                databaseURL: 'https://questionstudy-79a39.firebaseio.com'
            });
        }
        return firebase;
    }

}
module.exports= FirebaseUtil;