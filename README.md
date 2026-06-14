# SGCI Backend

Backend del Sistema de Gestion de Convocatorias Institucionales (SGCI).

El proyecto esta construido con Java 21, Spring Boot 4.1.0, Spring Security, JWT, Spring Data JPA, Flyway y SQL Server Express.

## Requisitos

- JDK 21 LTS
- SQL Server Express
- Git
- Maven, o usar el wrapper incluido: `mvnw.cmd`
- Postman para probar los endpoints

## Estado Actual

Modulos implementados:

- Autenticacion con JWT.
- Gestion de usuarios.
- Consulta de roles para administradores.
- Consulta de estados por tipo.
- Gestion de categorias.
- Gestion de convocatorias.
- Gestion de postulaciones.
- Reportes.
- Seguridad por roles.
- Manejo global de errores.
- Migraciones Flyway.
- Coleccion Postman.

Roles manejados:

- `ADMINISTRADOR`
- `DOCENTE`
- `ESTUDIANTE`

## Base De Datos

Crear previamente la base de datos en SQL Server:

```sql
CREATE DATABASE sgci_db;
```

Flyway no crea la base de datos; solo crea y actualiza las tablas dentro de una base existente.

## Configuracion

La conexion esta configurada en:

```text
src/main/resources/application.properties
```

Valores configurables por variables de entorno:

```properties
SERVER_PORT=8080
DB_HOST=localhost
DB_PORT=1433
DB_NAME=sgci_db
JWT_SECRET=clave-local-para-jwt
JWT_EXPIRATION_MINUTES=120
```

Ejemplo en PowerShell:

```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="1433"
$env:DB_NAME="sgci_db"
$env:JWT_SECRET="cambiar-esta-clave-local"
$env:JWT_EXPIRATION_MINUTES="120"
```

## Migraciones Flyway

Flyway ejecuta automaticamente los scripts ubicados en:

```text
src/main/resources/db/migration
```

Migraciones actuales:

- `V1__schema_inicial.sql`: crea tablas, llaves primarias, foraneas y restricciones.
- `V2__datos_iniciales.sql`: inserta estados, roles, categorias y usuario administrador inicial.
- `V3__indices_consultas.sql`: crea indices para consultas, reportes y valores unicos filtrados.

## Modelo Principal

Tablas principales:

- `estados`
- `roles`
- `usuarios`
- `categorias`
- `convocatorias`
- `convocatoria_categoria`
- `postulaciones`

Los estados de negocio se manejan en una tabla central `estados`.

Tipos de estado:

- `CONVOCATORIA`: `BORRADOR`, `PUBLICADA`, `CERRADA`
- `POSTULACION`: `PENDIENTE`, `APROBADA`, `RECHAZADA`

Los registros eliminables usan borrado logico mediante `deleted_at`.

## Usuario Inicial

Usuario administrador inicial de desarrollo:

```text
nombre_usuario: admin
correo: admin@universidad.edu.co
clave: password
```

Con este usuario se obtiene el JWT inicial desde:

```http
POST /api/auth/login
```

## Ejecutar El Proyecto

Desde la raiz del backend:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicacion queda disponible en:

```text
http://localhost:8080
```

## Seguridad

La API usa JWT mediante el header:

```text
Authorization: Bearer TOKEN_JWT
```

Reglas principales:

- `POST /api/auth/login`: publico.
- `/api/usuarios/**`: solo `ADMINISTRADOR`.
- `/api/roles/**`: solo `ADMINISTRADOR`.
- `/api/categorias/**`: solo `ADMINISTRADOR`.
- `GET /api/convocatorias/publicadas`: `DOCENTE` o `ESTUDIANTE`.
- `/api/convocatorias/**`: solo `ADMINISTRADOR`.
- `/api/reportes/**`: solo `ADMINISTRADOR`.
- `POST /api/postulaciones`: solo `ESTUDIANTE`.
- `GET /api/postulaciones/mis-postulaciones`: solo `ESTUDIANTE`.
- `GET /api/postulaciones`: solo `ADMINISTRADOR`.
- `PUT /api/postulaciones/{id}/estado`: solo `ADMINISTRADOR`.

CORS esta configurado para permitir consumo desde:

```text
http://localhost:4200
```

## Endpoints Y Postman

La documentacion manual de endpoints esta en:

```text
ENDPOINTS.md
```

La coleccion Postman esta en:

```text
postman/SGCI.postman_collection.json
```

La coleccion incluye variables para `baseUrl`, tokens JWT e IDs reutilizables durante el flujo de prueba.

## Flujo Manual Recomendado

1. Iniciar la aplicacion.
2. Importar la coleccion Postman.
3. Ejecutar login como administrador.
4. Crear un estudiante.
5. Crear o validar categorias.
6. Crear una convocatoria publicada con fechas vigentes.
7. Ejecutar login como estudiante.
8. Crear una postulacion.
9. Consultar mis postulaciones.
10. Ejecutar login como administrador.
11. Aprobar o rechazar la postulacion.
12. Consultar reportes.
