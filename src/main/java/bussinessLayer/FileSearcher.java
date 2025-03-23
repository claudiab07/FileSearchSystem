package bussinessLayer;

import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileSearcher {
    public String getSearchResults(String query) {
        StringBuilder resultText = new StringBuilder();
        String sql = "SELECT filename, content, extension, timestamp FROM files WHERE filename ILIKE ? OR content ILIKE ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String filename = rs.getString("filename");
                String content = rs.getString("content");
                String extension = rs.getString("extension");
                String timestamp = rs.getTimestamp("timestamp").toString();

                String[] lines = content.split("\n");
                StringBuilder snippet = new StringBuilder();
                int lineCount = 0;

                for (String line : lines) {
                    snippet.append(line).append("\n");
                    lineCount++;
                    if (lineCount == 3) {
                        break;
                    }
                }

                resultText.append("FILE NAME: ").append(filename).append("\n")
                        .append("EXTENSION: ").append(extension).append("\n")
                        .append("LAST MODIFIED: ").append(timestamp).append("\n")
                        .append("CONTENT: ").append("\n")
                        .append(snippet.toString()).append("\n")
                        .append("-------------------------------------------end-of-file--\n\n");
            }

            if (resultText.length() == 0) {
                resultText.append("No matches found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultText.append("Error fetching data");
        }

        return resultText.toString();
    }
}
