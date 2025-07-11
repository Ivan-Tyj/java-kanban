package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public abstract class Managers {

    private static final FileBackedTaskManager FILE_BACKED_TASK_MANAGER =
            new FileBackedTaskManager(Path.of("saver.txt"));


    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        try (Reader reader = new FileReader(String.valueOf(path))) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                if ((int) line.charAt(0) > 0) {
                    if (line.contains("TASK")) {
                        Task task = FileBackedTaskManager.fromString(line);
                        getDefault().addTask(task);
                    } else if (line.contains("EPIC")) {
                        Epic epic = (Epic) FileBackedTaskManager.fromString(line);
                        getDefault().addEpic(epic);
                    } else if (line.contains("SUBTASK")) {
                        SubTask subTask = (SubTask) FileBackedTaskManager.fromString(line);
                        getDefault().addSub(subTask);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return FILE_BACKED_TASK_MANAGER;
    }
}
