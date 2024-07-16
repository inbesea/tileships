package org.bitbucket.noahcrosby.shipGame.levelData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shapes.Line;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris.Asteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris.AsteroidSpawner;
import org.bitbucket.noahcrosby.shipGame.util.AngleUtils;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic element of the map
 */
public class MapNode extends GameObject {

    Array<MapNode> edges;
    // To generally connect nodes positions will give us an overall position
    int orbitRadius = generalUtil.getRandomNumber(3, 20);
    float radius = generalUtil.getRandomNumber(0.95f, 4f);
    Boolean visited = false;
    private boolean playerVisited;// Is this node visited by the player
    Boolean drawn = false;

    private Boolean hovered = false;

    float rotation = generalUtil.getRandomNumber(0, 360);
    float rotationSpeed = generalUtil.getRandomlyNegativeNumber(3f, 8f);
    private boolean clicked;
    private Array<Asteroid> asteroids = new Array<>();

    protected Integer refuels = 0;
    Boolean storeLocation = false;

    /**
     * General node of a map. Can be overridden for new visuals etc.
     * @param position
     */
    public MapNode(Vector2 position) {
        super(position, new Vector2(1,1));
        edges = new Array<>();
    }

    public MapNode(Vector2 position, Vector2 size) {
        super(position, size);
        edges = new Array<>();
    }

    /**
     * Draws this node
     * @param transform
     */
    public void draw(Matrix4 transform) {
        stepRotation();
        Line.drawFilledCircle(getDrawPosition(), this.radius, new Color(0 ,0,1,1), transform); // Draw node

        // Drawing hightlighted and hovered situations
        if(this.isClicked()){
            Line.drawFilledCircle(getDrawPosition(), this.radius + 5, Color.CHARTREUSE, transform); // Draw node
        } else if(this.isHovered()){
            Line.drawFilledCircle(getDrawPosition(), this.radius + 4, Color.GREEN, transform);
        }

        // Draw the player is here
        if(this.playerIsHere()){
            Line.drawHollowCircle(getDrawPosition(), this.radius + 9, 0.5f , Color.WHITE, transform);
            Line.drawFilledCircle(AngleUtils.getOrbitPoint(this.position, orbitRadius, rotation),
                this.radius, Color.RED, transform);
        }
    }

    /**
     * Steps the rotation of this node by the rotation speed and the time delta
     */
    private void stepRotation() {
        rotation += (Gdx.graphics.getDeltaTime() * rotationSpeed);
    }

    /**
     * Handles determining where this node will be drawn
     * @return
     */
    public Vector2 getDrawPosition() {
//        Vector2 position =  AngleUtils.getOrbitPoint(this.position, orbitRadius, rotation);
//        return position;
        return this.position;
    }

    /**
     * Draws the debug calls of this node
     */
    public void drawDebug() {
        drawCenterOfNode();

        TileShipGame.batch.begin();
        TileShipGame.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        float scale = TileShipGame.font.getScaleX();
        TileShipGame.font.getData().setScale(0.4f);
        TileShipGame.font.draw(TileShipGame.batch, getPositionAsString(), getDrawPosition().x, getDrawPosition().y);
        TileShipGame.font.getData().setScale(scale);
        TileShipGame.batch.end();
    }

    /**
     * Debug call to draw a point at the center of this node
     */
    protected void drawCenterOfNode() {
        Line.drawFilledCircle(position, 0.5f, new Color(150,200,250,1), TileShipGame.batch.getProjectionMatrix());
    }

    /**
     * Returns clean formatted position as a string
     * @return - String representing location
     */
    public String getPositionAsString(){
        return "(" + (int)position.x + ", " + (int) position.y + ")";
    }

    /**
     * Removes all edges from this node, and its current neighbors
     * Will do nothing if this node has no edges
     */
    public void orphanSelf(){
        for(MapNode node : edges){
            node.edges.removeValue(this, true);
        }
        edges.clear();
    }

    /**
     * Returns if the node is being hovered by the player
     * @return
     */
    public Boolean isHovered() {
        return hovered;
    }

    /**
     * Sets if the node is being hovered
     * likely changing the visuals of the node and giving an info popup.
     * @param hovered
     */
    public void setHovered(Boolean hovered) {
        this.hovered = hovered;
    }

    @Override
    public void render(TileShipGame game) {

    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    protected void setBoundsPosition(Vector2 boundsPosition) {

    }

    @Override
    public void collision(GameObject gameObject) {

    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public Circle getCircleBounds() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    public void clicked(boolean b) {
        this.clicked = b;
    }

    public boolean isClicked() {
        return this.clicked;
    }

    public boolean playerIsHere() {
        return this.playerVisited;
    }

    public void setPlayerIsHere(boolean b) {
        this.playerVisited = b;
    }

    /**
     * Set the asteroids in this MapNode
     */
    public MapNode setAsteroids(Array<Asteroid> asteroids){
        this.asteroids = asteroids;
        return this;
    }

    public Array<Asteroid> getAsteroids() {
        return this.asteroids;
    }

    /**
     * Returns this nodes asteroids and clears the node asteroid instance list.
     * @return
     */
    public Array<Asteroid> returnAsteroids() {
        Array<Asteroid> asteroids1 = this.asteroids;
        this.asteroids = new Array<>();
        return asteroids1;
    }

    public AsteroidSpawner getAsteroidSpawner() {
        Gdx.app.debug("MapNode", "DON'T FORGET TO IMPLEMENT MEEE");
        return null;
    }

    /**
     * Returns the edges of this node
     * @return - Array of edges
     */
    public Array<MapNode> getEdges() {
        return this.edges;
    }

    public void setVisited(boolean b) {
        this.visited = b;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public MapNode getForeground() {
        return null;
        // TODO : halp
    }

    public Boolean isNeighborsWith(MapNode node) {
        if(node == null){
            return false;
        } else if(node == this){
            return false;
        }
        return this.edges.contains(node, true);
    }

    /**
     * Weight is 1.
     * Used for pathfinding, but we care about edges not weights
     * @return
     */
    public int getWeight(){
        return 1;
    }

    public List<MapNode> getEdgesAsList() {
        List<MapNode> edges = new ArrayList<>();
        for (MapNode node : this.edges) {
            edges.add(node);
        }
        return edges;
    }

    public boolean tryRefuel() {
        if(refuels > 0){
            refuels--;
            return true;
        }
        return false;
    }


    /**
     * Returns number of refuels remaining
     * @return
     */
    public Integer getRefuels() {
        return refuels;
    }

    public void setRefuels(Integer refuels) {
        this.refuels = refuels;
    }

    public Boolean isStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(Boolean isStoreLocation){
        this.storeLocation = isStoreLocation;
    }
}
