CREATE INDEX IX_roles_estado_id ON dbo.roles (estado_id);

CREATE INDEX IX_usuarios_rol_id ON dbo.usuarios (rol_id);
CREATE INDEX IX_usuarios_estado_id ON dbo.usuarios (estado_id);
CREATE INDEX IX_usuarios_nombre ON dbo.usuarios (nombre);
CREATE INDEX IX_usuarios_nombre_usuario ON dbo.usuarios (nombre_usuario);

CREATE INDEX IX_categorias_estado_id ON dbo.categorias (estado_id);

CREATE INDEX IX_convocatorias_estado_id ON dbo.convocatorias (estado_id);
CREATE INDEX IX_convocatorias_fechas ON dbo.convocatorias (fecha_inicio, fecha_fin);
CREATE INDEX IX_convocatorias_nombre ON dbo.convocatorias (nombre);

CREATE INDEX IX_convocatoria_categoria_categoria_id ON dbo.convocatoria_categoria (categoria_id);
CREATE INDEX IX_convocatoria_categoria_estado_id ON dbo.convocatoria_categoria (estado_id);

CREATE INDEX IX_postulaciones_convocatoria_id ON dbo.postulaciones (convocatoria_id);
CREATE INDEX IX_postulaciones_usuario_id ON dbo.postulaciones (usuario_id);
CREATE INDEX IX_postulaciones_estado_id ON dbo.postulaciones (estado_id);
CREATE INDEX IX_postulaciones_created_at ON dbo.postulaciones (created_at);
