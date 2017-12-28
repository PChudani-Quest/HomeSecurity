var admin = require("firebase-admin");
var config = require('./config');

var serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: config.firebase.databaseURL,
  storageBucket: config.firebase.storageBucket
});

function Firebase(conf) {
  this.startFunction = conf.startFunction;
  this.stopFunction = conf.stopFunction;
  this.getStatus = conf.getStatus;

  var db = admin.database();
  // Get the Storage service for the default app
  this.defaultStorage = admin.storage();

  this.initControlRequests(db);
  this.initStatusRequests(db);
}

Firebase.prototype.initControlRequests = function(db) {
  var ref = db.ref("control_requests");
  ref.on('child_added', function(data) {
    var request = data.val();
    console.log("New control request", request);
	
    if (request.action == "STOP" && !request.processed) {
       this.stopFunction();
       db.ref("control_requests/" + data.key).child('processed').set(true);
    } else if (request.action == "START" && !request.processed) {
       this.startFunction();
       db.ref("control_requests/" + data.key).child('processed').set(true);
    }
  }.bind(this));
}

Firebase.prototype.initStatusRequests = function(db) {
  var ref = db.ref("status_requests");
  ref.on('child_added', function(data) {
    var request = data.val();
    console.log("New status request", request);
	
    if (!request.processed) {
       db.ref("status_requests/" + data.key).child('status').set(this.getStatus());
       db.ref("status_requests/" + data.key).child('date').set(new Date().getTime());
       db.ref("status_requests/" + data.key).child('processed').set(true);
    }
  }.bind(this));
}

Firebase.prototype.storeSecurityImageMetadata = function(conf) {
  var db = admin.database();
  var ref = db.ref("security_images");
  ref.push().set({
    timestamp: new Date().getTime(),
    name: conf.name,
    downloadUrl: conf.downloadUrl 
  });
}

Firebase.prototype.storeFile = function(image, callback) {
/*this.defaultStorage
    .bucket()
    .getFiles()
    .then(results => {
      const files = results[0];

      console.log('Files:');
      files.forEach(file => {
        console.log(file.name);
      });
    })
    .catch(err => {
      console.error('ERROR:', err);
    });*/

   this.defaultStorage
    .bucket()
    .upload(image)
    .then((err, newFile, apiResponse) => {
       const file = this.defaultStorage
        .bucket()
        .file(image)
        .getSignedUrl({
          action: 'read',
          expires: '03-09-2491'
        }).then(signedUrls => {
          callback(signedUrls[0]);
        });

      console.log(`${image} uploaded to Firebase.`);
    })
    .catch(err => {
      console.error('ERROR:', err);
    });
}

exports.Firebase = Firebase;