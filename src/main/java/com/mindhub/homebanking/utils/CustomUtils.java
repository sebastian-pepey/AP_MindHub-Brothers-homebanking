package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CustomUtils {

    public CustomUtils() {
    }

    public boolean exists(String methodToInvoke, String parameterToInvoke, JpaRepository repository) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if(parameterToInvoke.isEmpty()) {
            Method method = repository.getClass().getMethod(methodToInvoke);
            Object result = method.invoke(repository);
            return result != null;
        } else {
            Method method = repository.getClass().getMethod(methodToInvoke, parameterToInvoke.getClass());
            Object result = method.invoke(repository,parameterToInvoke);
            return result != null;
        }
    }

    public long count(String methodToInvoke, CardType parameterToInvoke, JpaRepository repository) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = repository.getClass().getMethod("countBy"+methodToInvoke, parameterToInvoke.getClass());
        return (long) method.invoke(repository,parameterToInvoke);
    }
}
