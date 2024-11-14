require('dotenv').config();
const express = require('express');
const stripe = require('stripe')(process.env.STRIPE_SECRET_KEY);
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());
app.use(cors({ origin: '*' }));

// Route to create a PaymentIntent
app.post('/create-payment-intent', async (req, res) => {
    try {
        const { amount } = req.body; // Amount in cents

        // Create a PaymentIntent with the specified amount and currency
        const paymentIntent = await stripe.paymentIntents.create({
            amount: amount,
            currency: 'usd',
            payment_method_types: ['card'],
        });

        // Send clientSecret to the frontend
        res.send({
            clientSecret: paymentIntent.client_secret,
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
