package http.handlers.subs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

public class PrioritizedHttpHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            getPriorityHandle(exchange);
        } else {
            System.out.println("Такого метода нет");
        }
    }

    public void getPriorityHandle(HttpExchange exchange) throws IOException {
        TreeSet<Task> sortedAllTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedAllTask.addAll(manager.getPrioritizedTasks());
        sortedAllTask.addAll(manager.getPrioritizedSubTasks());
        String text = gson.toJson(sortedAllTask);
        sendText(exchange, text, 200);
    }
}
