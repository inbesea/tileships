package com.ncrosby.game;

import java.awt.*;
import java.util.LinkedList;

// Game object updater
public class Handler {

	LinkedList<GameObject> object = new LinkedList<GameObject>();
	/*private Camera cam;
	
	public Handler(Camera cam) {
		this.cam = cam;
	}*/
	
	public void tick() {
		for(int i = 0 ; i < object.size() ; i++) {
			GameObject tempObject = object.get(i);
			
			tempObject.tick();
		}
	}
	
	public void render(Graphics g) {
		Player player = null;
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			if(tempObject.id == ID.Player)
				player = (Player)tempObject;
			else{
				tempObject.render(g);
			}
		}
		// render Player on top of everything
		// Could probably have a flag to show what objects to render first. 
		if(player != null)	player.render(g);
	}
	
	
	// Methods to append and delete objects from the linked list.
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
}
