package http.handlers.subs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.BaseHttpHandler;
import manager.Managers;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HistoryHttpHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] uriStrSplits = path.split("/");
        String url = uriStrSplits[1];
        if (url.equals("history")) {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    getHistoryHandle(exchange);
                    break;
                default:
                    System.out.println("Такого метода нет");
            }
        }
    }

    public void getHistoryHandle(HttpExchange exchange) throws IOException {
        List<Task> list = Managers.getDefaultHistory().getHistory();
        String text = list.toString();
        sendText(exchange, text, 200);
    }

}
