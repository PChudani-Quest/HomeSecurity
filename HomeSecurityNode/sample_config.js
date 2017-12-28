var config = {};

config.mailer = {
smtp_address : "",
smtp_port : 465,
smtp_user: ""
smtp_pass : "",
smtp_security : "SSL",
to_addresses: "email@gmail.com, email2@gmail.com",
from: "\"Security system\" <email@gmail.com>"
};

config.firebase = {
databaseURL: "https://xxxx.firebaseio.com",
storageBucket: "xxxx.appspot.com"
};

module.exports = config;