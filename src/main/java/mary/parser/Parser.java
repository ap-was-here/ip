package mary.parser;

import java.time.format.DateTimeParseException;

import mary.command.AddCommand;
import mary.command.Command;
import mary.command.DeleteCommand;
import mary.command.ExitCommand;
import mary.command.FindCommand;
import mary.command.ListCommand;
import mary.command.MarkCommand;
import mary.command.OnCommand;
import mary.command.SortCommand;
import mary.command.UnknownCommand;
import mary.exception.MaryException;
import mary.task.Deadline;
import mary.task.Event;
import mary.task.Task;
import mary.task.Todo;

/**
 * Interprets task-creation commands and validates their arguments.
 */
public class Parser {
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String BY_MARKER = " /by ";
    private static final String FROM_MARKER = " /from ";
    private static final String TO_MARKER = " /to ";

    /**
     * Creates the command object matching the input command.
     */
    public static Command parse(String command) {
        if (command.equals("sort")
                || (command.startsWith("sort") && command.length() > 4
                && Character.isWhitespace(command.charAt(4)))) {
            return new SortCommand(command.substring(4));
        }
        if (command.equals("find")
                || (command.startsWith("find") && command.length() > 4
                && Character.isWhitespace(command.charAt(4)))) {
            return new FindCommand(command.substring(4));
        }
        if (command.equals("bye")) {
            return new ExitCommand();
        }
        if (command.equals("list")) {
            return new ListCommand();
        }
        if (command.startsWith("delete")) {
            return new DeleteCommand(command);
        }
        if (command.startsWith("mark") || command.startsWith("unmark")) {
            return new MarkCommand(command);
        }
        if (command.startsWith("on")) {
            return new OnCommand(command);
        }
        if (command.startsWith("todo") || command.startsWith("deadline") || command.startsWith("event")) {
            return new AddCommand(command);
        }
        return new UnknownCommand(command);
    }

    /**
     * Converts a todo, deadline, or event command into a task.
     */
    public static Task parseTask(String command) throws MaryException {
        if (command.startsWith(TODO_PREFIX)) {
            return parseTodo(command.substring(TODO_PREFIX.length()).trim());
        }
        if (command.startsWith(DEADLINE_PREFIX)) {
            return parseDeadline(command.substring(DEADLINE_PREFIX.length()).trim());
        }
        if (command.startsWith(EVENT_PREFIX)) {
            return parseEvent(command.substring(EVENT_PREFIX.length()).trim());
        }
        throw new MaryException("use 'todo description' to add a task without a date.");
    }

    /**
     * Validates the trimmed todo description before creating a task.
     */
    private static Todo parseTodo(String description) throws MaryException {
        if (description.isEmpty()) {
            throw new MaryException("please add a task description after 'todo'.");
        }
        return new Todo(description);
    }

    /**
     * Separates deadline arguments and translates invalid dates into usage guidance.
     */
    private static Deadline parseDeadline(String content) throws MaryException {
        int marker = content.indexOf(BY_MARKER);
        if (marker < 0 || content.substring(0, marker).trim().isEmpty()
                || content.substring(marker + BY_MARKER.length()).trim().isEmpty()) {
            throw new MaryException("use 'deadline description /by date or time'.");
        }
        String description = content.substring(0, marker).trim();
        String by = content.substring(marker + BY_MARKER.length()).trim();
        try {
            return new Deadline(description, Task.parseDateTime(by));
        } catch (DateTimeParseException exception) {
            throw new MaryException("use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800.");
        }
    }

    /**
     * Separates event arguments, preserving marker order and date validation rules.
     */
    private static Event parseEvent(String content) throws MaryException {
        int fromMarker = content.indexOf(FROM_MARKER);
        int toMarker = content.indexOf(TO_MARKER);
        if (fromMarker < 0 || toMarker <= fromMarker) {
            throw new MaryException("use 'event description /from start /to end'.");
        }
        String description = content.substring(0, fromMarker).trim();
        String from = content.substring(fromMarker + FROM_MARKER.length(), toMarker).trim();
        String to = content.substring(toMarker + TO_MARKER.length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new MaryException("event description, start time, and end time cannot be empty.");
        }
        try {
            return new Event(description, Task.parseDateTime(from), Task.parseDateTime(to));
        } catch (DateTimeParseException exception) {
            throw new MaryException("use event date/time format d/M/yyyy HHmm for both /from and /to.");
        }
    }
}
