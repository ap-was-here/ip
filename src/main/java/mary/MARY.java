package mary;

import java.time.format.DateTimeParseException;

import mary.command.Command;
import mary.exception.MaryException;
import mary.parser.Parser;
import mary.storage.Storage;
import mary.task.Deadline;
import mary.task.Event;
import mary.task.Task;
import mary.task.TaskList;
import mary.task.Todo;
import mary.ui.Ui;

/** Starts MARY and coordinates its console command loop. */
public class MARY {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("mary-data.txt");
        TaskList tasks;
        String loadError = null;
        try {
            tasks = new TaskList(storage.load());
        } catch (MaryException exception) {
            tasks = new TaskList();
            loadError = "the saved task data is corrupted: " + exception.getMessage();
        }
        ui.showLine();
        if (loadError != null) {
            ui.showLoadingError(loadError);
        }
        ui.showWelcome();

        String command;
        while ((command = ui.readCommand()) != null) {

            ui.showLine();
            Command parsedCommand = Parser.parse(command);
            if (parsedCommand != null && parsedCommand.isExit()) {
                parsedCommand.execute(tasks, ui, storage);
                break;
            }
            if (parsedCommand != null) {
                parsedCommand.execute(tasks, ui, storage);
                ui.showLine();
                continue;
            }

            try {
                if (command.isBlank()) {
                    throw new MaryException("please enter a command or task.");
                }

                if (command.equals("list")) {
                if (tasks.isEmpty()) {
                    System.out.println(" MARY has no saved tasks yet.");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                }
                } else {
                    throw new MaryException("I don't recognize that command; use todo, deadline, event, on, list, mark, unmark, or bye.");
                }
            } catch (MaryException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showLine();
        }
    }

    /**
     * Converts a user's task command into a Task object without using inheritance.
     *
     * @param command the complete command entered by the user
     * @return the task represented by the command
     */
    private static Task createTask(String command) throws MaryException {
        if (command.startsWith("todo ")) {
            String description = command.substring(5).trim();
            if (description.isEmpty()) {
                throw new MaryException("please add a task description after 'todo'.");
            }
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


//to rectify branching error
