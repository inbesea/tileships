package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Animator {
    TextureAtlas textureAtlas;
    float frameDuration;
    com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode;
    Vector2 position;
    Sprite sprite;
    com.badlogic.gdx.graphics.g2d.Animation<Sprite> coinAnimate;
    float animateTime = 1f;

    public Animator(TextureAtlas animationSprites, Vector2 position,
                    float frameDuration, com.badlogic.gdx.graphics.g2d.Animation.PlayMode playMode){
        this.textureAtlas = animationSprites;
        this.position = position;
        this.frameDuration = frameDuration;
        this.playMode = playMode;
        coinAnimate = new Animation<>(frameDuration,
            textureAtlas.createSprites(),
            playMode);
    }

    public void render(SpriteBatch batch){
        animateTime += Gdx.graphics.getDeltaTime();
        sprite = coinAnimate.getKeyFrame(animateTime, true);
        sprite.setPosition(this.position.x, this.position.y);
        sprite.draw(batch);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition(){
        return this.position;
    }
}
