var nodemailer = require('nodemailer');
var config = require('./config');

function Mailer() {
}

Mailer.prototype.sendMail = function(conf) {

    // create reusable transporter object using the default SMTP transport
    var transporter = nodemailer.createTransport({
        host: config.mailer.smtp_address,
        port: config.mailer.smtp_port,
        secure: true, // true for 465, false for other ports
        auth: {
            user: config.mailer.smtp_user,
            pass: config.mailer.smtp_pass
        }
    });

    // setup email data with unicode symbols
    var mailOptions = {
        from: config.mailer.from, // sender address
        to: config.mailer.to_addresses, // list of receivers
        subject: conf.subject, // Subject line
        text: conf.content, // plain text body
        html: `<b>${conf.content}</b>` // html body
    };

    // send mail with defined transport object
    transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            return console.log(error);
        }
        console.log('Message sent: %s', info.messageId);
    });
}

exports.Mailer = Mailer;