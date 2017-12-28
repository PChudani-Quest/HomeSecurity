var Gpio = require('onoff').Gpio;

function Light(conf) {
  this.gpio = new Gpio(conf.gpio, 'out', 'none', {activeLow: true});
  this.gpio.write(0);
}

Light.prototype.shine = function(duration) {
  console.log("Starting shining");
  this.gpio.write(1);
  setTimeout(function() {
    console.log("Stopping shining");
    this.gpio.write(0);
  }.bind(this), duration);
}

Light.prototype.on = function() {
  console.log("Turning light on");
  this.gpio.write(1);
}


Light.prototype.off = function() {
  console.log("Turning light off");
  this.gpio.write(0);
}


Light.prototype.shutdown = function() {
  console.log('Light shutdown');
  this.gpio.unexport();
}

exports.Light = Light;
