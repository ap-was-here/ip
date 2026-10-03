package mary.command;

import mary.exception.MaryException;
import mary.storage.Storage;
import mary.task.TaskList;
import mary.ui.Ui;

/**
 * Reorders dated tasks chronologically and persists their new list numbers.
 */
public class SortCommand extends Command {
    private final String arguments;

    /**
     * Retains arguments so unsupported options can receive specific usage guidance.
     *
     * @param arguments text after the sort command word.
     */
    public SortCommand(String arguments) {
        this.arguments = arguments;
    }

    /**
     * Sorts and saves nonempty lists, then displays the new task numbers.
     * A save failure reports an error but leaves the in-memory list sorted.
     *
     * @param tasks current task list.
     * @param ui destination for confirmations and errors.
     * @param storage persistence service for the reordered list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (!arguments.isBlank()) {
            ui.showError("use 'sort' without arguments to order tasks chronologically.");
            return;
        }
        if (tasks.isEmpty()) {
            ui.showMessage(" MARY has no saved tasks yet.");
            return;
        }
        tasks.sortChronologically();
        try {
            storage.save(tasks.getTasks());
            ui.showMessage(" Sorted chronologically (deadlines by due time, events by start time; todos last).");
            new ListCommand().execute(tasks, ui, storage);
        } catch (MaryException exception) {
            ui.showError(exception.getMessage());
        }
    }
}
