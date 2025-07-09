package http.handlers.subs;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.Managers;
import tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubHttpHandler extends BaseHttpHandler implements HttpHandler {

    Gson gson;

    public SubHttpHandler(Gson gson) {
        super();
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] uriStrSplits = path.split("/");
        String url = uriStrSplits[1];
        if (url.equals("subtasks")) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    if (uriStrSplits.length > 2) {
                        getSubByIdHandle(exchange, uriStrSplits[2]);
                        break;
                    }
                    getSubsHandle(exchange);
                    break;
                case "POST":
                    if (uriStrSplits.length > 2) {
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

    }

    public void getSubsHandle(HttpExchange exchange) throws IOException {
        List<SubTask> list = Managers.getDefault().getSubTaskList();
        String text = list.toString();
        sendText(exchange, text, 200);
    }

    public void getSubByIdHandle(HttpExchange exchange, String s) throws IOException {
        try {
            SubTask subTask = Managers.getDefault().findSub(Integer.parseInt(s));
            sendText(exchange, subTask.toString(), 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }

    public void createSubHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        boolean isFindSub = Managers.getDefault().findSub(subTask.getTaskId()) == null;
        if (isFindSub) {
            Managers.getDefault().addSub(subTask);
            sendText(exchange, subTask.toString(), 201);
        } else {
            sendHasInteractions(exchange);
        }
    }

    public void updateSubHandle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        Managers.getDefault().updateSub(subTask);
        sendText(exchange, subTask.toString(), 201);
    }

    public void deleteSubHandle(HttpExchange exchange, String s) throws IOException {
        try {
            Managers.getDefault().removeSub(Integer.parseInt(s));
            sendText(exchange, "Подзадача с Id:" + s + " удалена.", 200);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        }
    }
}
