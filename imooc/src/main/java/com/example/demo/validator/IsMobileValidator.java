package com.example.demo.validator;


import com.example.demo.util.Validator;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required=false;
    @Override
    //拿到注解
    public void initialize(IsMobile constraintAnnotation) {
        required=constraintAnnotation.required();
    }

    @Override
    //判断注解是否合法
    public boolean isValid(String s, ConstraintValidatorContext Context) {
        //如果这个值是必须的，就判断值是否合法
        if(required){
            return Validator.isMobile(s);
            //如果这个值不是必须的，就判断是否为空，如果是空则直接通过校验
            //否则判断格式是否正确
        }else {
            if(StringUtils.isEmpty(s)){
                return true;
            }else {
                return Validator.isMobile(s);
            }
        }

    }
}
