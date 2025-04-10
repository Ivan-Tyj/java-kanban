package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;


    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getTaskId())) {
            remove(task.getTaskId());
        }
        linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void remove(int id) {
        Node<Task> otherNode = historyMap.get(id);
        removeNode(otherNode);
    }

    private void linkLast(Task task) {
        Node<Task> l = last;
        Node<Task> newNode = new Node<>(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
            historyMap.put(task.getTaskId(), first);
        } else {
            l.next = newNode;
            historyMap.put(task.getTaskId(), l.next);
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Node<Task> node = first; node != null; node = node.next) {
            list.add(node.item);
        }
        return list;
    }

    public void removeNode(Node<Task> node) {
        Node<Task> prevNode = node.prev;
        Node<Task> nextNode = node.next;

        if (prevNode == null) {
            first = nextNode;
        } else {
            prevNode.next = nextNode;
            historyMap.put(prevNode.item.getTaskId(), prevNode);
        }

        if (nextNode == null) {
            last = prevNode;
        } else {
            nextNode.prev = prevNode;
            historyMap.put(nextNode.item.getTaskId(), nextNode);
        }
        historyMap.remove(node.item.getTaskId());
    }
}