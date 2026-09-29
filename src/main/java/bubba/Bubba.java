package bubba;

/**
 * Coordinates user input, task operations and persistence for Bubba.
 */
public class Bubba {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Initializes the chatbot and loads saved tasks, displaying any loading warnings.
     *
     * @param filePath Path used to load and save tasks.
     */
    public Bubba(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
        for (String warning : storage.getLoadWarnings()) {
            ui.showError(warning);
        }
    }

    /**
     * Processes commands until the user exits or input ends, then closes console input.
     */
    public void run() {
        ui.showWelcome();
        try {
            String input;
            while ((input = ui.readCommand()) != null) {
                try {
                    Parser.ParsedCommand command = Parser.parse(input);
                    Task task;
                    switch (command.type()) {
                        case EXIT:
                            ui.showGoodbye();
                            return;
                        case LIST:
                            ui.showList(tasks);
                            break;
                        case FIND:
                            ui.showMatchingTasks(tasks.find(command.keyword()));
                            break;
                        case ADD:
                            task = command.task();
                            tasks.add(task);
                            saveTasks();
                            ui.showAdded(task, tasks.size());
                            break;
                        case DELETE:
                            task = tasks.delete(command.taskNumber());
                            saveTasks();
                            ui.showDeleted(task, tasks.size());
                            break;
                        case MARK:
                            // Fallthrough
                        case UNMARK:
                            boolean isDone = command.type() == Parser.Type.MARK;
                            task = tasks.mark(command.taskNumber(), isDone);
                            saveTasks();
                            ui.showMarked(task, isDone);
                            break;
                        default:
                            throw new AssertionError("Unexpected command type: " + command.type());
                    }
                } catch (BubbaException e) {
                    ui.showError(e.getMessage());
                }
            }
        } finally {
            ui.close();
        }
    }

    /**
     * Reports a save failure while retaining the user's change in memory.
     */
    private void saveTasks() {
        try {
            storage.save(tasks.toList());
        } catch (BubbaException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Starts Bubba with its save file relative to the working directory.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Bubba("./data/bubba.txt").run();
    }
}
