package bussinessLayer;

public class FileData {
    String filename;
    String content;
    String extension;
    long timestamp;
    String path;
    long fileSize;
    Double score;

    FileData(String filename, String content, String extension, long timestamp, String path, long fileSize, Double score) {
        this.filename = filename;
        this.content = content;
        this.extension = extension;
        this.timestamp = timestamp;
        this.path = path;
        this.fileSize = fileSize;
        this.score = score;
    }
}
