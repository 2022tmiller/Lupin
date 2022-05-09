package com.nashobarobotics.lupin.logging;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.nashobarobotics.lupin.Logger;

public class LoggedObject {
    Set<LoggedField> fields;
    Set<LoggedMethod> methods;
    Set<LoggedObject> subObjects;

    Object o;

    public LoggedObject(Path p, Object o) {
        Logger.info("before fields");
        Set<Field> annotatedFields = getAllLoggableFields(o.getClass());
        Logger.info("before methods");
        Set<Method> annotatedMethods = getAllLoggableMethods(o.getClass());
        Logger.info("before field stream");
        fields = annotatedFields.stream().map(f -> {
            Logged annotation = f.getAnnotation(Logged.class);
            if(annotation.name().length() > 0) {
                return new LoggedField(annotation.name(), p, f);
            } else {
                return new LoggedField(f.getName(), p, f);
            }
        }).collect(Collectors.toSet());
        Logger.info("before method stream");
        methods = annotatedMethods.stream().map(m -> {
            Logger.info("Method: " + m.getName());
            Logged annotation = m.getAnnotation(Logged.class);
            if(annotation.name().length() > 0) {
                return new LoggedMethod(annotation.name(), p, m);
            } else {
                return new LoggedMethod(m.getName(), p, m);
            }
        }).collect(Collectors.toSet());
        this.o = o;
    }

    public void setObject(Object o) {
        this.o = o;
    }

    public void register() {
        for(LoggedField f: fields) {
            f.register();
        }
        for(LoggedMethod m: methods) {
            m.register();
        }
    }

    public void log() {
        for(LoggedField f: fields) {
            f.log();
        }
        for(LoggedMethod m: methods) {
            m.log();
        }
    }

    private class LoggedElement {
        public Path path;
        public DataType type;
        public LoggedObject innerLo;

        public LoggedElement(String name, Path p, Class<?> cls) {
            this.path = p.append(name);
            if(cls.equals(Double.class) || cls.equals(double.class)) {
                type = DataType.DOUBLE;
            } else {
                type = DataType.OBJECT;
                innerLo = new LoggedObject(path, null);
            }
        }

        public void register() {
            if(type == DataType.OBJECT) {
                innerLo.register();
            } else {
                LogManager.register(path, type);
            }
        }

        public void log(Object result) {
            switch(this.type) {
                case DOUBLE:
                    LogManager.logDouble(path, (Double)result);
                    break;
                case OBJECT:
                    innerLo.setObject(result);
                    innerLo.log();
                    break;
            }   
        }
    }

    private class LoggedField extends LoggedElement {
        public Field field;

        public LoggedField(String name, Path p, Field field) {
            super(name, p, field.getType());
            this.field = field;
        }

        public void log() {
            try {
                Object result = field.get(o);
                super.log(result);
            } catch(IllegalAccessException e) {
                Logger.error("Error while logging: " + e.getMessage(), e);
            }
        }
    }

    private class LoggedMethod extends LoggedElement {
        public Method method;

        public LoggedMethod(String name, Path p, Method method) {
            super(name, p, method.getReturnType());
            this.method = method;
        }

        public void log() {
            try {
                Object result = method.invoke(o);
            } catch(IllegalAccessException  | InvocationTargetException e) {
                Logger.error("Error while logging: " + e.getMessage(), e);
            }
        }
    }

    private static <T> Set<Field> getAllLoggableFields(Class<? super T> cls) {
        Set<Field> fields = new HashSet<>();
        while(true) {
            for(Field f: cls.getDeclaredFields()) {
                Logger.info("Field: " + f.getName());
                f.setAccessible(true);
                Logger.info("annotation?");
                if(f.isAnnotationPresent(Logged.class)) {
                    fields.add(f);
                }
            }
            cls = cls.getSuperclass();
            if(cls == null) { break; }
        }
        return fields;
    }
    
    private static <T> Set<Method> getAllLoggableMethods(Class<? super T> cls) {
        Set<Method> methods = new HashSet<>();
        while(true) {
            for(Method m: cls.getDeclaredMethods()) {
                Logger.info("Method: " + m.getName());
                m.setAccessible(true);
                Logger.info("annotation?");
                if(m.isAnnotationPresent(Logged.class)) {
                    methods.add(m);
                }
            }
            cls = cls.getSuperclass();
            if(cls == null) { break; }
        }
        return methods;
    }
}
