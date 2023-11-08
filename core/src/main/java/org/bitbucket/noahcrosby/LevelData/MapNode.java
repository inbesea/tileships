package org.bitbucket.noahcrosby.LevelData;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.Shapes.Line;

/**
 * Basic element of the map
 */
public class MapNode {

    Array<MapNode> edges;
    // To generally connect nodes positions will give us an overall position
    Vector2 position;
    int radius;
    Boolean visited = false;
    Boolean drawn = false;

    public MapNode(Vector2 position) {
        this.position = position;
        edges = new Array<>();
    }

    public void draw(Matrix4 transform) {
        Line.drawFilledCircle(new Vector2(position.x, position.y), 3, new Color(0 ,0,1,1), transform); // Draw node
    }
}
