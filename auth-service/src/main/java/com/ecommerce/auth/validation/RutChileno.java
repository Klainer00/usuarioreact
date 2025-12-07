package com.ecommerce.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RutChilenoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RutChileno {
    String message() default "El RUT chileno no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
