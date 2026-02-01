# j5mclaunch
## Modern Minecraft Launcher for Legacy Versions

j5mclaunch is a modern Minecraft launcher designed to run older versions of the game (Alpha through Release 1.5.2) with enhanced features and a user-friendly interface. Originally created for compatibility with Java 5 systems like PowerPC Macs, it now features a modern UI with profile management and customization options.

## Features

- **Modern UI**: Resizable window with proper layout management and organized panels
- **Profile Management**: Customize memory allocation, Java arguments, and profile settings
- **Recent Versions**: Quick access to your recently played versions
- **Version Information**: Dynamic display of version details and system information
- **Microsoft Authentication**: Secure login with Microsoft accounts
- **Automatic Downloads**: Assets, libraries, and game files downloaded automatically
- **Betacraft Proxy Support**: Enables skins and online mode for legacy versions
- **Settings Panel**: Easy configuration of memory, Java arguments, and preferences
- **Multi-Version Support**: Play versions from Alpha 1.0.17_04 to Release 1.5.2

### This REQUIRES a newer version of cURL to be installed than what OS X installs by default. It needs a version of OpenSSL with TLS 1.3

Fallback doesn't work with old versions of Java so its important new cURL is present! You will either need to use MacPorts or TigerBrew to install a new version.

## Usage
1. Install a modern version of cURL (for Windows make sure its in PATH!)
2. Transfer the JAR onto a USB thumbdrive or download it
3. On your system, open j5mclaunch
4. Press "Login" and open the URL in a browser (if it doesnt work in TFF: `https://login.live.com/oauth20_authorize.srf?client_id=00000000402B5328&redirect_uri=https://login.live.com/oauth20_desktop.srf&response_type=code&scope=service::user.auth.xboxlive.com::MBI_SSL`)
5. Paste the URL of the blank page (WITHOUT A TITLE, if it has a title then the login is broken in your browser) into the text box

NOTE: Microsoft recently made some changes to the login page making it completely blank out the login code outside of the vanilla launcher. You will need to be fast to copy the URL

6. Select a version from the dropdown menu
7. (Optional) Configure settings via File → Settings menu:
   - Adjust memory allocation (256-8192 MB)
   - Add custom Java arguments
   - Set a profile name
8. Press "Launch" and wait for everything to download

## Advanced Features

### Settings Panel
Access via File → Settings to customize:
- **Memory Allocation**: Set custom memory (256-8192 MB) instead of auto-calculated values
- **Java Arguments**: Add custom JVM arguments for advanced configurations
- **Profile Name**: Name your profile for easy identification

### Recent Versions
The launcher tracks your 5 most recently played versions and displays them as quick-access buttons below the version selector.

### Version Information
The main panel displays:
- Version type (Alpha/Beta/Release)
- Memory allocation
- Current profile name
- Betacraft proxy status
- Quick tips and status information

## Notice

This does NOT install modifications like OptiFine by default. It is only the launcher. You need to go to the versions folder and mod your own jars.

## Building

You need to download 1.5.2 and 1.6.4 jars and place them into the `libraries` folder in the root of the project due to the shim requiring them. The launcher needs to be built using OpenJDK 8 or later (Java 5 target is no longer supported by modern compilers). Building has been tested with Maven 3.x.

```bash
mvn clean package
```

The compiled JAR will be in the `target` directory as `j5mclaunch-3.1-jar-with-dependencies.jar`.

## System Requirements

- **Java**: Java 8 or later to build; Java 5+ to run (with limitations)
- **Operating System**: Windows, macOS, or Linux
- **cURL**: Modern version with TLS 1.3 support
- **Memory**: At least 512 MB RAM (1 GB+ recommended)

## About Java 6

It "works" I guess, I got 1.2.5 to run a singleplayer world at 2 frames per second (worse performance than with java 5) on my 1.3ghz iBook G4 14"

You can intall it here: https://forums.macrumors.com/threads/how-to-install-java-6-and-7-on-ppc-os-x.2190159/ but I suggest only doing it if you need a modification that \*requires\* Java 6 (I haven't tested Forge, all I know is that those older versions need help downloading libraries or they crash)

Be aware that on Tiger it doesn't set Java in path successfully, but this launcher will use the symlinks the Java Updater makes so you can use the scripts to change version on MC

### DO NOT USE JAVA 7, USERS IN THE THREAD REPORT THAT IT DOES NOT LIKE MINECRAFT AND I HAD IT COMPLETELY BORK ALL JAVA GUI APPS IN THE PAST

