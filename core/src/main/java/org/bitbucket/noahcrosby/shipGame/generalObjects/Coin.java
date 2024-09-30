package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.util.Animator;

public class Coin extends Collectable {

    Animator coinAnimation;
    public Coin(Vector2 position, Array<GameObject> collectors, int driftDistance, int collectDistance, Vector2 size, ID id) {
        super(position, collectors, driftDistance, collectDistance, size, id);
//        coinAnimation = new Animator();
    }

    @Override
    public void render(TileShipGame game) {
        update();

    }

    private void update() {
        for(int i = 0 ; i < collectors.size ; i++){
            GameObject obj = collectors.get(i);
            if(obj.position.dst(this.position) < collectDistance){
                collect();
            }
        }
    }

    private void collect() {

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
