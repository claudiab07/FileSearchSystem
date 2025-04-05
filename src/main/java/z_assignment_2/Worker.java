package z_assignment_2;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;


import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Worker {
    private final String directory;
    private final int port;

    public Worker(String directory, int port) {
        this.directory = directory;
        this.port = port;
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/search", this::handleSearchRequest);
        server.setExecutor(null);
        server.start();
        System.out.println("Worker started on port " + port);
    }

    private void handleSearchRequest(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String query = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String result = searchFiles(query.trim());

        byte[] responseBytes = result.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    public String searchFiles(String query) {
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Directory does not exist: " + directory);
            return "";
        }

        StringBuilder resultBuilder = new StringBuilder();
        searchFilesRecursively(dir, query, resultBuilder);
        return resultBuilder.toString();
    }

    private void searchFilesRecursively(File dir, String query, StringBuilder resultBuilder) {
        File[] filesAndDirs = dir.listFiles();
        if (filesAndDirs != null) {
            for (File fileOrDir : filesAndDirs) {
                if (fileOrDir.isDirectory()) {
                    searchFilesRecursively(fileOrDir, query, resultBuilder);
                } else if (fileOrDir.isFile() && fileOrDir.getName().endsWith(".txt")) {
                    String contentSnippet = getFileContentSnippet(fileOrDir, query);
                    if (!contentSnippet.isEmpty()) {
                        resultBuilder.append(formatResult(fileOrDir.getName(), contentSnippet));
                    }
                }
            }
        }
    }


    private String getFileContentSnippet(File file, String query) {
        List<String> matchingLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(query.toLowerCase())) {
                    matchingLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder snippet = new StringBuilder();
        for (int i = 0; i < Math.min(3, matchingLines.size()); i++) {
            snippet.append(matchingLines.get(i)).append("\n");
        }
        return snippet.toString();
    }


    private String formatResult(String filename, String snippet) {
        return "FILE NAME: " +  "\n"
                + "CONTENT: \n" + snippet + "\n-------------------------------------------end-of-file--\n";
    }

}