import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Duke {
    public static void main(String[] args) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage("./data/duke.txt");
        ArrayList<Task> tasks = storage.load();

        System.out.println("Hello! I'm Bubba.");
        System.out.println("What can I do for you?");
        // loop and echo user input until bye.
        while (true) {
            String input = scanner.nextLine();
            try {
                if (input.equals("bye")) {
                    System.out.println("Goodbye. See you again.");
                    break;
                } else if (input.equals("list")) {
                    System.out.println("Current list of tasks:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                } else if (input.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(input.substring(5));
                    tasks.get(taskNumber - 1).done();
                    storage.save(tasks);
                    System.out.println("Good job, this task is done!");
                    System.out.println(tasks.get(taskNumber - 1));
                } else if (input.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(input.substring(7));
                    tasks.get(taskNumber - 1).undoDone();
                    storage.save(tasks);
                    System.out.println("This task has been unmarked.");
                    System.out.println(tasks.get(taskNumber - 1));
                } else if (input.startsWith("delete ")) {
                    try {
                        int taskNumber = Integer.parseInt(input.substring(7));
                        int idx = taskNumber-1;
                        if (idx<0 || idx >= tasks.size()) {
                            throw new BubbaException("Task does not exist.");
                        }

                        Task deletedTask = tasks.remove(idx);
                        storage.save(tasks);
                        System.out.println("Removed the following task: ");
                        System.out.println(deletedTask);
                        System.out.println("Number of tasks in list: " + tasks.size());
                    } catch (NumberFormatException e) {
                        throw new BubbaException("Please enter valid task number.");
                    }
                }else if (input.equals("todo")){
                    throw new BubbaException("Todo description cannot be empty!");
                }else if (input.startsWith("todo ")) {
                    String description = input.substring(5);
                    if (description.isEmpty()) {
                        throw new BubbaException("Todo description cannot be empty!");
                    }
                    Task task = new Todo(description);
                    tasks.add(task);
                    storage.save(tasks);
                    System.out.println("Added new task:");
                    System.out.println(task);
                    System.out.println("Number of tasks in list: " + tasks.size());
                } else if (input.startsWith("deadline ")) {
                    String remaining = input.substring(9);
                    String[] parts = remaining.split(" /by ", 2);
                    String description = parts[0];
                    //String by = parts[1];

                    //Task task = new Deadline(description, by);
                    String byText = parts[1].trim();
                    if (description.isEmpty()) {
                        throw new BubbaException("Description is empty!");
                    }
                    if (byText.isEmpty()) {
                        throw new BubbaException("The date of a deadline can't be empty!");
                    }

                    try {
                        LocalDateTime by = LocalDateTime.parse(byText, inputFormatter);

                        Task task = new Deadline(description, by);
                        tasks.add(task);
                        storage.save(tasks);
                        System.out.println("Added new task:");
                        System.out.println(task);
                        System.out.println("Number of tasks in list: " + tasks.size());
                    } catch (DateTimeParseException e) {
                        throw new BubbaException("Pleae enter deadline in d/M/yyyy HHmm format!");
                    }

                } else if (input.startsWith("event ")) {
                    String remaining = input.substring(6);
                    String[] parts = remaining.split(" /from ", 2);
                    String description = parts[0];

                    String[] times = parts[1].split(" /to ", 2);
                    String fromText = times[0].trim();
                    String toText = times[1].trim();
                    if (description.isEmpty()) {
                        throw new BubbaException("Description cannot be empty!");
                    }

                    if (fromText.isEmpty()) {
                        throw new BubbaException("The from time cannot be empty!");
                    }

                    if (toText.isEmpty()) {
                        throw new BubbaException("The to time cannot be empty!");
                    }

                    try {
                        LocalDateTime from = LocalDateTime.parse(fromText, inputFormatter);
                        LocalDateTime to = LocalDateTime.parse(toText, inputFormatter);
                        Task task = new Event(description, from, to);
                        tasks.add(task);
                        storage.save(tasks);
                        System.out.println("Added new task: ");
                        System.out.println(task);
                        System.out.println("Number of tasks in list: " + tasks.size());
                    } catch (DateTimeParseException e) {
                        throw new BubbaException("Enter the event time in the correct format d/M/yyyy HHmm!");
                    }

                } else {
                    throw new BubbaException("Sorry, unsure what you mean by that.");
                }
            } catch (BubbaException e) {
                System.out.println("My bad... " + e.getMessage());
            }
        }
        scanner.close();
    }
}
