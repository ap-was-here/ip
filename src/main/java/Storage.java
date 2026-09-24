import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Loads and saves tasks using a relative path. */
public class Storage {
    private final Path file;

    public Storage(String filePath) { file = Path.of(filePath); }

    /** Loads all tasks, rejecting malformed saved records. */
    public List<Task> load() throws MaryException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) return tasks;
        try {
            List<String> records = Files.readAllLines(file);
            for (int i = 0; i < records.size(); i++) {
                if (!records.get(i).isBlank()) tasks.add(parseRecord(records.get(i), i + 1));
            }
            return tasks;
        } catch (IOException exception) {
            throw new MaryException("could not read " + file + ".");
        }
    }

    /** Saves all tasks to disk. */
    public void save(List<Task> tasks) throws MaryException {
        try {
            ArrayList<String> records = new ArrayList<>();
            for (Task task : tasks) records.add(task.toStorageRecord());
            Files.write(file, records);
        } catch (IOException exception) {
            throw new MaryException("could not save tasks to " + file + ".");
        }
    }

    private Task parseRecord(String record, int lineNumber) throws MaryException {
        String[] fields = record.split(" \\| ", -1);
        if (fields.length < 3 || (fields[0].equals("D") && fields.length != 4)
                || (fields[0].equals("E") && fields.length != 5)
                || (!fields[0].equals("T") && !fields[0].equals("D") && !fields[0].equals("E"))) {
            throw new MaryException("invalid record on line " + lineNumber + ".");
        }
        if (!fields[1].equals("0") && !fields[1].equals("1")) {
            throw new MaryException("invalid completion status on line " + lineNumber + ".");
        }
        if (fields[2].isBlank()) throw new MaryException("empty task description on line " + lineNumber + ".");
        try {
            Task task;
            if (fields[0].equals("T")) task = new Todo(fields[2]);
            else if (fields[0].equals("D")) task = new Deadline(fields[2], LocalDateTime.parse(fields[3]));
            else task = new Event(fields[2], LocalDateTime.parse(fields[3]), LocalDateTime.parse(fields[4]));
            task.setDone(fields[1].equals("1"));
            return task;
        } catch (DateTimeParseException exception) {
            throw new MaryException("invalid date/time on line " + lineNumber + ".");
        }
    }
}
