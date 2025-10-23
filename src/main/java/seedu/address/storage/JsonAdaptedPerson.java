package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attendance.AttendanceRecord;
import seedu.address.model.grade.Grade;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String studentId;
    private final List<String> moduleCodes;
    private final List<JsonAdaptedTag> tags;
    private final List<JsonAdaptedAttendance> attendances;
    private final List<JsonAdaptedGrade> grades;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("studentId") String studentId, @JsonProperty("moduleCodes") List<String> moduleCodes,
            @JsonProperty("tags") List<JsonAdaptedTag> tags, @JsonProperty("grades") List<JsonAdaptedGrade> grades,
            @JsonProperty("attendances") List<JsonAdaptedAttendance> attendances) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.studentId = studentId;
        this.moduleCodes = moduleCodes != null ? new ArrayList<>(moduleCodes) : null;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.attendances = attendances != null ? new ArrayList<>(attendances) : new ArrayList<>();
        this.grades = grades != null ? new ArrayList<>(grades) : new ArrayList<>();
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone() != null ? source.getPhone().value : null;
        email = source.getEmail().value;
        address = source.getAddress() != null ? source.getAddress().value : null;
        studentId = source.getStudentId() != null ? source.getStudentId().value : null;
        moduleCodes = source.getModuleCodes().stream()
                .map(mc -> mc.value)
                .collect(Collectors.toList());
        tags = source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList());
        attendances = source.getAttendanceRecord().getAllAttendances().entrySet().stream()
                .map(entry -> new JsonAdaptedAttendance(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        grades = source.getGrades().stream()
                .map(JsonAdaptedGrade::new)
                .collect(Collectors.toList());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final List<Grade> personGrades = new ArrayList<>();
        for (JsonAdaptedGrade grade : grades) {
            personGrades.add(grade.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<Grade> modelGrades = new HashSet<>(personGrades);

        // Check if this is a student (has studentId and moduleCodes but NO phone/address)
        if (studentId != null && moduleCodes != null && !moduleCodes.isEmpty()
                && phone == null && address == null) {
            // Validate student ID
            if (!StudentId.isValidStudentId(studentId)) {
                throw new IllegalValueException(StudentId.MESSAGE_CONSTRAINTS);
            }
            final StudentId modelStudentId = new StudentId(studentId);

            // Parse module codes
            final Set<ModuleCode> modelModuleCodes = new HashSet<>();
            for (String mcString : moduleCodes) {
                if (!ModuleCode.isValidModuleCode(mcString)) {
                    throw new IllegalValueException(ModuleCode.MESSAGE_CONSTRAINTS);
                }
                modelModuleCodes.add(new ModuleCode(mcString));
            }

            // Parse attendance data
            AttendanceRecord modelAttendanceRecord = new AttendanceRecord();
            for (JsonAdaptedAttendance attendance : attendances) {
                modelAttendanceRecord = modelAttendanceRecord.markAttendance(
                    attendance.toModelType().getWeek(),
                    attendance.toModelType().getStatus()
                );
            }

            // Use student constructor (without phone/address)
            return new Person(modelName, modelStudentId, modelEmail, modelModuleCodes,
                    modelTags, modelGrades, modelAttendanceRecord);
        }

        // Otherwise, create a regular person (with phone and address)
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        // Parse module codes if present
        final Set<ModuleCode> modelModuleCodes = new HashSet<>();
        if (moduleCodes != null) {
            for (String mcString : moduleCodes) {
                if (!ModuleCode.isValidModuleCode(mcString)) {
                    throw new IllegalValueException(ModuleCode.MESSAGE_CONSTRAINTS);
                }
                modelModuleCodes.add(new ModuleCode(mcString));
            }
        }

        // Parse student ID if present
        final StudentId modelStudentId;
        if (studentId != null) {
            if (!StudentId.isValidStudentId(studentId)) {
                throw new IllegalValueException(StudentId.MESSAGE_CONSTRAINTS);
            }
            modelStudentId = new StudentId(studentId);
        } else {
            modelStudentId = null;
        }

        // Parse attendance data for regular person
        AttendanceRecord modelAttendanceRecord = new AttendanceRecord();
        for (JsonAdaptedAttendance attendance : attendances) {
            modelAttendanceRecord = modelAttendanceRecord.markAttendance(
                attendance.toModelType().getWeek(),
                attendance.toModelType().getStatus()
            );
        }

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags, modelStudentId,
                modelModuleCodes, modelGrades, modelAttendanceRecord);
    }

}
