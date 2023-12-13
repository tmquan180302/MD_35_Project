const Favorite = require('../models/Favorite');


exports.getAllFavorite = async (req, res, next) => {
    console.log(req.user._id);
    try {
        await Favorite.find({ user: req.user._id })
            .select('room user')
            .populate('room')
            .populate('user', 'fullName -_id')
            .then((result) => {
                res.json(result);
            });

    } catch (err) {
        console.error('Error fetching favorite:', err);
        res.status(500).json({ error: 'Internal server error' });
    }


};

exports.deleteFavorite = async (req, res, next) => {
    try {
        const { id } = req.params;
        await Favorite.findByIdAndDelete({ _id: id }, { new: true })
            .then((result) => {
                res.json(result);
            });

    } catch (err) {
        console.error('Error fetching favorite:', err);
        res.status(500).json({ error: 'Internal server error' });
    }

};

// add new favorite
exports.addFavorite = async (req, res, next) => {
    try {
        const { id } = req.params;
        const isExists = await Favorite.findOne({ room: id, user: req.user._id });

        if (isExists) {
            res.status(200).json({ message: 'Favorite already exists' });
            return;
        } else {
            const data = new Favorite({ room: id, user: req.user._id });
            await data.save()
                .then((result) => { res.json({
                    room : result,
                    message: 'Favorite added successfully'
                }) });
        }
    } catch (err) {
        console.error('Error adding favorite:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};