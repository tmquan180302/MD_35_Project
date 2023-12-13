const express = require('express');
const router = express.Router();
const roomController = require('../controller/room_controller');

// route cho man` Home
router.get('/getInfoRoom/:id', roomController.getInfoRoom);
//route cho man` Search 
router.post('/searchNameRoom', roomController.searchNameRoom);
//2 hop checkbox
router.get('/searchRoomHighToLow', roomController.searchRoomHighToLow);
router.get('/searchRoomLowToHigh', roomController.searchRoomLowToHigh);

router.get('/homeScreen', roomController.getDataForDashboard);

router.post('/addRoom', roomController.addRoom);



module.exports = router;
