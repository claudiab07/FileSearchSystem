package bussinessLayer;

import searchLayer.CachedSearcherProxy;
import searchLayer.SearchService;
import searchLayer.CorrectingSearchService;
import spellcheck.NorvigSpellingCorrector;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TextFilesConfigurator {
    private static String query;
    private static FileIndexer indexer = new FileIndexer();
    private static SearchService searcher;

    static {
        try {
            searcher = new CorrectingSearchService(new CachedSearcherProxy(), new NorvigSpellingCorrector());
        } catch (IOException e) {
            e.printStackTrace();
            searcher = new CachedSearcherProxy();
        }
    }


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


    public static Map<String, String> getSearchCache() {
        if (searcher instanceof CachedSearcherProxy proxy) {
            return proxy.getCache();
        }
        if (searcher instanceof CorrectingSearchService correcting) {
            SearchService inner = correcting.getDelegate();
            if (inner instanceof CachedSearcherProxy proxy) {
                return proxy.getCache();
            }
        }
        return null;
    }




    public static void setQuery(String query) {
        TextFilesConfigurator.query = query;
    }
}
