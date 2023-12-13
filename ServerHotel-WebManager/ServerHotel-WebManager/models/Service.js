const mongoose = require('mongoose');

const schema = mongoose.Schema;

const serviceSchema = new schema({


    name: {
        type: String,
        require: true
    },
    price: {
        type: Number,
        require: false
    },
    description: {
        type: String,
        require: false
    },
    image: {
        type: Array,
        required: true
    }
});

const Service = mongoose.model('Service', serviceSchema);

module.exports = Service;