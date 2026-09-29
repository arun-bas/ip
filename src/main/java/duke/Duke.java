package duke;

/** Coordinates user input, task operations and persistence for Bubba. */
public class Duke {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
        for (String warning : storage.getLoadWarnings()) {
            ui.showError(warning);
        }
    }

    /** Runs the command loop, leaving parsing and presentation to their own classes. */
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
                    case UNMARK:
                        boolean isDone = command.type() == Parser.Type.MARK;
                        task = tasks.mark(command.taskNumber(), isDone);
                        saveTasks();
                        ui.showMarked(task, isDone);
                        break;
                    }
                } catch (BubbaException e) {
                    ui.showError(e.getMessage());
                }
            }
        } finally {
            ui.close();
        }
    }

    /** Reports a save failure while retaining the user's change in memory. */
    private void saveTasks() {
        try {
            storage.save(tasks.toList());
        } catch (BubbaException e) {
            ui.showError(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Duke("./data/duke.txt").run();
    }
}
