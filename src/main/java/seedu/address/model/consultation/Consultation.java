package seedu.address.model.consultation;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Consultation's date and time in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Consultation {
    private final LocalDateTime dateTime;

    public Consultation(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return dateTime.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!((o instanceof Consultation))) {
            return false;
        }

        Consultation that = (Consultation) o;
        return dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime);
    }
}
