package tasks;

public class SubTask extends Task {
    private final int epicId;
    private final Type type = Type.SUBTASK;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return type;
    }
}
