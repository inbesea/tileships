package com.ncrosby.game;

import java.awt.*;

public abstract class GameObject {

	// Maybe we need to have a way to order these objects for rendering so we can have the objects rendered in a specific order. 
	
	protected int x, y;
	// From Enum list, needs a type
	protected ID id;
	protected int velX, velY;
	
	public GameObject(int x, int y, ID id) {
		this.x = x;
		this.y = y;
		this.id = id;
		
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}
	
	public void setID(ID id) {
		this.id = id;
	}
	public ID getID() {
		return id;
	}
	
	public void setVelX(int velX) {
		this.velX = velX;
	}
	public int getVelX() {
		return velX;
	}
	
	public void setVelY(int velY) {
		this.velY = velY;
	}
	public int getVelY() {
		return velY;
	}
}
