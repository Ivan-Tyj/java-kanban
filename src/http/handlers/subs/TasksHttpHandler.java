package http.handlers.subs;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHttpHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            URI requestURI = exchange.getRequestURI();
            String path = requestURI.getPath();
            String[] uriStrSplits = path.split("/");
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void getTasksHandle(HttpExchange exchange) throws IOException {
        List<Task> list = manager.getTaskList();
        String text = gson.toJson(list);
        sendText(exchange, text, 200);
    }

    public void getTaskByIdHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Task task = manager.findTask(Integer.parseInt(s));
            String text = gson.toJson(task);
            sendText(exchange, text, 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }

    public void createTaskHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        try {
            Task task = gson.fromJson(body, Task.class);
            if (manager.findTask(task.getTaskId()) == null) {
                manager.addTask(task);
                sendText(exchange, body, 201);
            } else {
                sendHasInteractions(exchange);
            }
        } catch (JsonSyntaxException e) {
            e.getMessage();
        }
    }

    public void updateTasksHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = gson.toJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        Task task = gson.fromJson(body, Task.class);
        manager.updateTask(task);
        sendText(exchange, body, 201);
    }

    public void deleteTasksHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Task task = manager.findTask(Integer.parseInt(s));
            boolean isFindTask = manager.findTask(task.getTaskId()) == null;
            if (isFindTask) {
                manager.removeTask(Integer.parseInt(s));
                sendText(exchange, "Задача с Id: " + s + " удалена.", 200);
            }
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }
}
