package org.j5mclaunch.launcher.util;

import org.j5mclaunch.launcher.Main;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class LauncherProfile {
    private static final String mcHome = Main.mclaunch.getMinecraftFolder();

    public static boolean betacraftProxy = false;
    public static String selectedVersion = "1.2.5";
    public static int memoryAllocation = 1024; // MB
    public static String javaArguments = "";
    public static String profileName = "Default";
    private static List<String> recentVersions = new ArrayList<String>();
    private static final int MAX_RECENT_VERSIONS = 5;

    public static void loadProfile() {
        File info = new File(mcHome+"/j5mclaunch-profile.json");
        if (info.exists()) {
            try {
                Scanner a = new Scanner(info);
                String json = "";
                while (a.hasNextLine()) {
                    json += "\n"+a.nextLine();
                }
                JSONObject b = new JSONObject(json);
                if (b.has("proxy")) {
                    betacraftProxy = b.getBoolean("proxy");
                }
                if (b.has("ver")) {
                    selectedVersion = b.getString("ver");
                }
                if (b.has("memory")) {
                    memoryAllocation = b.getInt("memory");
                }
                if (b.has("javaArgs")) {
                    javaArguments = b.getString("javaArgs");
                }
                if (b.has("profileName")) {
                    profileName = b.getString("profileName");
                }
                if (b.has("recentVersions")) {
                    JSONArray recents = b.getJSONArray("recentVersions");
                    recentVersions.clear();
                    for (int i = 0; i < recents.length(); i++) {
                        recentVersions.add(recents.getString(i));
                    }
                }
            } catch(Exception ignored) {
                System.out.println("Failed to load profile settings");
            }
        }
    }
    
    public static void saveProfile() {
        try {
            FileWriter file = new FileWriter(mcHome+"/j5mclaunch-profile.json");
            JSONObject json = new JSONObject();
            json.put("proxy",betacraftProxy);
            json.put("ver",selectedVersion);
            json.put("memory",memoryAllocation);
            json.put("javaArgs",javaArguments);
            json.put("profileName",profileName);
            
            JSONArray recents = new JSONArray();
            for (String version : recentVersions) {
                recents.put(version);
            }
            json.put("recentVersions", recents);
            
            file.write(json.toString());
            file.close();
        } catch(Exception ignored) {
            System.out.println("Failed to save profile settings");
        }
    }
    
    public static void setVersion(String str) {
        selectedVersion = str;
        addRecentVersion(str);
        saveProfile();
    }

    public static void setProxyEnabled(boolean str) {
        betacraftProxy = str;
        saveProfile();
    }
    
    public static void setMemoryAllocation(int mb) {
        memoryAllocation = mb;
        saveProfile();
    }
    
    public static void setJavaArguments(String args) {
        javaArguments = args;
        saveProfile();
    }
    
    public static void setProfileName(String name) {
        profileName = name;
        saveProfile();
    }
    
    private static void addRecentVersion(String version) {
        // Remove if already exists to avoid duplicates
        recentVersions.remove(version);
        // Add to front of list
        recentVersions.add(0, version);
        // Keep only MAX_RECENT_VERSIONS
        if (recentVersions.size() > MAX_RECENT_VERSIONS) {
            recentVersions = new ArrayList<String>(recentVersions.subList(0, MAX_RECENT_VERSIONS));
        }
    }
    
    public static List<String> getRecentVersions() {
        return new ArrayList<String>(recentVersions);
    }
}
