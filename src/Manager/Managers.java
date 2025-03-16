package Manager;

public abstract class Managers {
    static TaskManager InMemoryTaskManager = new InMemoryTaskManager();
    static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();


    public static TaskManager getDefault() {
        return InMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
