package com.poly.hopa.paypal;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.pyplcheckout.BuildConfig;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "Adl1CpoKbphQNLLm-pQHw37__mBuP5WygMDvCsMBEAClu2cHvO1LwRq3MPDh-RO2LnzJtU7OpMjYw1Xb",
                Environment.SANDBOX.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "com.poly.hopa://paypalpay"
        ));
    }
}
