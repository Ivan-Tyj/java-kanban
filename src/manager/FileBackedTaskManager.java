package manager;

import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final Path saver;

    public FileBackedTaskManager(Path saver) {
        this.saver = saver;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public ArrayList<Epic> getEpicTaskList() {
        return super.getEpicTaskList();
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        return super.getSubTaskList();
    }

    @Override
    public void clearTask() throws IOException, ManagerSaveException {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpicTask() throws IOException, ManagerSaveException {
        super.clearEpicTask();
        save();
    }

    @Override
    public void clearSubTask() throws ManagerSaveException {
        save();
    }

    @Override
    public Task findTask(int findTaskId) {
        return super.findTask(findTaskId);
    }

    @Override
    public Epic findEpic(int findTaskId) {
        return super.findEpic(findTaskId);
    }

    @Override
    public SubTask findSub(int findTaskId) {
        return super.findSub(findTaskId);
    }

    @Override
    public boolean findEpicForSub(int epicId) {
        return super.findEpicForSub(epicId);
    }

    @Override
    public void addTask(Task task) throws IOException, ManagerSaveException {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws IOException, ManagerSaveException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSub(SubTask sub) throws IOException, ManagerSaveException {
        super.addSub(sub);
        save();
    }

    @Override
    public void updateTask(Task newTask) throws IOException, ManagerSaveException {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) throws IOException, ManagerSaveException {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSub(SubTask newSub) throws IOException, ManagerSaveException {
        super.updateSub(newSub);
        save();
    }

    @Override
    public void removeTask(Integer key) throws IOException, ManagerSaveException {
        super.removeTask(key);
        save();
    }

    @Override
    public void removeEpic(Integer key) throws IOException, ManagerSaveException {
        super.removeEpic(key);
        save();
    }

    @Override
    public void removeSub(Integer key) throws IOException, ManagerSaveException {
        super.removeSub(key);
        save();
    }

    @Override
    public ArrayList<Task> subForEpic(int epicKey) {
        return super.subForEpic(epicKey);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return super.getHistory();
    }

    private void save() throws ManagerSaveException {
        List<Task> list = new ArrayList<>();
        list.addAll(getTaskList());
        list.addAll(getEpicTaskList());
        list.addAll(getSubTaskList());
        if (list.isEmpty()) {
            throw new ManagerSaveException("Задачи отсутствуют");
        }
        try (Writer save = new FileWriter(String.valueOf(saver))) {
            save.write("id,type,name,status,description,epic");
            for (int i = 0; i < list.size(); i++) {
                Epic epic;
                SubTask subTask;
                Task task;
                String line;
                if (list.get(i).getType() == Type.EPIC) {
                    epic = getEpicTaskList().get(i);
                    line = ("\n" + epic.getTaskId() + "," + Type.EPIC + "," + epic.getName() + ","
                            + epic.getStatus() + "," + epic.getDescription());
                } else if (list.get(i).getType() == Type.SUBTASK) {
                    subTask = getSubTaskList().get(i);
                    line = ("\n" + subTask.getTaskId() + "," + Type.SUBTASK + "," + subTask.getName() + ","
                            + subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicId());
                } else {
                    task = getTaskList().get(i);
                    line = ("\n" + task.getTaskId() + "," + Type.TASK + "," + task.getName() + ","
                            + task.getStatus() + "," + task.getDescription());
                }
                save.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка ввода: " + e.getMessage());
        }
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        String name = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        if (values[1].equals("EPIC")) {
            return new Epic(name, description, status);
        } else if (values[1].equals("SUBTASK")) {
            int epicId = Integer.parseInt(values[5]);
            return new SubTask(name, description, status, epicId);
        } else {
            return new Task(name, description, status);
        }
    }
}









/*
    private void save() throws ManagerSaveException {
        int size = getTaskList().size() + getEpicTaskList().size() + getSubTaskList().size();
        if (size <= 0) {
            throw new ManagerSaveException("Задачи отсутствуют");
        }
        try (Writer save = new FileWriter(String.valueOf(saver))) {
            save.write("id,type,name,status,description,epic");
            for (int i = 1; i <= size; i++) {
                Epic epic = findEpic(i);
                SubTask subTask = findSub(i);
                Task task = findTask(i);
                String line;
                if (epic != null) {
                    line = String.format("%s,%s,%s,%s,%s" + epic.getTaskId() + Type.EPIC + epic.getName()
                            + epic.getStatus() + epic.getDescription().trim());
                } else if (subTask != null) {
                    line = String.format("%s,%s,%s,%s,%s,%s" + subTask.getTaskId() + Type.SUBTASK + subTask.getName()
                            + subTask.getStatus() + subTask.getDescription() + subTask.getEpicId()).trim();
                } else {
                    line = String.format("%s,%s,%s,%s,%s" + task.getTaskId() + Type.TASK + task.getName()
                            + task.getStatus() + task.getDescription()).trim();
                }
                save.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка ввода: " + e.getMessage());
        }
    }
 */