package mary.parser;

import java.time.format.DateTimeParseException;

import mary.command.AddCommand;
import mary.command.Command;
import mary.command.DeleteCommand;
import mary.command.ExitCommand;
import mary.command.ListCommand;
import mary.command.MarkCommand;
import mary.command.OnCommand;
import mary.command.UnknownCommand;
import mary.exception.MaryException;
import mary.task.Deadline;
import mary.task.Event;
import mary.task.Task;
import mary.task.Todo;

/** Interprets task-creation commands and validates their arguments. */
public class Parser {
    /** Creates a command object for the exit command, if applicable. */
    public static Command parse(String command) {
        if (command.equals("bye")) return new ExitCommand();
        if (command.equals("list")) return new ListCommand();
        if (command.startsWith("delete")) return new DeleteCommand(command);
        if (command.startsWith("mark") || command.startsWith("unmark")) return new MarkCommand(command);
        if (command.startsWith("on")) return new OnCommand(command);
        if (command.startsWith("todo") || command.startsWith("deadline") || command.startsWith("event")) {
            return new AddCommand(command);
        }
        return new UnknownCommand(command);
    }

    /** Converts a todo, deadline, or event command into a task. */
    public static Task parseTask(String command) throws MaryException {
        if (command.startsWith("todo ")) {
            String description = command.substring(5).trim();
            if (description.isEmpty()) throw new MaryException("please add a task description after 'todo'.");
            return new Todo(description);
        }
        if (command.startsWith("deadline ")) {
            String content = command.substring(9).trim();
            int marker = content.indexOf(" /by ");
            if (marker < 0 || content.substring(0, marker).trim().isEmpty()
                    || content.substring(marker + 5).trim().isEmpty()) {
                throw new MaryException("use 'deadline description /by date or time'.");
            }
            try {
                return new Deadline(content.substring(0, marker).trim(),
                        Task.parseDateTime(content.substring(marker + 5).trim()));
            } catch (DateTimeParseException exception) {
                throw new MaryException("use date/time format d/M/yyyy HHmm, for example 2/12/2019 1800.");
            }
        }
        if (command.startsWith("event ")) {
            String content = command.substring(6).trim();
            int fromMarker = content.indexOf(" /from ");
            int toMarker = content.indexOf(" /to ");
            if (fromMarker < 0 || toMarker <= fromMarker) {
                throw new MaryException("use 'event description /from start /to end'.");
            }
            String description = content.substring(0, fromMarker).trim();
            String from = content.substring(fromMarker + 7, toMarker).trim();
            String to = content.substring(toMarker + 5).trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new MaryException("event description, start time, and end time cannot be empty.");
            }
            try {
                return new Event(description, Task.parseDateTime(from), Task.parseDateTime(to));
            } catch (DateTimeParseException exception) {
                throw new MaryException("use event date/time format d/M/yyyy HHmm for both /from and /to.");
            }
        }
        throw new MaryException("use 'todo description' to add a task without a date.");
    }
}
