INSERT INTO dbo.estados (nombre)
VALUES
    ('ACTIVO'),
    ('INACTIVO'),
    ('BORRADOR'),
    ('PUBLICADA'),
    ('CERRADA'),
    ('PENDIENTE'),
    ('APROBADA'),
    ('RECHAZADA');

INSERT INTO dbo.roles (nombre, estado_id)
SELECT datos.nombre, estados.id
FROM (
    VALUES
        ('ADMINISTRADOR'),
        ('DOCENTE'),
        ('ESTUDIANTE')
) AS datos(nombre)
CROSS JOIN dbo.estados estados
WHERE estados.nombre = 'ACTIVO';

INSERT INTO dbo.categorias (nombre, estado_id)
SELECT datos.nombre, estados.id
FROM (
    VALUES
        ('Investigacion'),
        ('Bienestar'),
        ('Academica'),
        ('Deportiva'),
        ('Cultural')
) AS datos(nombre)
CROSS JOIN dbo.estados estados
WHERE estados.nombre = 'ACTIVO';

-- Usuario inicial solo para desarrollo local.
-- Nombre de usuario: admin
-- Correo: admin@universidad.edu.co
-- Password temporal: password
-- Cambiar la clave cuando se implemente el flujo de gestion de usuarios.
INSERT INTO dbo.usuarios (
    identificacion,
    nombre,
    correo,
    nombre_usuario,
    clave,
    rol_id,
    estado_id
)
SELECT
    '1000000000',
    'Administrador SGCI',
    'admin@universidad.edu.co',
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZAgcfl7p92ldGxad68LJZdL17lhWy',
    roles.id,
    estados.id
FROM dbo.roles roles
CROSS JOIN dbo.estados estados
WHERE roles.nombre = 'ADMINISTRADOR'
  AND estados.nombre = 'ACTIVO';
