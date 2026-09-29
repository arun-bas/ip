package bubba;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Owns the tasks and provides operations using the user's one-based task numbers. */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this(new ArrayList<>());
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int taskNumber) throws BubbaException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BubbaException("Task does not exist.");
        }
        return tasks.get(taskNumber - 1);
    }

    public Task delete(int taskNumber) throws BubbaException {
        Task task = get(taskNumber);
        tasks.remove(taskNumber - 1);
        return task;
    }

    public Task mark(int taskNumber, boolean isDone) throws BubbaException {
        Task task = get(taskNumber);
        if (isDone) {
            task.done();
        } else {
            task.undoDone();
        }
        return task;
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Returns tasks whose descriptions contain the keyword, ignoring letter case.
     * The matches retain their order from the full task list.
     */
    public ArrayList<Task> find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.description.toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /** Returns a copy so callers cannot add or remove tasks behind this class's back. */
    public ArrayList<Task> toList() {
        return new ArrayList<>(tasks);
    }
}
