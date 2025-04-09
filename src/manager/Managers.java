package manager;

public abstract class Managers {
    private static final TaskManager IN_MEMORY_TASK_MANAGER  = new InMemoryTaskManager();
    private static final InMemoryHistoryManager IN_MEMORY_HISTORY_MANAGER = new InMemoryHistoryManager();


    public static TaskManager getDefault() {
        return IN_MEMORY_TASK_MANAGER;
    }

    public static HistoryManager getDefaultHistory() {
        return IN_MEMORY_HISTORY_MANAGER;
    }
}
