set STR_STOPPED to "STOPPED"
set STR_PLAYING to "PLAYING"
set STR_PAUSED to "PAUSED"

set state to ""

property savedTrack : missing value
property savedPlayerState : missing value

repeat
    delay 1
    # If iTunes is not running, set the state to "STOPPED" and do nothing
    if application "iTunes" is not running then
        if state is not STR_STOPPED then
            set state to STR_STOPPED
            log state
        end if
    else
        
        # If iTunes is running, execute the logic inside a try block so that, if iTunes is closed during the execution, the script doesn't launch an exception
        try
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
                        log name of currentTrack & ";;" & artist of currentTrack & ";;" & album of currentTrack & ";;" & state & ";;" & player position & ";;" & duration of currentTrack
                    end if
                    
                    # Update the player state and song name to detect when the track changes
                    set savedPlayerState to currentPlayerState
                    set savedTrack to currentTrack
                end if
            end tell
        on error
            # If iTunes is closed, set the state to "STOPPED" and do nothing
            set state to STR_STOPPED
            log state
        end try
    end if
end repeat
