const Booking = require("../models/Booking");
const Review = require("../models/Review");
const Room = require("../models/Room");
const Service = require("../models/Service");
const User = require("../models/User");
const path = require('path');

exports.getTest = async (req, res, next) => {

    const indexPath = path.join(__dirname, '../index.html');
    res.sendFile(indexPath);


}

exports.getAllUser = async (req, res, next) => {
    try {
        const users = await User.find();
        res.render('user', { users });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getAddUser = async (req, res, next) => {

    try {
        res.render('formUser', { mode: 'add', user: null });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getEditUser = async (req, res, next) => {

    try {
        const user = await User.findById(req.params.id);
        res.render('formUser', { mode: 'edit', user });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.addUser = async (req, res, next) => {

    try {
        const newUser = new User(req.body);
        await newUser.save();
        res.redirect('/admin/user');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.editUser = async (req, res, next) => {

    try {
        const userId = req.params.id;
        await User.findByIdAndUpdate(userId, req.body, { new: true });
        res.redirect('/admin/user');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.deletetUser = async (req, res, next) => {

    try {
        const userId = req.params.id;
        await User.findByIdAndDelete(userId);
        res.redirect('/admin/booking');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getAllRoom = async (req, res, next) => {
    try {
        const rooms = await Room.find();
        res.render('room', { rooms });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getAddRoom = async (req, res, next) => {

    try {
        res.render('formRoom', { mode: 'add', room: null });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getEditRoom = async (req, res, next) => {

    try {
        const room = await Room.findById(req.params.id);
        res.render('formRoom', { mode: 'edit', room });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.addRoom = async (req, res, next) => {

    try {
        const {
            type,
            price,
            description,
            nameConvenience,
            imageConvenience,
            isAvailable,
        } = req.body;


        // change ipV4 address 

        const imageUrls = req.files.map(file => 'http://192.168.1.7:3000/' + path.basename(file.path));

        const convenience = nameConvenience.map((name, index) => ({
            nameConvenience: name,
            imageConvenience: imageConvenience[index],
        }));

        const newRoom = new Room({
            type,
            price,
            description,
            convenience: convenience,
            isAvailable,
            image: imageUrls,
        });

        await newRoom.save();
        res.redirect('/admin/room');
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

exports.editRoom = async (req, res, next) => {

    try {
        const roomId = req.params.id;

        const {
            type,
            price,
            description,
            nameConvenience,
            imageConvenience,
            isAvailable,
        } = req.body;

        const imageUrls = req.files.map(file => 'http://192.168.1.7:3000/' + path.basename(file.path));

        const convenience = nameConvenience.map((name, index) => ({
            nameConvenience: name,
            imageConvenience: imageConvenience[index],
        }));
        if (imageUrls.length > 0) {
            const data = {
                type,
                price,
                description,
                convenience,
                isAvailable,
                image: imageUrls,
            }
            await Room.findByIdAndUpdate(roomId, data, { new: true });
        } else {
            const data = {
                type,
                price,
                description,
                convenience,
                isAvailable,
            }
            await Room.findByIdAndUpdate(roomId, data, { new: true });
        }

        res.redirect('/admin/room');
    } catch (error) {
        res.status(500).json({ error: error.message });
    }

};

exports.deletetRoom = async (req, res, next) => {

    try {
        const roomId = req.params.id;
        await Room.findByIdAndDelete(roomId);
        res.redirect('/admin/room');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};


exports.getAllService = async (req, res, next) => {
    try {
        const services = await Service.find();
        res.render('service', { services });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getAddService = async (req, res, next) => {

    try {
        res.render('formService', { mode: 'add', service: null });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getEditService = async (req, res, next) => {

    try {
        const service = await Service.findById(req.params.id);
        res.render('formService', { mode: 'edit', service });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.addService = async (req, res, next) => {

    try {
        const { name, price, description } = req.body;
        
        const imageUrls = req.files.map(file => 'http://192.168.1.7:3000/' + path.basename(file.path));

        console.log(imageUrls);
        
        const newService = new Service({
            name,
            price,
            description,
            image: imageUrls
        });
        await newService.save();
        res.redirect('/admin/service');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.editService = async (req, res, next) => {

    try {
        const serviceId = req.params.id;

        const imageUrls = req.files.map(file => 'http://192.168.1.7:3000/' + path.basename(file.path));
        if (imageUrls.length > 0) {
            const data = {
                type,
                price,
                description,
                image: imageUrls,
            }
            await Service.findByIdAndUpdate(serviceId, data, { new: true });
        } else {
            const data = {
                type,
                price,
                description,
            }
            await Service.findByIdAndUpdate(serviceId, data, { new: true });
        }
        res.redirect('/admin/service');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};
exports.deletetService = async (req, res, next) => {

    try {
        const serviceId = req.params.id;
        await Service.findByIdAndDelete(serviceId);
        res.redirect('/admin/service');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};



exports.getAllReview = async (req, res, next) => {
    try {
        const reviews = await Review.find()
            .populate('user');
        res.render('review', { reviews });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getAddReview = async (req, res, next) => {

    try {
        res.render('formReview', { mode: 'add' });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getEditReview = async (req, res, next) => {

    try {
        const review = await Review.findById(req.params.id)
            .populate('room')
            .populate('service');
        res.render('formReview', { mode: 'edit', review });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.addReview = async (req, res, next) => {

    try {
        const newReview = new Service(req.body);
        await newReview.save();
        res.redirect('/admin/review');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.editReview = async (req, res, next) => {

    try {
        const reviewId = req.params.id;
        await Review.findByIdAndUpdate(reviewId, req.body, { new: true });
        res.redirect('/admin/review');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};
exports.deleteReview = async (req, res, next) => {

    try {
        const reviewId = req.params.id;
        await Review.findByIdAndDelete(reviewId);
        res.redirect('/admin/review');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};




exports.getAllBooking = async (req, res, next) => {

    try {
        const bookings = await Booking.find()
            .populate('user')
            .populate('room')
            .populate('service')
        res.render('booking', { bookings });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }


};
exports.getAddBooking = async (req, res, next) => {

    try {
        const users = await User.find();
        const rooms = await Room.find();
        const services = await Service.find();
        res.render('formBooking', { mode: 'add', users: users, rooms: rooms, services: services, booking: null });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.getEditBooking = async (req, res, next) => {

    try {
        const users = await User.find();
        const rooms = await Room.find();
        const services = await Service.find();

        const booking = await Booking.findById(req.params.id)
            .populate('user')
            .populate('room')
            .populate('service');
        res.render('formBooking', { mode: 'edit', users: users, rooms: rooms, services: services, booking });
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.addBooking = async (req, res, next) => {

    try {
        const newBooking = new Booking(req.body);
        await newBooking.save();
        res.redirect('/admin/booking');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.editBooking = async (req, res, next) => {

    try {
        const bookingId = req.params.id;
        await Booking.findByIdAndUpdate(bookingId, req.body, { new: true });
        res.redirect('/admin/booking');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};

exports.deleteBooking = async (req, res, next) => {

    try {
        const bookingId = req.params.id;
        await Booking.findByIdAndDelete(bookingId);
        res.redirect('/admin/booking');
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }

};
