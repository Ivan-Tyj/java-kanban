package http.handlers.subs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import manager.Managers;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TasksHttpHandler extends BaseHttpHandler implements HttpHandler {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void handle(HttpExchange exchange) {
        try {
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
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (NullPointerException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public void getTasksHandle(HttpExchange exchange) throws IOException {
        List<Task> list = Managers.getDefault().getTaskList();
        String text = gson.toJson(list);
        sendText(exchange, text, 200);
    }

    public void getTaskByIdHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Task task = Managers.getDefault().findTask(Integer.parseInt(s));
            String text = gson.toJson(task);
            sendText(exchange, text, 200);
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
            sendText(exchange, body, 201);
        } else {
            sendHasInteractions(exchange);
        }
    }

    public void updateTasksHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = gson.toJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        Task task = gson.fromJson(body, Task.class);
        Managers.getDefault().updateTask(task);
        sendText(exchange, body, 201);
    }

    public void deleteTasksHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Task task = Managers.getDefault().findTask(Integer.parseInt(s));
            boolean isFindTask = Managers.getDefault().findTask(task.getTaskId()) == null;
            if (isFindTask) {
                Managers.getDefault().removeTask(Integer.parseInt(s));
                sendText(exchange, "Задача с Id: " + s + " удалена.", 200);
            }
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }
}
