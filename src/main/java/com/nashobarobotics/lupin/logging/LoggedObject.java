package com.nashobarobotics.lupin.logging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoggedObject {
    Set<LoggedField> fields;
    Set<LoggedMethod> methods;
    Set<LoggedObject> subObjects;

    Object o;

    public LoggedObject(Path p, Object o) {
        fields = getAllLoggableFields(o.getClass());
        methods = getAllLoggableMethods(o.getClass());
        this.o = o;
    }

    public void register() throws IllegalAccessException {
        for(LoggedField f: fields) {
            Object res = f.field.get(o);
            if(res instanceof Double) {
                LogManager.registerDouble(p);
            }
        }
    }

    private static class LoggedField {
        public String name;
        public Field field;
        public LoggedField(String name, Path p, Field field) {
            this.name = name;
            this.path = p.append(name);
            this.field = field;
        }
    }

    private static class LoggedMethod {
        public String name;
        public Method method;
        public LoggedMethod(String name, Method method) {
            this.name = name;
            this.method = method;
        }
    }

    private static <T> Set<LoggedField> getAllLoggableFields(Class<? super T> cls) {
        Set<LoggedField> fields = new HashSet<>();
        while(true) {
            for(Field f: cls.getDeclaredFields()) {
                f.setAccessible(true);
                if(f.isAnnotationPresent(Logged.class)) {
                    fields.add(new LoggedField(f.getName(), f));
                }
            }
            cls = cls.getSuperclass();
            if(cls == null) { break; }
        }
        return fields;
    }
    
    private static <T> Set<LoggedMethod> getAllLoggableMethods(Class<? super T> cls) {
        Set<LoggedMethod> methods = new HashSet<>();
        while(true) {
            for(Method m: cls.getDeclaredMethods()) {
                m.setAccessible(true);
                if(m.isAnnotationPresent(Logged.class)) {
                    methods.add(new LoggedMethod(m.getName(), m));
                }
            }
            cls = cls.getSuperclass();
            if(cls == null) { break; }
        }
        return methods;
    }
}
