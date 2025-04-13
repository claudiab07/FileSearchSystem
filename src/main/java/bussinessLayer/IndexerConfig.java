package bussinessLayer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class IndexerConfig {
    private static Properties config = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("indexerConfig")) {
            config.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getReportFormat() {
        return config.getProperty("report.format", "TEXT");
    }

    public static String getReportFilepath() {
        return config.getProperty("report.filepath", "index_report.txt");
    }

    public static int getSearchCacheSize() { return Integer.parseInt(config.getProperty("report.cacheSize", String.valueOf(10)));}
}
