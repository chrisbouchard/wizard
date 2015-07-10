package com.automate.wizard;

public class MissingValueException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    private final String propName;

    public MissingValueException(String propName) {
        super(String.format("No value for property: %s", propName));
        this.propName = propName;
    }
    
    public String getPropName() {
        return propName;
    }

}
