package bussinessLayer;

public class FileData {
    String filename;
    String content;
    String extension;
    long timestamp;

    FileData(String filename, String content, String extension, long timestamp) {
        this.filename = filename;
        this.content = content;
        this.extension = extension;
        this.timestamp = timestamp;
    }
}