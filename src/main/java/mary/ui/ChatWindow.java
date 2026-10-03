package mary.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mary.Mary;

/**
 * Presents commands and replies as chat bubbles using the shared MARY session.
 */
public class ChatWindow extends BorderPane {
    private final List<String> replyLines = new ArrayList<>();
    private final Mary mary;
    private final VBox messages = new VBox(16);
    private final ScrollPane scroll = new ScrollPane(messages);
    private final TextField input = new TextField();
    private final Button send = new Button("Send");
    private final Label status = new Label("Tasks are stored in mary-data.txt in your working folder.");
    private final FlowPane suggestions = new FlowPane(8, 8);
    private boolean isEnded;

    /**
     * Creates a session with an injected data path so tests never use real tasks.
     *
     * @param filePath relative task file path, or an isolated test file.
     */
    public ChatWindow(String filePath) {
        mary = new Mary(filePath, new Ui(replyLines::add, false));
        getStylesheets().add(ChatWindow.class.getResource("/mary/ui/chat.css").toExternalForm());
        getStyleClass().add("chat-window");
        setTop(createHeader());
        setCenter(createConversation());
        setBottom(createComposer());
        addMessage("A little less to remember.\n\nI'm MARY, your purr-sonal task assistant. "
                + "Add a task, find what matters, and take it one thing at a time.\n\n"
                + "Try: todo read book", false);
        if (mary.getLoadingError() != null) {
            addMessage("Error: " + mary.getLoadingError(), false);
        }
    }

    /**
     * Places keyboard focus in the command field.
     */
    public void focusInput() {
        input.requestFocus();
    }

    /**
     * Builds the identity and the expandable, keyboard-accessible command guide.
     */
    private VBox createHeader() {
        Label cat = new Label(" /\\_/\\\n( o.o )\n > ^ <");
        cat.getStyleClass().add("cat");
        Label title = new Label("MARY");
        title.getStyleClass().add("app-title");
        Label subtitle = new Label("Your purr-sonal task assistant");
        subtitle.getStyleClass().add("subtitle");
        HBox identity = new HBox(18, cat, new VBox(3, title, subtitle));
        identity.setAlignment(Pos.CENTER_LEFT);

        Label examples = new Label("todo read book\n"
                + "deadline return book /by 2/12/2026 1800\n"
                + "event meeting /from 2/12/2026 1400 /to 2/12/2026 1600\n"
                + "list  |  find book  |  on 2/12/2026  |  sort\n"
                + "mark 1  |  unmark 1  |  delete 1  |  bye\n\n"
                + "Dates: day/month/year; times: 24-hour HHmm.\n"
                + "Sort: earliest due/start time first; todos last. Saves new task numbers.\n"
                + "Use list for the task numbers used by mark, unmark, and delete.");
        examples.setWrapText(true);
        examples.getStyleClass().add("guide-text");
        TitledPane guide = new TitledPane("Command guide & examples", examples);
        guide.setId("command-guide");
        guide.setExpanded(false);
        guide.setAnimated(false);
        VBox header = new VBox(18, identity, guide);
        header.getStyleClass().add("header");
        return header;
    }

    /**
     * Creates a responsive transcript that scrolls to each new reply.
     */
    private ScrollPane createConversation() {
        messages.setId("messages");
        messages.getStyleClass().add("messages");
        scroll.setId("conversation");
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messages.heightProperty().addListener((observable, oldHeight, newHeight) -> scroll.setVvalue(1));
        return scroll;
    }

    /**
     * Builds editable command suggestions, input, and the shared Send/Enter action.
     */
    private VBox createComposer() {
        suggestions.getChildren().addAll(createSuggestion("List tasks", "list"),
                createSuggestion("Add a to-do", "todo "), createSuggestion("Find a task", "find "));
        input.setId("command-input");
        input.setPromptText("Type a command, e.g. todo read book");
        input.setAccessibleText("Command input");
        input.setOnAction(event -> submit());
        // Suggestions only fill the field; disable them while a draft is being edited.
        suggestions.disableProperty().bind(input.textProperty().isNotEmpty().or(input.disabledProperty()));
        send.setId("send-button");
        send.getStyleClass().add("send-button");
        send.setOnAction(event -> submit());
        HBox composer = new HBox(10, input, send);
        HBox.setHgrow(input, Priority.ALWAYS);
        input.setMinWidth(0);
        status.setId("session-status");
        status.setWrapText(true);
        status.getStyleClass().add("status");
        VBox footer = new VBox(12, suggestions, composer, status);
        footer.getStyleClass().add("footer");
        return footer;
    }

    /**
     * Creates a command template button without executing or saving anything.
     */
    private Button createSuggestion(String label, String command) {
        Button button = new Button(label);
        button.getStyleClass().add("suggestion");
        button.setOnAction(event -> {
            input.setText(command);
            input.positionCaret(command.length());
            focusInput();
        });
        return button;
    }

    /**
     * Executes a single command, keeps errors in the transcript, and ends on bye.
     */
    private void submit() {
        // UI callbacks must not execute commands or mutate controls from a worker thread.
        assert Platform.isFxApplicationThread() : "Command submission must run on the JavaFX thread";
        if (isEnded) {
            getScene().getWindow().hide();
            return;
        }
        String command = input.getText();
        addMessage(command.isBlank() ? "(empty command)" : command, true);
        replyLines.clear();
        isEnded = mary.execute(command);
        addMessage(String.join("\n", replyLines).strip(), false);
        input.clear();
        if (isEnded) {
            input.setDisable(true);
            send.setText("Close");
            status.setText("Session ended. Close this window when you're ready.");
            send.requestFocus();
        } else {
            focusInput();
        }
        Platform.runLater(() -> scroll.setVvalue(1));
    }

    /**
     * Appends a wrapped bubble with a readable sender label and responsive width.
     */
    private void addMessage(String text, boolean isUser) {
        // Both startup and command replies update the same scene graph.
        assert Platform.isFxApplicationThread() : "Chat messages must be added on the JavaFX thread";
        Label sender = new Label(isUser ? "YOU" : "MARY");
        sender.getStyleClass().add("sender");
        Label body = new Label(text);
        body.setWrapText(true);
        body.setMinHeight(Label.USE_PREF_SIZE);
        body.getStyleClass().add("message-text");
        body.maxWidthProperty().bind(scroll.widthProperty().subtract(100).multiply(0.9));
        VBox bubble = new VBox(7, sender, body);
        bubble.getStyleClass().addAll("bubble", isUser ? "user-bubble" : "mary-bubble");
        HBox row = new HBox(bubble);
        row.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messages.getChildren().add(row);
    }
}
