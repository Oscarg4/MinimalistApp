package com.example.minimalistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import com.example.minimalistapp.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilUsuario : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var editTextPassword: EditText
    private var userId: Int = 0 // ID del usuario actual

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = Conn.retrofit.create(ApiService::class.java)
        editTextPassword = view.findViewById(R.id.editTextPassword)

        // Obtener el ID del usuario actual (Aquí asumimos que lo obtienes de algún lugar)
        userId = obtenerIdUsuario()

        view.findViewById<Button>(R.id.changePasswordButton).setOnClickListener {
            cambiarContraseña()
        }

        view.findViewById<Button>(R.id.deleteAccountButton).setOnClickListener {
            eliminarCuenta()
        }

        view.findViewById<Button>(R.id.signOutButton).setOnClickListener {
            cerrarSesión()
        }
    }

    private fun cambiarContraseña() {
        val newPassword = editTextPassword.text.toString().trim()

        if (newPassword.isNotEmpty()) {
            val updatedUser = Users(id = userId, name = "", surname = "", email = "", password = newPassword)
            apiService.actualizarUser(updatedUser, userId.toLong()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Por favor, ingresa una nueva contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarCuenta() {
        apiService.eliminarUsers(userId.toLong()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cerrarSesión() {

        val sharedPreferences = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Luego, redirecciona al usuario a la página de registro de usuarios
        val intent = Intent(requireContext(), SignUp::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    // Método para obtener el ID del usuario actual (aquí deberías implementar tu lógica real)
    private fun obtenerIdUsuario(): Int {
        // Obtener SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        // Obtener el ID del usuario almacenado en SharedPreferences, si no se encuentra, devuelve -1 como valor predeterminado
        return sharedPreferences.getInt("user_id", -1)
    }
}
