package com.example.demo.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.*;

@Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class})
public @interface IsMobile {
    boolean required() default true;

    String message() default "手机号码格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};

}
