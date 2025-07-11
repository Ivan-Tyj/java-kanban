package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.handlers.subs.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String tasksPath = "/tasks";
    private static final String subTasksPath = "/subtasks";
    private static final String epicsPath = "/epics";
    private static final String historyPath = "/history";
    private static final String prioritizedPath = "/prioritized";

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext(tasksPath, new TasksHttpHandler());
        httpServer.createContext(subTasksPath, new SubHttpHandler());
        httpServer.createContext(epicsPath, new EpicHttpHandler());
        httpServer.createContext(historyPath, new HistoryHttpHandler());
        httpServer.createContext(prioritizedPath, new PrioritizedHttpHandler());
        httpServer.start();
    }
}
