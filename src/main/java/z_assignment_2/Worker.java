package z_assignment_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Worker {
    private final String directory;

    public Worker(String directory) {
        this.directory = directory;
    }

    public String searchFiles(String query) {
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Directory does not exist");
            return "";
        }

        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println("No files in directory " + directory);
            return "";
        }

        StringBuilder resultBuilder = new StringBuilder();

        for (File file : files) {
            if (file.isFile()) {
                String contentSnippet = getFileContentSnippet(file, query);
                if (!contentSnippet.isEmpty()) {
                    resultBuilder.append(formatResult(file.getName(), contentSnippet));
                }
            }
        }

        return resultBuilder.toString();
    }

    private String getFileContentSnippet(File file, String query) {
        StringBuilder snippet = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(query)) {
                    snippet.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snippet.toString();
    }

    private String formatResult(String filename, String snippet) {
        return "FILE NAME: " +  "\n"
                + "CONTENT: \n" + snippet + "\n-------------------------------------------end-of-file--\n";
    }

}
