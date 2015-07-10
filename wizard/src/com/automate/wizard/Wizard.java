package com.automate.wizard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wizard<T> {
    
    private final Class<T> resultInterface;
    private final Class<? extends T> controllerClass;

    public Wizard(final Class<T> resultInterface, final Class<? extends T> controllerClass) {
        this.resultInterface = resultInterface;
        this.controllerClass = controllerClass;
    }

    public T run(final Object... args) {
        final Map<String, Object> resultMap = new HashMap<>();
        final T result = WizardResultFactory.of(this.resultInterface, resultMap);
        
        final T controller = getControllerInstance(result, args);
        
        for (Method method = getStartMethod(); method != null; method = getNextMethod(method)) {
            resultMap.put(method.getName(), invokeMethod(method, controller));
        }
        
        return result;
    }
    
    private T getControllerInstance(final T result, final Object[] args) {
        final List<Object> argList = new ArrayList<>(args.length + 1);
        
        argList.add(result);
        Collections.addAll(argList, args);

        final List<Class<?>> classList = new ArrayList<>(argList.size());
        
        classList.add(resultInterface);
        
        for (Object arg : args) {
            classList.add(arg.getClass());
        }

        try {
            return this.controllerClass.getConstructor(classList.toArray(new Class<?>[classList.size()]))
                    .newInstance(argList.toArray());
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new InvalidWizardController("Could not instantiate", ex);
        }
    }
    
    private Method getStartMethod() {
        for (final Method method : this.controllerClass.getMethods()) {
            final Start[] annotations = method.getAnnotationsByType(Start.class);
            
            if (annotations.length > 0) {
                return method;
            }
        }

        throw new InvalidWizardController("Missing start method");
    }
    
    private Method getNextMethod(final Method currentMethod) {
        final Next[] annotations = currentMethod.getAnnotationsByType(Next.class);
        
        if (annotations.length == 0) {
            return null;
        }
        
        final String nextName = annotations[0].value();
        final Method nextMethod;
        
        try {
            nextMethod = this.controllerClass.getMethod(nextName);
        }
        catch (NoSuchMethodException | SecurityException ex) {
            throw new InvalidWizardController("Could not find next method", ex);
        }
        
        return nextMethod;
    }
    
    private Object invokeMethod(final Method method, final T controller) {
        try {
            return method.invoke(controller);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InvalidWizardController("Could not invoke method", ex);
        }
    }

}
