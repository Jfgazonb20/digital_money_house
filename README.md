# 🏦 Digital Money House - Billetera Virtual

## 📚 Descripción del Proyecto  
Digital Money House es una billetera virtual diseñada para ofrecer una experiencia ágil y segura. Este proyecto es parte del desafío profesional de backend, donde se implementa una API REST que permite la gestión de cuentas, sesiones de usuario, transacciones financieras y más.

---

## 🚀 Funcionalidades Implementadas  

### 🔒 Gestión de Usuario (Sprint 1)  

- **Registro de Usuario**  
  - Autogeneración de CVU (22 dígitos) y alias (3 palabras separadas por puntos).  
  - Validación de datos y manejo de errores.  
  - Respuesta JSON sin incluir contraseña.

- **Inicio de Sesión**  
  - Generación de token JWT para autenticación segura.  
  - Manejo de errores:  
    - Usuario inexistente (404).  
    - Contraseña incorrecta (400).  

- **Cierre de Sesión**  
  - Invalidación del token JWT.  

---

## 🌱 Nuevas Funcionalidades (Sprint 2 - 19/11/2024)  

### 📊 Dashboard  

- **GET /accounts/{ID}**: Obtener el balance actual.  
- **GET /accounts/{ID}/transactions**: Listar las últimas 5 transacciones.  


## 🛠️ Tecnologías Utilizadas  

- **Java + Spring Boot**: Desarrollo de la API REST.  
- **MySQL**: Gestión de la base de datos.  
- **JWT**: Seguridad y autenticación.  
- **Docker**: Contenerización.  
- **Swagger**: Documentación de la API.  
- **RestAssured**: Testing automatizado.  

---
## ❌ Borrar registros y reiniciar secuencia de account y Transaction
```sql
TRUNCATE TABLE account RESTART IDENTITY CASCADE;
```

```sql
TRUNCATE TABLE transaction RESTART IDENTITY CASCADE;

```
---
# 📚 Instrucciones para insertar datos en la base de datos

Para poder obtener información desde **Postman**, primero necesitas insertar algunos datos de ejemplo en las tablas correspondientes.

---

## 1️⃣ Insertar datos en la tabla `account`

1. Abre **pgAdmin4** y conéctate a tu base de datos.
2. Navega a **Query Tool**.
3. Ejecuta el siguiente SQL para insertar datos en la tabla `account`:

```sql
INSERT INTO account (account_number, balance)
VALUES
('123456789', 1000.00);
```
---
## 2️⃣ Insertar datos en la tabla transaction
Para insertar datos en la tabla transaction, sigue estos pasos:

Abre pgAdmin4 y conéctate a tu base de datos.
Navega a Query Tool.
Ejecuta el siguiente SQL para insertar las transacciones correspondientes:

```sql
INSERT INTO transaction (account_id, amount, date, description)
VALUES
(1, -200.00, '2024-11-19', 'Compra supermercado'),
(1, 500.00, '2024-11-18', 'Pago depósito'),
(1, -100.00, '2024-11-17', 'Pago de servicios'),
(1, 300.00, '2024-11-16', 'Transferencia recibida'),
(1, -50.00, '2024-11-15', 'Compra en línea');
```
---
### 🔥 API ENDPOITS (funciona insertando el bearer token)

- **GET http://localhost:8080/api/users/{id}

```🆗 Salida JSON espera 200 OK
  "id": 1,
  "username": "nuevoUsuario",
  "email": "nuevoUsuario@example.com",
  "cvu": "1234567890123456789012",
  "alias": "alias.ejemplo"
```


- **PATCH http://localhost:8080/api/users/{patch}
  
```🆗 Entrada campos  a actualizar
  "email": "usuarioactualizado@example.com",
  "alias": "nuevo.alias"
```

```🆗 Salida JSON espera 200 OK
  "id": 1,
  "username": "nuevoUsuario",
  "email": "usuarioactualizado@example.com",
  "cvu": "1234567890123456789012",
  "alias": "nuevo.alias"
```



---
💡 **¡Seguimos construyendo una experiencia financiera segura y eficiente!**
