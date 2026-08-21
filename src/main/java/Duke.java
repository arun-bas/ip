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
            }else {
                tasks[listCount] = new Task(input);
                listCount++;

                System.out.println("Added: " + input);
            }
        }
        scanner.close();
    }
}
