
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    private static final HashMap<Integer, Task> taskList = new HashMap<>();
    private static final HashMap<Integer, Task> epicTaskList = new HashMap<>();
    private static final HashMap<Integer, Task> subTaskList = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void printTaskList() {
        printMenuManager();
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                for (Integer key : taskList.keySet()) {
                    System.out.println("Идентификатор: " + key + ". Наименование задачи: " +
                            taskList.get(key).getName());
                }
                break;
            case 2:
                for (Integer key : epicTaskList.keySet()) {
                    System.out.println("Идентификатор: " + key + ". Наименование эпика: "
                            + taskList.get(key).getName());
                }
                break;
            case 3:
                for (Integer key : subTaskList.keySet()) {
                    System.out.println("Идентификатор: " + key + ". Наименование эпика: "
                            + taskList.get(key).getName());
                }
                break;
            default:
                System.out.println("Неверная команда");
                break;
        }
    }
    public static void clearAllTask() {
        printMenuManager();
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                if (!taskList.isEmpty()) {
                    taskList.clear();
                    System.out.println("Все задачи удалены");
                } else {
                    System.out.println("Список задач пуст");
                }
                break;
            case 2:
                if (!epicTaskList.isEmpty()) {
                    epicTaskList.clear();
                    System.out.println("Все задачи удалены");
                } else {
                    System.out.println("Список задач пуст");
                }
                break;
            case 3:
                if (!subTaskList.isEmpty()) {
                    subTaskList.clear();
                    System.out.println("Все задачи удалены");
                } else {
                    System.out.println("Список задач пуст");
                }
                break;
            default:
                System.out.println("Неверная команда");
                break;
        }
    }
    public static Task findTask(int findTaskId) {
        if (epicTaskList.containsKey(findTaskId)) {
            return epicTaskList.get(findTaskId);
        } else if (subTaskList.containsKey(findTaskId)) {
            return subTaskList.get(findTaskId);
        } else if (taskList.containsKey(findTaskId)) {
            return taskList.get(findTaskId);
        } else {
            System.out.println("Такого идентификатора нет");
            return null;
        }
    }
    public static boolean findEpicForSub(int epicId) {
        if (epicTaskList.containsKey(epicId)) {
            return true;
        } else {
            return false;
        }
    }
    public static void addTaskList (Task task) {
        int newTaskId = task.hashCode();
        taskList.put(newTaskId, task);
        System.out.println("Задача: " + task.getName() + " - добавлена, с идентификатором: " + newTaskId);
    }
    public static void addEpicList (Epic task) {
        int newTaskId = task.hashCode();
        epicTaskList.put(newTaskId, task);
        System.out.println("Эпик: " + task.getName() + " - добавлен, с идентификатором: " + newTaskId);
    }
    public static void addSubList (SubTask task) {
        int newTaskId = task.hashCode();
        subTaskList.put(newTaskId, task);
        System.out.println("Подзадача: " + task.getName() + " - добавлена, с идентификатором: " + newTaskId
                + ", в эпик с идентификатором: " + task.getEpicId());
        newEpicStatus(task);
        System.out.println("Статус эпика " + task.getEpicId() + " - " + epicTaskList.get(task.getEpicId()).getStatus());
    }
    private static void newEpicStatus(SubTask task) {
        if (epicTaskList.containsKey(task.getEpicId())) {
            Epic epic = (Epic) epicTaskList.get(task.getEpicId());
            if (epic.getStatus() == Status.NEW && epic.getStatus() == task.getStatus()) {
                epicTaskList.get(task.getEpicId()).setStatus(Status.NEW);
            } else if ((epic.getStatus() == Status.NEW || epic.getStatus() == Status.DONE)
                    && task.getStatus() == Status.DONE) {
                epicTaskList.get(task.getEpicId()).setStatus(Status.DONE);
            } else {
                epicTaskList.get(task.getEpicId()).setStatus(Status.IN_PROGRESS);
            }
        }
    }
    public static void updateTask(Integer key, Task newTask){
        taskList.put(key, newTask);
        System.out.println("Задача c идентификатором: " + key + " - обновлена");
    }
    public static void updateEpic(Integer key, Epic newEpic){
        epicTaskList.put(key, newEpic);
        System.out.println("Эпик с идентификатором: " + key + " - обновлен");
    }
    public static void updateSub(Integer key, SubTask newSub){
        subTaskList.put(key, newSub);
        System.out.println("Подзадача с идентификатором: " + key + " - обновлена");
    }
    public static void removeTask(Integer key){
        printMenuManager();
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                if (!taskList.containsKey(key)) {
                    taskList.remove(key);
                    System.out.println("Задача удалена");
                } else {
                    System.out.println("Такой задачи нет");
                }
                break;
            case 2:
                if (!epicTaskList.containsKey(key)) {
                    epicTaskList.remove(key);
                    System.out.println("Эпик удален");
                } else {
                    System.out.println("Такого эпика нет");
                }
                break;
            case 3:
                if (!subTaskList.containsKey(key)) {
                    subTaskList.remove(key);
                    System.out.println("Подзадача удалена");
                } else {
                    System.out.println("Такой подзадачи нет");
                }
                break;
            default:
                System.out.println("Неверная команда");
                break;
        }
    }
    public static ArrayList subForEpic(int epicKey) {
        ArrayList<SubTask> subForEpic = new ArrayList<>();
        for (Integer key : subTaskList.keySet()) {
            SubTask subTask = (SubTask) subTaskList.get(key);
            if (subTask.getEpicId() == epicKey) {
                subForEpic.add(subTask);
            }
        }
        System.out.println(subForEpic);
        return subForEpic;
    }
    public static void printMenuManager() {
        System.out.println("Выберите тип задач");
        System.out.println("1 - Задачи");
        System.out.println("2 - Эпики");
        System.out.println("3 - Подзадачи");
    }
}
