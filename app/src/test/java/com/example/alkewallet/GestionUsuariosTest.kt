package com.example.alkewallet

import com.example.alkewallet.dao.UsuarioDao
import com.example.alkewallet.model.Usuario
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GestionUsuariosTest {
    @Mock
    private lateinit var usuarioDaoMock: UsuarioDao

    @Before
    fun setUp() {
        // Inicializa las anotaciones @Mock de Mockito
        MockitoAnnotations.openMocks(this)
        println("Configuración de la prueba: Mockito inicializado para Gestión de Usuarios")
    }

    @After
    fun tearDown() {
        println("La validación de usuario fue ejecutada con éxito")
    }

    // PRUEBA 1: Inicio de sesión con credenciales correctas
    @Test
    fun iniciarSesion_conCredencialesCorrestas_retornaUsuario() {
        println("Inicio: Prueba iniciar sesión con credenciales correctas")

        val usuarioEsperado = Usuario(
            1,
            "11.111.111-1",
            "Amanda",
            "Silvia",
            "amanda@alkewallet.com",
            "password123",
            "password123",
            150000.0
        )

        Mockito.`when`(usuarioDaoMock.iniciarSesion("amanda@alkewallet.com", "password123"))
            .thenReturn(usuarioEsperado)

        val resultado = usuarioDaoMock.iniciarSesion("amanda@alkewallet.com", "password123")

        assertNotNull(resultado)
        assertEquals("Amanda", resultado?.nombre)

        println("Fin: Credenciales validadas con éxito. Usuario retornado: \${resultado?.nombre}")
    }

    // PRUEBA 2: Inicio de sesión con credenciales incorrectas
    @Test
    fun iniciarSesion_conCredencialesIncorrectas_retornaNull()  {
        println("Inicio: Prueba iniciar sesión con credenciales incorrectas")

        Mockito.`when`(usuarioDaoMock.iniciarSesion("invalido@correo.com", "claveMal"))
            .thenReturn(null)

        val resultado = usuarioDaoMock.iniciarSesion("invalido@correo.com", "claveMal")

        assertNull(resultado)
        println("Fin: Validación correcta, el sistema denegó el acceso retornando null")
    }

    // PRUEBA 3: Validación de email vacío - Retorna Falso
    @Test
    fun validarEmailVacio_retornaFalso() {
        println("Inicio: Prueba validación de email vacío")
        val emailInvalido = ""
        val esValido = emailInvalido.trim().isNotEmpty() && emailInvalido.contains("@")

        assertFalse("Error: Se aceptó una cadena vacía como email", esValido)
        println("Fin: Prueba email vacío exitosa")
    }

    // PRUEBA 4: Validación de email correcto - retorna True
    @Test
    fun validarEmailCorrecto_retornaVerdadero() {
        println("Inicio: Prueba validación de email correcto")
        val emailValido = "amanda@alkewallet.com"
        val esValido = emailValido.trim().isNotEmpty() && emailValido.contains("@")

        assertTrue("Error: No se validó un email con formato correcto", esValido)
        println("Fin: Prueba de email válido exitosa")
    }

    // PRUEBA 5: Validación contraseñas diferentes - retorna False
    @Test
    fun validadContraseñasDiferentes_retornaFalso() {
        println("Inicio: Prueba contraseñas no coinciden")
        val clave1 = "123456"
        val clave2 = "1234567"
        val coinciden = clave1 == clave2

        assertFalse("Error: El sistema indicó que las contraseñas coinciden cuando son diferentes", coinciden)
        println("Fin: Prueba contraseñas no coinciden exitosa")

    }

    // PRUEBA 6: Validar que el rut contenga el formato básico
    @Test
    fun validarRut_formatoCorrecto_retornaVerdadero() {
        println("Inicio: Prueba validación de formaro de RUT")

        val rutSimulado = "11.111.111-1"
        val formatoValido = rutSimulado.isNotEmpty() && rutSimulado.contains("-")

        assertTrue("La prueba falló: El Rut del usuario no cumple con el formato mínimo", formatoValido)
        println("Fin: Prueba formato de Rut verificada con éxito")
    }
}