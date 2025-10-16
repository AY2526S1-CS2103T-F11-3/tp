package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.module.ModuleCode;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_STUDENT_ID = "A0000000X";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private StudentId studentId;
    private Set<ModuleCode> moduleCodes;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        studentId = null;
        moduleCodes = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
        studentId = personToCopy.getStudentId();
        moduleCodes = new HashSet<>(personToCopy.getModuleCodes());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code StudentId} of the {@code Person} that we are building.
     */
    public PersonBuilder withStudentId(String studentId) {
        this.studentId = new StudentId(studentId);
        return this;
    }

    /**
     * Sets the {@code StudentId} of the {@code Person} that we are building to null.
     */
    public PersonBuilder withStudentId() {
        this.studentId = null;
        return this;
    }

    /**
     * Sets the {@code ModuleCodes} of the {@code Person} that we are building.
     */
    public PersonBuilder withModuleCodes(String... moduleCodes) {
        this.moduleCodes = SampleDataUtil.getModuleCodeSet(moduleCodes);
        return this;
    }

    /**
     * Sets the {@code ModuleCode} of the {@code Person} that we are building.
     * For backward compatibility with single module code tests.
     */
    public PersonBuilder withModuleCode(String moduleCode) {
        this.moduleCodes = SampleDataUtil.getModuleCodeSet(moduleCode);
        return this;
    }

    /**
     * Sets the {@code ModuleCodes} of the {@code Person} that we are building to empty.
     * For backward compatibility with tests that set moduleCode to null.
     */
    public PersonBuilder withModuleCode() {
        this.moduleCodes = new HashSet<>();
        return this;
    }

    /**
     * Builds a {@code Person} from the current builder state.
     * If both student-related fields are unset, the constructor without those fields is used.
     */
    public Person build() {
        if (studentId == null && moduleCodes.isEmpty()) {
            return new Person(name, phone, email, address, tags);
        }
        return new Person(name, phone, email, address, tags, studentId, moduleCodes);
    }

}
