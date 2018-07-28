package com.github.kevinmussi.itunesrp.core;

import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import com.github.kevinmussi.itunesrp.data.TrackMessage;
import com.github.kevinmussi.itunesrp.observer.Observer;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.EntityBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

public class DiscordHelper implements Observer<TrackMessage> {
    
	private JSONObject jsonGame;
	
    public DiscordHelper() {
        this.jsonGame = new JSONObject();
    }

	@Override
	public void update(TrackMessage message) {
		if(message == null) {
			return;
		}
		
	}
	
	public static void main(String[] args) {
		try {
			JDA jda = new JDABuilder(AccountType.CLIENT)
					.setToken("token")
					.buildBlocking();
			jda.addEventListener(new EventListener() {

				@Override
				public void onEvent(Event event) {
					System.out.println("ciaoo");
				}
				
			});
			//RichPresence rp = new RichPresence(null, null, null, 1L, null, null, null, null, null, null, 2, null, null, null, null);
			jda.getPresence().setGame(Game.listening("iTunes"));
			EntityBuilder eb = new EntityBuilder(jda);
			JSONObject jsonGame = new JSONObject();
			
			eb.createGame(jsonGame);
			//jda.getPresence().getGame().
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
}
