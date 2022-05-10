package com.nashobarobotics.lupin.logging;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LoggedObject {
    Set<LoggedField> fields;
    Set<LoggedMethod> methods;
    Set<LoggedObject> subObjects;

    Object o;

    public LoggedObject(Path p, Class<?> cls) {
        Set<Field> annotatedFields = getAllLoggableFields(cls);
        Set<Method> annotatedMethods = getAllLoggableMethods(cls);

        fields = annotatedFields.stream().map(f -> {
            Logged annotation = f.getAnnotation(Logged.class);
            if(annotation.name().length() > 0) {
                return new LoggedField(annotation.name(), p, f);
            } else {
                return new LoggedField(f.getName(), p, f);
            }
        }).collect(Collectors.toSet());

        methods = annotatedMethods.stream().map(m -> {
            Logged annotation = m.getAnnotation(Logged.class);
            if(annotation.name().length() > 0) {
                return new LoggedMethod(annotation.name(), p, m);
            } else {
                return new LoggedMethod(m.getName(), p, m);
            }
        }).collect(Collectors.toSet());
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
        if(o != null) {
            for(LoggedField f: fields) {
                f.log(o);
            }
            for(LoggedMethod m: methods) {
                m.log(o);
            }
        }
    }

    private static class LoggedElement {
        public Path path;
        public DataType type;
        public LoggedObject innerLo;

        public LoggedElement(String name, Path p, Class<?> cls) {
            this.path = p.append(name);
            if(Number.class.isAssignableFrom(cls)
            || cls.equals(double.class)  || cls.equals(float.class)
            || cls.equals(int.class) || cls.equals(long.class)
            || cls.equals(short.class) || cls.equals(byte.class)
            ) {
                type = DataType.DOUBLE;
            } else if(cls.equals(String.class)) {
                type = DataType.STRING;
            } else {
                type = DataType.OBJECT;
                innerLo = new LoggedObject(path, cls);
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
                    LogManager.logDouble(path, ((Number)result).doubleValue());
                    break;
                case STRING:
                    LogManager.logString(path, (String)result);
                    break;
                case BOOLEAN:
                    LogManager.logBoolean(path, (Boolean)result);
                    break;
                case OBJECT:
                    innerLo.setObject(result);
                    innerLo.log();
                    break;
            }   
        }
    }

    private static class LoggedField extends LoggedElement {
        public Field field;

        public LoggedField(String name, Path p, Field field) {
            super(name, p, field.getType());
            this.field = field;
        }

        public void log(Object o) {
            try {
                Object result = field.get(o);
                super.log(result);
            } catch(IllegalAccessException e) {
            }
        }
    }

    private static class LoggedMethod extends LoggedElement {
        public Method method;

        public LoggedMethod(String name, Path p, Method method) {
            super(name, p, method.getReturnType());
            this.method = method;
        }

        public void log(Object o) {
            try {
                Object result = method.invoke(o);
                super.log(result);
            } catch(IllegalAccessException  | InvocationTargetException e) {
            }
        }
    }

    private static Set<Field> getAllLoggableFields(Class<?> cls) {
        Set<Field> fields = new HashSet<>();
        for(Field f: cls.getFields()) {
            if(f.isAnnotationPresent(Logged.class)) {
                fields.add(f);
            }
        }
        return fields;
    }
    
    private static Set<Method> getAllLoggableMethods(Class<?> cls) {
        Set<Method> methods = new HashSet<>();
        for(Method m: cls.getMethods()) {
            if(m.isAnnotationPresent(Logged.class)) {
                methods.add(m);
            }
        }
        return methods;
    }
}
