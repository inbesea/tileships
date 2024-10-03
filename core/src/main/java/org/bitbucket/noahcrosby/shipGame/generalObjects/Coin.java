package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.Components.CollectableComponent;
import org.bitbucket.noahcrosby.shipGame.Components.PositionComponent;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.screens.MainMenu;
import org.bitbucket.noahcrosby.shipGame.util.Animator;

public class Coin extends Entity {
    float animationSpeed = 0.1f;

    public static class CoinComponent implements Component {

    }

    Animator coinAnimation;
    public Coin(Vector2 position) {
//        coinAnimation = new Animator();
        add(new CoinComponent());
        add(new Animator(MainMenu.textureAtlas, position,
            animationSpeed, Animation.PlayMode.LOOP_PINGPONG));
        add(new PositionComponent(position));
        add(new CollectableComponent(ID.Player));
    }

    public void render(TileShipGame game) {

    }

    private void update() {

    }

    private void collect() {

    }

}
