package com.example.nhran.whatsit

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_authenticator.*
import android.os.AsyncTask.execute
import com.amazonaws.mobile.auth.ui.SignInUI
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.AWSStartupResult
import com.amazonaws.mobile.client.AWSStartupHandler



class AuthenticatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticator)
       // setSupportActionBar(toolbar)
        // Add a call to initialize AWSMobileClient
        AWSMobileClient.getInstance().initialize(this) {
            val signin = AWSMobileClient.getInstance().getClient(
                this@AuthenticatorActivity,
                SignInUI::class.java
            ) as SignInUI
            signin.login(
                this@AuthenticatorActivity,
                HomeActivity::class.java
            ).execute()
        }.execute()
    }

}
