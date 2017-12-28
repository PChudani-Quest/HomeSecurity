var buzzer = require('./buzzer');
var pir = require('./pir');
var firebase = require('./firebase');
var webcam = require('./webcam');
var mailer = require('./mailer');
var light = require('./light');
var fs = require('fs');

var iWebcam = new webcam.Webcam();
var iMailer = new mailer.Mailer();

var iLight = new light.Light({
  gpio: 26
});

var iBuzzer = new buzzer.Buzzer({
  gpio: 20
});

var iPir = new pir.PIR({
  gpio: 17,
  callback: function() {
    iMailer.sendMail({
     subject: 'Alarm',
     content: 'Alarm ve sklepe zachytil pohyb. Zkontroluj zaznam z kamery'
    });
    iLight.on();
    iBuzzer.buzz(3000);

    var shotNumber = 1;
    var cameraTimer = setInterval(function(){ 
      var d = new Date();
      var name = d.getDate() + "_" + (d.getMonth() + 1) + "_" + d.getFullYear() + "_" 
	+ d.getHours() + "_" + d.getMinutes() + "_" + shotNumber;
      var afterUpload = function(signedUrl) {
	fs.unlink(name + ".jpg", (err) => {
	  if (err) {
            console.log(err);
            return;
          }
	  console.log(`Successfully deleted ${name}`);
        });
        iFirebase.storeSecurityImageMetadata({
          name: name,
          downloadUrl: signedUrl
        });
      }
      iWebcam.takePicture(name, function(image) {
	console.log("Taken picture, ", image);
	iFirebase.storeFile(image, afterUpload);
      });
      shotNumber++;
      if (shotNumber > 3) {
	clearInterval(cameraTimer);
        iLight.off();
      }
    }, 2500);
  }
});

var iFirebase = new firebase.Firebase({
  startFunction: function() {
    iPir.startProcessing();
  },
  stopFunction: function() {
    iPir.stopProcessing();
  },
  getStatus: function() {
    if (iPir.isProcessing()) {
      return "RUNNING";
    } else {
      return "STOPPED";
    }
  } 
});

process.on('SIGINT', function () {
   iBuzzer.shutdown();
   iPir.shutdown();
   process.exit();
});
