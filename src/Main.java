import Manager.HistoryManager;
import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager getDefaultHistory = Managers.getDefaultHistory();

        System.out.println("Приветствую!");
        while (true) {
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    printTasksList(scanner, inMemoryTaskManager);
                    break;
                case 2:
                    clearTasks(scanner, inMemoryTaskManager);
                    break;
                case 3:
                    findTask(scanner, inMemoryTaskManager);
                    break;
                case 4:
                    createTask(scanner, inMemoryTaskManager);
                    break;
                case 5:
                    updateOldTask(scanner, inMemoryTaskManager);
                    break;
                case 6:
                    removeTask(scanner, inMemoryTaskManager);
                    break;
                case 7:
                    System.out.println("Введите идентификатор эпика");
                    int epicKey = scanner.nextInt();
                    inMemoryTaskManager.subForEpic(epicKey);
                    break;
                case 8:
                    System.out.println("Вывод истории просмотра");
                    getDefaultHistory.getHistory();
                    break;
                case 9:
                    System.out.println("Напечатать все задачи");
                    printAllTasks(inMemoryTaskManager, getDefaultHistory);
                    break;
                case 0:
                    System.out.println("Выход");
                    return;
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
        System.out.println("8 - Вывести историю просмотра задач");
        System.out.println("9 - Напечатать все задачи");
        System.out.println("0 - Выход");
    }

    public static void printTasksList(Scanner scanner, TaskManager taskManager) {
        System.out.println("Получение списка задач");
        printMenuManager();
        int enterTask = scanner.nextInt();
        if (enterTask == 1) {
            taskManager.getTaskList();
        } else if (enterTask == 2) {
            taskManager.getEpicTaskList();
        } else if (enterTask == 3) {
            taskManager.getSubTaskList();
        }
    }

    public static void clearTasks(Scanner scanner, TaskManager taskManager) {
        System.out.println("Удаление задач");
        printMenuManager();
        int enterTask = scanner.nextInt();
        if (enterTask == 1) {
            taskManager.clearTask();
        } else if (enterTask == 2) {
            taskManager.clearEpicTask();
        } else if (enterTask == 3) {
            taskManager.clearSubTask();
        }
        System.out.println("Все задачи удалены");
    }

    public static void findTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Поиск задач");
        System.out.println("Введите идентификатор задачи");
        int findTaskId = scanner.nextInt();
        printMenuManager();
        int enterTask = scanner.nextInt();
        if (enterTask == 1) {
            taskManager.findTask(findTaskId);
        } else if (enterTask == 2) {
            taskManager.findEpic(findTaskId);
        } else if (enterTask == 3) {
            taskManager.findSub(findTaskId);
        }
    }

    public static void createTask(Scanner scanner, TaskManager inMemoryTaskManager) {
        System.out.println("Создание задачи");
        System.out.println("Введите параметры: ");
        System.out.println("Наименование: ");
        String name = scanner.next();
        System.out.println("Описание: ");
        String description = scanner.next();
        System.out.println("Статус: 1 - новая, 2 - выполняется, 3 - выполнена");
        Status status = null;
        int statusCommand = scanner.nextInt();
        if (statusCommand == 1) {
            status = Status.NEW;
        } else if (statusCommand == 2) {
            status = Status.IN_PROGRESS;
        } else if (statusCommand == 3) {
            status = Status.DONE;
        } else {
            System.out.println("Такой команды нет");
        }
        printMenuManager();
        int commandCreate = scanner.nextInt();
        switch (commandCreate) {
            case 1:
                inMemoryTaskManager.addTask(new Task(name, description, status));
                System.out.println("Задача: " + name + " - добавлена, с идентификатором: "
                        + inMemoryTaskManager);
                break;
            case 2:
                status = Status.NEW;
                inMemoryTaskManager.addEpic(new Epic(name, description, status));
                System.out.println("Эпик: " + name + " - добавлен, с идентификатором: "
                        + inMemoryTaskManager.getTaskId());
                break;
            case 3:
                System.out.println("Введите идентификатор эпика, в рамках которого выполняется подзадача");
                int epicId = scanner.nextInt();
                if (inMemoryTaskManager.findEpicForSub(epicId)) {
                    inMemoryTaskManager.addSub(new SubTask(name, description, status, epicId));
                    System.out.println("Подзадача: " + name + " - добавлена, с идентификатором: "
                            + inMemoryTaskManager.getTaskId()+ ", в эпик с идентификатором: " + epicId);
                } else {
                    System.out.println("Идентификатор эпика введён неверно");
                }
                break;
            default:
                System.out.println("Такой команды нет");
                break;
        }
    }

    public static void updateOldTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Обновление задачи");
        Task oldTask = null;
        System.out.println("Введите идентификатор задачи");
        int updateId = scanner.nextInt();
        printMenuManager();
        int enterTask = scanner.nextInt();
        if (enterTask == 1) {
            oldTask = taskManager.findTask(updateId);
        } else if (enterTask == 2) {
            oldTask = taskManager.findEpic(updateId);
        } else if (enterTask == 3) {
            oldTask = taskManager.findSub(updateId);
        }
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
            taskManager.updateEpic((Epic) oldTask);
            System.out.println("Эпик с идентификатором: " + updateId + " - обновлен");
        } else if (oldTask.getClass() == SubTask.class) {
            taskManager.updateSub((SubTask) oldTask);
            System.out.println("Подзадача с идентификатором: " + updateId + " - обновлена");
        } else if (oldTask.getClass() == Task.class) {
            taskManager.updateTask(oldTask);
            System.out.println("Задача c идентификатором: " + updateId + " - обновлена");
        }
    }

    public static void removeTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Удаление задачи");
        printMenuManager();
        int removeCommand = scanner.nextInt();
        System.out.println("Введите идентификатор");
        int removeTaskId = scanner.nextInt();
        if (removeCommand == 1) {
            taskManager.removeTask(removeTaskId);
        } else if (removeCommand == 2) {
            taskManager.removeEpic(removeTaskId);
        } else if (removeCommand == 3) {
            taskManager.removeSub(removeTaskId);
        }
    }
    private static void printAllTasks(TaskManager manager, HistoryManager historyManager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicTaskList()) {
            System.out.println(epic);

            for (SubTask task : manager.getSubTaskList()) {
                if (epic.getTaskId() == task.getEpicId()) {
                    System.out.println("--> " + task);
                }
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTaskList()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Object task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void printMenuManager() {
        System.out.println("Выберите тип задач");
        System.out.println("1 - Задачи");
        System.out.println("2 - Эпики");
        System.out.println("3 - Подзадачи");
    }
}
