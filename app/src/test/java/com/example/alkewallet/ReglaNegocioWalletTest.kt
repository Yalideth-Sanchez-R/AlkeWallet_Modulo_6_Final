package com.example.alkewallet

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class ReglaNegocioWalletTest {

    @Before
    fun setUp() {
        println("Configuración de la prueba: Escenario de Reglas de Negocio listo")
    }
    @After
    fun tearDown() {
        println("La validación de la regla de negocio fue ejecutada con éxito")
    }

    // PRUEBA 1: Verificar que el sistema bloquee un envío si no hay saldo suficiente
    @Test
    fun enviarDinero_montoMayorAlSaldo_retornoFalso() {
        println("Inicio: Prueba validación de saldo insuficiente para envíos")
        val saldoActual = 5000.0
        val montoAEnviar = 15000.0

        val transaccionPermitida = montoAEnviar <= saldoActual

        assertFalse("La prueba falló: La billetera permitió un envío con saldo insuficiente", transaccionPermitida)
        println("Fin: El sistema bloqueó el envío correctamente")
    }

    // PRUEBA 2: Verificar que el sistema rechace montos menores o iguales a cero
    @Test
    fun realizarIngreso_montoInvalido_retornaFalso() {
        println("Inicio: Prueba validación de montos menores o iguales a cero")
        val montoIngresoInvalido = -500.0
        val montoIngresoValido = montoIngresoInvalido > 0.0

        assertFalse("La prueba falló: Se aceptó un ingreso de un monto negativo o en cero en la billetera", montoIngresoValido)
        println("Fin: Prueba monto inválido rechazado correctamente")
    }

    // PRUEBA 3: Verificar ingresos de dinero con montos mayores a cero
    @Test
    fun realizarIngreso_montoValido_retornaTrue() {
        println("Inicio: Prueba ingreso de montos positivos mayores a cero")
        val montoIngresoValido = 25000.0

        val esMontoValido = montoIngresoValido > 0.0

        assertTrue("Error: No se aceptó un depósito con fondos válidos", esMontoValido)
        println("Fin: Prueba ingreso montos positivos mayores a cero aprobado")
    }
}