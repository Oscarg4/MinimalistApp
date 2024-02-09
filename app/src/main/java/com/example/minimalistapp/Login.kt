package com.example.minimalistapp


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.vishnusivadas.advanced_httpurlconnection.PutData


class Login : AppCompatActivity() {


    private lateinit var textInputEditTextUsername: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewSignUp: TextView
    private lateinit var progressBar: ProgressBar


    
    private val loginUrl = "http://192.168.0.16/minimalist/login.php"


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
                LoginTask().execute(username, password)
            } else {
                Toast.makeText(applicationContext, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }
    }


    inner class LoginTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            val username = params[0]
            val password = params[1]


            val field = arrayOf("username", "password")
            val data = arrayOf(username, password)
            val putData = PutData(loginUrl, "POST", field, data)
            if (putData.startPut() && putData.onComplete()) {
                return putData.getResult()
            }
            return "Error occurred"
        }


        override fun onPostExecute(result: String) {
            progressBar.visibility = View.GONE
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
