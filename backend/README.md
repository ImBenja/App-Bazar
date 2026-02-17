# 🔧 Backend - APP BAZAR

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-green?style=flat-square&logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?style=flat-square&logo=apache-maven)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![REST API](https://img.shields.io/badge/REST%20API-v1-brightgreen?style=flat-square)

**API REST profesional para la gestion en un bazar con Spring Boot 3**

</div>

## Stack tecnologico

- Java 17
- Spring Boot 3.5.10
- Spring Web
- Spring Data JPA
- Hibernate
- Maven Wrapper (`mvnw`, `mvnw.cmd`)
- MariaDB (principal)
- Docker y Docker Compose

## Estructura del backend

Ruta principal del backend: `backend/app`

Capas y paquetes:

- `controller`: endpoints REST de clientes, productos y ventas.
- `service`: logica de negocio y validaciones de estados/stock.
- `repository`: acceso a datos con Spring Data JPA.
- `model`: entidades JPA (`Cliente`, `Producto`, `Venta`, `DetalleVenta`).
- `dto`: contratos de entrada/salida para API.
- `mapper`: conversion entre entidades y DTO.
- `error`: excepciones de negocio y manejo global de errores.
- `config`: configuraciones web (CORS).

## Requisitos

- Java 17+
- Docker y Docker Compose (para entorno con contenedores)
- Maven (opcional, ya que el proyecto incluye wrapper)

## Configuracion por variables de entorno

El backend toma su configuracion desde variables definidas en `application.properties`.

Variables principales:

- `SPRING_APPLICATION_NAME`
- `SERVER_PORT`
- `SERVER_CONTEXT_PATH`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_DRIVER`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_DATABASE_PLATFORM`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `SPRING_JPA_SHOW_SQL`
- `SPRING_JPA_FORMAT_SQL`

En este repo tenes ejemplos y valores actuales en:

- `config/.env.example`
- `config/.env`

Importante: para MariaDB, `SPRING_DATASOURCE_DRIVER` debe ser `org.mariadb.jdbc.Driver`.

## Ejecucion local (sin Docker)

1. Ir al backend:

```bash
cd backend/app
```

2. Definir variables de entorno (por ejemplo usando `config/.env` como referencia).

3. Ejecutar con Maven Wrapper:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Tambien existe el script `backend/app/run-with-env.ps1`, que levanta el backend leyendo un `.env` ubicado en `backend/app/.env`.

## Ejecucion con Docker Compose (recomendada)

Desde la raiz del proyecto:

```bash
docker compose up --build
```

Servicios incluidos:

- `db`: MariaDB 10.11 (host `localhost:3307`).
- `app`: backend Spring Boot (host `localhost:8080`).
- `phpmyadmin`: administracion de base de datos (host `localhost:8081`).

## API REST

Base URL por defecto:

- `http://localhost:8080`

Context path configurable:

- `SERVER_CONTEXT_PATH` (en `config/.env` actualmente `/`)

### Clientes (`/api/clientes`)

- `GET /api/clientes`
- `GET /api/clientes/id/{id}`
- `GET /api/clientes/estado/{estadoCliente}`
- `POST /api/clientes/crear`
- `PUT /api/clientes/editar/{id}`
- `PATCH /api/clientes/editar/{id}`
- `PATCH /api/clientes/activar/{id}`
- `PATCH /api/clientes/inactivar/{id}`
- `PATCH /api/clientes/bloquear/{id}`

Estados validos: `ACTIVO`, `INACTIVO`, `BLOQUEADO`.

### Productos (`/api/productos`)

- `GET /api/productos`
- `GET /api/productos/codigo/{codigo}`
- `GET /api/productos/nombre/{nombre}`
- `GET /api/productos/categoria/{categoria}`
- `GET /api/productos/marca/{marca}`
- `GET /api/productos/estado/{estado}`
- `GET /api/productos/falta-stock`
- `GET /api/productos/precio-menor/{precio}`
- `GET /api/productos/precio-mayor/{precio}`
- `GET /api/productos/precio-rango?min={min}&max={max}`
- `POST /api/productos/crear`
- `PUT /api/productos/editar/{codigo}`
- `PATCH /api/productos/editar/{codigo}`
- `PATCH /api/productos/activar/{codigo}`
- `PATCH /api/productos/inactivar/{codigo}`
- `PATCH /api/productos/agotado/{codigo}`
- `PATCH /api/productos/descontinuar/{codigo}`

Estados validos: `ACTIVO`, `INACTIVO`, `AGOTADO`, `DESCONTINUADO`.

### Ventas (`/api/ventas`)

- `GET /api/ventas`
- `GET /api/ventas/codigo/{codigo}`
- `GET /api/ventas/cliente/{idCliente}`
- `GET /api/ventas/fecha/{fDate}`
- `GET /api/ventas/estado/{estado}`
- `GET /api/ventas/cliente-fecha?idCliente={id}&fecha={yyyy-MM-dd}`
- `GET /api/ventas/mayor-venta`
- `GET /api/ventas/estadistica-dia/{fechaVenta}`
- `POST /api/ventas/crear`
- `PUT /api/ventas/editar/{codigo}`
- `PATCH /api/ventas/editar/{codigo}`
- `PATCH /api/ventas/marcar-pagada/{codigo}`
- `PATCH /api/ventas/marcar-entregada/{codigo}`
- `PATCH /api/ventas/marcar-cancelada/{codigo}`

Estados validos: `PENDIENTE`, `PAGADA`, `ENTREGADA`, `CANCELADA`.

## Ejemplos de payload

### Crear cliente

```json
{
  "nombre": "Juan",
  "apellido": "Perez",
  "dni": "12.345.678"
}
```

### Crear producto

```json
{
  "nombre": "Yerba Mate 1kg",
  "marca": "Rosamonte",
  "categoria": "Almacen",
  "precio": 4200.0,
  "stock": 30
}
```

### Crear venta

```json
{
  "fecha": "2026-02-17",
  "idCliente": 1,
  "detalles": [
    {
      "productoId": 2,
      "cantidad": 3
    },
    {
      "productoId": 4,
      "cantidad": 1
    }
  ]
}
```

## Reglas de negocio relevantes

- Cliente:
- No se puede crear con DNI duplicado.
- Un cliente `BLOQUEADO` no puede editarse ni comprar.
- Un cliente `INACTIVO` o `BLOQUEADO` no puede comprar.

- Producto:
- `DESCONTINUADO` no puede editarse ni reactivarse.
- `AGOTADO` no puede activarse ni inactivarse directamente.
- En ventas, productos `INACTIVO`, `DESCONTINUADO` o `AGOTADO` no son vendibles.

- Venta:
- Se crea en estado `PENDIENTE`.
- Al crear una venta, se valida stock y se descuenta.
- Si se cancela una venta, se restaura el stock.
- Ventas `PAGADA`, `ENTREGADA` o `CANCELADA` no son editables.

## Manejo de errores

Formato de error:

```json
{
  "status": 400,
  "message": "Descripcion del error",
  "timestamp": "2026-02-17T12:00:00",
  "details": ["campo: mensaje de validacion"]
}
```

Codigos manejados por `GlobalExceptionHandler`:

- `400` validaciones, argumentos invalidos, JSON mal formado, tipos invalidos.
- `404` recurso no encontrado.
- `405` metodo HTTP no permitido.
- `409` conflicto de datos (integridad/duplicados/FK).
- `422` reglas de negocio.
- `500` error interno no controlado.

## CORS

Se permiten requests a `/api/**` desde:

- `http://localhost:5500`
- `http://localhost:3000`
- `http://127.0.0.1:5500`
- `http://127.0.0.1:3000`

## Testing

El proyecto incluye pruebas con Spring Boot Test (`src/test/java`).

Ejecucion:

```bash
cd backend/app
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

## Integracion con el frontend del repo

El frontend (`frontend/app.js`) consume el backend con base URL configurable, por defecto:

- `http://localhost:8080`

Si modificas puerto o contexto en backend, actualiza la URL en el frontend para evitar errores de conexion.
