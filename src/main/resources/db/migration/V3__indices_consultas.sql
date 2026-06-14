CREATE UNIQUE INDEX UX_estados_tipo_nombre_no_eliminado
ON dbo.estados (tipo, nombre)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX UX_roles_nombre_no_eliminado
ON dbo.roles (nombre)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX UX_usuarios_identificacion_no_eliminado
ON dbo.usuarios (identificacion)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX UX_usuarios_correo_no_eliminado
ON dbo.usuarios (correo)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX UX_usuarios_nombre_usuario_no_eliminado
ON dbo.usuarios (nombre_usuario)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX UX_categorias_nombre_no_eliminado
ON dbo.categorias (nombre)
WHERE deleted_at IS NULL;

CREATE INDEX IX_usuarios_rol_id ON dbo.usuarios (rol_id);
CREATE INDEX IX_usuarios_nombre ON dbo.usuarios (nombre);

CREATE INDEX IX_convocatorias_estado_id ON dbo.convocatorias (estado_id);
CREATE INDEX IX_convocatorias_fechas ON dbo.convocatorias (fecha_inicio, fecha_fin);
CREATE INDEX IX_convocatorias_nombre ON dbo.convocatorias (nombre);
CREATE INDEX IX_convocatorias_deleted_at ON dbo.convocatorias (deleted_at);

CREATE INDEX IX_convocatoria_categoria_categoria_id ON dbo.convocatoria_categoria (categoria_id);

CREATE INDEX IX_postulaciones_convocatoria_id ON dbo.postulaciones (convocatoria_id);
CREATE INDEX IX_postulaciones_usuario_id ON dbo.postulaciones (usuario_id);
CREATE INDEX IX_postulaciones_estado_id ON dbo.postulaciones (estado_id);
CREATE INDEX IX_postulaciones_created_at ON dbo.postulaciones (created_at);
