var sQuery = "SELECT * FROM Win32_Process WHERE Name = 'iTunes.exe'";
var stderr = WScript.CreateObject("Scripting.FileSystemObject").GetStandardStream(2);

var iTunesApp = null;
var state = "";

function WaitForITunes() {
    var service = WScript.CreateObject("WbemScripting.SWbemLocator").ConnectServer();
    while(true) {
        // Search for the "iTunes.exe" process
        var items = service.ExecQuery(sQuery);
        if(items.Count != 0) {
            // If there is, create the object used for retrieving the tracks and return
            iTunesApp = WScript.CreateObject("iTunes.Application");
            return;
        }
        WScript.sleep(1000);
    }
}

function MainLoop() {
    while(true) {
        if(iTunesApp == null) {
            LogStopped();
            WaitForITunes();
        } else {
            try {
                var currentTrack = iTunesApp.CurrentTrack;
                var playerState = iTunesApp.PlayerState;
                if(currentTrack == null || playerState == null) {
                    LogStopped();
                } else {
                    if(playerState == 0) {
                        state = "PAUSED";
                    } else {
                        state = "PLAYING";
                    }
                    
                    var output = currentTrack.Name + ";;" + currentTrack.Artist + ";;" + currentTrack.Album + ";;" + state + ";;" + iTunesApp.PlayerPosition + ";;" + currentTrack.Duration + ";;" + currentTrack.TrackNumber + ";;" + currentTrack.TrackCount;
                    stderr.WriteLine(encodeURIComponent(output));
                }
                
                WScript.sleep(1000);
            } catch(err) {
                // If iTunes stops, reset the variables and do nothing
                iTunesApp = null;
            }
        }
    }
}

function LogStopped() {
    if(state != "STOPPED") {
        state = "STOPPED";
        stderr.WriteLine(state);
    }
}

WaitForITunes();
MainLoop();
