# 📱 Alke Wallet - Billetera Digital para Android

---
## 📌 Caso de estudio: Alke Wallet

### 1. Descripción General del Proyecto
**Alke Wallet** es una aplicación móvil desarrollada para Android orientada a resolver la necesidad de un control financiero personal ágil, seguro y accesible. La aplicación cubre el ciclo completo de interacción del usuario: gestión de sesiones, administración del perfil profesional, consulta de saldos en tiempo real y registro histórico detallado de movimientos.

### 2. Desafío Técnico Principal
El reto clave consistió en integrar una arquitectura escalable capaz de operar de forma híbrida: mantener la interoperabilidad entre capas escritas en distintos lenguajes (**Java** y **Kotlin**), garantizar el funcionamiento sin conexión a internet mediante persistencia local y sincronizar transacciones a través de servicios web expuestos en API REST, protegiendo las cuotas de consumo del servidor.

### 3. Solución Propuesta
* **Arquitectura Escalable (MVVM):** Desacoplamiento total entre la interfaz gráfica y la lógica de negocio.
* **Persistencia Local Robustecida:** Uso de la biblioteca **Room** como fuente única de verdad en el dispositivo.
* **Integración API REST:** Uso de **Retrofit** para el consumo asíncrono de endpoints.
* **Garantía de Calidad:** Implementación de pruebas unitarias con **JUnit** y **Mockito** para la validación del modelo de datos sin dependencia de red.

### 4. Principales Aprendizajes y Habilidades Aplicadas
* Implementación del patrón de diseño **MVVM** en entornos multilingüe (**Java / Kotlin**).
* Mapeo de bases de datos relacionales locales a través de **Room ORM** y **DAOs**.
* Consumo y parseo de arreglos/objetos JSON con **Retrofit**.
* Mapeo y simulación de entornos de prueba desacoplados con **Mockito**.

---
## 🛠️ Especificaciones Técnicas de la Arquitectura

### 1. Patrón Arquitectónico (MVVM)
La aplicación implementa de manera estricta el patrón **Model-View-ViewModel**, asegurando la separación de responsabilidades y la interoperabilidad lingüística del entorno:
* **Capa de la Vista (View):** Diseñada y programada en **Java** para las actividades principales (`Activity_Home_Page`, `Activity_Profile_Page`, Fragments, etc.), encargándose puramente de la renderización de la interfaz gráfica y la captura de eventos del usuario.
* **Lógica de Control y Modelado (Model/ViewModel):** Desarrollada nativamente en **Kotlin** para aprovechar las capacidades asíncronas de las corrutinas en el procesamiento de datos y la gestión del estado de la interfaz.

### 2. Persistencia de Datos Local (Room)
Se integró la biblioteca **Room ORM** para actuar como fuente de verdad única local a través de `AppDatabase`:
* Se diseñaron las entidades nativas (`Usuario.kt` y `Transaccion.kt`) mapeando los tipos de datos requeridos por las tablas.
* Se configuraron objetos de acceso a datos (DAOs) para listar el historial de movimientos de manera inmediata, garantizando la disponibilidad total de las funciones críticas de la billetera en escenarios sin conexión a internet.

### 3. Consumo de Servicios Web (Retrofit & Beeceptor)
La integración con el ecosistema de red se realiza mediante **Retrofit**, abstrayendo las llamadas HTTP en la interfaz `AlkeWalletApi`. El endpoint ficticio está montado y validado en **Beeceptor**, cubriendo un flujo CRUD optimizado bajo restricciones de planes de uso:
* `GET /transacciones`: Devuelve la lista estructurada de movimientos financieros en formato de arreglo JSON (`[...]`) para alimentar la vista del Fragment local.
* `PUT /transacciones/id/{id}`: Ejecuta la validación de actualización segura de una transacción mediante su identificador único.
* `POST /user/profile`: Gestiona la simulación del registro asíncrono de nuevos perfiles en el sistema.

### 4. Pruebas Unitarias Automatizadas
Se diseñó una suite de pruebas robusta en la carpeta de test locales utilizando **JUnit** y **Mockito**. Las pruebas aíslan las interfaces y simulan las respuestas del servidor remoto para garantizar el correcto funcionamiento del software sin consumir las cuotas diarias de la API externa:
* Validación matemática exacta del incremento del saldo simulado base (`150000.0`) tras un ingreso de dinero (`200000.0 OK`).
* Validación matemática exacta de la disminución del saldo tras un envío de fondos (`120000.0 OK`).
* Simulación y aserción de campos clave (como el mapeo obligatorio de `emailUsuario`) al procesar actualizaciones de ID mediante interceptores locales.

---
## 🖼️ Capturas de Pantalla
<table>
  <tr>
    <td align="center">
      <b>Splash Screen</b>
      <img width="495" height="420" alt="img_alkewallet" src="https://github.com/user-attachments/assets/ac100c28-8ed1-4548-9334-2c71aee2c12f" />
    </td>
    <td align="center">
      <b>Login/Signup Page</b>
      <img width="359" height="641" alt="img_alkewallet" src="https://github.com/user-attachments/assets/d2b53363-649a-47b2-8e82-2ca413053b34" />
    </td>
    <td align="center">
      <b>Login Page</b>
      <img width="359" height="641" alt="img_alkewallet" src="https://github.com/user-attachments/assets/95ec1d72-118b-49cc-b065-1a270408ef8d" />
    </td>
    <td align="center">
      <b>Singup Page</b>
      <img width="359" height="641" alt="img_alkewallet" src="https://github.com/user-attachments/assets/36949a69-6ea4-416d-aaa9-4ee49dd6b319" />
    </td> 
    <td align="center">
      <b>Home Page</b>
      <img width="359" height="641" alt="img_alkewallet" src="https://github.com/user-attachments/assets/bc1ff233-0997-4cfd-91c6-e5ef7d5d23ac" width="0" />
    </td>
    <td align="center">
      <b>Home Page - Empty Case</b>
      <img width="359" height="641" alt="img_alkewallet" src="https://github.com/user-attachments/assets/02ee96be-3401-4d73-8853-ae180b243090" width="0" />
    </td>
    <td align="center">
      <b>Request Money</b>
      <img width="359" height="641" alt="img_alkewallet" src="https://github.com/user-attachments/assets/84a68d2b-2f2d-444f-bb6e-a33ee9a36b8f" width="0" />
    </td>
    <td align="center">
      <b>Send Money</b>
      <img width="359" height="641" alt="img_alkewallet" src="https://github.com/user-attachments/assets/9f7a8601-395d-4820-8169-06afd0985896" width="0" />
    </td>
  </tr>
</table>

---
## ✒️ Autora
* **Yalideth Sánchez** - *Desarrolladora Android / Java & Kotlin*
* **GitHub:** [Yalideth-Sanchez-R](https://github.com/Yalideth-Sanchez-R)
  
