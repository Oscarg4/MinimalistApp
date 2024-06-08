import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object PasswordEncryptor {

    // Función para encriptar la contraseña con SHA-256
    fun encryptPassword(password: String): String {
        try {
            // Crear una instancia de MessageDigest con el algoritmo SHA-256
            val digest = MessageDigest.getInstance("SHA-256")

            // Obtener el arreglo de bytes de la contraseña
            val bytes = password.toByteArray()

            // Calcular el hash de la contraseña
            val hashedBytes = digest.digest(bytes)

            // Convertir el arreglo de bytes a una representación hexadecimal
            val stringBuilder = StringBuilder()
            for (hashedByte in hashedBytes) {
                stringBuilder.append(Integer.toString((hashedByte.toInt() and 0xff) + 0x100, 16).substring(1))
            }
            return stringBuilder.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}

// Ejemplo de uso de la función para encriptar una contraseña
fun main() {
    val password = "miContraseña"
    val hashedPassword = PasswordEncryptor.encryptPassword(password)
    Log.d("PasswordEncryptor", "Contraseña encriptada: $hashedPassword")
}
