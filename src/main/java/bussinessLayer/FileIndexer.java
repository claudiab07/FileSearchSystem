package bussinessLayer;

import connection.ConnectionFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileIndexer {
    private List<FileData> filesToInsert;

    public FileIndexer() {
        filesToInsert = new ArrayList<>();
    }

    public void indexFile(File file) {
        String content = FileReaderUtil.readFileContent(file);
        if (content.isEmpty()) return;

        String filename = getFilenameWithoutExtension(file);
        String extension = getFileExtension(file);
        long timestamp = file.lastModified();
        String path = file.getAbsolutePath();
        long fileSize = file.length();

        Double score = claculateScore(file, content);

        filesToInsert.add(new FileData(filename, content, extension, timestamp, path, fileSize, score));
    }

    private Double claculateScore(File file, String content) {
        long age = System.currentTimeMillis() - file.lastModified();
        double ageScore = Math.max(0, 14 - (age / (1000.0 * 60 * 60 * 24)));

        int pathDepth = file.getPath().split("[\\\\/]").length;
        double pathScore = Math.max(0, 5 - pathDepth);

        double lengthScore = scoreFileByContentLength(content);

        double score = ageScore + pathScore + lengthScore;

        return score;

    }

    private double scoreFileByContentLength(String content) {
        int length = content.length();

        if (length < 10) {
            return 0;
        } else if (length < 500) {
            return 10;
        } else if (length < 1000) {
            return 10;
        } else if (length < 1300) {
            return 3;
        } else if (length < 3000) {
            return 1;
        }
        return 0;
    }



    private String getFilenameWithoutExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf(".");
        return (lastIndex == -1) ? name : name.substring(0, lastIndex);
    }


    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf(".");
        return (lastIndex == -1) ? "" : name.substring(lastIndex);
    }


    public void insertFiles() {
        if (filesToInsert.isEmpty()) return;

        String sql = "INSERT INTO files (filename, content, extension, timestamp, path, filesize, score) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (FileData fileData : filesToInsert) {
                stmt.setString(1, fileData.filename);
                stmt.setString(2, fileData.content);
                stmt.setString(3, fileData.extension);
                stmt.setTimestamp(4, new java.sql.Timestamp(fileData.timestamp));
                stmt.setString(5, fileData.path);
                stmt.setLong(6, fileData.fileSize);
                stmt.setDouble(7, fileData.score);

                stmt.addBatch();
            }

            int[] result = stmt.executeBatch();
            conn.commit();

            System.out.println("Batch insert complete. " + result.length + " records inserted.");

            generateReport(result.length);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            filesToInsert.clear();
        }
    }

    private void generateReport(int length) {
        String reportFormat = IndexerConfig.getReportFormat();
        String reportFilepath = IndexerConfig.getReportFilepath();

        try (FileWriter writer = new FileWriter(reportFilepath)) {
            if ("TEXT".equalsIgnoreCase(reportFormat)) {
                writeTextReport(writer, length);
            } else if ("CSV".equalsIgnoreCase(reportFormat)) {
                writeCSVReport(writer, length);
            } else {
                writer.write("Unknown report format.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTextReport(FileWriter writer, int length) throws IOException {
        writer.write("Indexing Report\n");
        writer.write("Total Files Indexed: " + length + "\n");
        writer.write("Files Indexed Successfully: " + filesToInsert.size() + "\n");
        writer.write("Timestamp: " + new java.util.Date() + "\n");
    }

    private void writeCSVReport(FileWriter writer, int length) throws IOException {
        writer.write("Indexing Report\n");
        writer.write("Total Files Indexed, Files Indexed Successfully, Timestamp\n");
        writer.write(length + "," + filesToInsert.size() + "," + new java.util.Date() + "\n");
    }

    public void clearDatabase() {
        String sql = "DELETE FROM files";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to clear database.");
        }
    }
}
