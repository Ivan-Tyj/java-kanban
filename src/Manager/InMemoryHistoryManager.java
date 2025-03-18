package Manager;

import Tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private final int HISTORY_SIZE_LIMIT = 10;
    private ArrayList<Task> historyList = new ArrayList<>(HISTORY_SIZE_LIMIT);

    @Override
    public void add(Task task) {
        historyList.add(task);
        if (historyList.size() > HISTORY_SIZE_LIMIT) {
            historyList.removeFirst();
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
