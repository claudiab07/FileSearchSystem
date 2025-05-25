package searchLayer;

import bussinessLayer.FileResult;
import bussinessLayer.QueryParser;
import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RealFileSearcher implements SearchService {

    @Override
    public String getSearchResults(String rawQuery) {
        StringBuilder resultText = new StringBuilder();

        Map<String, String> queryMap = QueryParser.parseQuery(rawQuery);
        StringBuilder sql = new StringBuilder("SELECT * FROM files WHERE 1=1");
        List<String> paramValues = new ArrayList<>();

        if (queryMap.isEmpty()) {
            sql.append(" AND (filename ILIKE ? OR content ILIKE ?)");
            paramValues.add("%" + rawQuery + "%");
            paramValues.add("%" + rawQuery + "%");
        } else {
            if (queryMap.containsKey("path")) {
                sql.append(" AND path ILIKE ?");
                paramValues.add("%" + queryMap.get("path").replace("\\", "\\\\") + "%");
            }
            if (queryMap.containsKey("content")) {
                sql.append(" AND content ILIKE ?");
                paramValues.add("%" + queryMap.get("content") + "%");
            }
            if (queryMap.containsKey("filename")) {
                sql.append(" AND filename ILIKE ?");
                paramValues.add("%" + queryMap.get("filename") + "%");
            }
        }

        sql.append(" ORDER BY score DESC");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < paramValues.size(); i++) {
                stmt.setString(i + 1, paramValues.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                FileResult result = new FileResult(rs);
                resultText.append(result.format());
            }

            if (!hasResults) {
                resultText.append("No matches found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultText.append("Error fetching data");
        }

        return resultText.toString();
    }
}
