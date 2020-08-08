set STR_STOPPED to "STOPPED"
set STR_PLAYING to "PLAYING"
set STR_PAUSED to "PAUSED"

set state to ""

repeat
    delay 1
    # If iTunes is not running, set the state to "STOPPED" and do nothing
    if application "Music" is not running then
        if state is not STR_STOPPED then
            set state to STR_STOPPED
            log state
        end if
    else
        # If iTunes is running, execute the logic inside a try block so that, if iTunes is closed during the execution, the script doesn't launch an exception
        try
            tell application "Music"
                set currentPlayerState to player state
                
                if currentPlayerState is not playing and currentPlayerState is not paused then
                    # If there's no song playing or paused, set the state to "STOPPED" and do nothing
                    if state is not STR_STOPPED then
                        set state to STR_STOPPED
                        log state
                    end if
                else
                    if currentPlayerState is playing then
                        set state to STR_PLAYING
                    else
                        set state to STR_PAUSED
                    end if
                        
                    # Write the track main information in the log
                    log name of current track & ";;" & artist of current track & ";;" & album of current track & ";;" & state & ";;" & player position & ";;" & duration of current track & ";;" & track number of current track & ";;" & track count of current track
                end if
            end tell
        on error
            # If iTunes is closed, set the state to "STOPPED" and do nothing
            if state is not STR_STOPPED then
                set state to STR_STOPPED
                log state
            end if
        end try
    end if
end repeat
