package http.handlers.subs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.Managers;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.TreeSet;

public class PrioritizedHttpHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] uriStrSplits = path.split("/");
        String url = uriStrSplits[1];
        if (url.equals("prioritized")) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    getPriorityHandle(exchange);
                    break;
                default:
                    System.out.println("Такого метода нет");
            }
        }
    }

    public void getPriorityHandle(HttpExchange exchange) throws IOException {
        TreeSet<Task> sortedAllTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedAllTask.addAll(Managers.getDefault().getPrioritizedTasks());
        sortedAllTask.addAll(Managers.getDefault().getPrioritizedSubTasks());
        String text = gson.toJson(sortedAllTask);
        sendText(exchange, text, 200);
    }

}
