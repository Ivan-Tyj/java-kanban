package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;
    private final Type type = Type.SUBTASK;

    public SubTask(String name, String description, Status status, int epicId, LocalDateTime startTime,
                   Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
       return  "\n" + getTaskId() + "," + getType() + "," + getName() + ","
                + getStatus() + "," + getDescription() + getEpicId()
                + "," + getStartTime() + "," + getEndTime()
                + "," + getDuration().toMinutes();
    }
}
