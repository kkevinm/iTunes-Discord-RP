# Check for OS X version, if >= 10.15 then set music app name to "Music" instead of "iTunes"
set sysinfo to system info
set OS_VERSION to system version of sysinfo

considering numeric strings
	if OS_VERSION ≥ "10.15" then
		set MUSIC_APP to "Music"
	else
		set MUSIC_APP to "iTunes"
	end if
end considering

set STR_STOPPED to "STOPPED"
set STR_PLAYING to "PLAYING"
set STR_PAUSED to "PAUSED"

set state to ""

property savedTrack : missing value
property savedPlayerState : missing value

repeat
    delay 1
    # If iTunes is not running, set the state to "STOPPED" and do nothing
    if application MUSIC_APP is not running then
        if state is not STR_STOPPED then
            set state to STR_STOPPED
            log state
        end if
    else
        # If iTunes is running, execute the logic inside a try block so that, if iTunes is closed during the execution, the script doesn't launch an exception
        try
        	# Apparently we can't use variables with tell so I have to replicate this block
        	if MUSIC_APP is "iTunes" then
	        	tell application "iTunes"
	                set currentPlayerState to player state
	                
	                if currentPlayerState is not playing and currentPlayerState is not paused then
	                    # If there's no song playing or paused, set the state to "STOPPED" and do nothing
	                    if state is not STR_STOPPED then
	                        set state to STR_STOPPED
	                        log state
	                    end if
	                else
	                    set currentTrack to current track
	                    
	                    # If the current song or the player status has changed, log the new track information
	                    if currentPlayerState is not savedPlayerState or currentTrack is not savedTrack then
	                        if currentPlayerState is playing then
	                            set state to STR_PLAYING
	                        else
	                            set state to STR_PAUSED
	                        end if
	                        
	                        # Write the track main information in the log
	                        log name of currentTrack & ";;" & artist of currentTrack & ";;" & album of currentTrack & ";;" & state & ";;" & player position & ";;" & duration of currentTrack & ";;" & track number of currentTrack & ";;" & track count of currentTrack
	                    end if
	                    
	                    # Update the player state and song name to detect when the track changes
	                    set savedPlayerState to currentPlayerState
	                    set savedTrack to currentTrack
	                end if
	            end tell
        	else if MUSIC_APP is "Music" then
        		tell application "Music"
	                set currentPlayerState to player state
	                
	                if currentPlayerState is not playing and currentPlayerState is not paused then
	                    # If there's no song playing or paused, set the state to "STOPPED" and do nothing
	                    if state is not STR_STOPPED then
	                        set state to STR_STOPPED
	                        log state
	                    end if
	                else
	                    set currentTrack to current track
	                    
	                    # If the current song or the player status has changed, log the new track information
	                    if currentPlayerState is not savedPlayerState or currentTrack is not savedTrack then
	                        if currentPlayerState is playing then
	                            set state to STR_PLAYING
	                        else
	                            set state to STR_PAUSED
	                        end if
	                        
	                        # Write the track main information in the log
	                        log name of currentTrack & ";;" & artist of currentTrack & ";;" & album of currentTrack & ";;" & state & ";;" & player position & ";;" & duration of currentTrack & ";;" & track number of currentTrack & ";;" & track count of currentTrack
	                    end if
	                    
	                    # Update the player state and song name to detect when the track changes
	                    set savedPlayerState to currentPlayerState
	                    set savedTrack to currentTrack
	                end if
	            end tell
        	else
        		set state to STR_STOPPED
        		log state
        	end if
        on error
            # If iTunes is closed, set the state to "STOPPED" and do nothing
            if state is not STR_STOPPED then
                set state to STR_STOPPED
                log state
            end if
        end try
    end if
end repeat
