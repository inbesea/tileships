package com.shipGame.generalObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.javapoet.Resources;
import com.shipGame.ID;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.StrongTile;
import com.shipGame.managers.AsteroidManager;
import com.shipGame.physics.PhysicsObject;
import com.shipGame.TileShipGame;
import com.shipGame.util.generalUtil;

import static com.shipGame.util.generalUtil.getRandomlyNegativeNumber;

public class Asteroid extends GameObject implements PhysicsObject {

	public static float maxSpeed = 2f;
	public static float minSpeed = 0.2f;
	public static float radius = 0.5f;
	Circle circle;
	AsteroidManager asteroidManager;
	public Asteroid(Vector2 position, Vector2 size , ID id, AsteroidManager asteroidManager) {
		super(position, size, id);

		velX = getRandomlyNegativeNumber(minSpeed,maxSpeed);
		velY = getRandomlyNegativeNumber(minSpeed,maxSpeed);
		radius = size.y * 0.5f;
		circle = new Circle(position.x + radius, position.y + radius, radius);
		this.asteroidManager = asteroidManager;
	}

	/**
	 * Create asteroid with specific velocity
	 * @param position
	 * @param size
	 * @param velocity
	 * @param id
	 */
	public Asteroid(Vector2 position, Vector2 size, Vector2 velocity, ID id, AsteroidManager asteroidManager) {
		super(position, size, id);

		velX = velocity.x;
		velY = velocity.y;
		radius = size.y * 0.5f;
		circle = new Circle(position.x + radius, position.y + radius, radius);
		this.asteroidManager = asteroidManager;
	}

	public Rectangle getBounds(){
		return null;
	}

	@Override
	protected void setBoundsPosition(Vector2 boundsPosition) {
		circle.setPosition(boundsPosition);
	}

	public Circle getCircleBounds() {
		return circle;
	}

	@Override
	public boolean deleteFromGame() {
		System.out.println("Asteroid is deleting itself from the manager");
		asteroidManager.deleteMember(this);
		System.out.println("Returning true after deleting asteroid");
		return true;
	}

	@Override
	public Body setPhysics(World world) {

		BodyDef bodyDef = generalUtil.newDynamicBodyDef(
				(position.x / ShipTile.TILE_SIZE) + this.getCircleBounds().radius,
				(position.y / ShipTile.TILE_SIZE) + this.getCircleBounds().radius
		);
		Body body = world.createBody(bodyDef);

		body.setLinearVelocity(getRandomlyNegativeNumber(Asteroid.minSpeed, Asteroid.maxSpeed),
				getRandomlyNegativeNumber(Asteroid.minSpeed, Asteroid.maxSpeed));
		// Create circle to add to asteroid
		CircleShape circle = new CircleShape();
		circle.setRadius(Asteroid.radius / ShipTile.TILE_SIZE);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.0f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();

		return body;
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return body;
	}

	/**
	 * Used to handle asteroid bounces
	 * @param gameObject
	 */
	@Override
	public void collision(GameObject gameObject) {
			bounce(gameObject);
	}

	@Override
	public Texture getTexture() {
		return Resources.AsteroidPurpleTexture;
	}

	private void bounce(GameObject gameObject) {
		System.out.println("Bouncing Asteroid , current velocity is " + velY + ", " + velX);
		if(gameObject instanceof StrongTile){
			StrongTile strongTile = (StrongTile) gameObject;
//			strongTile.
			// ToDO : We want the strong tile to bounce the asteroid correctly so it looks nice.
			// It bounces dumb right now.
		}
		// Adjust positions to prevent repeat bounces
		position.x -= velX;
		position.y -= velY;
		circle.x -= velX;
		circle.y -= velY;

		velX *= -1;
		velY *= -1;
	}

	// TODO : burn this to the ground
	// This method is totally wrong. There's a lot of inappropriate intimacy going on here.
	// Each Game Object should not need a reference to the camera held inside it. We need to give the
	// Camera to the update method.
	public void tick() {
//		x += velX;
//		y += velY;
//
//		if(y <= 0 + cam.y || y >= legacyGame.HEIGHT - 55 + cam.y) {
//			y -= velY;
//			velY *= -1;
//		}
//		if(x <= 0 + cam.x || x >= legacyGame.WIDTH - 32 + cam.x) {
//			x -= velX;
//			velX *= -1;
//		}
//
//		// Extra stuff to make the enemy bounce off the ship
//		if(shiphandler.returnTile(x - velX - cam.x, y - cam.y) != null) {
//			y -= velY;
//			velY *= -1;
//		}
//		if(shiphandler.returnTile(x - cam.x, y - velX - cam.y) != null) {
//			x -= velX;
//			velX *= -1;
//		}
	}

	/**
	 * Draws basic enemy on screen relative to the camera
	 */
	public void render(TileShipGame game) {
		circle.setPosition(body.getPosition());
	}

}
