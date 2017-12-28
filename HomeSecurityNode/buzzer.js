var Gpio = require('onoff').Gpio;

function Buzzer(conf) {
  this.gpio = new Gpio(conf.gpio, 'out', 'none', {activeLow: true});
  this.gpio.write(0);
}

Buzzer.prototype.buzz = function(duration) {
  console.log("Starting buzzing");
  this.gpio.write(1);
  setTimeout(function() {
    console.log("Stopping buzzing");
    this.gpio.write(0);
  }.bind(this), duration);
}

Buzzer.prototype.shutdown = function() {
  console.log('Buzzer shutdown');
  this.gpio.unexport();
}

exports.Buzzer = Buzzer;
