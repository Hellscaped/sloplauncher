package org.j5mclaunch.launcher.util;

import org.j5mclaunch.launcher.Main;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class LauncherProfile {
    private static final String mcHome = Main.mclaunch.getMinecraftFolder();

    public static boolean betacraftProxy = false;
    public static String selectedVersion = "1.2.5";
    public static int memoryAllocation = 1024; // MB
    public static String javaArguments = "";
    public static String profileName = "Default";

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
            file.write(json.toString());
            file.close();
        } catch(Exception ignored) {
            System.out.println("Failed to save profile settings");
        }
    }
    public static void setVersion(String str) {
        selectedVersion = str;
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
}
