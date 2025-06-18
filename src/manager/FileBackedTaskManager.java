package manager;
import tasks.*;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final Path saver;
    private static final String HEAD = "id,type,name,status,description,epic,startTime,endTime,duration";

    public FileBackedTaskManager(Path saver) {
        this.saver = saver;
    }

    @Override
    public void clearTask() throws ManagerSaveException {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpicTask() throws ManagerSaveException {
        super.clearEpicTask();
        save();
    }

    @Override
    public void clearSubTask() throws ManagerSaveException {
        super.clearSubTask();
        save();
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSub(SubTask sub) throws ManagerSaveException {
        super.addSub(sub);
        save();
    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSub(SubTask newSub) throws ManagerSaveException {
        super.updateSub(newSub);
        save();
    }

    @Override
    public void removeTask(Integer key) throws ManagerSaveException {
        super.removeTask(key);
        save();
    }

    @Override
    public void removeEpic(Integer key) throws ManagerSaveException {
        super.removeEpic(key);
        save();
    }

    @Override
    public void removeSub(Integer key) throws ManagerSaveException {
        super.removeSub(key);
        save();
    }

    private void save() throws ManagerSaveException {
        try (Writer save = new FileWriter(String.valueOf(saver))) {
            save.write(HEAD);
            epicToCSV(save);
            subToCSV(save);
            taskToCSV(save);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка ввода файла: " + e.getMessage());
        }
    }

    private void taskToCSV(Writer save) throws ManagerSaveException {
        if (!getTaskList().isEmpty()) {
            for (Task task : getTaskList()) {
                String line = task.toString();
                try {
                    save.write(line);
                } catch (IOException e) {
                    throw new ManagerSaveException("Произошла ошибка записи задачи в файл: " + e.getMessage());
                }
            }
        }
    }

    private void epicToCSV(Writer save) throws ManagerSaveException {
        if (!getEpicTaskList().isEmpty()) {
            for (Epic epic : getEpicTaskList()) {
                String line = epic.toString();
                try {
                    save.write(line);
                } catch (IOException e) {
                    throw new ManagerSaveException("Произошла ошибка записи эпика в файл: " + e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    private void subToCSV(Writer save) throws ManagerSaveException {
        if (!getSubTaskList().isEmpty()) {
            for (SubTask subTask : getSubTaskList()) {
                String line = subTask.toString();
                try {
                    save.write(line);
                } catch (IOException e) {
                    throw new ManagerSaveException("Произошла ошибка записи подзадачи в файл: " + e.getMessage());
                }
            }
        }
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        String name = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        Duration duration = Duration.parse(values[8]);
        if (values[1].equals("EPIC")) {
            return new Epic(name, description, status, startTime, duration);
        } else if (values[1].equals("SUBTASK")) {
            int epicId = Integer.parseInt(values[5]);
            return new SubTask(name, description, status, epicId, startTime, duration);
        }
        return new Task(name, description, status, startTime, duration);
    }
}