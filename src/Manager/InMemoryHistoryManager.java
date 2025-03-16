package Manager;

import Tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private ArrayList<Task> historyList = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        historyList.add(task);
        if (historyList.size() > 10) {
            historyList.removeFirst();
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
