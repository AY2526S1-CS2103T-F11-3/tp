package seedu.address.ui;

import java.util.Comparator;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.attendance.AttendanceStatus;
import seedu.address.model.attendance.Week;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label studentId;
    @FXML
    private Label moduleCode;
    @FXML
    private Label attendance;
    @FXML
    private FlowPane grades;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        studentId.setText(person.getStudentId() != null ? person.getStudentId().value : "N/A");
        if (!person.getModuleCodes().isEmpty()) {
            String moduleCodesText = person.getModuleCodes().stream()
                    .map(mc -> mc.value)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("N/A");
            moduleCode.setText(moduleCodesText);
        } else {
            moduleCode.setText("N/A");
        }
        email.setText(person.getEmail().value);
        attendance.setText(formatAttendanceSummary(person.getAttendanceRecord()));
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        person.getGrades().stream()
                .sorted(Comparator.comparing(grade -> grade.assignmentName))
                .forEach(grade -> {
                    Label gradeLabel = new Label(grade.toString());
                    gradeLabel.getStyleClass().add("grade-label");
                    grades.getChildren().add(gradeLabel);
                });
    }

    /**
     * Formats the attendance record as a short summary string.
     * Example: "W1:✓ W2:✗ W3:✓"
     */
    private String formatAttendanceSummary(seedu.address.model.attendance.AttendanceRecord attendanceRecord) {
        if (attendanceRecord.isEmpty()) {
            return "No attendance recorded";
        }

        StringBuilder sb = new StringBuilder();
        Map<Week, AttendanceStatus> attendances = attendanceRecord.getAllAttendances();

        // Sort by week number and format as W1:✓ W2:✗ etc.
        attendances.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(week -> week.value)))
                .forEach(entry -> {
                    Week week = entry.getKey();
                    AttendanceStatus status = entry.getValue();
                    String symbol = status == AttendanceStatus.PRESENT ? "✓" : "✗";
                    sb.append("W").append(week.value).append(":").append(symbol).append(" ");
                });

        return sb.toString().trim();
    }
}
