package bussinessLayer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileResult {
    private final String filename;
    private final String content;
    private final String extension;
    private final String timestamp;
    private final double score;

    public FileResult(ResultSet rs) throws SQLException {
        this.filename = rs.getString("filename");
        this.content = rs.getString("content");
        this.extension = rs.getString("extension");
        this.timestamp = rs.getTimestamp("timestamp").toString();
        this.score = rs.getDouble("score");
    }

    public String format() {
        String[] lines = content.split("\n");
        StringBuilder snippet = new StringBuilder();

        for (int i = 0; i < Math.min(3, lines.length); i++) {
            snippet.append(lines[i]).append("\n");
        }

        return "FILE NAME: " + filename + "\n" +
                "EXTENSION: " + extension + "\n" +
                "LAST MODIFIED: " + timestamp + "\n" +
                "SCORE: " + score + "\n" +
                "CONTENT:\n" +
                snippet.toString() +
                "-------------------------------------------end-of-file--\n\n";
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public String getExtension() {
        return extension;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getScore() {
        return score;
    }
}
