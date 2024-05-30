package com.example.minimalistapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper

class splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        // Duración del splash screen (3 segundos)
        Handler(Looper.getMainLooper()).postDelayed({
            // Inicia la actividad principal
            startActivity(Intent(this@splash, Login::class.java))
            // Finaliza la actividad de splash
            finish()
        }, 3000) 
    }
}
