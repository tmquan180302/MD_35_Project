const mongoose = require('mongoose');

const schema = mongoose.Schema;

const userSchema = new schema({


    userName: {
        type: String,
        require: true
    },
    passWord: {
        type: String,
        require: true
    },
    email: {
        type: String,
        require: true,
    },
    fullName: {
        type: String,
        require: false
    },
    phoneNumber: {
        type: Number,
        required: false
    },
    address: {
        type: String,
        required: false
    },
    role: {
        type: String,
        required: false,
        default: 'user'
    },
    image: {
        type: String,
        require: false
    }

});

const User = mongoose.model('User', userSchema);

module.exports = User;