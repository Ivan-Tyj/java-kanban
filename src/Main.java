import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Приветствую!");
        while (true) {
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    TaskManager.printTaskList();
                    break;
                case 2:
                    TaskManager.clearAllTask();
                    break;
                case 3:
                    System.out.println("Введите идентификатор задачи");
                    int findTaskId = scanner.nextInt();
                    TaskManager.findTask(findTaskId);
                    break;
                case 4:
                    createTask();
                    break;
                case 5:
                    updateOldTask();
                    break;
                case 6:
                    System.out.println("Введите идентификатор задачи");
                    int removeTaskId = scanner.nextInt();
                    TaskManager.removeTask(removeTaskId);
                    break;
                case 7:
                    System.out.println("Введите идентификатор эпика");
                    int epicKey = scanner.nextInt();
                    TaskManager.subForEpic(epicKey);
                    break;
                case 0:
                    System.out.println("Выход");
                    break;
                default:
                    System.out.println("Такой команды нет");
                    break;
            }

        }
    }
    public static void printMenu() {
        System.out.println("Выберите пункт меню: ");
        System.out.println("1 - Получение списка задач");
        System.out.println("2 - Удаление всех задач");
        System.out.println("3 - Получить задачу по идентификатору");
        System.out.println("4 - Создать задачу");
        System.out.println("5 - Обновить задачу");
        System.out.println("6 - Удалить задачу по идентификатору");
        System.out.println("7 - Получить список подзадач определенного эпика");
        System.out.println("0 - Выход");
    }
    public static void createTask() {
        System.out.println("Создание задачи");
        System.out.println("Введите параметры: ");
        System.out.println("Наименование: ");
        String name = scanner.nextLine();
        System.out.println("Описание: ");
        String description = scanner.nextLine();
        System.out.println("Статус: 1 - новая, 2 - выполняется, 3 - выполнена");
        int statusCommand = scanner.nextInt();
        Status status = null;
        if (statusCommand == 1) {
            status = Status.NEW;
        } else if (statusCommand == 2) {
            status = Status.IN_PROGRESS;
        } else if (statusCommand == 3) {
            status = Status.DONE;
        } else {
            System.out.println("Такой команды нет");
        }
        TaskManager.printMenuManager();
        int commandCreate = scanner.nextInt();
        switch (commandCreate) {
            case 1:
                TaskManager.addTaskList(new Task(name, description, status));
                break;
            case 2:
                status = Status.NEW;
                TaskManager.addEpicList(new Epic(name, description, status));
                break;
            case 3:
                System.out.println("Введите идентификатор эпика, в рамках которого выполняется подзадача");
                int epicId = scanner.nextInt();
                if (TaskManager.findEpicForSub(epicId)) {
                    TaskManager.addSubList(new SubTask(name, description, status, epicId));
                } else {
                    System.out.println("Идентификатор эпика введён неверно");
                }
                break;
            default:
                System.out.println("Такой команды нет");
                break;
        }
    }
    public static void updateOldTask() {
        System.out.println("Обновление задачи");
        System.out.println("Введите идентификатор задачи");
        int updateId = scanner.nextInt();
        Task oldTask = TaskManager.findTask(updateId);
        System.out.println("Выберите параметр, который хотите изменить");
        System.out.println("1 - наименование, 2 - описание, 3 - статус, 4 - выход");
        int commandUpdate = scanner.nextInt();
        switch (commandUpdate) {
            case 1:
                oldTask.setName(scanner.nextLine());
                System.out.println("Наименование изменено на: " + oldTask.getName());
                break;
            case 2:
                oldTask.setDescription(scanner.nextLine());
                System.out.println("Описание изменено на: " + oldTask.getDescription());
                break;
            case 3:
                System.out.println("Выберите новый статус: 1 - новая, 2 - выполняется, 3 - выполнена");
                int statusCommand = scanner.nextInt();
                if (statusCommand == 1) {
                    assert oldTask != null;
                    oldTask.setStatus(Status.NEW);
                } else if (statusCommand == 2) {
                    assert oldTask != null;
                    oldTask.setStatus(Status.IN_PROGRESS);
                } else if (statusCommand == 3) {
                    assert oldTask != null;
                    oldTask.setStatus(Status.DONE);
                } else {
                    System.out.println("Такой команды нет");
                }
            default:
                System.out.println("Такой команды нет");
                break;
        }
        assert oldTask != null;
        if (oldTask.getClass() == Epic.class) {
            TaskManager.updateEpic(updateId, (Epic) oldTask);
        } else if (oldTask.getClass() == SubTask.class) {
            TaskManager.updateSub(updateId, (SubTask) oldTask);
        } else if (oldTask.getClass() == Task.class) {
            TaskManager.updateTask(updateId, oldTask);
        }
    }
}
