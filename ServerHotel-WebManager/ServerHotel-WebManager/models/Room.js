const mongoose = require('mongoose');

const schema = mongoose.Schema;

const roomSchema = new schema({

    
    type: {
        type: String,
        required: true
    },
    price: {
        type: Number,
        require: true
    },
    description: {
        type: String,
        require: false
    },
    convenience: {
        type: Array,
        nameConvenience: {
            type: String,
        },
        imageConvenience: {
            type: String,
        }
    },
    isAvailable: {
        type: Boolean,
        required: true
    },
    image: {
        type: Array,
        required: true
    }
});

const Room = mongoose.model('Room', roomSchema);

module.exports = Room;