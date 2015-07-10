package com.automate.wizard;

public class InvalidWizardController extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public InvalidWizardController(final String message) {
        super(message);
    }
    
    public InvalidWizardController(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
