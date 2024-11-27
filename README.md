# 🏦 Digital Money House - Billetera Virtual

## 📚 Descripción del Proyecto  
Digital Money House es una billetera virtual diseñada para ofrecer una experiencia ágil y segura. Este proyecto es parte del desafío profesional de backend, donde se implementa una API REST que permite la gestión de cuentas, sesiones de usuario, transacciones financieras y más.

---

## 🚀 Funcionalidades Implementadas  

## 🔒 Gestión de Usuario (Sprint 1 Completado)  

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

## 🌱 Datos User y Card (Sprint 2 Completado)  

### 📊 Dashboard  

- **GET /accounts/{ID}**: Obtener el balance actual.  
- **GET /accounts/{ID}/transactions**: Listar las últimas 5 transacciones.  

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
### 🔥👤 API ENDPOITS USERS (funciona insertando el bearer token)

- **GET http://localhost:8080/api/users/{id}**: Obtener User por ID

```🆗 Salida JSON espera 200 OK
  "id": 1,
  "username": "nuevoUsuario",
  "email": "nuevoUsuario@example.com",
  "cvu": "1234567890123456789012",
  "alias": "alias.ejemplo"
```


- **PATCH http://localhost:8080/api/users/{id}**: Actualizar campos
  
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


### 🔥💳 API ENDPOITS CARDS (funciona insertando el bearer token)


- **POST http://localhost:8080/accounts/{id}/cards**: Agregar Tarjeta

```🆗 Datos a Ingresar Para Hacer POST
{
  "cardNumber": "6543210987654321",
  "cardHolderName": "Carlos López",
  "cardType": "Crédito"
}
```

```🆗 Respuestsa esperada 
{
  "id": 3,
  "cardNumber": "6543210987654321",
  "cardHolderName": "Carlos López",
  "cardType": "Crédito"
}
```

- **GET http://localhost:8080/accounts/{id}/cards**: Obtener Tarjetas Asociadas

```🆗 Salida JSON espera 200 OK
[
  {
    "id": 1,
    "cardNumber": "1234567890123456",
    "cardHolderName": "Juan Pérez",
    "cardType": "Crédito"
  },
  {
    "id": 2,
    "cardNumber": "9876543210987654",
    "cardHolderName": "María García",
    "cardType": "Débito"
  }
]
```

- **GET http://localhost:8080/accounts/{accountId}/cards/{cardId}**: Obtener Detalle De Tarjeta Específica

```🆗 Salida JSON espera 200 OK
{
  "id": 1,
  "cardNumber": "1234567890123456",
  "cardHolderName": "Juan Pérez",
  "cardType": "Crédito"
}
```

- **DELETE http://localhost:8080/accounts/{accountId}/cards/{cardId}**: Eliminar Tarjeta 

```🆗 Salida JSON espera 200 OK
{
  "message": "Card deleted successfully"
}
```
---
## 🎮 Gestión de Actividad Reciente (Sprint 3 Completado)

- **GET http://localhost:8080/accounts/{id}/activity**: Obtener Actividad Reciente

```🆗 Salida JSON espera 200 OK
[
  {
    "id": 1,
    "amount": -200.00,
    "date": "2024-11-19T12:00:00",
    "description": "Compra supermercado"
  },
  {
    "id": 2,
    "amount": 500.00,
    "date": "2024-11-18T15:00:00",
    "description": "Pago depósito"
  }
]
```

- **GET http://localhost:8080/accounts/{accountId}/activity/{transactionId}**: Obtener Detalles De Una Actividad

```🆗 Salida JSON espera 200 OK
{
  "id": 1,
  "amount": -200.00,
  "date": "2024-11-19T12:00:00",
  "description": "Compra supermercado"
}
```

- **POST http://localhost:8080/accounts/{id}/transferences**: Ingresar Dinero

### Parámetros en el Body (URL encoded):

amount: Monto a ingresar (ej. 1000).
description: Descripción de la transacción (ej. "Depósito en cuenta").
```🆗 Salida JSON espera 200 OK
{
  "id": 10,
  "description": "Depósito en cuenta",
  "amount": 1000.00,
  "date": "2024-11-19T12:00:00",
  "account": {
    "id": 1,
    "accountNumber": "123456789",
    "balance": 2000.00
  }
}
```




---
💡 **¡Seguimos construyendo una experiencia financiera segura y eficiente!**

## 🛠️ Tecnologías Utilizadas  

- **Java + Spring Boot**: Desarrollo de la API REST.  
- **POSTGRESQL**: Gestión de la base de datos.  
- **JWT**: Seguridad y autenticación.  
- **Docker**: Contenerización.  
- **Swagger**: Documentación de la API.  
- **RestAssured**: Testing automatizado.  

