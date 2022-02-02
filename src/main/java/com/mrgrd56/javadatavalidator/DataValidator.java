package com.mrgrd56.javadatavalidator;

import com.google.common.base.Strings;
import com.mrgrd56.javadatavalidator.exception.DataValidatorNotInitializedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DataValidator<T> {
    private T object;
    private List<String> errors;

    public final boolean isValid() {
        return getErrors().isEmpty();
    }

    public final List<String> getErrors() {
        if (errors == null) {
            throw new DataValidatorNotInitializedException();
        }

        return errors;
    }

    protected final Optional<String> getFirstError() {
        return getErrors().stream().findFirst();
    }

    public void throwExceptionIfInvalid() {
        Optional<String> firstError = getFirstError();
        if (firstError.isPresent()) {
            throw new RuntimeException(firstError.get());
        }
    }

    private T getObject() {
        if (object == null) {
            throw new DataValidatorNotInitializedException();
        }

        return object;
    }

    protected DataValidator() {
    }

    protected DataValidator(T object) {
        initialize(object);
    }

    protected abstract List<String> validate(T object);

    protected final void initialize(T object) {
        this.object = object;
        errors = validate(getObject());
    }

    protected final DataValidatorErrorsBuilder validationBuilder() {
        return new DataValidatorErrorsBuilder();
    }

    private final DataValidator<T> dataValidator = this;

    protected class DataValidatorErrorsBuilder {

        private final List<String> errors = new ArrayList<>();

        public final DataValidatorErrorsBuilder addRule(boolean isValid, String invalidError) {
            if (!isValid) {
                errors.add(invalidError);
            }

            return this;
        }

        public DataValidatorErrorsBuilder addRequired(Object value, String invalidError) {
            boolean isValid;
            if (value instanceof String) {
                isValid = !Strings.isNullOrEmpty((String) value);
            } else if (value instanceof Optional<?>) {
                isValid = ((Optional<?>) value).isPresent();
            } else {
                isValid = value != null;
            }

            return this.addRule(isValid, invalidError);
        }

        public DataValidatorErrorsBuilder addRequiredField(String fieldName) {
            return addRequiredField(fieldName, null);
        }

        public DataValidatorErrorsBuilder addRequiredField(String fieldName, String invalidMessage) {
            Class<?> objectClass = dataValidator.getObject().getClass();
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object fieldValue = field.get(dataValidator.getObject());

                String error;
                if (invalidMessage == null) {
                    String typeName = field.getType().getSimpleName();
                    error = String.format("Field \"%s\" of type \"%s\" is required", fieldName, typeName);
                } else {
                    error = invalidMessage;
                }
                field.setAccessible(false);

                return this.addRequired(fieldValue, error);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(
                        String.format("The validated object does not have the specified field '%s'", fieldName), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public final List<String> build() {
            return errors;
        }
    }
}
