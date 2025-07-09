package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import http.handlers.subs.*;
import manager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String tasksPath = "/tasks";
    private static final String subTasksPath = "/subtasks";
    private static final String epicsPath = "/epics";
    private static final String historyPath = "/history";
    private static final String prioritizedPath = "/prioritized";
    static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext(tasksPath, new TasksHttpHandler(gson));
        httpServer.createContext(subTasksPath, new SubHttpHandler(gson));
        httpServer.createContext(epicsPath, new EpicHttpHandler(gson));
        httpServer.createContext(historyPath, new HistoryHttpHandler());
        httpServer.createContext(prioritizedPath, new PrioritizedHttpHandler());
        httpServer.start();
    }
}
