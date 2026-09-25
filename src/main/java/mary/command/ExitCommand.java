package mary.command;

import mary.storage.Storage;
import mary.task.TaskList;
import mary.ui.Ui;

/** Command that ends the chatbot session. */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() { return true; }
}
