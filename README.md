# iTunes Rich Presence for Discord
This is an application that lets you have a personalized Discord status based on what you're listening to on iTunes (or Music, if you're on MacOS 10.15+). The status will show the song name, artist, album name, time elapsed since the track started playing and index of the track in the album. Additionally, an image with a "playing" or "paused" button will be shown in case the track is playing or paused. For some examples, see the [screenshots](#screenshots).

## Features
* Simple to use.
* No in-app login required.
* Lightweight (~1.5MB file).
* Works on both MacOS and Windows.
* Ugly UI.
* Some customization options on how the Discord status will be displayed.

## Requirements
* **MacOS** / **Windows**.
* A Java Runtime Environment (**JRE**), minimum version **1.8** (you can download it from [here](https://java.com/download/)).
* If the application displays an error window even when everything is set up correctly, installing the Java Development Kit will fix the issue (you can download it from [here](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)).
* [iTunes](https://www.apple.com/itunes/download/) (or *Music*, if using MacOS 10.15+) and [Discord](https://discordapp.com/download) applications.
* For **Windows** users: this application should work for iTunes version 4.7 or later.

## Usage
* Download the application as:
  * *jar*: it can be run on either OS.
  * *dmg*: image that contains the *.app* bundle that can be run on **MacOS** as a native application (double click on the .dmg file, then extract the .app file to wherever you want).
  * *exe*: executable that can be run on **Windows** as a native application.
* All of these versions can be found [*here*](https://github.com/kevinmussi/iTunes-Discord-RP/releases/latest) under *Assets*
* Double click on it.
* Done!

## F.A.Q.
1) * *Q*: The application runs fine, but my status won't update, what do I do?
   * *A*: go to ```Settings -> Game Activity``` and make sure the option *Display currently running game as a status message* is on. Additionally, the status won't be displayed while your Discord status is set to **invisible**.
2) * *Q*: Gatekeeper won't let me run the app/jar on my Mac!
   * *A*: Right-click on the app/jar file and hit **Open**. Then hit **Open** in the dialog that appears. After doing it this way, the app should open fine just by double clicking on it.
3) * *Q*: Can I hide the app's window?
   * *A*: Yes, if your system supports it, the app will show a button on the menu bar / system tray. Clicking on it will hide (or show, if clicking it again) the main window.
4) * *Q*: I get "JRELoadError" when trying to open the .app!
   * *A*: This should be fixed by installing the [latest version of the JRE](https://www.java.com/download/).
5) * *Q*: I'm on Mac and the app seems to freeze often (for example, the status on Discord doesn't update after a while).
   * *A*: This can be caused by App Nap, a feature that puts apps to sleep when they're in the background. You can try disabling App Nap by pasting this line in the Terminal: `defaults write NSGlobalDomain NSAppSleepDisabled -bool YES` (restart the system to make it take effect).
6) * *Q*: Can you make so the album cover of the playing song is shown in the status?
   * *A*: no, Discord doesn't allow you to upload pictures dynamically (outside of special applications like Spotify).

## Screenshots
### Status preview
![alt](screenshots/status-preview1.png)

### Status while playing
![alt](screenshots/status-playing.png)

### Status while paused
![alt](screenshots/status-paused.png)

### Status with long names
![alt](screenshots/status-playing-shortened.png)

## To-dos
* Javadocs.
* Unit tests.
* Spotify integration (easy but rather useless).
* Command line only interface.
* ~~Allow customization of the rich presence elements.~~
* Make it a menu bar app for MacOS.

## Acknowledgements
* [DiscordIPC](https://github.com/jagrosh/DiscordIPC) by @jagrosh.

## Disclaimer
iTunes is a trademark of Apple Inc., registered in the U.S. and other countries.\
This application has not been authorized, sponsored, or otherwise approved by Apple Inc.
