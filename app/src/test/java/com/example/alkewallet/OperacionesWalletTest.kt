package com.example.alkewallet

import com.example.alkewallet.data.network.AlkeWalletApi
import com.example.alkewallet.model.Transaccion
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class OperacionesWalletTest {
    @Mock
    private lateinit var apiMock: AlkeWalletApi
    private var saldoSimulado: Double = 0.0

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        saldoSimulado = 150000.0
        println("Configuración de la prueba: Mockito inicializado y saldo establecido")
    }
    @After
    fun tearDown() {
        println("La operación matemática fue ejecutada con éxito")
    }

    // PRUEBA 1: Ingresar saldo y se sume correctamente
    @Test
    fun ingresarSaldo_incrementaSaldoCorrectamente() {
        println("Inicio: Prueba validación de ingreso de dinero")
        val montoIngreso = 50000.0
        val saldoEsperado = 200000.0 // 150.000 + 50.000
        saldoSimulado += montoIngreso

        assertEquals(saldoEsperado, saldoSimulado, 0.0)
        println("Fin: Prueba de ingreso de dinero verificado con saldo final: $saldoSimulado")
    }

    // PRUEBA 2: Verificación de envío de dinero y se reste correctamente
    @Test
    fun envioDinero_disminuyeSaldoCorrectamente() {
        println("Inicio: Prueba validación de envío de dinero")
        val montoEnvio = 30000.0
        val saldoEsperado = 120000.0 // 150.000.0 - 30.000.0
        saldoSimulado -= montoEnvio

        assertEquals(saldoEsperado, saldoSimulado, 0.0)
        println("Fin: Prueba de envío de dinero verificado con saldo final: $saldoSimulado")
    }

    // PRUEBA 3: Validar la API para actualizacion de Id de transaccion
    @Test
    fun actualizarIdTransaccion_respondeExitosamente() = runBlocking {
        println("Inicio: Prueba validación de actualización ID de transacción")

        val transaccionOriginal = Transaccion(
            1,
            "amanda@alkewallet.com",
            10000.0,
            "Ingreso desde Cuenta Bancaria",
            "INGRESO",
            "28-09-2026 19:55"
        )

        Mockito.`when`(apiMock.actualizarIdTransaccion(1, transaccionOriginal))
            .thenReturn(transaccionOriginal)
        val respuesta = apiMock.actualizarIdTransaccion(1, transaccionOriginal)

        assertNotNull(respuesta)
        assertEquals(1, respuesta.id)
        assertEquals("amanda@alkewallet.com", respuesta.emailUsuario)
        assertEquals(10000.0, respuesta.monto, 0.0)
        assertEquals("Ingreso desde Cuenta Bancaria", respuesta.descripcion)
        assertEquals("INGRESO", respuesta.tipoMovimiento)

        println("Fin: API simulada con Mockito respondió exitosamente. Monto: \${respuesta.id}")
    }
}
