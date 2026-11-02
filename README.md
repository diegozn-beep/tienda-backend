# Tienda Backend

API REST para una tienda online de tecnología, hecha con Spring Boot, Spring Security (JWT) y MySQL.

## Funciones
- Registro e inicio de sesión con JWT, con contraseñas cifradas con BCrypt
- Dos roles: `CLIENT` (cliente) y `ADMIN` (administrador)
- Catálogo público de productos, con búsqueda y filtro por categoría
- Solo el administrador puede crear, editar y borrar productos
- Los pedidos validan el stock y lo descuentan automáticamente
- Cada cliente solo puede ver sus propios pedidos
- El administrador ve todos los pedidos, cambia su estado y consulta estadísticas básicas

## Tecnologías
Java, Spring Boot, Spring Web, Spring Data JPA, Spring Security, JWT (jjwt), MySQL, Maven

## Cómo ejecutarlo en tu PC
1. Instala Java 17 o superior y MySQL.
2. Configura estas variables de entorno:

| Variable | Descripción |
|---|---|
| `DB_PASSWORD` | Tu contraseña de MySQL |
| `DB_USER` | Usuario de MySQL (por defecto: `root`) |
| `JWT_SECRET` | Texto largo y aleatorio, de al menos 32 caracteres |
| `CORS_ORIGIN` | URL del frontend (por defecto: `http://localhost:4200`) |

3. Inicia la aplicación:

```
mvn spring-boot:run
```

(o ejecuta la clase `TiendaBackendApplication` desde tu IDE)

4. La base de datos `tienda_db` se crea sola, con productos de ejemplo y un usuario administrador de prueba para desarrollo:
   - correo: `admin@tecnodiego.pe`
   - contraseña: `admin123`

Cambia la contraseña del administrador y el `JWT_SECRET` antes de publicar la aplicación en internet.

## Rutas de la API

| Método | Ruta | Acceso |
|---|---|---|
| POST | `/api/auth/register` | Público |
| POST | `/api/auth/login` | Público |
| GET | `/api/products?category=&search=` | Público |
| GET | `/api/products/{id}` | Público |
| POST, PUT, DELETE | `/api/products` | Administrador |
| POST | `/api/orders` | Usuario con sesión iniciada |
| GET | `/api/orders/my` | Usuario con sesión iniciada |
| GET | `/api/admin/orders` | Administrador |
| PUT | `/api/admin/orders/{id}/status?status=SHIPPED` | Administrador |
| GET | `/api/admin/stats` | Administrador |

Estados de un pedido: `PENDING` (pendiente), `SHIPPED` (enviado), `DELIVERED` (entregado).

El token se envía en el encabezado: `Authorization: Bearer <token>`.

## Ejemplos

Inicio de sesión:

```
POST /api/auth/login
{
  "email": "admin@tecnodiego.pe",
  "password": "admin123"
}
```

Crear un pedido (como cliente con sesión iniciada):

```
POST /api/orders
{
  "customerName": "Maria Torres",
  "address": "Av. Los Alisos 123",
  "phone": "999999999",
  "paymentMethod": "Yape",
  "items": [
    { "productId": 1, "quantity": 2 }
  ]
}
```

## Próximos pasos
- Frontend
- Docker y despliegue
- Tests unitarios y documentación con Swagger
