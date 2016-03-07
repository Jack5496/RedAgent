package com.redagent.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class ResourceLoader{
	
	public AssetManager assets;
	
	public static ResourceLoader instance;
	
	public ResourceLoader(){
		instance = this;
		assets = new AssetManager();
	}
	
	public static ResourceLoader getInstance(){
		return instance;
	}
	
	public static String data = "data/";
	public static String entitys = data+"entitys/";
	public static String player = entitys+"player.png";
	
	public static String tiles = data+"tiles/";
	
//	public void loadAll(){
//		addToLoad();
//		float progress = 0;
//		Main.log(getClass(), "Loaded: " + assets.getProgress() * 100 + "%");
//
//		while (!assets.update()) {
//			if (progress != assets.getProgress()) {
//				progress = assets.getProgress();
//				Main.log(getClass(), "Loaded: " + assets.getProgress() * 100 + "%");
//			}
//		}
//		progress = assets.getProgress();
//		Main.log(getClass(), "Loaded: " + assets.getProgress() * 100 + "%");
//		
//		assets.update();
//	}

	public void addToLoad(String name) {
		assets.load(Gdx.files.internal(name).path(), Texture.class);
	}
	
	private void loadAsset(String name){
		addToLoad(name);
		float progress = 0;
		Main.log(getClass(), "Loading: "+name);
		while (!assets.update()) {
			if (progress != assets.getProgress()) {
				progress = assets.getProgress();
				Main.log(getClass(), "Loaded: " + assets.getProgress() * 100 + "%");
			}
		}
	}
	
	public Texture getTexture(String name){
		if(!assets.isLoaded(name)){
			loadAsset(name);
		}
		return assets.get(name);
	}
	
	public Texture getTile(String name){
		return getTexture(tiles+name+".png");
	}
	
}
