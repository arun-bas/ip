import java.util.Scanner;

/** Handles console input and all messages shown to the user. */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        System.out.println("Hello! I'm Bubba.");
        System.out.println("What can I do for you?");
    }

    /** Returns null when the input stream has ended. */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    public void showGoodbye() {
        System.out.println("Goodbye. See you again.");
    }

    public void showList(TaskList tasks) {
        System.out.println("Current list of tasks:");
        int number = 1;
        for (Task task : tasks.toList()) {
            System.out.println(number++ + ". " + task);
        }
    }

    public void showAdded(Task task, int size) {
        System.out.println(task instanceof Event ? "Added new task: " : "Added new task:");
        System.out.println(task);
        showCount(size);
    }

    public void showDeleted(Task task, int size) {
        System.out.println("Removed the following task: ");
        System.out.println(task);
        showCount(size);
    }

    public void showMarked(Task task, boolean isDone) {
        System.out.println(isDone ? "Good job, this task is done!" : "This task has been unmarked.");
        System.out.println(task);
    }

    private void showCount(int size) {
        System.out.println("Number of tasks in list: " + size);
    }

    public void showError(String message) {
        System.out.println("My bad... " + message);
    }

    public void close() {
        scanner.close();
    }
}
