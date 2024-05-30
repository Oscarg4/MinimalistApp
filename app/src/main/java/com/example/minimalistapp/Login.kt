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

class Login : AppCompatActivity() {

    private lateinit var textInputEditTextUsername: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewSignUp: TextView
    private lateinit var progressBar: ProgressBar
    private val apiService = Conn.retrofit.create(ApiService::class.java)

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = SharedPreferences(this)

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
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        a(username, password)
                    } catch (e: Exception) {
                        Log.e("Resultado", "Failed")
                    }
                }
            } else {
                Toast.makeText(applicationContext, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun a(name : String, pass : String){
        return suspendCoroutine {continuation ->
            val call = apiService.buscarUsuarioPorNombreYContraseña(name, pass)
            call.enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    //progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Log Up Success", Toast.LENGTH_SHORT).show()
                        val user = response.body()
                        Log.e("Resultado1234", user.toString())
                        if (user != null) {
                            sharedPreferences.addUser(user)
                        }
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("Resultado", t.toString())
                    Toast.makeText(applicationContext, "Error occurreasdasd", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
