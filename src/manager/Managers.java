package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;

public abstract class Managers {
    private static final TaskManager IN_MEMORY_TASK_MANAGER = new InMemoryTaskManager();
    private static final InMemoryHistoryManager IN_MEMORY_HISTORY_MANAGER = new InMemoryHistoryManager();
    private static final FileBackedTaskManager FILE_BACKED_TASK_MANAGER =
            new FileBackedTaskManager(Path.of("saver.txt"));


    public static TaskManager getDefault() {
        return IN_MEMORY_TASK_MANAGER;
    }

    public static HistoryManager getDefaultHistory() {
        return IN_MEMORY_HISTORY_MANAGER;
    }

    public static FileBackedTaskManager loadFromFile(Path path) throws ManagerSaveException {
        try (Reader reader = new FileReader(String.valueOf(path))) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                if ((int) line.charAt(0) > 0) {
                    if (line.contains("TASK")) {
                        Task task = FileBackedTaskManager.fromString(line);
                        IN_MEMORY_TASK_MANAGER.addTask(task);
                    } else if (line.contains("EPIC")) {
                        Epic epic = (Epic) FileBackedTaskManager.fromString(line);
                        IN_MEMORY_TASK_MANAGER.addEpic(epic);
                    } else if (line.contains("SUBTASK")) {
                        SubTask subTask = (SubTask) FileBackedTaskManager.fromString(line);
                        IN_MEMORY_TASK_MANAGER.addSub(subTask);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Произошла ошибка ввода: " + e.getMessage());
        }
        return FILE_BACKED_TASK_MANAGER;
    }
}
