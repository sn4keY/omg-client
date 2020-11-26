package com.norbertneudert.openmygarage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.norbertneudert.openmygarage.databinding.ActivityLoginBinding
import com.norbertneudert.openmygarage.service.auth.AuthenticationRepository
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.*

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiHandlerAuthentication = AuthenticationRepository()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = LoginViewModel("", "")
        binding.progressBar.visibility = View.INVISIBLE
        binding.button.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val login = LoginViewModel(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPassword.text.toString())
            Util.getInstance().saveLogin(login)
            GlobalScope.launch(Dispatchers.Main) {
                apiHandlerAuthentication.login(login)
                val token = Util.getInstance().getToken()
                if (token?.token!!.isNotEmpty()) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Unauthorized", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}