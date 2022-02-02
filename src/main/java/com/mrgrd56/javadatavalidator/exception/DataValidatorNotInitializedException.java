package com.mrgrd56.javadatavalidator.exception;

public class DataValidatorNotInitializedException extends RuntimeException {
    public DataValidatorNotInitializedException() {
        super("DataValidator is not initialized. Ensure calling super(T object) constructor or initialize method");
    }
}
