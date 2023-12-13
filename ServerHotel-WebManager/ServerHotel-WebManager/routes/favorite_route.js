const express = require('express');
const router = express.Router();
const favoriteController = require('../controller/favorite_controller');
const { authenToken } = require('../middleware/auth');


///need to add middleware berfore acess router like this
//  ===> router.get('/TestRouter',mdl.addMiddleware, testController.getTest )

//params of user
router.get('/getAllFavorite', authenToken, favoriteController.getAllFavorite);

router.delete('/deleteFavorite/:id', favoriteController.deleteFavorite);

//params of room
//use params beacuse button add favorite on item of room
router.post('/addFavorite/:id', authenToken, favoriteController.addFavorite);


module.exports = router;