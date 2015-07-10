package com.automate.wizard;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class WizardResultFactory {
    
    @SuppressWarnings("unchecked")
    public static <T> T of(final Class<T> cls, final Map<String, Object> map) {
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[] { cls }, new ResultInvocationHandler(map));
    }
    
    private static class ResultInvocationHandler implements InvocationHandler {

        private final Map<String, Object> map;

        public ResultInvocationHandler(final Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            final String key = method.getName();

            if (map.containsKey(key)) {
                return map.get(key);
            }

            throw new MissingValueException(key);
        }

    }

}
