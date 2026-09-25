package mary.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import mary.exception.MaryException;
import mary.storage.Storage;
import mary.task.Deadline;
import mary.task.Event;
import mary.task.Task;
import mary.task.TaskList;
import mary.ui.Ui;

/** Displays deadlines and events occurring on a date. */
public class OnCommand extends Command {
    private final String fullCommand;

    public OnCommand(String fullCommand) { this.fullCommand = fullCommand; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            if (!fullCommand.startsWith("on ") || fullCommand.substring(3).trim().isEmpty()) {
                throw new MaryException("use 'on d/M/yyyy', for example 'on 2/12/2019'.");
            }
            LocalDate date;
            try {
                date = Task.parseDate(fullCommand.substring(3).trim());
            } catch (DateTimeParseException exception) {
                throw new MaryException("use date format d/M/yyyy, for example 2/12/2019.");
            }
            boolean found = false;
            for (Task task : tasks.getTasks()) {
                boolean occurs = task instanceof Deadline && ((Deadline) task).getBy().toLocalDate().equals(date)
                        || task instanceof Event && !((Event) task).getFrom().toLocalDate().isAfter(date)
                        && !((Event) task).getTo().toLocalDate().isBefore(date);
                if (occurs) {
                    if (!found) System.out.println(" Tasks occurring on " + date + ":");
                    found = true;
                    System.out.println(" " + task);
                }
            }
            if (!found) System.out.println(" No deadlines or events occur on " + date + ".");
        } catch (MaryException exception) {
            ui.showError(exception.getMessage());
        }
    }
}
