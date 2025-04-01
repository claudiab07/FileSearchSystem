package z_assignment_2;


import java.util.*;

public class Manager {
    private String query;
    private static final Worker[] workers = new Worker[3];
    private List<String> searchResults = new ArrayList<>();

    public Manager() {
        initializeWorkers();
    }


    public void initializeWorkers() {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("C:\\Users\\claud\\OneDrive\\Documente\\Facultate\\An 3 Sem2\\SD\\Data\\folder" + (i + 1));
        }
    }

    public void performSearch() {
        if (query == null || query.isEmpty()) {
            return;
        }

        searchResults.clear();

        for (Worker worker : workers) {
            String result = worker.searchFiles(query);
            if (!result.isEmpty()) {
                System.out.println("RESULT " + result);
                searchResults.add(result);
            } else{
                System.out.println("No result for this worker");
            }
        }
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
