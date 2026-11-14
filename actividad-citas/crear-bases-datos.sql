-- Actividad: Sistema de Gestión de Citas - Clínica "Salud Integral"
-- Una base de datos por microservicio. Las tablas las crea JPA (ddl-auto: update)
-- con la misma estructura de la guía.
CREATE DATABASE IF NOT EXISTS bd_paciente;  -- paciente
CREATE DATABASE IF NOT EXISTS bd_medico;    -- especialidad, medico, horario_medico
CREATE DATABASE IF NOT EXISTS bd_cita;      -- cita, atencion
