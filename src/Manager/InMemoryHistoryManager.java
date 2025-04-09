package Manager;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{


    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;


    @Override
    public void add(Task task) {
        if (!historyMap.containsKey(task.getTaskId())) {
            linkLast(task);
        } else {
            remove(task.getTaskId());
            linkLast(task);
        }
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
        if (first == null) {
            first = new Node<>(null, task, null);
            historyMap.put(task.getTaskId(), first);
        } else {
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
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> list =  new ArrayList<>();
        for (Node<Task> x = first; x != null; x = x.next)  {
            list.add(x.item);
        }
        return list;
    }

    public void removeNode(Node<Task> node) {
        Node<Task> prevNode;
        Node<Task> nextNode;
        if (node.prev != null && node.next != null) {
            prevNode = historyMap.get(node.prev.item.getTaskId());
            nextNode = historyMap.get(node.next.item.getTaskId());
            prevNode.next = nextNode;
            historyMap.put(prevNode.item.getTaskId(), prevNode);
            nextNode.prev = prevNode;
            historyMap.put(nextNode.item.getTaskId(), nextNode);
            historyMap.remove(node.item.getTaskId());
        } else if (node.prev == null && node.next != null) {
            nextNode = historyMap.get(node.next.item.getTaskId());
            nextNode.prev = null;
            historyMap.put(nextNode.item.getTaskId(), nextNode);
            historyMap.remove(node.item.getTaskId());
            first = null;
        } else if (node.prev != null && node.next == null) {
            prevNode = historyMap.get(node.prev.item.getTaskId());
            prevNode.next = null;
            historyMap.put(prevNode.item.getTaskId(), prevNode);
            historyMap.remove(node.item.getTaskId());
            last = null;
        } else {
            historyMap.remove(node.item.getTaskId());
            first = null;
        }
    }
}
