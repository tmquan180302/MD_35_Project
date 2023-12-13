const express = require('express');
const router = express.Router();
const userController = require('../controller/user_controller');
const { authenToken } = require('../middleware/auth');


///need to add middleware berfore acess router like this
//  ===> router.get('/TestRouter',mdl.addMiddleware, testController.getTest )

// router.get('/getUser', userController.getAll);

router.post('/register', userController.addUser);

router.post('/login', userController.login);

router.post('/loginByEmail', userController.loginByEmail);

router.get('/getUserInfo', authenToken, userController.getUserInfo);

router.post('/changeUserInfo', authenToken, userController.changeUserInfo);

router.post('/changeUserPassWord', authenToken, userController.changeUserPassWord);

router.post('/changeUserPassWordForgot', userController.changeUserPassWordForgot);







module.exports = router;