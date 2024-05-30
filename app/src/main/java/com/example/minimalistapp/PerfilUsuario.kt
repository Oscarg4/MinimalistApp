package com.example.minimalistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var actualUser: Users

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil_usuario, container, false)
        sharedPreferences = SharedPreferences(container!!.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        apiService = Conn.retrofit.create(ApiService::class.java)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        sharedPreferences = SharedPreferences(requireContext())

        actualUser = sharedPreferences.getUser()!!

        Log.e("Resultado", actualUser.toString())

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
        val newPassword = editTextPassword.text.toString()

        if (newPassword.isNotEmpty()) {
            val updatedUser = Users(id_Users = actualUser.id_Users, name = "", surname = "", email = "", password = newPassword)
            // Verificar si el usuario actual existe antes de actualizar
            if (actualUser.id_Users != -1) {
                apiService.actualizarUser(updatedUser, actualUser.id_Users).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show()
                        } else {
                            val errorMessage = "Error al cambiar la contraseña: ${response.code()}"
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(requireContext(), "El usuario no se encontró en la base de datos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Por favor, ingresa una nueva contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarCuenta() {
        apiService.eliminarUsers(actualUser.id_Users.toLong()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show()
                    cerrarSesión()  // Cierra sesión tras eliminar la cuenta
                } else {
                    val errorMessage = "Error al eliminar la cuenta: ${response.code()}"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cerrarSesión() {
        // Limpiar la sesión del usuario
        sharedPreferences.clearUser()

        // Redirigir al usuario a la pantalla de inicio de sesión
        val intent = Intent(requireContext(), SignUp::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
    private fun obtenerIdUsuario(): Int {
        val user = sharedPreferences.getUser()
        return user?.id_Users ?: -1
    }
}
