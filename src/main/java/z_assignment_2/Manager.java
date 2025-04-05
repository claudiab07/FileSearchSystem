package z_assignment_2;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Manager {
    private String query;
    private List<String> searchResults = new ArrayList<>();
    private final Map<String, String> resultCache = new HashMap<>();
    private static final int[] workerPorts = {8001, 8002, 8003};

    public Manager() {}

    public void performSearch() {
        if (query == null || query.isEmpty()) {
            return;
        }

        if (resultCache.containsKey(query)) {
            System.out.println("Cache hit!");
            searchResults = new ArrayList<>(Arrays.asList(resultCache.get(query).split("\n\n")));
            return;
        }

        searchResults.clear();

        for (int port : workerPorts) {
            try {
                String result = sendQueryToWorker(port, query);
                if (!result.isEmpty()) {
                    searchResults.add(result);
                }
            } catch (Exception e) {
                System.out.println("Worker on port " + port + " is not responding.");
            }
        }

        if (!searchResults.isEmpty()) {
            String cachedResult = String.join("\n\n", searchResults);
            resultCache.put(query, cachedResult);
        }
    }

    private String sendQueryToWorker(int port, String query) throws Exception {
        URL url = new URL("http://localhost:" + port + "/search");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(query.getBytes());
        }

        Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
        String result = scanner.hasNext() ? scanner.next() : "";

        return result;
    }


    public String getQuery() {
        if (searchResults.isEmpty()) {
            return "No results found";
        }

        StringBuilder resultString = new StringBuilder();
        for (String result : searchResults) {
            resultString.append(result).append("\n\n");
        }

        return resultString.toString();
    }

    public void setQuery(String query) {
        this.query = query;
        performSearch();
    }

}