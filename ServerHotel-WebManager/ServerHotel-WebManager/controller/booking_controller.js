const Booking = require('../models/Booking');


exports.addBooking = async (req, res, next) => {

    try {
        const { service, room, checkInDate, checkOutDate, note, totalPrice } = req.body;
        const data = new Booking({user: req.user, service : service, room: room, checkInDate: checkInDate, checkOutDate: checkOutDate, note: note, totalPrice: totalPrice });
        await data.save()
            .then((result) => res.json(result))

    } catch (err) {
        console.error('Error adding booking:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};

exports.getBookingRoomUser = async (req, res, next) => {

    try {
        await Booking.find({ user: req.user._id , room: { $exists: true }})
        .select('room user checkInDate checkOutDate totalPrice')
        .populate('room')
        .populate('user', 'fullName -_id')
        .then((result) => {
            res.json(result);
        });

    } catch (err) {
        console.error('Error fetching favorite:', err);
        res.status(500).json({ error: 'Internal server error' });
    }

};

exports.getBookingServiceUser = async (req, res, next) => {

    try {
        await Booking.find({ user: req.user._id , service: { $exists: true } })
        .select('service user  checkInDate checkOutDate totalPrice')
        .populate('service')
        .populate('user', 'fullName -_id')
        .then((result) => {
            res.json(result);
        });

    } catch (err) {
        console.error('Error fetching favorite:', err);
        res.status(500).json({ error: 'Internal server error' });
    }

};