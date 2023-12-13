const express = require('express');
const router = express.Router();
const serviceController = require('../controller/service_controller');

//route HomePage
router.get('/getServiceRecommended', serviceController.getServiceRecommended); 
router.get('/getInfoService/:id', serviceController.getInfoService);


router.post('/addService', serviceController.addService);


module.exports = router