const buzzer = require('./buzzer');
const pir = require('./pir');
const firebase = require('./firebase');
const webcam = require('./webcam');
const mailer = require('./mailer');
const light = require('./light');
const fs = require('fs');

const iWebcam = new webcam.Webcam();
const iMailer = new mailer.Mailer();

const iLight = new light.Light({
    gpio: 26
});

const iBuzzer = new buzzer.Buzzer({
    gpio: 20
});

const iPir = new pir.PIR({
    gpio: 17,
    callback: function () {
        iMailer.sendMail({
            subject: 'Alarm',
            content: 'Alarm ve sklepe zachytil pohyb. Zkontroluj zaznam z kamery'
        });
        iLight.on();
        iBuzzer.buzz(3000);

        var shotNumber = 1;

        var cameraAction = function() {
            var d = new Date();
            var name = d.getFullYear() + "_" + (d.getMonth() + 1) + "_" + d.getDate() + "_"
                + d.getHours() + "_" + d.getMinutes() + "_" + d.getSeconds() + "_" + shotNumber;
            var afterUpload = function (signedUrl) {
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
            };
            iWebcam.takePicture(name, function (image) {
                console.log("Taken picture, ", image);
                iFirebase.storeFile(image, afterUpload);
            });
            shotNumber++;
            if (shotNumber > 5) {
                clearInterval(cameraTimer);
                iLight.off();
            }
        };
        cameraAction();
        var cameraTimer = setInterval(cameraAction, 1000);
    }
});



const iFirebase = new firebase.Firebase({
    startFunction: function () {
        iPir.startProcessing();
    },
    stopFunction: function () {
        iPir.stopProcessing();
    },
    getStatus: function () {
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
