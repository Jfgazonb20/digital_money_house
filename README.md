# 🏦 **Digital Money House** - Billetera Virtual

![Digital Money House]([https://via.placeholder.com/800x200.png?text=Digital+Money+House](https://www.google.com/search?client=opera-gx&hs=XPP&sca_esv=d608386cb98ae779&sxsrf=ADLYWIKBmRawtR7Y4eRADlmR2CfJqBl-Ow:1733677732583&q=billetera+virtual&udm=2&fbs=AEQNm0DqQl1xJn7RM6dknF9x5SeXi_UWhHwN3Y_uJFBxWuAM84_aydcqo5g5jFzPITThqmWVYI1gRL4ybxhBaHYeA4Fuqrwv9MLeKnIiLPMN_Yk4rQ49tqs402ug03IVEsEg7nMup2YHHR1n0L0jAdrFKMQGkNT3OZnVzqR_l0k-Ya3OGCPfBFlBJzeCL98Qe7-NXO6J4iC93_qadRunX_C7ctC8UbSGmw&sa=X&ved=2ahUKEwjR_Z7r1JiKAxVct4QIHRMeBjYQtKgLegQIFBAB&biw=1877&bih=958&dpr=1#vhid=F-05bd1v70wdoM&vssid=mosaic))  
*¡La solución financiera que necesitas al alcance de tu mano!*

---

## 📚 **Descripción del Proyecto**
Digital Money House es una billetera virtual diseñada para ofrecer una experiencia ágil y segura. Este proyecto es parte de un desafío profesional de backend, donde se implementa una API REST que permite la gestión de usuarios, cuentas, transacciones financieras y más.

---

## 🚀 **Funcionalidades Implementadas**

### 🔒 **Gestión de Usuario (Sprint 1)**
- **Registro de Usuario**
  - Generación automática de:
    - **CVU**: 22 dígitos únicos.
    - **Alias**: 3 palabras separadas por puntos.
  - Validación de datos y manejo de errores.
  - **Nota:** Respuesta JSON sin incluir la contraseña.
  - **Manejo de errores**:
    - **400 Bad Request**: Datos inválidos.
    - **500 Internal Server Error**: Error en el servidor.

- **Inicio de Sesión**
  - Login con email y contraseña.
  - Generación de token JWT para autenticación segura.
  - **Manejo de errores**:
    - **404 Not Found**: Usuario inexistente.
    - **400 Bad Request**: Contraseña incorrecta.
    - **500 Internal Server Error**: Error en el servidor.

- **Cierre de Sesión**
  - API para invalidar el token JWT.
  - **Respuesta**:
    - **200 OK**: Token invalidado correctamente.

📌 **Importante:** Todos los endpoints posteriores requieren ingresar el Bearer Token para su uso.

---

### 🌱 **Dashboard y Gestión de Tarjetas (Sprint 2)**
- **Dashboard**
  - **GET /accounts/{ID}**: Consultar saldo disponible.
  - **GET /accounts/{ID}/transactions**: Listar los últimos 5 movimientos de la cuenta.

- **Gestión de Perfil**
  - **GET /users/{ID}** y **PATCH /users/{ID}**: Consultar y editar información de usuario.
  - **GET /accounts/{ID}** y **PATCH /accounts/{ID}**: Consultar y editar información de cuenta.

- **Gestión de Tarjetas**
  - **GET /accounts/{ID}/cards**: Consultar tarjetas asociadas.
  - **POST /accounts/{ID}/cards**: Asociar nuevas tarjetas.
  - **DELETE /accounts/{ID}/cards/{cardID}**: Eliminar una tarjeta existente.
  - **Manejo de errores**:
    - **201 Created**: Tarjeta asociada correctamente.
    - **409 Conflict**: Tarjeta ya asociada a otra cuenta.
    - **404 Not Found**: Tarjeta no encontrada.

---

### 🎮 **Gestión de Actividades y Transacciones (Sprint 3)**
- **Gestión de Actividad**
  - **GET /accounts/{ID}/activity**: Consultar todas las actividades recientes.
  - **GET /accounts/{ID}/activity/{transactionID}**: Consultar detalles de una actividad específica.
  - **Manejo de errores**:
    - **200 OK**: Datos obtenidos exitosamente.
    - **400 Bad Request**: Solicitud inválida.
    - **403 Forbidden**: Sin permisos.
    - **404 Not Found**: Transacción no encontrada.

- **Ingreso de Dinero**
  - **POST /accounts/{ID}/transferences**: Registrar ingresos desde tarjeta de débito/crédito.
  - **Respuesta**:
    - **201 Created**: Dinero ingresado correctamente.

- **Opcionales**
  - Filtros avanzados para actividades:
    - Por monto (e.g., $0-$1000, $1000-$5000).
    - Por periodo o tipo de transacción (ingreso/egreso).

---

### 💸 **Transferencias y Mejoras (Sprint 4)**
- **Transferencias**
  - **GET /accounts/{ID}/transferences**: Consultar los últimos destinatarios.
  - **POST /accounts/{ID}/transferences**: Realizar transferencias de saldo.
  - **Manejo de errores**:
    - **200 OK**: Transferencia realizada con éxito.
    - **400 Bad Request**: Cuenta inexistente.
    - **410 Gone**: Fondos insuficientes.

- **Infraestructura**
  - Configuración de un archivo `Docker Compose`.
  - Generación de imágenes Docker para despliegue en la nube (AWS).

- **Opcionales**
  - Generación de comprobantes de transacciones en formato PDF.
  - Visualización de las últimas 5 cuentas destinatarias de transferencias.

---

## 📦 **Tecnologías Utilizadas**
- **Java + Spring Boot**: Desarrollo de la API REST.
- **PostgreSQL**: Base de datos relacional.
- **JWT**: Seguridad y autenticación.
- **Docker**: Contenerización para despliegues ágiles.
- **Swagger**: Documentación interactiva de la API.
- **RestAssured**: Testing automatizado de endpoints.

---

## 🛡️ **Seguridad**
- **JWT** (JSON Web Token): Todos los endpoints requieren autenticación mediante Bearer Token tras iniciar sesión exitosamente.

---
## ⚠️ **Importante**
- **AliasWords** tener presente la direccion del archivo para que pueda generar los alias.

---

💡 **¡Con Digital Money House, construimos una experiencia financiera segura y eficiente para todos!**
