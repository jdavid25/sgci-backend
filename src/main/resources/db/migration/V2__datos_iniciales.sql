INSERT INTO dbo.estados (nombre, tipo)
VALUES
    ('BORRADOR', 'CONVOCATORIA'),
    ('PUBLICADA', 'CONVOCATORIA'),
    ('CERRADA', 'CONVOCATORIA'),
    ('PENDIENTE', 'POSTULACION'),
    ('APROBADA', 'POSTULACION'),
    ('RECHAZADA', 'POSTULACION');

INSERT INTO dbo.roles (nombre)
VALUES
    ('ADMINISTRADOR'),
    ('DOCENTE'),
    ('ESTUDIANTE');

INSERT INTO dbo.categorias (nombre)
VALUES
    ('Investigacion'),
    ('Bienestar'),
    ('Academica'),
    ('Deportiva'),
    ('Cultural');

-- Usuario inicial solo para desarrollo local.
-- Nombre de usuario: admin
-- Correo: admin@universidad.edu.co
-- Password temporal: password123
-- Cambiar la clave cuando se implemente el flujo de gestion de usuarios.
INSERT INTO dbo.usuarios (
    identificacion,
    nombre,
    correo,
    nombre_usuario,
    clave,
    rol_id
)
SELECT
    '1000000000',
    'Administrador SGCI',
    'admin@universidad.edu.co',
    'admin',
    '$2a$10$0ckI6D9vzdu0oZPAP0pNMus1kLzCzeqrgMpZlKCuam095Oh78.YfK',
    roles.id
FROM dbo.roles roles
WHERE roles.nombre = 'ADMINISTRADOR';
