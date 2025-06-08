package tasks;

import manager.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class Epic extends Task {
    private final Type type = Type.EPIC;


    public Epic(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        setStatus(Status.NEW);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public LocalDateTime getStartTime() {
        List<SubTask> list = Managers.getDefault().getSubTaskList();
        return list.stream()
                .filter(subTask -> subTask.getEpicId() == this.getTaskId())
                .min((Comparator.comparing(SubTask::getStartTime)))
                .get()
                .getStartTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        List<SubTask> list = Managers.getDefault().getSubTaskList();
        return list.stream()
                .filter(subTask -> subTask.getEpicId() == this.getTaskId())
                .max((Comparator.comparing(SubTask::getEndTime)))
                .get()
                .getEndTime();
    }

    @Override
    public Duration getDuration() {
        LocalDateTime first = getStartTime();
        LocalDateTime last = getEndTime();
        return Duration.between(first, last);
    }
}
