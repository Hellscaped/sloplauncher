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
    public static MinecraftLauncher mclaunch;
    private static String ver = "1.2.5";
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
        vs.setSelectedIndex(Arrays.asList(clientVers).indexOf(LauncherProfile.selectedVersion));
        ver = LauncherProfile.selectedVersion;
        vs.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ver = vs.getSelectedItem().toString();
                        LauncherProfile.setVersion(ver);
                    }
                });
        statusPanel.add(vs, BorderLayout.EAST);
        topPanel.add(statusPanel, BorderLayout.NORTH);

        // Center panel - News/Updates section
        JPanel newsPanel = new JPanel(new BorderLayout());
        newsPanel.setBorder(BorderFactory.createTitledBorder("Latest Updates"));
        JTextArea newsArea = new JTextArea();
        newsArea.setText("Welcome to j5mclaunch!\n\n" +
                "This is a modern Minecraft launcher supporting versions from Alpha to Release 1.5.2.\n\n" +
                "Features:\n" +
                "- Microsoft account authentication\n" +
                "- Automatic asset and library downloads\n" +
                "- Betacraft proxy support for legacy online play\n" +
                "- Compatible with older Java versions\n\n" +
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
                        if (mclaunch.userName == "" || mclaunch.plrUuid == "") {
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
        mclaunch.refreshAuth();
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