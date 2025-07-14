package http.handlers.subs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubHttpHandler extends BaseHttpHandler implements HttpHandler {

    public SubHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] uriStrSplits = path.split("/");
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (uriStrSplits.length == 3) {
                    getSubByIdHandle(exchange, uriStrSplits[2]);
                    break;
                }
                getSubsHandle(exchange);
                break;
            case "POST":
                if (uriStrSplits.length == 3) {
                    updateSubHandle(exchange);
                    break;
                }
                createSubHandle(exchange);
                break;
            case "DELETE":
                deleteSubHandle(exchange, uriStrSplits[2]);
                break;
            default:
                System.out.println("Такого метода нет");
        }
    }

    public void getSubsHandle(HttpExchange exchange) throws IOException {
        List<SubTask> list = manager.getSubTaskList();
        String text = gson.toJson(list);
        sendText(exchange, text, 200);
    }

    public void getSubByIdHandle(HttpExchange exchange, String s) throws IOException {
        try {
            SubTask subTask = manager.findSub(Integer.parseInt(s));
            String text = gson.toJson(subTask);
            sendText(exchange, text, 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }

    public void createSubHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        if (manager.findSub(subTask.getTaskId()) == null) {
            manager.addSub(subTask);
            sendText(exchange, body, 201);
        } else {
            sendHasInteractions(exchange);
        }
    }

    public void updateSubHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        manager.updateSub(subTask);
        sendText(exchange, body, 201);
    }

    public void deleteSubHandle(HttpExchange exchange, String s) throws IOException {
        try {
            manager.removeSub(Integer.parseInt(s));
            sendText(exchange, "Подзадача с Id:" + s + " удалена.", 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }
}
