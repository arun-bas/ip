package duke;

public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public void done() {
        isDone = true;
    }

    public void undoDone() {
        isDone = false;
    }

    public String toStorageString() {
        return type.getSymbol()
                + " | "
                + (isDone ? "1" : "0")
                + " | "
                + description;
    }
    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        return "[" + type.getSymbol() + "][" + status + "] " + description;
    }
}
