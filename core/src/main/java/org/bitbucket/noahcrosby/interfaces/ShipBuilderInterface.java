package org.bitbucket.noahcrosby.interfaces;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;

public interface ShipBuilderInterface extends BuilderInterface {
    void addTile(int x, int y, ID id);
    public void setBox2DWrapper(Box2DWrapper box2DWrapper);
    public void setCenter(Vector2 center);
}
