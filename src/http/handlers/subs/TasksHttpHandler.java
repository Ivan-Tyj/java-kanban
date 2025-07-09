package http.handlers.subs;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.Managers;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHttpHandler extends BaseHttpHandler implements HttpHandler {

    Gson gson;

    public TasksHttpHandler(Gson gson) {
        super();
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] uriStrSplits = path.split("/");
        String url = uriStrSplits[1];
        if (url.equals("tasks")) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    if (uriStrSplits.length > 2) {
                        getTaskByIdHandle(exchange, uriStrSplits[2]);
                        break;
                    }
                    getTasksHandle(exchange);
                    break;
                case "POST":
                    if (uriStrSplits.length > 2) {
                        updateTasksHandle(exchange);
                        break;
                    }
                    createTaskHandle(exchange);
                    break;
                case "DELETE":
                    deleteTasksHandle(exchange, uriStrSplits[2]);
                    break;
                default:
                    System.out.println("Такого метода нет");
            }
        }
    }

    public void getTasksHandle(HttpExchange exchange) throws IOException {
        List<Task> list = Managers.getDefault().getTaskList();
        String text = list.toString();
        sendText(exchange, text, 200);
    }

    public void getTaskByIdHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Task task = Managers.getDefault().findTask(Integer.parseInt(s));
            sendText(exchange, task.toString(), 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }

    public void createTaskHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);
        boolean isFindTask = Managers.getDefault().findTask(task.getTaskId()) == null;
        if (isFindTask) {
            Managers.getDefault().addTask(task);
            sendText(exchange, task.toString(), 201);
        } else {
            sendHasInteractions(exchange);
        }
    }

    public void updateTasksHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);
        Managers.getDefault().updateTask(task);
        sendText(exchange, task.toString(), 201);
    }

    public void deleteTasksHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Managers.getDefault().removeTask(Integer.parseInt(s));
            sendText(exchange, "Задача с Id:" + s + " удалена.", 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }
}
