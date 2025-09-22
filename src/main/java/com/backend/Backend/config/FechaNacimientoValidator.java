package com.backend.Backend.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FechaNacimientoValidator implements ConstraintValidator<FechaNacimientoValida, Date> {

    @Override
    public boolean isValid(Date fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return false;
        }

        LocalDate fechaNac = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaMinima = LocalDate.now().minusYears(18);  // el minimo de edad que es 18 años

        return fechaNac.isBefore(fechaMinima); // la fecha debe ser anterior a la fecha minima
    }
}