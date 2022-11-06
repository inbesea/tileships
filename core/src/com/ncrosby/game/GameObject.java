package com.ncrosby.game;

import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public abstract class GameObject {


	protected Vector2 position;
	// From Enum list, needs a type
	protected ID id;
	protected int velX, velY;
	
	public GameObject(Vector2 position, ID id) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.id = id;
		
	}
	
	public abstract void tick();
	public abstract void render();
	public abstract Rectangle getBounds();
	
	public void setX(float x) {
		this.position.x = x;
	}
	public float getX() {
		return position.x;
	}
	
	public void setY(float y) {
		this.position.y = y;
	}
	public float getY() {
		return position.y;
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

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
}
