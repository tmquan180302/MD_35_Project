const express = require('express');
const router = express.Router();
const multer = require('multer');
const path = require('path');
const adminController = require('../controller/admin_controller');


const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, path.join(__dirname, '../uploads'));
    },
    filename: function (req, file, cb) {
        cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
    },
});

const upload = multer({ storage: storage });

router.get('/', adminController.getAllBooking);




router.get('/user', adminController.getAllUser);
router.get('/user/add', adminController.getAddUser);
router.get('/user/edit/:id', adminController.getEditUser);
router.post('/user/add', adminController.addUser);
router.post('/user/edit/:id', adminController.editUser);
router.get('/user/delete/:id', adminController.deletetUser);





router.get('/room', adminController.getAllRoom);
router.get('/room/add', adminController.getAddRoom);
router.get('/room/edit/:id', adminController.getEditRoom);
router.post('/room/add', upload.array('image'), adminController.addRoom);
router.post('/room/edit/:id', upload.array('image'), adminController.editRoom);
router.get('/room/delete/:id', adminController.deletetRoom);





router.get('/service', adminController.getAllService);
router.get('/service/add', adminController.getAddService);
router.get('/service/edit/:id', adminController.getEditService);
router.post('/service/add', upload.array('image'), adminController.addService);
router.post('/service/edit/:id', upload.array('image'), adminController.editService);
router.get('/service/delete/:id', adminController.deletetService);





router.get('/review', adminController.getAllReview);
router.get('/review/delete/:id', adminController.deleteReview);



router.get('/booking', adminController.getAllBooking);
router.get('/booking/add', adminController.getAddBooking);
router.get('/booking/edit/:id', adminController.getEditBooking);
router.post('/booking/add', adminController.addBooking);
router.post('/booking/edit/:id', adminController.editBooking);
router.get('/booking/delete/:id', adminController.deleteBooking);






module.exports = router;