
const EventEmitter = require('events');
const userCollection = 'user';
const groupCollection = 'group';
const groupMapCollection = 'groupMap';
const Util = require('../lib/util');

class Group extends EventEmitter {
    AddGroup(firebase, request) {
        var Emitter=this;
        var util=new Util()
        var data = request;
        var mobiles = data.users;
        var userId = data.userId;
        var dataResponse = util.data();
        var query = firebase.firestore().collection(userCollection).where('id','==',userId);
        query.get().then(function(querySnapshot){
            if(querySnapshot.empty){
                var responseData = util.data();
                responseData.message = "User doesn't exist";
                Emitter.emit('AddGroupEvent', responseData);
                return;
            }else{
                var loggedInMobile;
                querySnapshot.forEach(function (doc) {
                    loggedInMobile = doc.data().mobile;
                });
                mobiles.push(loggedInMobile);
                var groupId = util.idGenerator("groupCollection");
                var docData = {
                    id: groupId,
                    title: data.title,
                    description: data.description,
                    admin: loggedInMobile,
                    createdDate:util.currentDate(),
                    updatedDate:util.currentDate(),
                    active: true,
                    tags: null
                };
                firebase.firestore().collection(groupCollection).add(docData).then(function(qs){
                    for(var i=0; i < mobiles.length; i++) {
                        console.log(mobiles[i]);
                        var mobile = mobiles[i];
                        var data = {
                            id: util.idGenerator("GroupMap"),
                            createdDate:Date.now().toString(),
                            updatedDate:Date.now().toString(),
                            groupId: groupId,
                            user: mobile
                        };
                        firebase.firestore().collection(groupMapCollection).add(data);
                    }
                    dataResponse.status = 200;
                    dataResponse.data.push(docData);
                    dataResponse.message = "Group created successfully.";
                    Emitter.emit('AddGroupEvent', dataResponse);
                });
            }
        });
    }

    GetGroups(firebase,request)
    {
        var Emitter=this;
        var util=new Util();
        var data = request.params;
        var userMobile = data.mobile;
        var dataResponse = util.data();
        if (userMobile != "" && userMobile != "undefined") {
            firebase.firestore().collection(groupMapCollection).where('user', '==', userMobile).get()
                .then(groupMapSnapshot => {
                    var groupIDs = [];
                    if (groupMapSnapshot.empty) {
                        dataResponse.status = 400;
                        dataResponse.errorMessage = "No Groups for this user."
                        Emitter.emit('GetGroupEvent', dataResponse);
                    }
                    else {
                        groupMapSnapshot.forEach(d => {
                            groupIDs.push(d.data().groupId);
                        });
                        groupIDs = groupIDs.filter(util.onlyUnique);
                        var count = 0;
                        groupIDs.forEach(function (groupId) {
                            var query = firebase.firestore().collection(groupCollection);
                            query = query.where('id', '==', groupId);
                            query.get().then(groupSnapshot => {
                                groupSnapshot.forEach(group => {
                                    firebase.firestore().collection(userCollection).where('mobile', '==', group.data().admin).get()
                                        .then(userSnapshot => {
                                            var adminInfo;
                                            userSnapshot.forEach(user => {
                                                adminInfo = {
                                                    mobile: user.data().mobile,
                                                    name: user.data().name
                                                };
                                            });
                                            var groupData = {
                                                id: group.data().id,
                                                tags: null,
                                                title: group.data().title,
                                                createdDate: group.data().createdDate,
                                                description: group.data().description,
                                                active: true,
                                                admin: adminInfo,
                                                updatedDate: group.data().updatedDate
                                            };
                                            dataResponse.data.push(groupData);
                                            count = count + 1;
                                            if (groupIDs.length == count) {
                                                dataResponse.status = 200;
                                                dataResponse.message = "Groups retrieved.";
                                                Emitter.emit('GetGroupEvent', dataResponse);
                                            }
                                        });
                            });
                        });
                    });
                }
                }).catch(err => {
                    console.log('Error getting documents', err);
                });
        }
        else {
            firebase.firestore().collection(groupCollection).get().then(groupSnapshot => {
                var count = 0;
                groupSnapshot.forEach(group => {
                    firebase.firestore().collection(userCollection).where('mobile', '==', group.data().admin).get()
                        .then(userSnapshot => {
                            var adminInfo;
                            userSnapshot.forEach(user => {
                                adminInfo = {
                                    mobile: user.data().mobile,
                                    name: user.data().name
                                };
                            });
                            var groupData = {
                                id: group.data().id,
                                tags: null,
                                title: group.data().title,
                                createdDate: group.data().createdDate,
                                description: group.data().description,
                                active: true,
                                admin: adminInfo,
                                updatedDate: group.data().updatedDate
                            };
                            dataResponse.data.push(groupData);
                            count = count + 1;
                            if (groupSnapshot.size == count) {
                                dataResponse.status = 200;
                                dataResponse.message = "Groups retrieved.";
                                Emitter.emit('GetGroupEvent', dataResponse);
                            }
                        });
                });
    
            });
        }
    }

    getGroupMembers(firebase, request){
        var Emitter = this;
        var util = new Util();
        var data = request.params;
        var groupId = data.groupId;
        var dataResponse = util.data();
        if (groupId) {
            firebase.firestore().collection(groupMapCollection).where('groupId', '==', groupId).get()
                .then(groupMapSnapshot => {
                    var groupMembers = [];
                    if (groupMapSnapshot.empty) {
                        dataResponse.status = 400;
                        dataResponse.errorMessage = "No members for this group."
                        Emitter.emit('GetGroupMembers', dataResponse);
                    }
                else {
                    groupMapSnapshot.forEach(d => {
                        groupMembers.push(d.data().user);
                    });
                    groupMembers = groupMembers.filter(util.onlyUnique);
                    var count = 0;
                    groupMembers.forEach(function (member) {
                        var query = firebase.firestore().collection(userCollection);
                        query = query.where('mobile', '==', member);
                        query.get().then(memberSnapshot => {
                            if(memberSnapshot.empty){
                                count = count + 1;
                            }else{
                                memberSnapshot.forEach(user => {
                                    var memberData = {
                                        name: user.data().name,
                                        mobile: member
                                    };
                                    dataResponse.data.push(memberData);
                                    count = count + 1;
                                });
                            }
                            if (groupMembers.length == count) {
                                dataResponse.status = 200;
                                dataResponse.message = "Group Members Retrieved.";
                                Emitter.emit('GetGroupMembers', dataResponse);
                            }
                        });
                    });
                }
            }).catch(err => {
                console.log('Error getting documents', err);
                dataResponse.status = 500;
                dataResponse.errorMessage = "Internal Server Error.";
                Emitter.emit('GetGroupMembers', dataResponse);
            });
        }
        else {
            dataResponse.status = 400;
            dataResponse.errorMessage = "Invalid Request. Please pass groupId.";
            Emitter.emit('GetGroupMembers', dataResponse);
        } 
    }
}

module.exports = Group;

