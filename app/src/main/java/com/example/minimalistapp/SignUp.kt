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

class SignUp : AppCompatActivity() {

    private lateinit var textInputEditTextFullname: TextInputEditText
    private lateinit var textInputEditTextUsername: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var buttonSignUp: Button
    private lateinit var textViewLogin: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        textInputEditTextFullname = findViewById(R.id.fullname)
        textInputEditTextUsername = findViewById(R.id.username)
        textInputEditTextPassword = findViewById(R.id.password)
        textInputEditTextEmail = findViewById(R.id.email)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        textViewLogin = findViewById(R.id.loginText)
        progressBar = findViewById(R.id.progress)

        textViewLogin.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonSignUp.setOnClickListener {
            val fullname = textInputEditTextFullname.text.toString()
            val username = textInputEditTextUsername.text.toString()
            val password = textInputEditTextPassword.text.toString()
            val email = textInputEditTextEmail.text.toString()

            if (fullname.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                val handler = Handler()
                handler.post {
                    val field = arrayOf("fullname", "username", "password", "email")
                    val data = arrayOf(fullname, username, password, email)
                    val putData = PutData("http://192.168.0.24/LoginRegister/signup.php", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.visibility = View.GONE
                            val result = putData.getResult()
                            if (result == "Sign Up Success") {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, Login::class.java)
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