import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] list = new String[100];
        int listCount = 0;

        System.out.println("Hello! I'm Bubba.");
        System.out.println("What can I do for you?");
        // loop and echo user input until bye.
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")){
                for (int i=0;i<listCount;i++) {
                    System.out.println((i+1) + ". " + list[i]);
                }
            } else {
                list[listCount] = input;
                listCount++;

                System.out.println("Added: " + input);
            }
        }

        System.out.println("Goodbye. See you again.");
        scanner.close();
    }
}
