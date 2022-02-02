import models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import validation.PersonValidator;

public class PersonValidatorTest {

    @Test
    public void TestInvalid() {
        var person = new Person(null, "LN", -2);
        var validator = new PersonValidator(person);
        Assertions.assertFalse(validator.isValid());
        Assertions.assertEquals(2, validator.getErrors().size());
        Assertions.assertTrue(validator.getErrors().stream().anyMatch(error -> error.equals("Field \"firstName\" of type \"String\" is required")));
        Assertions.assertTrue(validator.getErrors().stream().anyMatch(error -> error.equals("Age must be a positive number")));
    }

    @Test
    public void TestValid() {
        var person = new Person("FN", "LN", 18);
        var validator = new PersonValidator(person);
        Assertions.assertTrue(validator.isValid());
        Assertions.assertEquals(0, validator.getErrors().size());
    }

    @Test
    public void TextException() {
        var person = new Person("FN", "LN", -2);
        var validator = new PersonValidator(person);
        Assertions.assertThrows(RuntimeException.class, validator::throwExceptionIfInvalid, "Age must be a positive number");
    }
}
