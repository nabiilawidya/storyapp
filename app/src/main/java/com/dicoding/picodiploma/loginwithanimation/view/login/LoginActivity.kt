package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupObserve()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isBlank() || password.isBlank()) {
                showToast("Please fill all fields")
                return@setOnClickListener
            } else {
                binding.apply{
                    loginViewModel.login(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                }
            }
        }
    }

    private fun setupObserve(){
        loginViewModel.loginResponse.observe(this){ loginResponse ->
            if(loginResponse.error == false){
                loginViewModel.saveSession(
                    UserModel(
                        binding.emailEditText.text.toString(),
                        loginResponse.loginResult?.token.toString(),
                        true
                    )
                )
                val intent = Intent(this@LoginActivity,  MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }else{
                showToast(loginResponse.message.toString())
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(200)
        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val edEmail = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(200)
        val tvPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val edPassword = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(200)
        val button = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(200)
        val together = AnimatorSet().apply {
            playTogether(title, message, tvEmail, edEmail, tvPassword, edPassword, button)
            startDelay = 100
        }
        AnimatorSet().apply {
            playSequentially(title, message, tvEmail, edEmail, tvPassword, edPassword, button, together)
            start()
        }
    }
}
