import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line =  scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(" \\| ");

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];
                Task task;

                if (type.equals("T")) {
                    task = new Todo(description);
                } else if (type.equals("D")) {
                    String by = parts[3];
                    task = new Deadline(description, by);
                } else if (type.equals("E")) {
                    String from = parts[3];
                    String to = parts[4];
                    task = new Event(description, from, to);
                } else {
                    continue;
                }

                if (isDone) {
                    task.done();
                }

                tasks.add(task);
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File wasn't found!");
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
        try {
            File file = new File(filePath);

            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            PrintWriter writer = new PrintWriter(file);

            for (Task task: tasks) {
                writer.println(task.toStorageString());
            }

            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save tasks to file!");
        }
    }
}
