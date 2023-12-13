const mongoose = require('mongoose');
const Room = require('../models/Room');
const Review = require('../models/Review');
const Service = require('../models/Service');

exports.getInfoRoom = async (req, res, next) => {
    try {
        await Room.findById(req.params.id)
            .then((result) => {
                res.json(result);
            });
    } catch (err) {
        console.error('Error fetching rooms:', err);
        res.status(500).json({ error: 'Internal server error' });
    }

};

exports.searchNameRoom = async (req, res, next) => {
    const { type } = req.body;

    // Truy vấn tất cả các phòng có trường type phù hợp và lưu chúng trong mảng "rooms".
    const rooms = await Room.aggregate([
        { $match: { type: { $regex: type, $options: 'i' } } }
    ]);

    const roomIds = rooms.map(room => room._id);

    // Truy vấn trung bình rating cho từng phòng và lưu chúng trong mảng "ratings".
    const ratings = await Review.aggregate([
        {
            $match: { room: { $in: roomIds } }
        },
        {
            $group: {
                _id: "$room",
                averageRating: { $avg: "$rating" }
            }
        }
    ]);

    // Tạo một đối tượng Map để lưu trung bình rating theo roomId.
    const ratingMap = new Map();
    ratings.forEach(rating => {
        ratingMap.set(rating._id.toString(), rating.averageRating);
    });

    // Tạo một mảng JSON kết quả để chứa thông tin phòng và trung bình rating.
    const result = rooms.map(room => ({
        room: room,
        review: { rating: ratingMap.get(room._id.toString()) || 0 }
    }));

    // Sắp xếp lại danh sách phòng dựa trên trung bình rating giảm dần.
    result.sort((a, b) => b.averageRating - a.averageRating);

    // Trả về kết quả JSON chứa thông tin phòng và trung bình rating.
    res.json(result);
};

// Checkbox price room from high to low price
exports.searchRoomHighToLow = async (req, res, next) => {

    // Truy vấn tất cả các phòng có trường type phù hợp và lưu chúng trong mảng "rooms".
    const rooms = await Room.aggregate([
        { $sort: { price: -1 } }
    ]);

    const roomIds = rooms.map(room => room._id);

    // Truy vấn trung bình rating cho từng phòng và lưu chúng trong mảng "ratings".
    const ratings = await Review.aggregate([
        {
            $match: { room: { $in: roomIds } }
        },
        {
            $group: {
                _id: "$room",
                averageRating: { $avg: "$rating" }
            }
        }
    ]);

    // Tạo một đối tượng Map để lưu trung bình rating theo roomId.
    const ratingMap = new Map();
    ratings.forEach(rating => {
        ratingMap.set(rating._id.toString(), rating.averageRating);
    });

    // Tạo một mảng JSON kết quả để chứa thông tin phòng và trung bình rating.
    const result = rooms.map(room => ({
        room: room,
        review: { rating: ratingMap.get(room._id.toString()) || 0 }
    }));

    // // Sắp xếp lại danh sách phòng dựa trên trung bình rating giảm dần.
    // result.sort((a, b) => b.averageRating - a.averageRating);

    // Trả về kết quả JSON chứa thông tin phòng và trung bình rating.
    res.json(result);
};

exports.searchRoomLowToHigh = async (req, res, next) => {

    // Truy vấn tất cả các phòng có trường type phù hợp và lưu chúng trong mảng "rooms".
    const rooms = await Room.aggregate([
        { $sort: { price: 1 } }
    ]);

    const roomIds = rooms.map(room => room._id);

    // Truy vấn trung bình rating cho từng phòng và lưu chúng trong mảng "ratings".
    const ratings = await Review.aggregate([
        {
            $match: { room: { $in: roomIds } }
        },
        {
            $group: {
                _id: "$room",
                averageRating: { $avg: "$rating" }
            }
        }
    ]);

    // Tạo một đối tượng Map để lưu trung bình rating theo roomId.
    const ratingMap = new Map();
    ratings.forEach(rating => {
        ratingMap.set(rating._id.toString(), rating.averageRating);
    });

    // Tạo một mảng JSON kết quả để chứa thông tin phòng và trung bình rating.
    const result = rooms.map(room => ({
        room: room,
        review: { rating: ratingMap.get(room._id.toString()) || 0 }
    }));

    // // Sắp xếp lại danh sách phòng dựa trên trung bình rating giảm dần.
    // result.sort((a, b) => b.averageRating - a.averageRating);

    // Trả về kết quả JSON chứa thông tin phòng và trung bình rating.
    res.json(result);
};

exports.addRoom = async (req, res, next) => {
    try {
        const { type, price, description, convenience, isAvailable, image } = req.body;
        const data = new Room({ _id: new mongoose.Types.ObjectId(), type: type, price: price, description: description, convenience: convenience, isAvailable: isAvailable, image: image });
        await data.save()
            .then((result) => res.json(result))

    } catch (err) {
        console.error('Error adding room:', err);
        res.status(500).json({ error: 'Internal server error' });
    }
};

exports.getRating = async (req, res, next) => {

    const roomId = req.params.id;

    Room.find()
        .exec()
        .then(rooms => {
            // Lấy danh sách các _id của Room
            const roomIds = rooms.map(room => room._id);

            // Truy vấn tài liệu trong bảng Review
            Review.find({ room: { $in: roomIds } })
                .exec()
                .then(reviews => {
                    // Sắp xếp lại dữ liệu để nhóm đánh giá theo phòng
                    const roomsWithReviews = rooms.map(room => ({
                        room,
                        reviews: reviews.filter(review => review.room.equals(room._id))
                    }));

                    // Trả về dữ liệu đã sắp xếp
                    res.json(roomsWithReviews);
                })
                .catch(err => {
                    console.error('Lỗi truy vấn bảng Review:', err);
                    res.status(500).json({ error: 'Lỗi truy vấn bảng Review' });
                });
        })
        .catch(err => {
            console.error('Lỗi truy vấn bảng Room:', err);
            res.status(500).json({ error: 'Lỗi truy vấn bảng Room' });
        });
};




exports.getDataForDashboard = async (req, res, next) => {
    try {
        // Truy vấn tất cả phòng và tính trung bình rating cho từng phòng
        const roomsByHighPrice = await Room.aggregate([
            { $sort: { price: -1 } },
            {
                $lookup: {
                    from: 'reviews', // Tên của bảng Review
                    localField: '_id',
                    foreignField: 'room',
                    as: 'reviews'
                }
            },
            {
                $addFields: {
                    averageRating: { $avg: '$reviews.rating' }
                }
            },

        ]);

        const ratingMap = new Map();
        roomsByHighPrice.forEach(rating => {
            ratingMap.set(rating._id.toString(), rating.averageRating);
        });

        const result = roomsByHighPrice.map(room => ({
            room: room,
            review: { rating: ratingMap.get(room._id.toString()) || 0 }
        }));

        // Truy vấn tất cả dịch vụ
        const services = await Service.find();

        // Truy vấn tất cả phòng và tính trung bình rating cho từng phòng
        const roomsByLowPrice = await Room.aggregate([
            { $sort: { price: 1 } },
            {
                $lookup: {
                    from: 'reviews', // Tên của bảng Review
                    localField: '_id',
                    foreignField: 'room',
                    as: 'reviews'
                }
            },
            {
                $addFields: {
                    averageRating: { $avg: '$reviews.rating' }
                }
            },
        ]);


        const ratingMapLow = new Map();
        roomsByLowPrice.forEach(rating => {
            ratingMapLow.set(rating._id.toString(), rating.averageRating);
        });

        const resultLow = roomsByLowPrice.map(room => ({
            room: room,
            review: { rating: ratingMapLow.get(room._id.toString()) || 0 }
        }));

        // // Sắp xếp lại danh sách phòng theo trung bình rating giảm dần
        // roomsByHighPrice.sort((a, b) => b.averageRating - a.averageRating);

        // // Sắp xếp lại danh sách phòng theo trung bình rating tăng dần
        // roomsByLowPrice.sort((a, b) => a.averageRatisng - b.averageRating);



        res.json({
            roomHigh: result,
            service: services,
            roomLow: resultLow
        });
    } catch (error) {
        console.error('Lỗi trong truy vấn dữ liệu:', error);
        res.status(500).json({ error: 'Lỗi trong truy vấn dữ liệu' });
    }
};



