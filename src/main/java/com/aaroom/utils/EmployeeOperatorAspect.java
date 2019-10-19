package com.aaroom.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;

/**
 * Created by sosoda on 2018/6/7.
 */
@Aspect //AOP 切面
@Component
public class EmployeeOperatorAspect {

    @Pointcut(value = "@annotation(EmployeeOperator)")
    private void anyMethod(){}//定义一个切入点

    @Around("@annotation(EmployeeOperator)")
    public Object doAccessCheck(ProceedingJoinPoint pjp) throws Throwable {

        //String clazz =call.getTarget().getClass().getName();

        // 获取目标对象上正在执行的方法名


        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        HttpSession session =request.getSession();

        Integer user_id = (Integer)session.getAttribute("employee_id");

        //Object[] args = call.getArgs();

        Object[] args = pjp.getArgs();
        Object obj = args[0];

        Field declaredField = obj.getClass().getDeclaredField("id");
        declaredField.setAccessible(true);
        //String name = declaredField.getName();
        Object id = declaredField.get(obj);
        if(id==null) {
            Field createrField = obj.getClass().getDeclaredField("creater_id");
            createrField.setAccessible(true);
            createrField.set(obj, user_id);
        } else {
            Field updaterField = obj.getClass().getDeclaredField("updater_id");
            updaterField.setAccessible(true);
            updaterField.set(obj, user_id);
        }
        args[0] = obj;

        return pjp.proceed(args);
    }
}
