package bussinessLayer;

import java.io.File;

public class TextFilesConfigurator {
    private static String query;
    private static FileIndexer indexer = new FileIndexer();
    private static FileSearcher searcher = new FileSearcher();

    public TextFilesConfigurator() {
    }

    public static void configureTextFiles() {
        indexer.clearDatabase();

        String dirPath = "C:\\Users\\claud\\OneDrive\\Documente\\Facultate";
        File dir = new File(dirPath);

        indexFilesRecursively(dir, indexer);
        indexer.insertFiles();

    }

    private static void indexFilesRecursively(File dir, FileIndexer indexer) {
        File[] filesAndDirs = dir.listFiles();

        if (filesAndDirs != null) {
            for (File fileOrDir : filesAndDirs) {
                if (fileOrDir.isFile() && fileOrDir.getName().endsWith(".txt")) {
                    indexer.indexFile(fileOrDir);
                } else if (fileOrDir.isDirectory()) {
                    indexFilesRecursively(fileOrDir, indexer);
                }
            }
        }
    }

    public static String getQuery() {
        return searcher.getSearchResults(query);
    }

    public static void setQuery(String query) {
        TextFilesConfigurator.query = query;
    }
}
