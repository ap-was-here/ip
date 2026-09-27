package mary.command;

import mary.exception.MaryException;
import mary.storage.Storage;
import mary.task.Task;
import mary.task.TaskList;
import mary.ui.Ui;

/** Removes a task by its one-based list number. */
public class DeleteCommand extends Command {
    private final String fullCommand;

    /**
     * Creates a command retaining the supplied input for execution-time validation.
     *
     * @param fullCommand complete, non-null user input
     */
    public DeleteCommand(String fullCommand) { this.fullCommand = fullCommand; }

    /**
     * Removes the selected one-based task number, saves the list, and displays confirmation.
     * Invalid numbers and storage errors are displayed; a save failure does not restore the removed task.
     *
     * @param tasks current in-memory task list
     * @param ui user-facing message handler
     * @param storage persistence service (unused by read-only commands)
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String numberText = fullCommand.startsWith("delete ")
                ? fullCommand.substring(7).trim() : "";
        try {
            if (numberText.isEmpty()) throw new MaryException("use 'delete N', where N is a task number.");
            int index = Integer.parseInt(numberText) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new MaryException("task " + numberText
                        + " does not exist; use 'list' to see valid task numbers.");
            }
            Task removed = tasks.remove(index);
            storage.save(tasks.getTasks());
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + removed);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException exception) {
            ui.showError("'" + numberText + "' is not a valid task number; use a positive whole number.");
        } catch (MaryException exception) {
            ui.showError(exception.getMessage());
        }
    }
}
