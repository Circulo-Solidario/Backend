-- Migration: Add recovery columns to usuarios table
-- This migration adds the recovery code columns to support password recovery feature

--ALTER TABLE usuarios ADD COLUMN codigo_recuperacion VARCHAR(10) AFTER estado;
--ALTER TABLE usuarios ADD COLUMN fecha_codigo_recuperacion TIMESTAMP AFTER codigo_recuperacion;
