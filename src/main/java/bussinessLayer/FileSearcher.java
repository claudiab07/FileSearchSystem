package bussinessLayer;

import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FileSearcher {
    private Map<String, String> searchCache;
    private int cacheSize;

    public FileSearcher() {
        this.cacheSize = IndexerConfig.getSearchCacheSize();
        this.searchCache = new LinkedHashMap<>(cacheSize, 0.75f, true);
    }

    public String getSearchResults(String rawQuery) {
        StringBuilder resultText = new StringBuilder();

        if (searchCache.containsKey(rawQuery)) {
            resultText.append("Cache hit!\n");
            resultText.append(searchCache.get(rawQuery));
            return resultText.toString();
        }

        Map<String, String> queryMap = QueryParser.parseQuery(rawQuery);

        StringBuilder sql = new StringBuilder("SELECT * FROM files WHERE 1=1");
        List<String> paramValues = new ArrayList<>();

        if (queryMap.containsKey("path")) {
            sql.append(" AND path ILIKE ?");
//            System.out.println("path =" + queryMap.get("path"));
            paramValues.add("%" + queryMap.get("path").replace("\\", "\\\\") + "%");
        }
        if (queryMap.containsKey("content")) {
            sql.append(" AND content ILIKE ?");
//            System.out.println("content =" + queryMap.get("content"));
            paramValues.add("%" + queryMap.get("content") + "%");
        }
        if (queryMap.containsKey("filename")) {
            sql.append(" AND filename ILIKE ?");
//            System.out.println("filename =" + queryMap.get("filename"));
            paramValues.add("%" + queryMap.get("filename") + "%");
        }

        sql.append(" ORDER BY score DESC");

//        System.out.println(" SQL " + sql);
//        System.out.println("PARAMS " + paramValues);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < paramValues.size(); i++) {
                stmt.setString(i + 1, paramValues.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            boolean hasResults = false;
            StringBuilder queryResult = new StringBuilder();

            while (rs.next()) {
                hasResults = true;
                FileResult result = new FileResult(rs);
                resultText.append(result.format());
            }

            if (!hasResults) {
                resultText.append("No matches found");
            }

            if (searchCache.size() >= cacheSize) {
                Iterator<String> iterator = searchCache.keySet().iterator();
                if (iterator.hasNext()) {
                    iterator.next();
                    iterator.remove();
                }
            }

            searchCache.put(rawQuery, queryResult.toString());

            resultText.append(queryResult.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            resultText.append("Error fetching data");
        }


        return resultText.toString();
    }

    public Map<String, String> getSearchCache() {
        return searchCache;
    }
}
