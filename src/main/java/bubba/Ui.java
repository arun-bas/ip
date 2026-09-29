package bubba;

import java.util.List;
import java.util.Scanner;

/**
 * Handles console input and all messages shown to the user.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a console interface that reads from standard input.
     */
    public Ui() {
    }

    /**
     * Displays the greeting and initial prompt.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Bubba.");
        System.out.println("What can I do for you?");
    }

    /**
     * Reads the next command line, waiting for input when necessary.
     *
     * @return Entered line, or {@code null} if the input stream has ended.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println("Goodbye. See you again.");
    }

    /**
     * Displays tasks in order, numbered starting from one.
     *
     * @param tasks Tasks to display.
     */
    public void showList(TaskList tasks) {
        System.out.println("Current list of tasks:");
        int number = 1;
        for (Task task : tasks.toList()) {
            System.out.println(number++ + ". " + task);
        }
    }

    /**
     * Displays matching tasks in their original order, numbered from one.
     *
     * @param tasks Matching tasks to display.
     */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println("Here are the matching tasks in your list:");
        int number = 1;
        for (Task task : tasks) {
            System.out.println(number++ + ". " + task);
        }
    }

    /**
     * Displays confirmation of an addition and the updated task count.
     *
     * @param task Added task.
     * @param size Total task count after the addition.
     */
    public void showAdded(Task task, int size) {
        System.out.println(task instanceof Event ? "Added new task: " : "Added new task:");
        System.out.println(task);
        showCount(size);
    }

    /**
     * Displays confirmation of a deletion and the remaining task count.
     *
     * @param task Deleted task.
     * @param size Total task count after the deletion.
     */
    public void showDeleted(Task task, int size) {
        System.out.println("Removed the following task: ");
        System.out.println(task);
        showCount(size);
    }

    /**
     * Displays confirmation of a task's updated completion status.
     *
     * @param task Updated task.
     * @param isDone Whether the task was marked as completed.
     */
    public void showMarked(Task task, boolean isDone) {
        System.out.println(isDone
                ? "Good job, this task is done!"
                : "This task has been unmarked.");
        System.out.println(task);
    }

    private void showCount(int size) {
        System.out.println("Number of tasks in list: " + size);
    }

    /**
     * Displays an error with Bubba's error prefix.
     *
     * @param message Explanation of the error.
     */
    public void showError(String message) {
        System.out.println("My bad... " + message);
    }

    /**
     * Closes the console scanner and its underlying standard input stream.
     */
    public void close() {
        scanner.close();
    }
}
