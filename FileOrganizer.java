package files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class FileOrganizer {
    private static final Map<String, String> FILE_TYPES = new HashMap<>();
    private static final String TRASH_FOLDER = "Trash";

    static {
        FILE_TYPES.put(".jpg", "Images");
        FILE_TYPES.put(".jpeg", "Images");
        FILE_TYPES.put(".png", "Images");
        FILE_TYPES.put(".gif", "Images");
        FILE_TYPES.put(".bmp", "Images");
        FILE_TYPES.put(".svg", "Images");

        FILE_TYPES.put(".pdf", "Documents");
        FILE_TYPES.put(".docx", "Documents");
        FILE_TYPES.put(".doc", "Documents");
        FILE_TYPES.put(".txt", "Documents");
        FILE_TYPES.put(".xlsx", "Documents");
        FILE_TYPES.put(".pptx", "Documents");

        FILE_TYPES.put(".mp3", "Audio");
        FILE_TYPES.put(".wav", "Audio");
        FILE_TYPES.put(".aac", "Audio");
        FILE_TYPES.put(".flac", "Audio");

        FILE_TYPES.put(".mp4", "Videos");
        FILE_TYPES.put(".mov", "Videos");
        FILE_TYPES.put(".avi", "Videos");
        FILE_TYPES.put(".mkv", "Videos");

        FILE_TYPES.put(".zip", "Archives");
        FILE_TYPES.put(".rar", "Archives");
        FILE_TYPES.put(".7z", "Archives");
        FILE_TYPES.put(".tar", "Archives");
        FILE_TYPES.put(".gz", "Archives");

        FILE_TYPES.put(".dmg", "Programs");
        FILE_TYPES.put(".sh", "Programs");
        FILE_TYPES.put(".deb", "Programs");

        FILE_TYPES.put(".py", "Code");
        FILE_TYPES.put(".cpp", "Code");
        FILE_TYPES.put(".html", "Code");
        FILE_TYPES.put(".css", "Code");
        FILE_TYPES.put(".js", "Code");
        FILE_TYPES.put(".java", "Code");
    }

    public static void organizeFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Directory does not exist.");
            return;
        }

        for (String folder : FILE_TYPES.values()) {
            new File(directoryPath + File.separator + folder).mkdirs();
        }
        new File(directoryPath + File.separator + TRASH_FOLDER).mkdirs();

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files to organize.");
            return;
        }

        int totalFiles = files.length;
        int filesMoved = 0;

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                int lastDotIndex = fileName.lastIndexOf(".");
                String fileExtension = (lastDotIndex != -1) ? fileName.substring(lastDotIndex) : "";
                String targetFolder = FILE_TYPES.getOrDefault(fileExtension, "Others");
                File targetDir = new File(directoryPath + File.separator + targetFolder);
                
                try {
                    Files.move(file.toPath(), Path.of(targetDir.getPath(), fileName), StandardCopyOption.REPLACE_EXISTING);
                    filesMoved++;
                    printProgressBar(filesMoved, totalFiles);
                } catch (IOException e) {
                    System.out.println("Failed to move " + fileName + ". Moving to Trash.");
                    moveToTrash(file, directoryPath);
                }
            }
        }
        System.out.println("\nFile organization complete.");
    }

    private static void moveToTrash(File file, String directoryPath) {
        File trashDir = new File(directoryPath + File.separator + TRASH_FOLDER);
        try {
            Files.move(file.toPath(), Path.of(trashDir.getPath(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println(file.getName() + " moved to Trash.");
        } catch (IOException e) {
            System.out.println("Failed to move to Trash: " + e.getMessage());
        }
    }

    private static void printProgressBar(int filesMoved, int totalFiles) {
        int progress = (int) ((double) filesMoved / totalFiles * 50);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 50; i++) {
            if (i < progress) {
                bar.append("#");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");
        System.out.print("\r" + bar + " " + filesMoved + "/" + totalFiles + " files moved");
    }

    public static void main(String[] args) {
        try {
            System.out.print("Enter the directory path to organize: ");
            byte[] buffer = new byte[1024];
            int length = System.in.read(buffer);
            String path = new String(buffer, 0, length).trim();
            organizeFiles(path);
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }
}
