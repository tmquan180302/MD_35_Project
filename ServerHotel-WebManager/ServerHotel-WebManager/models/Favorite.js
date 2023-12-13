const mongoose = require('mongoose');

const schema = mongoose.Schema;

const favoriteSchema = new schema({

    user: {
        type: mongoose.Schema.Types.ObjectId, ref : 'User',
        require: true
    },
  
    room: {
        type: mongoose.Schema.Types.ObjectId, ref : 'Room',
        require: false
    },
});

const Favorite = mongoose.model('Favorite', favoriteSchema);

module.exports = Favorite;