const path = require('path');
const Review = require('../models/Review');

// Done
exports.getRoomReviews = async (req, res, next) => {

    try {
        const { id } = req.params;
        await Review.find({room: id})
            .select('room user rating comment')
            .populate('room', 'type -_id')
            .populate('user', 'fullName -_id')
            .then((result) => {
                res.json(result)
            });

    } catch (err) {
        console.error('Error fetching review:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};
// Done 
exports.getServiceReviews = async (req, res, next) => {

    try {
        const { id } = req.params;
        await Review.find({service: id})
            .select('service user rating comment')
            .populate('service', 'name -_id')
            .populate('user', 'fullName -_id')
            .then((result) => {
                res.json(result)
            });
    } catch (err) {
        console.error('Error fetching review:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};

// Jus add service or room 
exports.addReview = async (req, res, next) => {
    try {
        const { service, room, rating, comment } = req.body;
        const data = new Review({ service: service, room: room, user: req.user, rating: rating, comment: comment });
        await data.save()
            .then((result) => { res.json(result) });
    } catch (err) {
        console.error('Error adding review:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};