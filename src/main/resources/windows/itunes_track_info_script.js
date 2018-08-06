var sQuery = "SELECT * FROM Win32_Process WHERE Name = 'iTunes.exe'";
var stderr = WScript.CreateObject("Scripting.FileSystemObject").GetStandardStream(2);

var iTunesApp = null;
var savedTrack = null;
var savedState = null;
var state = "";

function WaitForITunes() {
	var service = WScript.CreateObject("WbemScripting.SWbemLocator").ConnectServer();
	while(true) {
		var items = service.ExecQuery(sQuery);
		if(items.Count != 0) {
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
				if(currentTrack == null) {
					LogStopped();
				} else if(!EqualTracks(currentTrack, savedTrack) || playerState != savedState) {
					savedTrack = currentTrack;
					savedState = playerState;
					
					if(playerState == 0) {
						state = "PAUSED";
					} else {
						state = "PLAYING";
					}
					
					var output = currentTrack.Name + ";;" + currentTrack.Artist + ";;" + currentTrack.Album + ";;" + state + ";;" + iTunesApp.PlayerPosition + ";;" + currentTrack.Duration + ";;" + currentTrack.TrackNumber + ";;" + currentTrack.TrackCount;
					
					stderr.WriteLine(output);
				}
				
				WScript.sleep(1000);
			} catch(err) {
				iTunesApp = null;
				savedTrack = null;
				savedState = null;
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

function EqualTracks(track1, track2) {
	if((track1 == null && track2 != null) || (track1 != null && track2 == null)) {
		return false;
	} else if(track1 == null && track2 == null) {
		return true;
	} else {
		return track1.Name == track2.Name && track1.Artist == track2.Artist && track1.Album == track2.Album;
	}
}

WaitForITunes();
MainLoop();
