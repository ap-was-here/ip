import java.util.ArrayList;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
                } else if (command.startsWith("on")) {
                    showTasksOnDate(command, tasks);
                } else if (command.startsWith("delete") ) {
                    String numberText = command.startsWith("delete ")
                            ? command.substring(7).trim() : "";
                    if (numberText.isEmpty()) {
                        throw new MaryException("use 'delete N', where N is a task number.");
                    }
                    try {
                        int taskNumber = Integer.parseInt(numberText);
                        int taskIndex = taskNumber - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            throw new MaryException("task " + taskNumber
                                    + " does not exist; use 'list' to see valid task numbers.");
                        }
                        Task removedTask = tasks.remove(taskIndex);
                        storage.save(tasks.getTasks());
                        System.out.println(" Noted. I've removed this task:");
                        System.out.println("   " + removedTask);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    } catch (NumberFormatException exception) {
                        throw new MaryException("'" + numberText
                                + "' is not a valid task number; use a positive whole number.");
                    }
            } else if (command.startsWith("mark") || command.startsWith("unmark")) {
                boolean markDone = command.startsWith("mark ");
                int prefixLength = markDone ? 5 : 7;
                if (!command.startsWith(markDone ? "mark " : "unmark ")
                        || command.substring(prefixLength).trim().isEmpty()) {
                    throw new MaryException("use 'mark N' or 'unmark N', where N is a task number.");
                }
                String numberText = command.substring(prefixLength).trim();

                try {
                    int taskNumber = Integer.parseInt(numberText);
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new MaryException("task " + taskNumber
                                + " does not exist; use 'list' to see valid task numbers.");
                    } else {
                        if (markDone) {
                            tasks.get(taskIndex).markAsDone();
                        } else {
                            tasks.get(taskIndex).markAsNotDone();
                        }
                        storage.save(tasks.getTasks());
                        if (markDone) {
                            System.out.println(" Nice! I've marked this task as done:");
                        } else {
                            System.out.println(" OK, I've marked this task as not done yet:");
                        }
                        System.out.println("   " + tasks.get(taskIndex));
                    }
                } catch (NumberFormatException exception) {
                    throw new MaryException("'" + numberText
                            + "' is not a valid task number; use a positive whole number.");
                }
                } else if (command.startsWith("todo") || command.startsWith("deadline")
                        || command.startsWith("event")) {
                    Task newTask = Parser.parseTask(command);
                    tasks.add(newTask);
                    storage.save(tasks.getTasks());
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
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

    /** Displays deadlines and events that occur on the requested date. */
    private static void showTasksOnDate(String command, TaskList tasks) throws MaryException {
        if (!command.startsWith("on ") || command.substring(3).trim().isEmpty()) {
            throw new MaryException("use 'on d/M/yyyy', for example 'on 2/12/2019'.");
        }
        LocalDate date;
        try {
            date = Task.parseDate(command.substring(3).trim());
        } catch (DateTimeParseException exception) {
            throw new MaryException("use date format d/M/yyyy, for example 2/12/2019.");
        }
        boolean found = false;
        for (Task task : tasks.getTasks()) {
            boolean occurs = task instanceof Deadline && ((Deadline) task).getBy().toLocalDate().equals(date)
                    || task instanceof Event && (!((Event) task).getFrom().toLocalDate().isAfter(date)
                    && !((Event) task).getTo().toLocalDate().isBefore(date));
            if (occurs) {
                if (!found) {
                    System.out.println(" Tasks occurring on " + date + ":");
                }
                found = true;
                System.out.println(" " + task);
            }
        }
        if (!found) {
            System.out.println(" No deadlines or events occur on " + date + ".");
        }
    }
}


//to rectify branching error
