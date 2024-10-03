package org.bitbucket.noahcrosby.shipGame.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 position = new Vector2(0,0);

    public PositionComponent (Vector2 position){
        this.position = position;
    }
}
