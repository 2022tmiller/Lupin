package com.nashobarobotics.lupin.logging;

public enum DataType {
    STRING("string"), 
    STRING_ARRAY("string[]"), 
    DOUBLE("double"), 
    DOUBLE_ARRAY("double[]"), 
    BOOLEAN("boolean"), 
    BOOLEAN_ARRAY("boolean[]"), 
    RAW("raw"),
    OBJECT(null);

    public final String type;

    private DataType(String type) {
        this.type = type;
    }
}
