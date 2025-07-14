package manager;

import java.nio.file.Path;

public abstract class Managers {


    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultBackedManager() {
        return new FileBackedTaskManager(Path.of("saver.txt"));
    }
}
