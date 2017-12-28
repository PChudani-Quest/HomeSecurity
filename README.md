# HomeSecurity

This is home security project that consists of Raspberry Pi(RPi) that has connected alarm, reflector, USB webcam and PIR motion
sensor. Node.js server is running on RPi and controls those inputs and outputs.
It also communicates with Firebase Real-time database to store captured pictures and control its state.
Second part of the system is an Android application to control the system and view security pictures captured by the camera.

It works as follows:

If PIR sensor detects some motion then node.js server
- starts the buzzer (alarm) for specified time period
- sends a notification email to all the specified email addresses
- turns the reflector on
- captures a serie of images on webcam and stores them in Firebase Real-time database
- then turns the reflector off

On android app you can 
- see system status
- turn system on/off
- see captured security pictures

## HW setup
To start using HomeSecurity project first you need to setup hardware side of it.

I used 
- Raspberry Pi 3 Model B 64-bit 1GB RAM that has integrated WiFi module
- PIR module HC-SR501
- Buzzer KXG1212C
- USB webcam
- RPi Relay Board
- LED reflector 230V AC

Connection schematics

![connection schematics](https://github.com/PChudani-Quest/HomeSecurity/blob/master/images/schematics.png)

You also need to connect webcam to RPi USB port. See actual image of connected HW. Notice RPi relay board has jumpers that connect
relays automatically to gpio so you don't have to.

![connection schematics](https://github.com/PChudani-Quest/HomeSecurity/blob/master/images/hw.JPG)

## Setup
### Firebase setup
You need Google's Firebase project.

1. Go to [https://console.firebase.google.com/](https://console.firebase.google.com/)
2. Create a new Project
3. Setup Authentication:
    1. Enable Email/Password user authentication
    2. Add users - They will be able to start or stop the security system
4. Navigate to the Service Accounts tab in your project's settings page.
6. Click the Generate New Private Key button at the bottom of the Firebase Admin SDK section of the Service Accounts tab.
After you click the button, a JSON file containing your service account's credentials will be downloaded. You'll need this to initialize the SDK in the next step.

### RPi requirements
- checkout this project onto your RPi

- fswebcam 
```
sudo apt-get install fswebcam
```

- node.js
[W3School guide to node.js setup](https://www.w3schools.com/nodejs/nodejs_raspberrypi.asp)

- npm
```
sudo apt-get install npm
```

- config.js 
in /HomeSecurityNode folder there is sample_config.js. Configure it so that it contains your SMTP and also firebase parameters.

- service account key
copy your service account key to /HomeSecurityNode folder as serviceAccountKey.json

- install
go to /HomeSecurityNode and run npm install

### RPi run
You can start the node server by running
```
node /HomeSecurityNode/main.js
```

I recommend creating service that will start that automatically for you when RPi is (re)started.

## Android setup
- In android studio open /HomeSecurityMobile project folder.

- Connect your project to Google Services
In main menu of Android Studio select
Tools -> Firebase

In Assistant select to connect 
* Authentication (Email and password)
* Realtime Database
* Storage

It should include firebase dependencies in HomeSecurityMobile/app/build.gradle (should be already there)
and create HomeSecurityMobile/app/google-services.json with your connection informations.

- Build/Run 
now you should be able to build/run the app
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
