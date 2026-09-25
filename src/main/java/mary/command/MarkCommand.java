package mary.command;

import mary.exception.MaryException;
import mary.storage.Storage;
import mary.task.Task;
import mary.task.TaskList;
import mary.ui.Ui;

/** Marks a task done or not done. */
public class MarkCommand extends Command {
    private final String fullCommand;

    public MarkCommand(String fullCommand) { this.fullCommand = fullCommand; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        boolean done = fullCommand.startsWith("mark ");
        String numberText = fullCommand.substring(done ? 5 : 7).trim();
        try {
            if (numberText.isEmpty()) throw new MaryException("use 'mark N' or 'unmark N', where N is a task number.");
            int index = Integer.parseInt(numberText) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new MaryException("task " + numberText
                        + " does not exist; use 'list' to see valid task numbers.");
            }
            Task task = tasks.get(index);
            if (done) task.markAsDone(); else task.markAsNotDone();
            storage.save(tasks.getTasks());
            System.out.println(done ? " Nice! I've marked this task as done:" :
                    " OK, I've marked this task as not done yet:");
            System.out.println("   " + task);
        } catch (NumberFormatException exception) {
            ui.showError("'" + numberText + "' is not a valid task number; use a positive whole number.");
        } catch (MaryException exception) {
            ui.showError(exception.getMessage());
        }
    }
}
