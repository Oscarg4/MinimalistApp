package com.example.minimalistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import com.example.minimalistapp.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView
    private var userId: Int = 0 // ID del usuario actual

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null){
            sharedPreferences = SharedPreferences(container.context)
        }
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = Conn.retrofit.create(ApiService::class.java)
        textViewUsername = view.findViewById(R.id.usernameTextView)
        textViewEmail = view.findViewById(R.id.emailTextView)

        textViewUsername.text = sharedPreferences.getUser()!!.name
        textViewEmail.text = sharedPreferences.getUser()!!.email

        // Obtener el ID del usuario actual desde las preferencias compartidas
        userId = obtenerIdUsuario()

        // Cargar información del usuario desde el backend
        cargarInformacionUsuario()

        // Configurar listener del botón de editar perfil
        view.findViewById<Button>(R.id.editProfileButton).setOnClickListener {
            // Navegar al fragmento PerfilUsuario
            val fragment = PerfilUsuario()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.ContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        // Configurar listener del botón de favoritos
        view.findViewById<Button>(R.id.favoritesButton).setOnClickListener {
            // Navegar a la página de favoritos
            // Aquí debes implementar la lógica para la navegación
        }
    }

    private fun cargarInformacionUsuario() {
        // Realizar llamada al backend para obtener los datos del usuario logueado
        apiService.buscarUsuarioPorId(userId).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val usuario = response.body()
                    usuario?.let {
                        // Mostrar los datos del usuario en los TextView correspondientes
                        textViewUsername.text = usuario.name
                        textViewEmail.text = usuario.email
                    }
                } else {
                    // Mostrar mensaje de error si la llamada no fue exitosa
                    Toast.makeText(requireContext(), "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                // Mostrar mensaje de error en caso de fallo en la llamada
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtenerIdUsuario(): Int {
        // Obtener el ID del usuario actual desde las preferencias compartidas
        val sharedPreferences = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        // Si no se encuentra el ID del usuario, devuelve -1 como valor predeterminado
        return sharedPreferences.getInt("user_id", -1)
    }
}
