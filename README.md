
¡Entendido! Vamos a mejorar la estructura de tu README para que sea más legible y profesional. Aquí tienes una versión actualizada:

🏦 Digital Money House - Billetera Virtual
📚 Descripción del Proyecto
Digital Money House es una billetera virtual diseñada para ofrecer una experiencia ágil y segura. Este proyecto es parte del desafío profesional de backend, donde se implementa una API REST que permite la gestión de cuentas, sesiones de usuario, transacciones financieras y más.


🚀 Funcionalidades Implementadas
🔒 Gestión de Usuario (Sprint 1)
Registro de Usuario


Autogeneración de CVU (22 dígitos) y alias (3 palabras separadas por puntos).
Validación de datos y manejo de errores.
Respuesta JSON sin incluir contraseña.
Inicio de Sesión


Generación de token JWT para autenticación segura.
Manejo de errores:
Usuario inexistente (404).
Contraseña incorrecta (400).
Cierre de Sesión
Invalidación del token JWT.


🌱 Nuevas Funcionalidades (Sprint 2 - 19/11/2024)
📊 Dashboard
GET /accounts/{ID}: Obtener el balance actual.
GET /accounts/{ID}/transactions: Listar las últimas 5 transacciones.



🛠️ Tecnologías Utilizadas
Java + Spring Boot: Desarrollo de la API REST.
MySQL: Gestión de la base de datos.
JWT: Seguridad y autenticación.
Docker: Contenerización.
Swagger: Documentación de la API.
RestAssured: Testing automatizado.
