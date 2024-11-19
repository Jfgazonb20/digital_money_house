🏦 Digital Money House - Billetera Virtual
📚 Descripción del Proyecto
Digital Money House es una billetera virtual diseñada para ofrecer una experiencia ágil y segura. Este proyecto es parte del desafío profesional de backend, donde se implementa una API REST que permite la gestión de cuentas, sesiones de usuario, transacciones financieras y más.

🚀 Funcionalidades Implementadas
🔒 Gestión de Usuario (Sprint 1)
Registro de Usuario:

Autogeneración de CVU (22 dígitos) y alias (3 palabras separadas por puntos).
Validación de datos y manejo de errores.
Respuesta JSON sin incluir contraseña.
Inicio de Sesión:

Generación de token JWT para autenticación segura.
Manejo de errores:
Usuario inexistente (404).
Contraseña incorrecta (400).
Cierre de Sesión:

Invalidación del token JWT.
🌱 Nuevas Funcionalidades (Sprint 2 19/11/2024)
📊 Dashboard
GET /accounts/{ID}: Obtener el balance actual.
GET /accounts/{ID}/transactions: Listar las últimas 5 transacciones.
🧑‍💼 Mi Perfil
GET /users/{ID} y PATCH /users/{ID}: Consultar y actualizar perfil del usuario.
GET /accounts/{ID} y PATCH /accounts/{ID}: Consultar y actualizar información de la cuenta.
💳 Registro de Tarjetas
CRUD de Tarjetas:

GET /accounts/{ID}/cards: Obtener todas las tarjetas asociadas.
GET /accounts/{ID}/cards/{cardID}: Obtener datos de una tarjeta específica.
POST /cards: Crear y asociar una nueva tarjeta.
DELETE /accounts/{ID}/cards/{cardID}: Eliminar tarjeta asociada.
Respuestas posibles:

200: Operación exitosa (vacío si no hay datos).
201: Creación exitosa.
400: Bad request.
404: No encontrado.
409: Conflicto.
500: Error interno.
🧪 Testing & Calidad
Testing manual:

Clasificación de casos en smoke y regression tests.
Ejecución y mantenimiento de casos existentes.
Testing automatizado:

Automatización con Java y RestAssured.
🛠️ Tecnologías Utilizadas
Java + Spring Boot: Desarrollo de la API REST.
MySQL: Gestión de la base de datos.
JWT: Seguridad y autenticación.
Docker: Contenerización.
Swagger: Documentación de la API.
RestAssured: Testing automatizado.
💡 ¡Seguimos construyendo una experiencia financiera segura y eficiente!
