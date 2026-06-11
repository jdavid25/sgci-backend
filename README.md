# SGCI Backend

Backend del Sistema de Gestion de Convocatorias Institucionales (SGCI).

El proyecto esta construido con Java 21, Spring Boot 4.1.0, Spring Security, Spring Data JPA, Flyway y SQL Server Express.

## Requisitos

- JDK 21 LTS
- SQL Server Express
- Git
- Maven, o usar el wrapper incluido: `mvnw.cmd`
- Postman o Insomnia para probar los endpoints

## Base de datos

Crear previamente la base de datos en SQL Server:

```sql
CREATE DATABASE sgci_db;
```

## Configuracion

La conexion esta configurada en:

```text
src/main/resources/application.properties
```

Valores por defecto:

```properties
DB_HOST=localhost
DB_PORT=1433
DB_NAME=sgci_db
SERVER_PORT=8080
```

Tambien se pueden cambiar por variables de entorno antes de ejecutar:

```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="1433"
$env:DB_NAME="sgci_db"
```

## Migraciones Flyway

Flyway ejecuta automaticamente los scripts ubicados en:

```text
src/main/resources/db/migration
```

Migraciones actuales:

- `V1__schema_inicial.sql`: crea tablas, llaves primarias, foraneas y restricciones.
- `V2__datos_iniciales.sql`: inserta estados, roles, categorias y usuario administrador inicial.
- `V3__indices_consultas.sql`: crea indices para consultas y reportes.

Flyway no crea la base `sgci_db`; la base debe existir antes de iniciar la aplicacion.

## Modelo inicial

Tablas principales:

- `estados`
- `roles`
- `usuarios`
- `categorias`
- `convocatorias`
- `convocatoria_categoria`
- `postulaciones`

Estados iniciales:

- `ACTIVO`
- `INACTIVO`
- `BORRADOR`
- `PUBLICADA`
- `CERRADA`
- `PENDIENTE`
- `APROBADA`
- `RECHAZADA`

Roles iniciales:

- `ADMINISTRADOR`
- `DOCENTE`
- `ESTUDIANTE`

Usuario inicial de desarrollo:

```text
nombre_usuario: admin
correo: admin@universidad.edu.co
clave: password
```

## Ejecutar el proyecto

Desde la raiz del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicacion quedara disponible en:

```text
http://localhost:8080
```

Mientras no este implementada la configuracion JWT propia del proyecto, Spring Security puede mostrar un login web por defecto. Ese login es temporal y sera reemplazado por autenticacion REST mediante JWT.
