package com.norbertneudert.openmygarage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.norbertneudert.openmygarage.databinding.ActivityLoginBinding
import com.norbertneudert.openmygarage.service.ApiHandlerAuthentication
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.*

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiHandlerAuthentication = ApiHandlerAuthentication(this)
        val activity = this
        val appcontext = this.applicationContext
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = LoginViewModel("", "")
        binding.progressBar.visibility = View.INVISIBLE
        binding.button.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val login = LoginViewModel(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPassword.text.toString())
            GlobalScope.launch(Dispatchers.Main) {
                apiHandlerAuthentication.login(login)
                val token = Util.getToken(activity)
                Log.i("LoginActivity", token!!.token)
                if (token?.token!!.isNotEmpty()) {
                    startActivity(Intent(appcontext, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}