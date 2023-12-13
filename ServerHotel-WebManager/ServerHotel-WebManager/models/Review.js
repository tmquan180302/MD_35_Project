const mongoose = require('mongoose');
const schema = mongoose.Schema;

const reviewSchema = new schema({

    service: {
        type: mongoose.Schema.Types.ObjectId, ref : 'Service',
        require: false
    },
    room: {
        type: mongoose.Schema.Types.ObjectId, ref : 'Room',
        require: false
    },
    user: {
        type: mongoose.Schema.Types.ObjectId, ref : 'User',
        require: false
    },
    rating: {
        type: Number,
        require: true
    },
    comment:{
        type: String,
        require: true
    }
});

const Review = mongoose.model('Review', reviewSchema);

module.exports = Review;