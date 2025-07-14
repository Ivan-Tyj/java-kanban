package http.handlers.subs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class EpicHttpHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHttpHandler(TaskManager manager) {
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
                    if (uriStrSplits.length == 4) {
                        getEpicSubsHandle(exchange, uriStrSplits[2]);
                        break;
                    }
                    if (uriStrSplits.length == 3) {
                        getEpicByIdHandle(exchange, uriStrSplits[2]);
                        break;
                    }
                    getEpicsHandle(exchange);
                    break;
                case "POST":
                    createEpicHandle(exchange);
                    break;
                case "DELETE":
                    deleteEpicHandle(exchange, uriStrSplits[2]);
                    break;
                default:
                    System.out.println("Такого метода нет");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (NullPointerException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        }

    }

    public void getEpicsHandle(HttpExchange exchange) throws IOException {
        List<Epic> list = manager.getEpicTaskList();
        String text = gson.toJson(list);
        sendText(exchange, text, 200);
    }

    public void getEpicByIdHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Epic epic = manager.findEpic(Integer.parseInt(s));
            String text = gson.toJson(epic);
            sendText(exchange, text, 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }

    public void getEpicSubsHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Epic epic = manager.findEpic(Integer.parseInt(s));
            List<SubTask> list = epic.getSubForEpicList();
            String text = gson.toJson(list);
            sendText(exchange, text, 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }

    public void createEpicHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        if (manager.findEpic(epic.getTaskId()) == null) {
            manager.addEpic(epic);
            sendText(exchange, body, 201);
        } else {
            sendHasInteractions(exchange);
        }
    }

    public void deleteEpicHandle(HttpExchange exchange, String s) throws IOException {
        try {
            manager.removeEpic(Integer.parseInt(s));
            sendText(exchange, "Epic с Id:" + s + " удален.", 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }
}
