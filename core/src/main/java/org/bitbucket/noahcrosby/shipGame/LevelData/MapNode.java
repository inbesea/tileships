package org.bitbucket.noahcrosby.shipGame.LevelData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.Shapes.Line;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.util.AngleUtils;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

/**
 * Basic element of the map
 */
public class MapNode {

    Array<MapNode> edges;
    // To generally connect nodes positions will give us an overall position
    Vector2 position;
    int orbitRadius = generalUtil.getRandomNumber(3, 20);
    float radius = generalUtil.getRandomNumber(0.75f, 4f);
    Boolean visited = false;
    Boolean drawn = false;

    float rotation = 0f;
    float rotationSpeed = generalUtil.getRandomlyNegativeNumber(3f, 8f);

    public MapNode(Vector2 position) {
        this.position = position;
        edges = new Array<>();
    }

    public void draw(Matrix4 transform) {
        stepRotation();
        Line.drawFilledCircle(getDrawPosition(), this.radius, new Color(0 ,0,1,1), transform); // Draw node
    }

    private void stepRotation() {
        rotation += (Gdx.graphics.getDeltaTime() * rotationSpeed);
    }

    public Vector2 getDrawPosition() {
        Vector2 position =  AngleUtils.getOrbitPoint(this.position, orbitRadius, rotation);
        return position;
    }

    public void drawDebug(Matrix4 transform) {
        Line.drawFilledCircle(position, 0.5f, new Color(150,200,250,1), transform);

        TileShipGame.batch.begin();
        TileShipGame.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        float scale = TileShipGame.font.getScaleX();
        TileShipGame.font.getData().setScale(0.4f);
        TileShipGame.font.draw(TileShipGame.batch, getPositionAsString(), getDrawPosition().x, getDrawPosition().y);
        TileShipGame.font.getData().setScale(scale);
        TileShipGame.batch.end();
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
}
