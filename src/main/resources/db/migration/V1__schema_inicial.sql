CREATE TABLE dbo.estados (
    id BIGINT IDENTITY(1,1) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_estados_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2(0) NOT NULL CONSTRAINT DF_estados_updated_at DEFAULT SYSUTCDATETIME(),
    deleted_at DATETIME2(0) NULL,
    CONSTRAINT PK_estados PRIMARY KEY (id),
    CONSTRAINT CK_estados_tipo CHECK (tipo IN ('CONVOCATORIA', 'POSTULACION'))
);

CREATE TABLE dbo.roles (
    id BIGINT IDENTITY(1,1) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_roles_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2(0) NOT NULL CONSTRAINT DF_roles_updated_at DEFAULT SYSUTCDATETIME(),
    deleted_at DATETIME2(0) NULL,
    CONSTRAINT PK_roles PRIMARY KEY (id)
);

CREATE TABLE dbo.usuarios (
    id BIGINT IDENTITY(1,1) NOT NULL,
    identificacion VARCHAR(30) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    nombre_usuario VARCHAR(80) NOT NULL,
    clave VARCHAR(255) NOT NULL,
    rol_id BIGINT NOT NULL,
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_usuarios_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2(0) NOT NULL CONSTRAINT DF_usuarios_updated_at DEFAULT SYSUTCDATETIME(),
    deleted_at DATETIME2(0) NULL,
    CONSTRAINT PK_usuarios PRIMARY KEY (id),
    CONSTRAINT FK_usuarios_roles FOREIGN KEY (rol_id) REFERENCES dbo.roles(id)
);

CREATE TABLE dbo.categorias (
    id BIGINT IDENTITY(1,1) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_categorias_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2(0) NOT NULL CONSTRAINT DF_categorias_updated_at DEFAULT SYSUTCDATETIME(),
    deleted_at DATETIME2(0) NULL,
    CONSTRAINT PK_categorias PRIMARY KEY (id)
);

CREATE TABLE dbo.convocatorias (
    id BIGINT IDENTITY(1,1) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(MAX) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    cupos_disponibles INT NOT NULL,
    estado_id BIGINT NOT NULL,
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_convocatorias_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2(0) NOT NULL CONSTRAINT DF_convocatorias_updated_at DEFAULT SYSUTCDATETIME(),
    deleted_at DATETIME2(0) NULL,
    CONSTRAINT PK_convocatorias PRIMARY KEY (id),
    CONSTRAINT FK_convocatorias_estados FOREIGN KEY (estado_id) REFERENCES dbo.estados(id),
    CONSTRAINT CK_convocatorias_fechas CHECK (fecha_fin >= fecha_inicio),
    CONSTRAINT CK_convocatorias_cupos CHECK (cupos_disponibles > 0)
);

CREATE TABLE dbo.convocatoria_categoria (
    convocatoria_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_convocatoria_categoria_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2(0) NOT NULL CONSTRAINT DF_convocatoria_categoria_updated_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT PK_convocatoria_categoria PRIMARY KEY (convocatoria_id, categoria_id),
    CONSTRAINT FK_convocatoria_categoria_convocatorias FOREIGN KEY (convocatoria_id) REFERENCES dbo.convocatorias(id),
    CONSTRAINT FK_convocatoria_categoria_categorias FOREIGN KEY (categoria_id) REFERENCES dbo.categorias(id)
);

CREATE TABLE dbo.postulaciones (
    id BIGINT IDENTITY(1,1) NOT NULL,
    convocatoria_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    estado_id BIGINT NOT NULL,
    created_at DATETIME2(0) NOT NULL CONSTRAINT DF_postulaciones_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2(0) NOT NULL CONSTRAINT DF_postulaciones_updated_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT PK_postulaciones PRIMARY KEY (id),
    CONSTRAINT UQ_postulaciones_convocatoria_usuario UNIQUE (convocatoria_id, usuario_id),
    CONSTRAINT FK_postulaciones_convocatorias FOREIGN KEY (convocatoria_id) REFERENCES dbo.convocatorias(id),
    CONSTRAINT FK_postulaciones_usuarios FOREIGN KEY (usuario_id) REFERENCES dbo.usuarios(id),
    CONSTRAINT FK_postulaciones_estados FOREIGN KEY (estado_id) REFERENCES dbo.estados(id)
);
