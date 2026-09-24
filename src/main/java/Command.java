/** Base type for executable chatbot commands. */
public abstract class Command {
    /** Executes this command using the chatbot collaborators. */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /** Returns whether this command ends the chatbot session. */
    public boolean isExit() { return false; }
}
