const User = require('../models/User');
const jwt = require('jsonwebtoken');
const dotenv = require('dotenv');
dotenv.config();




exports.addUser = async (req, res, next) => {
    try {
        const { userName, passWord, email, phoneNumber } = req.body;
        const data = new User({ userName: userName, passWord: passWord, email: email, phoneNumber });
        await data.save()
            .then((result) => res.json(result))

    } catch (err) {
        console.error('Error adding user:', err);
        res.status(500).json({ error: 'Internal server error' });
    }

};

exports.login = async (req, res, next) => {
    try {
        const { userName, passWord } = req.body;
        console.log(userName, passWord);
        await User.findOne({ userName: userName, passWord: passWord })
            .then((result) => {
                if (!result) {
                    res.status(500).json({ error: 'Internal server error' });
                } else if (result) {
                    const acessToken = jwt.sign({ user: result }, process.env.TOKEN_SEC_KEY, { expiresIn: '5m' })
                    res.json({ token: acessToken });
                }
            });
    } catch (err) {
        console.error('Error adding user:', err);
        res.status(500).json({ error: 'Internal server error' });
    }



    // const acessToken = jwt.sign({data}, process.env.TOKEN_SEC_KEY, { expiresIn: '30s' })

};

exports.getUserInfo = async (req, res, next) => {

    try {
        await User.findById(req.user._id)
            .then((result) => {
                res.json(result);
            })
    } catch (err) {
        console.error('Error fetch user:', err);
        res.status(500).json({ error: 'Internal server error' });
    }


};
exports.changeUserInfo = async (req, res, next) => {
    try {
        const { fullName, email, phoneNumber } = req.body;
        await User.findOneAndUpdate({ _id: req.user }, { fullName: fullName, email: email, phoneNumber: phoneNumber }, { new: true })
            .then((result) => {
                res.json(result);
            });
    } catch (err) {
        console.error('Error changing user:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};


exports.changeUserPassWord = async (req, res, next) => {
    try {
        const { oldPassWord, newPassWord } = req.body;
        await User.findOneAndUpdate({ _id: req.user, passWord: oldPassWord }, { passWord: newPassWord }, { new: true },)
            .then((result) => {
                res.json(result);
            });
    } catch (err) {
        console.error('Error fixing user:', err);
        res.status(500).json({ error: 'Internal server error' });   
    }
};

exports.changeUserPassWordForgot = async (req, res, next) => {
    try {
        const { phoneNumber, newPassWord } = req.body;
        await User.findOneAndUpdate({ phoneNumber: phoneNumber }, { passWord: newPassWord }, { new: true },)
            .then((result) => {
                res.json(result);
            });
    } catch (err) {
        console.error('Error fixing user:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};
exports.loginByEmail = async (req, res, next) => {
    try {
        const { email } = req.body;
        console.log(email);
        await User.findOne({email: email})
            .then((result) => {
                if (!result) {
                    res.status(500).json({ error: 'Internal server error' });
                } else if (result) {
                    const acessToken = jwt.sign({ user: result }, process.env.TOKEN_SEC_KEY, { expiresIn: '5m' })
                    res.json({ token: acessToken });
                }
            });
    } catch (err) {
        console.error('Error fething user:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};
