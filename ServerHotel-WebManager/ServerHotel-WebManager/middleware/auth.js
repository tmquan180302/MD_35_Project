const jwt = require('jsonwebtoken');
const dotenv = require('dotenv');
dotenv.config();


// Hàm tạo JWT
function generateToken(user) {
    const payload = {
        userId: user.id,
        username: user.username,
        // Thêm các thông tin khác về người dùng nếu cần
    };

    const options = {
        expiresIn: '1h', // Thời hạn của JWT, ví dụ 1 giờ
    };

    return jwt.sign(payload, process.env.TOKEN_SEC_KEY, options);
}

// Hàm xác thực JWT
function verifyToken(token) {
    try {
        return jwt.verify(token, process.env.TOKEN_SEC_KEY);
    } catch (error) {
        return null; // Token không hợp lệ
    }
}

function authenToken(req, res, next) {
    const authorizationHeader = req.headers['authorization'];
    const token = authorizationHeader.split(' ')[1];
    if (!token) res.sendStatus(401);

    jwt.verify(token, process.env.TOKEN_SEC_KEY, (err, data) => {
        console.log(err, data);
        if (err) res.sendStatus(403);
        req.user = data.user;
        next();
    })

};

module.exports = {
    generateToken,
    verifyToken,
    authenToken,
};
