const mongoose = require('mongoose');

const schema = mongoose.Schema;
const currentDate = new Date(Date.now());

const bookingSchema = new schema({

    user: {
        type: mongoose.Schema.Types.ObjectId, ref : 'User',
        require: true
    },
    service: {
        type: mongoose.Schema.Types.ObjectId, ref : 'Service',
        require: false
    },
    room: {
        type: mongoose.Schema.Types.ObjectId, ref : 'Room',
        require: false
    },
    checkInDate: {
        type: String,
        require: false,
        default: ` ${currentDate.getHours()}:${currentDate.getMinutes()} :
         ${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}`

    },
    checkOutDate: {
        type: String,
        require: false,
        default: ` ${currentDate.getHours()}:${currentDate.getMinutes()} :
         ${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}`

    },
    note: {
        type: String,
        require: false
    },
    totalPrice: {
        type: Number,
        require: false
    },

});

const Booking = mongoose.model('Booking', bookingSchema);

module.exports = Booking;