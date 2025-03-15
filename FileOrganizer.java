package files;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
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

    public FileOrganizer() {
        JFrame frame = new JFrame("File Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(20, 20));
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 30, 30, 30)); // Increased top padding

        JLabel headerLabel = new JLabel("Choose the Folder to Organize", JLabel.CENTER);
        headerLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Keep it centered
        panel.add(headerLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 30))); // Extra space below text

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        JButton organizeButton = new JButton("Choose Folder");
        organizeButton.setPreferredSize(new Dimension(200, 50));
        organizeButton.setBackground(new Color(0, 123, 255));
        organizeButton.setForeground(Color.BLACK);
        organizeButton.setFocusPainted(false);
        organizeButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        buttonPanel.add(organizeButton, gbc);

        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        organizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(null);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedDir = fileChooser.getSelectedFile();
                    organizeFiles(selectedDir.getAbsolutePath());
                }
            }
        });
    }

    public void organizeFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Directory does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (String folder : FILE_TYPES.values()) {
            new File(directoryPath + File.separator + folder).mkdirs();
        }
        new File(directoryPath + File.separator + TRASH_FOLDER).mkdirs();

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(null, "No files to organize.");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                int lastDotIndex = fileName.lastIndexOf(".");
                String fileExtension = (lastDotIndex != -1) ? fileName.substring(lastDotIndex) : "";
                String targetFolder = FILE_TYPES.getOrDefault(fileExtension, "Others");
                File targetDir = new File(directoryPath + File.separator + targetFolder);
                try {
                    Files.move(file.toPath(), Path.of(targetDir.getPath(), fileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    moveToTrash(file, directoryPath);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "File organization complete.");
    }

    private void moveToTrash(File file, String directoryPath) {
        File trashDir = new File(directoryPath + File.separator + TRASH_FOLDER);
        try {
            Files.move(file.toPath(), Path.of(trashDir.getPath(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to move to Trash: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileOrganizer::new);
    }
}
