var Gpio = require('onoff').Gpio;

function PIR(conf) {
  this.gpio = new Gpio(conf.gpio, 'in', 'both');
  this.callback = conf.callback;
  this.processing = false;
}

PIR.prototype.startProcessing = function() {
  console.log('PIR starting processing');
  if (this.processing) {
     console.log("Already processing");
     return;
  }

  this.processing = true;
  this.gpio.watch(function (err, value) {
    if (err) {
      console.log(err);
    }

    if (value == 1) {
      console.log("PIR detected something, calling callback");
      this.callback();
    }
  }.bind(this));
}

PIR.prototype.stopProcessing = function() {
  console.log('PIR stopping processing');
  if (this.processing) {
    this.gpio.unwatchAll();
    this.processing = false;
  }
}

PIR.prototype.isProcessing = function() {
  return this.processing;
}

PIR.prototype.shutdown = function() {
  console.log('PIR shutdown');
  this.gpio.unexport();
  this.gpio.unwatchAll();
}

exports.PIR = PIR;