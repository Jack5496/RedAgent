package com.redagent.player;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.redagent.game.Main;

public class PlayerHandler {

	HashMap<String, Player> localPlayers;

	public Player getPlayer(int id){
		Player[] players = getPlayers();
		return players[id];
	}
	
	public Player[] getPlayers(){
		Player[] players = new Player[localPlayers.values().size()];
		localPlayers.values().toArray(players);
		return players;
	}
	
	public int getPlayerAmount(){
		return getPlayers().length;
	}
	
	public PlayerHandler() {
		localPlayers = new HashMap<String, Player>();
	}

	public Player getPlayerByInput(String inputHandlerName) {
		boolean found = localPlayers.containsKey(inputHandlerName);

		if (!found) {
			Main.log(getClass(), "New Player found");
			Player p = new Player("Bob");
			p.initCamera();
			localPlayers.put(inputHandlerName, p);
		}

		return localPlayers.get(inputHandlerName);
	}
	
	public void updateEntityHealth(){
//		Array<Player> toDie = new Array<Player>();
//		for(Player p : localPlayers.values()){
//			if(p.isStillAlive()){
//				
//			}
//			else{
//				toDie.add(p);
//			}
//		}
//		for(Player p : toDie){
//			p.obj.respawn();
//			p.health = 100;
////			localPlayers.remove(p);
//		}
	}
	
	public void updatePlayers(){
		for(Player p : localPlayers.values()){
			p.updateMyGameObjects();
		}
	}

}
