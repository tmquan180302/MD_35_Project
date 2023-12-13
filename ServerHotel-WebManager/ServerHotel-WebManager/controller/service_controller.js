const Service = require('../models/Service');



//route HomePage
exports.getServiceRecommended = async (req, res, next) => {
    try {
        await Service.find()
            .sort({ price: -1 })
            .limit(10)
            .then((result) => {
                res.json(result);
            })
    } catch (err) {
        console.error('Error fetching service:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};

exports.getInfoService = async (req, res, next) => {
    await Service.findById(req.params.id)
        .then((result) => {
            res.json(result);
        });
};



exports.addService = async (req, res, next) => {
    try {
        const { name, price, description, image } = req.body;
        const data = new Service({ name: name, price: price, description: description, image: image });
        await data.save()
            .then((result) => res.json(result))

    } catch (err) {
        console.error('Error adding Service:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};