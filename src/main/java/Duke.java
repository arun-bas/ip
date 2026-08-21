import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int listCount = 0;

        System.out.println("Hello! I'm Bubba.");
        System.out.println("What can I do for you?");
        // loop and echo user input until bye.
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Goodbye. See you again.");
                break;
            } else if (input.equals("list")){
                System.out.println("Current list of tasks:");
                for (int i=0;i<listCount;i++) {
                    System.out.println((i+1) + ". " + tasks[i]);
                }
            } else if (input.startsWith("mark ")){
                int taskNumber = Integer.parseInt(input.substring(5));
                tasks[taskNumber-1].done();
                System.out.println("Good job, this task is done!");
                System.out.println(tasks[taskNumber-1]);
            } else if (input.startsWith("unmark ")){
                int taskNumber = Integer.parseInt(input.substring(7));
                tasks[taskNumber-1].undoDone();
                System.out.println("This task has been unmarked.");
                System.out.println(tasks[taskNumber-1]);
            }else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                tasks[listCount] = new Todo(description);
                listCount++;
                System.out.println("Added new task:");
                System.out.println(tasks[listCount-1]);
                System.out.println("Number of tasks in list: " + listCount);
            }else if (input.startsWith("deadline ")) {
                String remaining = input.substring(9);
                String[] parts = remaining.split(" /by ", 2);
                String description = parts[0];
                String by = parts[1];

                tasks[listCount] = new Deadline(description, by);
                listCount++;
                System.out.println("Added new task:");
                System.out.println(tasks[listCount-1]);
                System.out.println("Number of tasks in list: " + listCount);
            }else if (input.startsWith("event ")) {
                String remaining = input.substring(6);
                String[] parts = remaining.split(" /from ", 2);
                String description = parts[0];

                String[] times = parts[1].split(" /to ", 2);
                String from = times[0];
                String to = times[1];

                tasks[listCount] = new Event(description, from, to);
                listCount++;
                System.out.println("Added new task: ");
                System.out.println(tasks[listCount-1]);
                System.out.println("Number of tasks in list: " + listCount);
            }else {
                System.out.println("Unknown: " + input);
            }
        }
        scanner.close();
    }
}
