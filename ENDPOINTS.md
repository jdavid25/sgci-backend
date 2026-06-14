# Endpoints SGCI Backend

Base URL local:

```text
http://localhost:8080
```

Header para endpoints protegidos:

```text
Authorization: Bearer TOKEN_JWT
Content-Type: application/json
```

## Datos Iniciales

Con las migraciones actuales:

Estados de negocio:

| ID | Nombre | Tipo |
| --- | --- | --- |
| 1 | BORRADOR | CONVOCATORIA |
| 2 | PUBLICADA | CONVOCATORIA |
| 3 | CERRADA | CONVOCATORIA |
| 4 | PENDIENTE | POSTULACION |
| 5 | APROBADA | POSTULACION |
| 6 | RECHAZADA | POSTULACION |

Roles:

| ID | Nombre |
| --- | --- |
| 1 | ADMINISTRADOR |
| 2 | DOCENTE |
| 3 | ESTUDIANTE |

## 1. Autenticacion

### Login

```http
POST /api/auth/login
```

Body:

```json
{
  "nombreUsuario": "admin",
  "clave": "password"
}
```

Respuesta:

```json
{
  "token": "TOKEN_JWT",
  "usuarioId": 1,
  "nombre": "Administrador SGCI",
  "rol": "ADMINISTRADOR"
}
```

## 2. Usuarios

Requiere rol `ADMINISTRADOR`.

### Listar usuarios

```http
GET /api/usuarios
```

### Obtener usuario por ID

```http
GET /api/usuarios/1
```

### Crear usuario

```http
POST /api/usuarios
```

Body:

```json
{
  "identificacion": "1000000001",
  "nombre": "Estudiante Uno",
  "correo": "estudiante1@universidad.edu.co",
  "nombreUsuario": "estudiante1",
  "clave": "password123",
  "rolId": 3
}
```

### Actualizar usuario

```http
PUT /api/usuarios/1
```

Body:

```json
{
  "identificacion": "1000000001",
  "nombre": "Estudiante Uno Actualizado",
  "correo": "estudiante1@universidad.edu.co",
  "nombreUsuario": "estudiante1",
  "clave": "password123",
  "rolId": 3
}
```

La clave puede omitirse o enviarse en blanco si no se desea cambiar.

### Eliminar usuario

```http
DELETE /api/usuarios/1
```

La eliminacion es logica: se asigna `deleted_at` y deja de aparecer en los listados.

## 3. Estados

Requiere token valido.

### Listar estados por tipo

```http
GET /api/estados?tipo=CONVOCATORIA
```

Tipos validos:

```text
CONVOCATORIA
POSTULACION
```

Respuesta:

```json
[
  {
    "id": 1,
    "nombre": "BORRADOR",
    "tipo": "CONVOCATORIA"
  },
  {
    "id": 2,
    "nombre": "PUBLICADA",
    "tipo": "CONVOCATORIA"
  },
  {
    "id": 3,
    "nombre": "CERRADA",
    "tipo": "CONVOCATORIA"
  }
]
```

## 4. Roles

Requiere rol `ADMINISTRADOR`.

### Listar roles

```http
GET /api/roles
```

Respuesta:

```json
[
  {
    "id": 1,
    "nombre": "ADMINISTRADOR"
  },
  {
    "id": 2,
    "nombre": "DOCENTE"
  },
  {
    "id": 3,
    "nombre": "ESTUDIANTE"
  }
]
```

## 5. Categorias

Requiere rol `ADMINISTRADOR`.

### Listar categorias

```http
GET /api/categorias
```

### Obtener categoria por ID

```http
GET /api/categorias/1
```

### Crear categoria

```http
POST /api/categorias
```

Body:

```json
{
  "nombre": "Investigacion"
}
```

### Actualizar categoria

```http
PUT /api/categorias/1
```

Body:

```json
{
  "nombre": "Investigacion Aplicada"
}
```

### Eliminar categoria

```http
DELETE /api/categorias/1
```

La eliminacion es logica: se asigna `deleted_at` y deja de aparecer en los listados.

## 6. Convocatorias

El CRUD de convocatorias requiere rol `ADMINISTRADOR`.

### Listar convocatorias

```http
GET /api/convocatorias
```

### Listar convocatorias publicadas

Requiere rol `DOCENTE` o `ESTUDIANTE`.

Devuelve solo convocatorias en estado `PUBLICADA` y dentro del rango vigente de fechas.

```http
GET /api/convocatorias/publicadas
```

### Obtener convocatoria por ID

```http
GET /api/convocatorias/1
```

### Crear convocatoria

```http
POST /api/convocatorias
```

Body:

```json
{
  "nombre": "Monitorias Academicas",
  "descripcion": "Convocatoria para monitorias del periodo actual.",
  "fechaInicio": "2026-06-12",
  "fechaFin": "2026-07-12",
  "cuposDisponibles": 10,
  "estadoId": 2,
  "categoriaIds": [1, 3]
}
```

Reglas:

- `fechaFin` debe ser mayor o igual a `fechaInicio`.
- `cuposDisponibles` debe ser mayor a cero.
- Las categorias no deben estar eliminadas.
- Estados validos: `BORRADOR`, `PUBLICADA`, `CERRADA`.

### Actualizar convocatoria

```http
PUT /api/convocatorias/1
```

Body:

```json
{
  "nombre": "Monitorias Academicas 2026",
  "descripcion": "Convocatoria actualizada.",
  "fechaInicio": "2026-06-12",
  "fechaFin": "2026-07-20",
  "cuposDisponibles": 12,
  "estadoId": 2,
  "categoriaIds": [1, 3]
}
```

### Eliminar convocatoria

```http
DELETE /api/convocatorias/1
```

La eliminacion es logica: se asigna `deleted_at` y deja de aparecer en los listados.

## 7. Postulaciones

### Crear postulacion

Requiere rol `ESTUDIANTE`.

```http
POST /api/postulaciones
```

Body:

```json
{
  "convocatoriaId": 1
}
```

Reglas:

- El usuario autenticado debe ser estudiante.
- La convocatoria debe estar `PUBLICADA`.
- La fecha actual debe estar entre `fechaInicio` y `fechaFin`.
- El estudiante no puede postularse dos veces a la misma convocatoria.
- La convocatoria debe tener cupos disponibles.

### Consultar mis postulaciones

Requiere rol `ESTUDIANTE`.

```http
GET /api/postulaciones/mis-postulaciones
```

### Consultar todas las postulaciones

Requiere rol `ADMINISTRADOR`.

```http
GET /api/postulaciones
```

### Aprobar o rechazar postulacion

Requiere rol `ADMINISTRADOR`.

```http
PUT /api/postulaciones/1/estado
```

Body para aprobar:

```json
{
  "estadoId": 5
}
```

Body para rechazar:

```json
{
  "estadoId": 6
}
```

## 8. Reportes

Requiere rol `ADMINISTRADOR`.

### Convocatorias por categoria

```http
GET /api/reportes/convocatorias-categoria
```

Respuesta:

```json
[
  {
    "categoriaId": 1,
    "categoriaNombre": "Investigacion",
    "totalConvocatorias": 2
  }
]
```

### Postulaciones por convocatoria

```http
GET /api/reportes/postulaciones-convocatoria
```

Respuesta:

```json
[
  {
    "convocatoriaId": 1,
    "convocatoriaNombre": "Monitorias Academicas",
    "totalPostulaciones": 5
  }
]
```

### Resultado de postulaciones

```http
GET /api/reportes/resultado-postulaciones
```

Respuesta:

```json
[
  {
    "estadoNombre": "APROBADA",
    "totalPostulaciones": 3
  },
  {
    "estadoNombre": "RECHAZADA",
    "totalPostulaciones": 1
  }
]
```

## 9. Errores

Formato general de errores manejados por la aplicacion:

```json
{
  "timestamp": "2026-06-12T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensaje del error",
  "path": "/api/usuarios"
}
```

Errores de seguridad:

```json
{
  "message": "No autenticado."
}
```

```json
{
  "message": "Acceso denegado."
}
```

## 10. Orden Sugerido De Prueba Manual

1. Ejecutar `POST /api/auth/login` con administrador.
2. Crear un usuario estudiante con `POST /api/usuarios`.
3. Crear o validar categorias con `GET /api/categorias`.
4. Crear una convocatoria publicada con fechas vigentes.
5. Ejecutar login con el estudiante creado.
6. Crear postulacion con `POST /api/postulaciones`.
7. Consultar `GET /api/postulaciones/mis-postulaciones`.
8. Ejecutar login nuevamente como administrador.
9. Consultar `GET /api/postulaciones`.
10. Aprobar o rechazar con `PUT /api/postulaciones/{id}/estado`.
11. Consultar los tres endpoints de reportes.
