IF COL_LENGTH('dbo.estados', 'tipo') IS NULL
BEGIN
    ALTER TABLE dbo.estados
    ADD tipo VARCHAR(50) NULL;
END
GO

UPDATE dbo.estados
SET tipo = CASE
    WHEN nombre IN ('ACTIVO', 'INACTIVO') THEN 'GENERAL'
    WHEN nombre IN ('BORRADOR', 'PUBLICADA', 'CERRADA') THEN 'CONVOCATORIA'
    WHEN nombre IN ('PENDIENTE', 'APROBADA', 'RECHAZADA') THEN 'POSTULACION'
    ELSE 'GENERAL'
END
WHERE tipo IS NULL;
GO

ALTER TABLE dbo.estados
ALTER COLUMN tipo VARCHAR(50) NOT NULL;
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = 'CK_estados_tipo'
      AND parent_object_id = OBJECT_ID('dbo.estados')
)
BEGIN
    ALTER TABLE dbo.estados
    ADD CONSTRAINT CK_estados_tipo
    CHECK (tipo IN ('GENERAL', 'CONVOCATORIA', 'POSTULACION'));
END
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_estados_tipo'
      AND object_id = OBJECT_ID('dbo.estados')
)
BEGIN
    CREATE INDEX IX_estados_tipo
    ON dbo.estados (tipo);
END
GO
