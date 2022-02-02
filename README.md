# Java Data Validator
### Usage

#### Create your validator
```java
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
```

#### Validate
```java
var person = new Person(null, "LN", -2);
var validator = new PersonValidator(person);
var errors = validator.getErrors();
```