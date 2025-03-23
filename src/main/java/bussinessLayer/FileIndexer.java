package bussinessLayer;

import connection.ConnectionFactory;
import java.io.File;
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

        filesToInsert.add(new FileData(filename, content, extension, timestamp));
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

        String sql = "INSERT INTO files (filename, content, extension, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (FileData fileData : filesToInsert) {
                stmt.setString(1, fileData.filename);
                stmt.setString(2, fileData.content);
                stmt.setString(3, fileData.extension);
                stmt.setTimestamp(4, new java.sql.Timestamp(fileData.timestamp));
                stmt.addBatch();
            }

            int[] result = stmt.executeBatch();
            conn.commit();

            System.out.println("Batch insert complete. " + result.length + " records inserted.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            filesToInsert.clear();
        }
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
