package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;

public class Collectable extends  GameObject{

    Array<GameObject> collectors;
    int driftDistance, collectDistance;


    public Collectable(Vector2 position, Array<GameObject> collectors, int driftDistance, int collectDistance, Vector2 size, ID id) {
        super(position, size, id);
        this.collectors =  collectors;
        this.driftDistance = driftDistance;
        this.collectDistance = collectDistance;
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
    public Circle getCircleBounds() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }
}
