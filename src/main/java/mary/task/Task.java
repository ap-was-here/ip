package mary.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Base class for all tasks.
 */
public class Task {
    // Strict resolution rejects impossible dates instead of adjusting their day.
    protected static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("d/M/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    protected static final DateTimeFormatter DISPLAY_DATE_TIME = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm");
    protected String description;
    protected boolean isDone;

    /**
     * Creates an unfinished task with the given description.
     *
     * @param description task text to retain.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as unfinished.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Sets the completion state when loading a saved task.
     *
     * @param isDone true for completed, false for unfinished.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Serializes this task as a todo record; dated subtypes override this format.
     * Fields are separated by {@code  | }; descriptions are not escaped.
     *
     * @return type, completion flag (1 or 0), and description.
     */
    public String toStorageRecord() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Parses a date/time strictly, rejecting impossible dates and times.
     *
     * @param value date/time in {@code d/M/uuuu HHmm} format using a 24-hour clock.
     * @return the parsed local date/time, without a time zone.
     * @throws java.time.format.DateTimeParseException if the format or value is invalid.
     */
    public static LocalDateTime parseDateTime(String value) {
        return LocalDateTime.parse(value, INPUT_DATE_TIME);
    }

    /**
     * Parses a date strictly for date-based task searches.
     *
     * @param value date in {@code d/M/uuuu} format.
     * @return the parsed local date.
     * @throws java.time.format.DateTimeParseException if the format or date is invalid.
     */
    public static LocalDate parseDate(String value) {
        return LocalDate.parse(value, INPUT_DATE);
    }

    /**
     * Formats a date/time with an abbreviated month name in the default format locale.
     *
     * @param value local date/time to display.
     * @return text in {@code d MMM uuuu HH:mm} format.
     */
    public static String formatDateTime(LocalDateTime value) {
        return value.format(DISPLAY_DATE_TIME);
    }

    /**
     * Returns the symbol used to display this task's status.
     *
     * @return {@code X} for a completed task, otherwise a space.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task with its completion status.
     *
     * @return bracketed status followed by the description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
