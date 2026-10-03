# Alke Wallet - Aplicación de Billetera Digital (Módulo 6)

## 1. Descripción del Proyecto
Este proyecto consiste en el desarrollo y optimización de una billetera digital móvil llamada **Alke Wallet**. La aplicación permite la gestión de sesiones de usuario, visualización del perfil, control de saldos y un registro completo del historial de transacciones financieras.

## 2. Arquitectura de Software (Patrón MVVM)
La aplicación implementa de manera estricta el patrón arquitectónico **MVVM (Model-View-ViewModel)**, asegurando la separación de responsabilidades y la interoperabilidad lingüística del entorno:
*   **Capas de la Vista (View):** Diseñada y programada en **Java** para las actividades principales (`Activity_Home_Page`, `Activity_Profile_Page`, fragmentos, etc.), encargándose puramente de la renderización de la interfaz y la captura de eventos.
*   **Lógica de Control y Modelado (Model/ViewModel):** Desarrollada nativamente en **Kotlin** para aprovechar las capacidades asíncronas de las corrutinas en el procesamiento de datos.

## 3. Persistencia de Datos Local (Room)
Se integró la biblioteca **Room ORM** para actuar como fuente de verdad única local a través de `AppDatabase`.
*   Se diseñaron las entidades nativas (`Usuario.kt` y `Transaccion.kt`) mapeando los tipos de datos requeridos por las tablas.
*   Se configuraron objetos de acceso a datos (DAOs) para listar el historial de movimientos de manera inmediata, garantizando la disponibilidad total de las funciones críticas de la billetera en escenarios sin conexión a internet.

## 4. Consumo de Servicios Web (Retrofit & Beeceptor)
La integración con el ecosistema de red se realiza mediante **Retrofit**, abstrayendo las llamadas HTTP en la interfaz `AlkeWalletApi`. El endpoint ficticio está montado y validado en **Beeceptor**, cubriendo un flujo CRUD optimizado bajo restricciones de planes de uso:
*   `GET /transacciones`: Devuelve la lista estructurada de movimientos financieros en formato de arreglo JSON (`[...]`) para alimentar la lista del Fragment local.
*   `PUT /transacciones/id/{id}`: Ejecuta la validación de actualización segura de una transacción mediante su identificador único.
*   `POST /user/profile`: Gestiona la simulación del registro asíncrono de nuevos perfiles en el sistema.

## 5. Pruebas Unitarias Automatizadas
Se diseñó una suite de pruebas robusta en la carpeta de test locales utilizando **JUnit** y **Mockito**. Las pruebas aíslan las interfaces y simulan las respuestas del servidor remoto para garantizar el correcto funcionamiento del software sin consumir las cuotas diarias del API externa:
*   Validación matemática exacta del incremento del saldo simulado base (150000.0) tras un ingreso de dinero (`200000.0 OK`).
*   Validación matemática exacta de la disminución del saldo tras un envío de fondos (`120000.0 OK`).
*   Simulación y aserción de campos clave (como el mapeo obligatorio de `emailUsuario`) al procesar actualizaciones de ID mediante interceptores locales.
