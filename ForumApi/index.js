const express = require('express');
const Firebase=require('./lib/firebase.js');
const app = express();
app.use(express.json());
const port=process.env.PORT||3000;


app.post('/api/GetQuestions',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const GetQuestionModule=require('./question/Question.js');
    const getQuestion=new GetQuestionModule();
    getQuestion.on('QuestionLoaded',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    getQuestion.getQuestions(firebaseInstance,req);

});

app.post('/api/AddQuestion',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const AddQuestionModule=require('./question/Question.js');
    const addQuestion=new AddQuestionModule();
    addQuestion.on('QuestionSaved',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    addQuestion.addQuestion(firebaseInstance,req.body);
});
app.post('/api/login',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const LoginModule=require('./user/User.js');
    const login=new LoginModule();
    login.on('LoginEvent',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    
    login.login(firebaseInstance,req.body);
});
app.post('/api/register',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const RegisterModule=require('./user/User.js');
    const register=new RegisterModule();
    register.on('RegisterEvent',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    register.registerUser(firebaseInstance,req.body);
});
app.post('/api/saveAnswer',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const AnswerModule=require('./answer/Answer.js');
    const answer=new AnswerModule();
    answer.on('AddAnswerEvent',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    answer.SaveAnswer(firebaseInstance,req.body);
});

app.get('/api/getAnswer/:questionId',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const AnswerModule=require('./answer/Answer.js');
    const answer=new AnswerModule();
    answer.on('GetAnswerEvent',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    answer.GetAnswer(firebaseInstance,req);
});
app.post('/api/addGroup',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const GroupModule=require('./group/Group.js');
    const group=new GroupModule();
    group.on('AddGroupEvent',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    group.AddGroup(firebaseInstance,req.body);
});

app.get('/api/getGroups/:mobile',(req,res)=>{
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const GroupModule=require('./group/Group.js');
    const group=new GroupModule();
    group.on('GetGroupEvent',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    group.GetGroups(firebaseInstance,req);
});

app.get('/api/getGroupMembers/:groupId',(req,res)=>{    
    const firebaseUtil=new Firebase();
    var firebaseInstance=    firebaseUtil.getFirebase();
    const GroupModule=require('./group/Group.js');
    const group=new GroupModule();
    group.on('GetGroupMembers',(data)=>{
        res.status(parseInt(data.status)).send(data);
    });
    group.getGroupMembers(firebaseInstance,req);
});

app.listen(port, () => {
    console.log(`Listening on port ${port}`);
});

