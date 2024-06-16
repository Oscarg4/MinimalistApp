package com.example.minimalistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import com.example.minimalistapp.model.Users
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.suspendCoroutine

class SignUp : AppCompatActivity() {

    private lateinit var textInputEditTextFullname: TextInputEditText
    private lateinit var textInputEditTextUsername: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var buttonSignUp: Button
    private lateinit var textViewLogin: TextView
    private lateinit var progressBar: ProgressBar

    private val apiService = Conn.retrofit.create(ApiService::class.java)

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
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val user = Users(name = fullname, email = email, password = password, surname = username, id_Users = 0)
                        a(user)
                        val intent = Intent(applicationContext, Login::class.java)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        Log.e("Resultado", "Failed")
                    }
                }
            } else {
                Toast.makeText(applicationContext, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private suspend fun a(users: Users){
        return suspendCoroutine {continuation ->
            val call = apiService.addUsers(users)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    //progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Sign Up Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Error Ocurred", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("Resultado", t.toString())
                    Toast.makeText(applicationContext, "Error occurreasdasd", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
