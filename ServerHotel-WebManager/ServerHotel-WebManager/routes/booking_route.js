const express = require('express');
const router = express.Router();
const bookingController = require('../controller/booking_controller');
const { authenToken } = require('../middleware/auth');


///need to add middleware berfore acess router like this
//  ===> router.get('/TestRouter',mdl.addMiddleware, testController.getTest )

router.post('/addBooking', authenToken, bookingController.addBooking);

router.get('/getBookingRoomUser', authenToken, bookingController.getBookingRoomUser);

router.get('/getBookingServiceUser', authenToken, bookingController.getBookingServiceUser);






module.exports = router;