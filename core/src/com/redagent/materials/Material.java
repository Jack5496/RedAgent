package com.redagent.materials;

import com.badlogic.gdx.graphics.Texture;
import com.redagent.game.Main;
import com.redagent.game.ResourceLoader;

public class Material{
	
	public String texture;
	
	public Texture getTexture(){
		if(texture==null) return ResourceLoader.getInstance().getTile("error");
		return ResourceLoader.getInstance().getTile(texture);
	}
	
	public void setTexture(String tex){
		this.texture = tex;
	}
	
	public boolean isSame(Material m){
		return this.getClass()==m.getClass();
	}
	
}
