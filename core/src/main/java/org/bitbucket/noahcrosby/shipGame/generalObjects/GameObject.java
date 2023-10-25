package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;

/**
 * Class to handle the basic game object needs.
 */
public abstract class GameObject {
	protected Vector2 position = new Vector2();
	protected Vector2 size = new Vector2();
	// From Enum list, needs a type
	protected ID id;
	protected float velX, velY;

	protected boolean debugMode = true; //TODO:move to game object
	protected Body body;
	protected float rotation;
	protected boolean physicsDeletable;

	public GameObject(Vector2 position, Vector2 size , ID id) {
		// set location and id to define basics of the game object.
		this.position.x = position.x;
		this.position.y = position.y;
		this.size.x = size.x;
		this.size.y = size.y;
		this.id = id;

		physicsDeletable = false;
	}

	/**
	 * No ID super const.
	 * @param position
	 * @param size
	 */
	public GameObject(Vector2 position, Vector2 size) {
		// set location and id to define basics of the game object.
		this.position.x = position.x;
		this.position.y = position.y;
		this.size.x = size.x;
		this.size.y = size.y;
	}

	public abstract void tick();
	public abstract void render(TileShipGame game);
	public abstract Rectangle getBounds();

	// Meant to reflect the physical bounds of the gameObject
	protected abstract void setBoundsPosition(Vector2 boundsPosition);
	public abstract void collision(GameObject gameObject);
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
	public float getVelX() {
		return velX;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}
	public float getVelY() {
		return velY;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
		this.setBoundsPosition(position);
	}

	/**
	 * Returns a texture used for drawing/rendering object
	 * can be implemented to return null
	 *
	 * @return - Texture of relevant concrete type
	 */
	public abstract Texture getTexture();
	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	/**
	 * Returns a float representing the difference between the right and left size point
	 * Equal to the size.x Vector (x = width, y = height)
	 * @return - a float representing the size of the Game Object
	 */
	public float getWidth(){
		return size.x;
	}

	public float getHeight(){
		return size.y;
	}

	public abstract Circle getCircleBounds();


	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * Mark to be swept during a physics step.
	 * All gameObjects are checked on a physics step and swept.
	 */
	public void physicsDelete(){
		physicsDeletable = true;
	}

	/**
	 * Returns value of physics death from a collision or some other physics based action.
	 * @return
	 */
	public boolean isDead(){
		return physicsDeletable;
	}

	/**
	 * This should be implemented to use whatever means available to delete the object at hand.
	 * @return
	 */
	public abstract boolean deleteFromGame();
}
