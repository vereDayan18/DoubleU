var PORT = process.env.PORT || 3000;
const express = require('express');
const admin = require('firebase-admin');
var serviceAccount = require("./finalproject-de28d-firebase-adminsdk");
const crypto = require("crypto");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://finalproject-de28d.firebaseio.com"
});

const db = admin.database();
let app = express();
app.use(express.json());

var ref = db.ref("/volunteers");
var ref_req = db.ref("/Requests");

var topic = 'requests';

// As an admin, the app has access to read and write all data
// Attach an asynchronous callback to read the data at our posts reference
// In case of Node.js, the listener function is called anytime new data is added to your database reference or changed
var dataFromDb = ref_req.on("value", function(snapshot) {
    console.log(snapshot.val());
  }, function (errorObject) {
    console.log("The read failed: " + errorObject.code);
  });


// This is when the elderly sends a request for help. It sends notification
// to all the volunteers with the relevant data
app.post('/postRequest', (req, res, next) => {
    console.log("Got POST request to /postRequest");
    let requestedDomain = req.body.domain;
    let name = req.body.name;
    let phoneNumber = req.body.phoneNumber;
    
    // A new db entry is created via the app
    // A notification of Firebase is sent to all the volunteers
    writeInDB(requestedDomain, name, phoneNumber);
    sendToFCM(name, requestedDomain);
    res.status(200).json({msg: "OK"});
});


// This method is resposible to send FCM the data, so that it could send a
// notification to the user
function sendToFCM(elderlyName, requestedDomain){
    var payload = {
        data: {
            notification: `You have a new request from ${elderlyName}`
        },
        notification:{ body: `You have a new request from ${elderlyName}`},
        topic: topic
    };
    // Send a message to devices subscribed to the provided topic.
    admin.messaging().send(payload)
        .then(function(response) {
        console.log("Successfully sent message:", response);
    })
        .catch(function(error) {
        console.log("Error sending message:", error);
    });
}

function writeInDB(requestedDomain, name, phoneNumber){
    const id = crypto.randomBytes(16).toString("hex");
    db.ref(`/Requests/${id}`).set({
    domain: requestedDomain,
    key: id,
    name: name,
    phoneNumber: phoneNumber
});
}

app.listen(PORT, () => {
    console.log(`Listening on port ${PORT}`);
});
