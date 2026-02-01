package org.j5mclaunch.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.plaf.FontUIResource;

import org.j5mclaunch.launcher.util.Helper;
import org.j5mclaunch.launcher.util.LauncherProfile;
import org.j5mclaunch.launcher.util.MinecraftLauncher;
import org.json.JSONObject;

public class Main {

    public static JFrame frame;
    public static JButton launch;
    static JLabel status;
    public static JButton login;
    static JCheckBox proxy;
    static JComboBox<String> vs;
    static JTextArea newsArea;
    static JPanel recentVersionsPanel;
    public static MinecraftLauncher mclaunch;
    private static String ver = "1.2.5";
    
    private static void showSettingsDialog() {
        JDialog settingsDialog = new JDialog(frame, "Settings", true);
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(frame);
        settingsDialog.setLayout(new BorderLayout());
        
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Memory allocation setting
        JPanel memoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memoryPanel.add(new JLabel("Memory Allocation (MB):"));
        JTextField memoryField = new JTextField(String.valueOf(LauncherProfile.memoryAllocation), 10);
        memoryPanel.add(memoryField);
        settingsPanel.add(memoryPanel);
        
        // Java arguments setting
        JPanel argsPanel = new JPanel(new BorderLayout());
        argsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        argsPanel.add(new JLabel("Java Arguments:"), BorderLayout.NORTH);
        JTextField argsField = new JTextField(LauncherProfile.javaArguments);
        argsPanel.add(argsField, BorderLayout.CENTER);
        settingsPanel.add(argsPanel);
        
        // Profile name setting
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.add(new JLabel("Profile Name:"));
        JTextField profileField = new JTextField(LauncherProfile.profileName, 15);
        profilePanel.add(profileField);
        settingsPanel.add(profilePanel);
        
        // Info label
        JLabel infoLabel = new JLabel("<html><i>Note: Changes are saved automatically</i></html>");
        settingsPanel.add(infoLabel);
        
        settingsDialog.add(settingsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                int memory = Integer.parseInt(memoryField.getText());
                if (memory < 256 || memory > 8192) {
                    JOptionPane.showMessageDialog(settingsDialog, 
                        "Memory allocation must be between 256 and 8192 MB", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LauncherProfile.setMemoryAllocation(memory);
                LauncherProfile.setJavaArguments(argsField.getText());
                LauncherProfile.setProfileName(profileField.getText());
                settingsDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(settingsDialog, 
                    "Please enter a valid number for memory allocation", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(saveButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> settingsDialog.dispose());
        buttonPanel.add(cancelButton);
        
        settingsDialog.add(buttonPanel, BorderLayout.SOUTH);
        settingsDialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        if (Helper.isOSX()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "j5mclaunch");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "j5mclaunch");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            java.util.Enumeration keys = UIManager.getDefaults().keys();
            FontUIResource f = new javax.swing.plaf.FontUIResource("Sansserif", Font.TRUETYPE_FONT,13);
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get (key);
                if (value instanceof javax.swing.plaf.FontUIResource)
                    UIManager.put (key, f);
            }
        } catch(Exception e) {
            System.out.println("Failed to set look and feel! :(");
        }
        System.out.println("-------System information------");
        System.out.println("java.vendor: "+System.getProperty("java.vendor"));
        System.out.println("java.version: "+System.getProperty("java.version"));
        System.out.println("java TLSv1.3: "+Helper.javaClientSupported());
        System.out.println("os.name: "+System.getProperty("os.name"));
        System.out.println("os.version: "+System.getProperty("os.version"));
        System.out.println("os.arch: "+System.getProperty("os.arch"));
        System.out.println("system ram: "+Helper.getRamAmount()+" Mb");
        System.out.println("-------------------------------\n");

        mclaunch = new MinecraftLauncher();
        LauncherProfile.loadProfile();
        frame = new JFrame("j5mclaunch");

        frame.setSize(600,400);
        frame.setName("j5mclaunch");
        frame.setTitle("Minecraft Launcher");
        try {
            frame.setIconImage(ImageIO.read(Main.class.getResource("/icon.png")));
        }catch(Exception ex) {
            System.out.println("Failed to set window icon! :(");
            System.out.println(ex);
        }
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(600, 400));
        frame.pack();
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> showSettingsDialog());
        fileMenu.add(settingsItem);
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame,
            "j5mclaunch v3.1\n\nA modern Minecraft launcher for legacy versions\n" +
            "Supporting versions from Alpha to Release 1.5.2\n\n" +
            "Created for compatibility with older Java versions",
            "About j5mclaunch",
            JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        frame.setJMenuBar(menuBar);

        mclaunch.setupMinecraftFolder();

        // Create main panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Status and version selector
        JPanel statusPanel = new JPanel(new BorderLayout());
        status = new JLabel("Please log in to play.");
        status.setFont(status.getFont().deriveFont(14.0f));
        statusPanel.add(status, BorderLayout.WEST);
        
        String[] clientVers = mclaunch.getClientVersions();
        vs = new JComboBox<String>(clientVers);
        vs.setPreferredSize(new Dimension(120, 25));
        vs.setSelectedIndex(3);
        int selectedIndex = Arrays.asList(clientVers).indexOf(LauncherProfile.selectedVersion);
        if (selectedIndex >= 0) {
            vs.setSelectedIndex(selectedIndex);
        }
        ver = LauncherProfile.selectedVersion;
        vs.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ver = vs.getSelectedItem().toString();
                        LauncherProfile.setVersion(ver);
                        updateVersionInfo(ver);
                        updateRecentVersionsPanel();
                    }
                });
        topPanel.add(statusPanel, BorderLayout.NORTH);
        
        // Add recent versions panel
        recentVersionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recentVersionsPanel.setBorder(BorderFactory.createTitledBorder("Recent Versions"));
        updateRecentVersionsPanel();
        topPanel.add(recentVersionsPanel, BorderLayout.SOUTH);

        // Center panel - News/Updates section
        JPanel newsPanel = new JPanel(new BorderLayout());
        newsPanel.setBorder(BorderFactory.createTitledBorder("Latest Updates & Version Info"));
        newsArea = new JTextArea();
        newsArea.setText("Welcome to j5mclaunch!\n\n" +
                "This is a modern Minecraft launcher supporting versions from Alpha to Release 1.5.2.\n\n" +
                "Features:\n" +
                "- Microsoft account authentication\n" +
                "- Automatic asset and library downloads\n" +
                "- Betacraft proxy support for legacy online play\n" +
                "- Compatible with older Java versions\n" +
                "- Memory allocation and Java arguments customization\n" +
                "- Profile management\n\n" +
                "Select a version from the dropdown above and click Launch to play!");
        newsArea.setEditable(false);
        newsArea.setLineWrap(true);
        newsArea.setWrapStyleWord(true);
        newsArea.setBackground(frame.getBackground());
        JScrollPane newsScroll = new JScrollPane(newsArea);
        newsPanel.add(newsScroll, BorderLayout.CENTER);
        centerPanel.add(newsPanel, BorderLayout.CENTER);

        // Bottom panel - Options and launch button
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        proxy = new JCheckBox("Betacraft Proxy");
        proxy.setToolTipText("Fixes skins, needed for online mode on b1.7.3");
        proxy.setSelected(LauncherProfile.betacraftProxy);
        proxy.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        LauncherProfile.setProxyEnabled(proxy.isSelected());
                    }
                });
        optionsPanel.add(proxy);
        bottomPanel.add(optionsPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        login = new JButton("Login");
        login.setPreferredSize(new Dimension(100, 30));
        login.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (mclaunch.userName.isEmpty() || mclaunch.plrUuid.isEmpty()) {
                            mclaunch.login();
                        }
                    }
                });
        login.setVisible(false);
        login.setEnabled(false);
        buttonPanel.add(login);

        launch = new JButton("Launch");
        launch.setPreferredSize(new Dimension(100, 30));
        launch.setVisible(false);
        launch.setEnabled(false);
        launch.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (mclaunch.downloadVersion(ver)) {
                            mclaunch.downloadLibraries();
                            mclaunch.downloadAssets();
                            mclaunch.launchGame(ver);
                        }
                    }
                });
        buttonPanel.add(launch);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        updateVersionInfo(ver);
        mclaunch.refreshAuth();
    }
    
    private static void updateVersionInfo(String version) {
        String info = "Selected Version: " + version + "\n\n";
        
        // Add version-specific information
        if (version.startsWith("a")) {
            info += "Alpha Version - Very early Minecraft version\n";
            info += "Note: Limited features and may have bugs\n";
        } else if (version.startsWith("b")) {
            info += "Beta Version - More stable than Alpha\n";
            info += "Note: Many classic features from this era\n";
        } else if (version.matches("1\\.[0-5].*")) {
            info += "Release Version - Official stable release\n";
            info += "Note: Fully featured classic Minecraft\n";
        }
        
        info += "\nMemory Allocated: " + LauncherProfile.memoryAllocation + " MB\n";
        info += "Profile: " + LauncherProfile.profileName + "\n";
        
        if (LauncherProfile.betacraftProxy) {
            info += "\n✓ Betacraft Proxy Enabled\n";
            info += "  (Skins and online mode support)\n";
        }
        
        info += "\n---\n\n";
        info += "Ready to launch! Click the Launch button to start playing.\n";
        info += "Use File → Settings to customize memory and Java arguments.";
        
        newsArea.setText(info);
    }
    
    private static void updateRecentVersionsPanel() {
        recentVersionsPanel.removeAll();
        java.util.List<String> recents = LauncherProfile.getRecentVersions();
        
        if (recents.isEmpty()) {
            recentVersionsPanel.add(new JLabel("No recent versions"));
        } else {
            for (final String version : recents) {
                JButton versionButton = new JButton(version);
                versionButton.setPreferredSize(new Dimension(80, 25));
                versionButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        vs.setSelectedItem(version);
                    }
                });
                recentVersionsPanel.add(versionButton);
            }
        }
        
        recentVersionsPanel.revalidate();
        recentVersionsPanel.repaint();
    }
    
    public static void setStatus(String txt) {
        status.setText(txt);
        status.repaint();
    }
    public static void setPlayEnabled() {
        login.setVisible(false);
        login.setEnabled(false);
        launch.setVisible(true);
        launch.setEnabled(true);
        frame.repaint();
    }
    public static void setPlayDisabled() {
        login.setVisible(true);
        login.setEnabled(true);
        launch.setVisible(false);
        launch.setEnabled(false);
        frame.repaint();
    }
}