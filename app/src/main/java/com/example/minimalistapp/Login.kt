package com.example.minimalistapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.vishnusivadas.advanced_httpurlconnection.PutData

class Login : AppCompatActivity() {

    private lateinit var textInputEditTextUsername: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewSignUp: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textInputEditTextUsername = findViewById(R.id.username)
        textInputEditTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewSignUp = findViewById(R.id.signUpText)
        progressBar = findViewById(R.id.progress)

        textViewSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener {
            val username = textInputEditTextUsername.text.toString()
            val password = textInputEditTextPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                val handler = Handler()
                handler.post {
                    val field = arrayOf("username", "password")
                    val data = arrayOf(username, password)
                    val putData = PutData("http://192.168.0.24/login/login.php", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.visibility = View.GONE
                            val result = putData.getResult()
                            if (result == "Login Success") {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}