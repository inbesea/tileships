package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.StandardTile;
import org.bitbucket.noahcrosby.shipGame.physics.PhysicsObject;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

import static org.bitbucket.noahcrosby.shipGame.util.generalUtil.getRandomlyNegativeNumber;

public class Asteroid extends GameObject implements PhysicsObject {

	public static float maxSpeed = 2f;
	public static float minSpeed = 0.2f;
	public static float radius = 0.5f;
	Circle circle;
    private boolean isDead;

    public Asteroid() {
        super(new Vector2(0,0), new Vector2(ShipTile.TILE_SIZE,ShipTile.TILE_SIZE), ID.Asteroid);

        velX = getRandomlyNegativeNumber(minSpeed,maxSpeed);
        velY = getRandomlyNegativeNumber(minSpeed,maxSpeed);
        radius = size.y * 0.5f;
        circle = new Circle(position.x + radius, position.y + radius, radius);
    }
	public Asteroid(Vector2 position, Vector2 size , ID id) {
		super(position, size, id);

		velX = getRandomlyNegativeNumber(minSpeed,maxSpeed);
		velY = getRandomlyNegativeNumber(minSpeed,maxSpeed);
		radius = size.y * 0.5f;
		circle = new Circle(position.x + radius, position.y + radius, radius);
	}

	/**
	 * Create asteroid with specific velocity
	 * @param position
	 * @param size
	 * @param velocity
	 * @param id
	 */
	public Asteroid(Vector2 position, Vector2 size, Vector2 velocity, ID id) {
		super(position, size, id);

		velX = velocity.x;
		velY = velocity.y;
		radius = size.y * 0.5f;
		circle = new Circle(position.x + radius, position.y + radius, radius);
	}

	public Rectangle getBounds(){
		return null;
	}

    /**
     * Returns the class this returns when created. Should be overridden.
     * NOTE : This could take specific data about an asteroid and produce a specific tile instance?
     * Nope, we want the asteroid to know what it can result in, and we could have another class determine the attributes
     * of the tile.
     * @return
     */
    public Class productionOutput(){
        return StandardTile.class;
    }

    @Override
	protected void setBoundsPosition(Vector2 boundsPosition) {
		circle.setPosition(boundsPosition);
	}

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        circle.setPosition(position.x + radius, position.y + radius);
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        velX = velocity.x;
        velY = velocity.y;
    }

    public Circle getCircleBounds() {
		return circle;
	}

	@Override
	public boolean deleteFromGame() {
		setIsDead(true);
        return getIsDead();
	}

    public boolean getIsDead(){
        return this.isDead;
    }

    public boolean setIsDead(boolean isDead){
        this.isDead = isDead;
        return this.isDead;
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

	}

	@Override
	public Texture getTexture() {
		return Resources.AsteroidPurpleTexture;
	}

    public ID productionOutputID(){
        return ID.StandardTile;
    }

    /**
     * Returns the type of tile this produces
     * @return
     */
    public Class getTileType(){
        return StandardTile.class;
    }

	/**
	 * Sets the position of the asteroid to the physics body's position
	 */
	public void render(TileShipGame game) {
	}
}
