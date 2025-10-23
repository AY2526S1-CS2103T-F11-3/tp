package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.attendance.AttendanceRecord;
import seedu.address.model.consultation.Consultation;
import seedu.address.model.grade.Grade;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final StudentId studentId;
    private final Set<ModuleCode> moduleCodes = new HashSet<>();
    // Data fields
    private final Address address;
    private final List<Consultation> consultations;
    private final Set<Tag> tags = new HashSet<>();
    private final AttendanceRecord attendanceRecord;
    private final Set<Grade> grades = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  StudentId studentId, Set<ModuleCode> moduleCodes, Set<Grade> grades) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.studentId = studentId;
        if (moduleCodes != null) {
            this.moduleCodes.addAll(moduleCodes);
        }
        this.attendanceRecord = new AttendanceRecord();
        this.consultations = null;
        if (grades != null) {
            this.grades.addAll(grades);
        }
    }

    /**
     * Every field must be present and not null, with custom AttendanceRecord.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  StudentId studentId, Set<ModuleCode> moduleCodes, Set<Grade> grades,
                  AttendanceRecord attendanceRecord) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.studentId = studentId;
        if (moduleCodes != null) {
            this.moduleCodes.addAll(moduleCodes);
        }
        this.attendanceRecord = attendanceRecord != null ? attendanceRecord : new AttendanceRecord();
        this.consultations = null;
        if (grades != null) {
            this.grades.addAll(grades);
        }
    }

    /**
     * Alternate constructor for non students
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.studentId = null;
        this.attendanceRecord = new AttendanceRecord();
        this.consultations = null;
    }

    /**
     * Constructor for Student (with StudentId and ModuleCodes, no phone/address/consultations)
     */
    public Person(Name name, StudentId studentId, Email email,
                  Set<ModuleCode> moduleCodes, Set<Tag> tags, Set<Grade> grades) {
        requireAllNonNull(name, studentId, email, moduleCodes, tags, grades);
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.phone = null; // Not used for students
        this.address = null; // Not used for students
        this.moduleCodes.addAll(moduleCodes);
        this.tags.addAll(tags);
        this.grades.addAll(grades);
        this.attendanceRecord = new AttendanceRecord();
        this.consultations = new ArrayList<>();
    }

    /**
     * Constructor for Student with custom AttendanceRecord
     */
    public Person(Name name, StudentId studentId, Email email,
                  Set<ModuleCode> moduleCodes, Set<Tag> tags, Set<Grade> grades,
                  AttendanceRecord attendanceRecord) {
        requireAllNonNull(name, studentId, email, moduleCodes, tags, grades);
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.phone = null; // Not used for students
        this.address = null; // Not used for students
        this.moduleCodes.addAll(moduleCodes);
        this.tags.addAll(tags);
        this.grades.addAll(grades);
        this.attendanceRecord = attendanceRecord != null ? attendanceRecord : new AttendanceRecord();
        this.consultations = new ArrayList<>();
    }

    /**
     * Constructor for Student (with StudentId and ModuleCodes and Consultations, no phone/address)
     */
    public Person(Name name, StudentId studentId, Email email,
                  Set<ModuleCode> moduleCodes, Set<Tag> tags, Set<Grade> grades,
                  List<Consultation> consultations) {
        requireAllNonNull(name, studentId, email, moduleCodes, tags, grades, consultations);
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.phone = null; // Not used for students
        this.address = null; // Not used for students
        this.moduleCodes.addAll(moduleCodes);
        this.consultations = new ArrayList<>(consultations);
        this.tags.addAll(tags);
        this.grades.addAll(grades);
        this.attendanceRecord = new AttendanceRecord();
    }

    /**
     * Constructor for Student (with StudentId, ModuleCodes, Consultations, and custom AttendanceRecord)
     */
    public Person(Name name, StudentId studentId, Email email,
                  Set<ModuleCode> moduleCodes, Set<Tag> tags, Set<Grade> grades,
                  List<Consultation> consultations, AttendanceRecord attendanceRecord) {
        requireAllNonNull(name, studentId, email, moduleCodes, tags, grades, consultations);
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.phone = null; // Not used for students
        this.address = null; // Not used for students
        this.moduleCodes.addAll(moduleCodes);
        this.consultations = new ArrayList<>(consultations);
        this.tags.addAll(tags);
        this.grades.addAll(grades);
        this.attendanceRecord = attendanceRecord != null ? attendanceRecord : new AttendanceRecord();
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    /**
     * Returns an immutable module code set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<ModuleCode> getModuleCodes() {
        return Collections.unmodifiableSet(moduleCodes);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns the attendance record for this person.
     */
    public AttendanceRecord getAttendanceRecord() {
        return attendanceRecord;
    }

    /**
     * Returns an immutable consultation list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Consultation> getConsultations() {
        return consultations == null ? null : Collections.unmodifiableList(consultations);
    }

    /**
     * Returns an immutable grade set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Grade> getGrades() {
        return Collections.unmodifiableSet(grades);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && email.equals(otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && tags.equals(otherPerson.tags)
                && Objects.equals(studentId, otherPerson.studentId)
                && moduleCodes.equals(otherPerson.moduleCodes)
                && attendanceRecord.equals(otherPerson.attendanceRecord)
                && grades.equals(otherPerson.grades);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, studentId, moduleCodes, attendanceRecord, grades);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("studentId", studentId)
                .add("moduleCodes", moduleCodes)
                .add("attendanceRecord", attendanceRecord)
                .add("grades", grades)
                .toString();
    }

}
