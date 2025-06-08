package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicTaskList = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private final HistoryManager getDefaultHistory = Managers.getDefaultHistory();
    private final TreeSet<Task> sortedTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final TreeSet<SubTask> sortedSubTask = new TreeSet<>(Comparator.comparing(SubTask::getStartTime));

    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> listOfTasks = new ArrayList<>(taskList.size());
        listOfTasks.addAll(taskList.values());
        return listOfTasks;
    }

    @Override
    public ArrayList<Epic> getEpicTaskList() {
        ArrayList<Epic> listOfTasks = new ArrayList<>(epicTaskList.size());
        listOfTasks.addAll(epicTaskList.values());
        return listOfTasks;
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        ArrayList<SubTask> listOfTasks = new ArrayList<>(subTaskList.size());
        listOfTasks.addAll(subTaskList.values());
        return listOfTasks;
    }

    @Override
    public void clearTask() throws ManagerSaveException {
        if (!taskList.isEmpty()) {
            taskList.clear();
            sortedTask.clear();
        }
    }

    @Override
    public void clearEpicTask() throws ManagerSaveException {
        if (!epicTaskList.isEmpty()) {
            epicTaskList.clear();
            if (!subTaskList.isEmpty()) {
                subTaskList.clear();
                sortedSubTask.clear();
            }
        }
    }

    @Override
    public void clearSubTask() throws ManagerSaveException {
        if (!subTaskList.isEmpty()) {
            subTaskList.clear();
            sortedSubTask.clear();
            for (Integer key : epicTaskList.keySet()) {
                epicTaskList.get(key).setStatus(Status.NEW);
            }
        }
    }

    @Override
    public Task findTask(int findTaskId) {
        Task task = taskList.getOrDefault(findTaskId, null);
        getDefaultHistory.add(task);
        return task;
    }

    @Override
    public Epic findEpic(int findTaskId) {
        Epic epic = epicTaskList.getOrDefault(findTaskId, null);
        getDefaultHistory.add(epic);
        return epic;
    }

    @Override
    public SubTask findSub(int findTaskId) {
        SubTask subTask = subTaskList.getOrDefault(findTaskId, null);
        getDefaultHistory.add(subTask);
        return subTask;
    }

    @Override
    public boolean findEpicForSub(int epicId) {
        return epicTaskList.containsKey(epicId);
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        taskId++;
        task.setTaskId(taskId);
        taskList.put(taskId, task);
        getDefaultHistory.add(task);
        if (task.getStartTime() != null) {
            sortedTask.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        taskId++;
        epic.setTaskId(taskId);
        epicTaskList.put(taskId, epic);
        getDefaultHistory.add(epic);
    }

    @Override
    public void addSub(SubTask sub) throws ManagerSaveException {
        taskId++;
        sub.setTaskId(taskId);
        subTaskList.put(taskId, sub);
        epicStatus(sub);
        getDefaultHistory.add(sub);
        if (sub.getStartTime() != null) {
            sortedSubTask.add(sub);
        }
    }

    private void epicStatus(SubTask subTask) {
        int statusNew = 0;
        int statusDone = 0;
        int statusInProgress = 0;
        int epicId = epicTaskList.get(subTask.getEpicId()).getTaskId();
        for (SubTask sub : subTaskList.values()) {
            if (sub.getEpicId() == epicId) {
                if (sub.getStatus().equals(Status.NEW)) {
                    statusNew++;
                } else if (sub.getStatus().equals(Status.DONE)) {
                    statusDone++;
                } else {
                    statusInProgress++;
                    break;
                }
            }
        }
        if (statusDone == 0 && statusInProgress == 0) {
            epicTaskList.get(subTask.getEpicId()).setStatus(Status.NEW);
        } else if (statusDone > 0 && statusNew == 0 && statusInProgress == 0) {
            epicTaskList.get(subTask.getEpicId()).setStatus(Status.DONE);
        } else {
            epicTaskList.get(subTask.getEpicId()).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        int key = newTask.getTaskId();
        taskList.put(key, newTask);
        getDefaultHistory.add(newTask);
        if (newTask.getStartTime() != null) {
            sortedTask.addAll(getTaskList());
        }
    }

    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {
        int key = newEpic.getTaskId();
        epicTaskList.put(key, newEpic);
        getDefaultHistory.add(newEpic);
    }

    @Override
    public void updateSub(SubTask newSub) throws ManagerSaveException {
        int key = newSub.getTaskId();
        subTaskList.put(key, newSub);
        epicStatus(newSub);
        getDefaultHistory.add(newSub);
        if (newSub.getStartTime() != null) {
            sortedSubTask.addAll(getSubTaskList());
        }
    }

    @Override
    public void removeTask(Integer key) throws ManagerSaveException {
        getDefaultHistory.add(taskList.get(key));
        taskList.remove(key);
        sortedTask.addAll(getTaskList());
    }

    @Override
    public void removeEpic(Integer key) throws ManagerSaveException {
        getDefaultHistory.add(epicTaskList.get(key));
        epicTaskList.remove(key);
        for (Integer keySub : subTaskList.keySet()) {
            if (subTaskList.get(keySub).getEpicId() == key) {
                subTaskList.remove(keySub);
            }
        }
    }

    @Override
    public void removeSub(Integer key) throws ManagerSaveException {
        getDefaultHistory.add(subTaskList.get(key));
        SubTask subTask = subTaskList.get(key);
        subTaskList.remove(key);
        epicStatus(subTask);
        sortedSubTask.addAll(getSubTaskList());
    }

    @Override
    public ArrayList<SubTask> subForEpic(int epicKey) {
        List<SubTask> list = new ArrayList<>(getSubTaskList());
        return (ArrayList<SubTask>) list.stream()
                .filter(sub -> sub.getEpicId() == epicKey)
                .toList();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) getDefaultHistory.getHistory();
    }

    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTask);
    }

    public ArrayList<SubTask> getPrioritizedSubTasks() {
        return new ArrayList<>(sortedSubTask);
    }

    public boolean isIntersectionTask() {
        boolean isIntersection = false;
        ArrayList<Task> list = getPrioritizedTasks();
        for (int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            Task nextTask = list.get((i + 1));
            if (task.getEndTime().isAfter(nextTask.getStartTime())) {
                isIntersection = true;
                break;
            }
        }
        return isIntersection;
    }

    public boolean isIntersectionSubTask() {
        boolean isIntersection = false;
        List<SubTask> list = getPrioritizedSubTasks();
        for (int i = 0; i < list.size(); i++) {
            SubTask task = list.get(i);
            SubTask nextTask = list.get((i + 1));
            if (task.getEndTime().isAfter(nextTask.getStartTime())) {
                isIntersection = true;
            }
        }
        return isIntersection;
    }
}
