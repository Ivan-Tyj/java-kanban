package http.handlers.subs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHttpHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            getHistoryHandle(exchange);
        } else {
            System.out.println("Такого метода нет");
        }
    }

    public void getHistoryHandle(HttpExchange exchange) throws IOException {
        List<Task> list = manager.getHistory();
        String text = gson.toJson(list);
        sendText(exchange, text, 200);
    }

}
