const express = require('express');
const router = express.Router();
const reviewController = require('../controller/review_controller');
const { authenToken } = require('../middleware/auth');

router.get('/getRoomReviews/:id', reviewController.getRoomReviews);
router.get('/getServiceReviews/:id', reviewController.getServiceReviews);



router.post('/addReview', authenToken, reviewController.addReview);



module.exports = router