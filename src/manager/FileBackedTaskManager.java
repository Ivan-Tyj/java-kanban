package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final Path saver;
    private static final String HEAD = "id,type,name,status,description,epic,startTime,endTime,duration";

    public FileBackedTaskManager(Path saver) {
        this.saver = saver;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path);
        try (Reader reader = new FileReader(String.valueOf(path))) {
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                if ((int) line.charAt(0) > 0) {
                    if (line.contains("TASK")) {
                        Task task = fromString(line);
                        fileBackedTaskManager.addTask(task);
                    } else if (line.contains("SUBTASK")) {
                        SubTask subTask = (SubTask) fromString(line);
                        fileBackedTaskManager.addSub(subTask);

                    } else if (line.contains("EPIC")) {
                        Epic epic = (Epic) fromString(line);
                        fileBackedTaskManager.addEpic(epic);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Файл не прочитан");
        }
        return fileBackedTaskManager;
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
        }
        if (values[1].equals("SUBTASK")) {
            int epicId = Integer.parseInt(values[5]);
            return new SubTask(name, description, status, epicId, startTime, duration);
        }
        return new Task(name, description, status, startTime, duration);
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpicTask() {
        super.clearEpicTask();
        save();
    }

    @Override
    public void clearSubTask() {
        super.clearSubTask();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSub(SubTask sub) {
        super.addSub(sub);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSub(SubTask newSub) {
        super.updateSub(newSub);
        save();
    }

    @Override
    public void removeTask(Integer key) {
        super.removeTask(key);
        save();
    }

    @Override
    public void removeEpic(Integer key) {
        super.removeEpic(key);
        save();
    }

    @Override
    public void removeSub(Integer key) {
        super.removeSub(key);
        save();
    }

    private void save() {
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
        for (Task task : getTaskList()) {
            String line = task.toString();
            try {
                save.write(line);
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка записи задачи в файл: " + e.getMessage());
            }
        }
    }

    private void epicToCSV(Writer save) throws ManagerSaveException {
        for (Epic epic : getEpicTaskList()) {
            String line = epic.toString();
            try {
                save.write(line);
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка записи эпика в файл: " + e.getMessage());
            } catch (NullPointerException e) {
                throw new ManagerSaveException("Эпик = null");
            }
        }
    }

    private void subToCSV(Writer save) throws ManagerSaveException {
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