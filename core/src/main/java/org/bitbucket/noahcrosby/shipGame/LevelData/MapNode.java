package org.bitbucket.noahcrosby.shipGame.LevelData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.Shapes.Line;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.util.AngleUtils;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

import java.util.Random;

/**
 * Basic element of the map
 */
public class MapNode extends GameObject {

    Array<MapNode> edges;
    // To generally connect nodes positions will give us an overall position
    int orbitRadius = generalUtil.getRandomNumber(3, 20);
    float radius = generalUtil.getRandomNumber(0.95f, 4f);
    Boolean visited = false;
    Boolean drawn = false;

    private Boolean hovered = false;

    float rotation = generalUtil.getRandomNumber(0, 360);
    float rotationSpeed = generalUtil.getRandomlyNegativeNumber(3f, 8f);

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
    public Boolean getHovered() {
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
}
