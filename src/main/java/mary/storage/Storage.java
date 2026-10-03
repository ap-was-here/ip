package mary.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import mary.exception.MaryException;
import mary.task.Deadline;
import mary.task.Event;
import mary.task.Task;
import mary.task.Todo;

/**
 * Loads and saves UTF-8 task records at a configured path.
 */
public class Storage {
    private final Path file;

    /**
     * Selects the data file without creating it or its parent directories.
     *
     * @param filePath file path; relative paths resolve from the working directory.
     */
    public Storage(String filePath) {
        file = Path.of(filePath);
    }

    /**
     * Loads records in file order, skipping blank lines.
     *
     * @return loaded tasks, or an empty list if the file does not exist.
     * @throws MaryException if the file cannot be read or any record is malformed.
     */
    public List<Task> load() throws MaryException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) {
            return tasks;
        }
        try {
            List<String> records = Files.readAllLines(file);
            for (int i = 0; i < records.size(); i++) {
                if (!records.get(i).isBlank()) {
                    tasks.add(parseRecord(records.get(i), i + 1));
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new MaryException("could not read " + file + ".");
        }
    }

    /**
     * Creates or overwrites the data file with the supplied tasks in list order.
     * Parent directories must already exist; an empty list clears the file.
     *
     * @param tasks tasks to serialize.
     * @throws MaryException if writing the file fails.
     */
    public void save(List<Task> tasks) throws MaryException {
        try {
            ArrayList<String> records = new ArrayList<>();
            for (Task task : tasks) {
                records.add(task.toStorageRecord());
            }
            Files.write(file, records);
        } catch (IOException exception) {
            throw new MaryException("could not save tasks to " + file + ".");
        }
    }

    /**
     * Validates a pipe-delimited record and restores its subtype and completion state.
     *
     * @param record saved record containing ISO date/times for dated tasks.
     * @param lineNumber one-based file line number for error messages.
     * @return the restored task.
     * @throws MaryException if the type, field count, status, description, or dates are invalid.
     */
    private Task parseRecord(String record, int lineNumber) throws MaryException {
        // load supplies a one-based physical line number, including skipped blank lines.
        assert lineNumber > 0 : "Saved-record line numbers must be positive";
        String[] fields = record.split(" \\| ", -1);
        validateFields(fields, lineNumber);
        try {
            Task task;
            if (fields[0].equals("T")) {
                task = new Todo(fields[2]);
            } else if (fields[0].equals("D")) {
                task = new Deadline(fields[2], LocalDateTime.parse(fields[3]));
            } else {
                // Validation above leaves only an event after the todo/deadline branches.
                assert fields[0].equals("E") : "Validated remaining record type must be E";
                task = new Event(fields[2], LocalDateTime.parse(fields[3]), LocalDateTime.parse(fields[4]));
            }
            task.setDone(fields[1].equals("1"));
            return task;
        } catch (DateTimeParseException exception) {
            throw new MaryException("invalid date/time on line " + lineNumber + ".");
        }
    }

    /**
     * Checks record shape before accessing common fields, preserving error precedence.
     */
    private void validateFields(String[] fields, int lineNumber) throws MaryException {
        int expectedFieldCount = switch (fields[0]) {
            case "T" -> 3;
            case "D" -> 4;
            case "E" -> 5;
            default -> throw new MaryException("invalid record on line " + lineNumber + ".");
        };
        if (fields.length != expectedFieldCount) {
            throw new MaryException("invalid record on line " + lineNumber + ".");
        }
        boolean isValidStatus = fields[1].equals("0") || fields[1].equals("1");
        if (!isValidStatus) {
            throw new MaryException("invalid completion status on line " + lineNumber + ".");
        }
        if (fields[2].isBlank()) {
            throw new MaryException("empty task description on line " + lineNumber + ".");
        }
    }
}
