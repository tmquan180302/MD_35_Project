const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const session = require('express-session')
const morgan = require('morgan');


const app = express();
const PORT = 3000;

const AdminRoute = require('./routes/admin_route')
const UserRouter = require('./routes/user_route');
const RoomRouter = require('./routes/room_route')
const ServiceRouter = require('./routes/service_route');
const ReviewRouter = require('./routes/review_route');
const FavoriteRouter = require('./routes/favorite_route');
const BookingRouter = require('./routes/booking_route');
const URI = 'mongodb+srv://quantmph19466:ZehXhobhPJMbgUY7@cluster0.qiz9d1q.mongodb.net/hopa?retryWrites=true&w=majority'



//////
mongoose.connect(URI);
mongoose.connection.on('connected', () => {
    console.log('conected to mongoose');
})
mongoose.connection.on('error', (err) => {
    console.log('this is error', err);
});
app.set('view engine', 'ejs');
app.set('views', __dirname + '/views');


/////
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
// app.set('trust proxy', 1) // trust first proxy
app.use(session({
    secret: 'keyboard cat',
    resave: false,
    saveUninitialized: true,
    cookie: {
        secure: false,
        httpOnly: true,
        maxAge: 5 * 60 * 1000
    }
}))
app.use(express.static('./uploads'));


app.use(morgan('tiny'));



///use routes
app.use('/admin', AdminRoute)
app.use('/user', UserRouter);
app.use('/room', RoomRouter);
app.use('/service', ServiceRouter);
app.use('/review', ReviewRouter);
app.use('/favorite', FavoriteRouter);
app.use('/booking', BookingRouter);




app.listen(PORT, console.log(`Server listening on ${PORT}`));