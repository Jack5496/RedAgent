package com.redagent.menu;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.redagent.entitys.LocalPlayer;

public class MenuHandler {

	public Menu activMenu;
	public LocalPlayer p;
	
	public MenuHandler(LocalPlayer p){
		activMenu = new Ingame(this);
		this.p = p;
	}
	
	public void renderActivMenu(ModelBatch batch){
		activMenu.render(batch);
	}
	
}
