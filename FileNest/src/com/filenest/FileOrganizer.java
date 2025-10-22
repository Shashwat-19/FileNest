package com.filenest;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@SuppressWarnings("unused")
public class FileOrganizer {
    private static final Map<String, String> FILE_TYPES = new HashMap<>();
    private final Stack<Map<Path, Path>> moveHistory = new Stack<>();
    
    // Statistics tracking variables
    private int totalFilesMoved = 0;
    private final Map<String, Integer> fileTypeCounts = new HashMap<>();
    private long totalSpaceFreed = 0;

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
        panel.setBorder(BorderFactory.createEmptyBorder(60, 30, 30, 30));

        JLabel headerLabel = new JLabel("Drag & Drop a Folder or Choose Manually", JLabel.CENTER);
        headerLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);

        JButton organizeButton = createStyledButton("Choose Folder");
        JButton undoButton = createStyledButton("Undo Last Action");

        buttonPanel.add(organizeButton, gbc);
        buttonPanel.add(undoButton, gbc);

        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        organizeButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                organizeFiles(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        undoButton.addActionListener(e -> undoLastAction());

        // Drag and Drop Support
        new DropTarget(panel, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {}
            @Override
            public void dragOver(DropTargetDragEvent dtde) {}
            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {}
            @Override
            public void dragExit(DropTargetEvent dte) {}
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (!droppedFiles.isEmpty() && droppedFiles.get(0).isDirectory()) {
                        organizeFiles(droppedFiles.get(0).getAbsolutePath());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    public void organizeFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Directory does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Reset stats for the new session
        int filesMoved = 0;
        long spaceFreed = 0;
        Map<String, Integer> currentFileTypeCounts = new HashMap<>();
    
        Map<Path, Path> movedFiles = new HashMap<>();
    
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String ext = file.getName().replaceAll(".*\\.", ".");
                String folder = FILE_TYPES.getOrDefault(ext, "Others");
                File targetDir = new File(directoryPath, folder);
                targetDir.mkdirs();
    
                Path source = file.toPath();
                Path target = targetDir.toPath().resolve(file.getName());
                try {
                    long fileSize = file.length();
                    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                    movedFiles.put(target, source);
    
                    // Update session stats (not global stats)
                    filesMoved++;
                    spaceFreed += fileSize;
                    currentFileTypeCounts.put(folder, currentFileTypeCounts.getOrDefault(folder, 0) + 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    
        if (!movedFiles.isEmpty()) {
            moveHistory.push(movedFiles);
        }
    
        showStatistics(filesMoved, spaceFreed, currentFileTypeCounts);
        JOptionPane.showMessageDialog(null, "File organization complete.");
    }

    private void showStatistics(int filesMoved, long spaceFreed, Map<String, Integer> currentFileTypeCounts) {
        StringBuilder stats = new StringBuilder("<html><b>Statistics Dashboard</b><br>");
        stats.append("Files Moved: ").append(filesMoved).append("<br>");
        stats.append("Space Freed: ").append(spaceFreed / 1024).append(" KB<br>");
        stats.append("<b>File Types Organized:</b><br>");
        
        currentFileTypeCounts.forEach((key, value) -> 
            stats.append(key).append(": ").append(value).append("<br>")
        );
    
        stats.append("</html>");
    
        JOptionPane.showMessageDialog(null, new JLabel(stats.toString()), "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void undoLastAction() {
        if (moveHistory.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No actions to undo.");
            return;
        }
        // movement of the files from the target to the source
        Map<Path, Path> lastMove = moveHistory.pop();
        lastMove.forEach((target, source) -> {
            try {
                Files.move(target, source, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        JOptionPane.showMessageDialog(null, "Last action undone.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileOrganizer::new);
    }
}
