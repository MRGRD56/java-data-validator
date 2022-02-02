package validation;

import com.mrgrd56.javadatavalidator.DataValidator;
import models.Person;

import java.util.List;

public final class PersonValidator extends DataValidator<Person> {
    public PersonValidator(Person object) {
        super(object);
    }

    @Override
    protected List<String> validate(Person object) {
        return validationBuilder()
                .addRequiredField("firstName")
                .addRequiredField("lastName")
                .addRule(object.age() >= 0, "Age must be a positive number")
                .build();
    }
}
