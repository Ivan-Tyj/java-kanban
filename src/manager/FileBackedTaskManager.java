package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final Path saver;

    public FileBackedTaskManager(Path saver) {
        this.saver = saver;
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
    public void clearSubTask() throws ManagerSaveException, IOException {
        super.clearSubTask();
        save();
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

    private void save() throws ManagerSaveException {
        String head = "id,type,name,status,description,epic";
        List<Task> list = new ArrayList<>();
        list.addAll(getTaskList());
        list.addAll(getEpicTaskList());
        list.addAll(getSubTaskList());
        try (Writer save = new FileWriter(String.valueOf(saver))) {
            save.write(head);
            for (int i = 0; i < list.size(); i++) {
                String line;
                if (list.get(i).getType() == Type.EPIC) {
                    Epic epic = getEpicTaskList().get(i);
                    line = ("\n" + epic.getTaskId() + "," + Type.EPIC + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getDescription());
                } else if (list.get(i).getType() == Type.SUBTASK) {
                    SubTask subTask = getSubTaskList().get(i);
                    line = ("\n" + subTask.getTaskId() + "," + Type.SUBTASK + "," + subTask.getName() + "," + subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicId());
                } else {
                    Task task = getTaskList().get(i);
                    line = ("\n" + task.getTaskId() + "," + Type.TASK + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription());
                }
                save.write(line + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка ввода: " + e.getMessage());
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
        }
        return new Task(name, description, status);
    }
}