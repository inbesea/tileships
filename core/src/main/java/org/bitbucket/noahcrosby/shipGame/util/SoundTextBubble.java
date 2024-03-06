package org.bitbucket.noahcrosby.shipGame.util;

import org.bitbucket.noahcrosby.interfaces.TextBoxInterface;
import org.bitbucket.noahcrosby.shapes.Tentacle;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;

/**
 * Text bubble object.
 * Call this from TextBoxBulder.java!! Meant to be called from a builder due
 * to the extensive customizability.
 */
public class SoundTextBubble extends TextBubble implements TextBoxInterface {
    private ArrayList<Sound> letterSounds;
    ShapeDrawer shapeDrawer;
    Tentacle tentacle;
    Vector2 target;

    Vector2 textBoxStartLocation;
    boolean fixed; // Is the text bubble fixed in place?
    // Reference this to know how close/far to the target we can go.
    float minFollowDistance;
    float maxFollowDistance;

    private int arrowSegments = 40;

    public SoundTextBubble(String text, int millisecondsBetweenLetters,
                           Long timeout, boolean timeoutAfterCrawl,
                           float fadeoutSpeed, ArrayList<Sound> letterSounds, Vector2 target,
                           Tentacle tentacle) {
        super(text, millisecondsBetweenLetters, timeout, timeoutAfterCrawl, fadeoutSpeed, target);
        this.letterSounds = letterSounds;

        if(target != null){
            this.target = target;
            tentacle = new Tentacle(arrowSegments, 2, 1 , 20 , Color.WHITE, Color.WHITE);
            shapeDrawer = createShapeDrawer();
        }

    }

    public SoundTextBubble(String text, int millisecondsBetweenLetters,
                           Long timeout, boolean timeoutAfterCrawl,
                           float fadeoutSpeed, Sound letterSounds, Vector2 target) {
        super(text, millisecondsBetweenLetters, timeout, timeoutAfterCrawl, fadeoutSpeed, target);
        this.letterSounds = new ArrayList<>();
        this.letterSounds.add(letterSounds);

        if(target != null){
            this.target = target;
            tentacle = new Tentacle(arrowSegments, 2, 1 , 20 , Color.WHITE, Color.WHITE);
            shapeDrawer = createShapeDrawer();
        }
    }

    /**
     * Convenience method to create a ShapeDrawer
     * (needed for Tentacle)
     * @return - Shape drawer object to draw tentacles
     */
    private ShapeDrawer createShapeDrawer() {
        ShapeDrawer shapeDrawer1 = new ShapeDrawer(TileShipGame.batch);
        shapeDrawer1.setTextureRegion(new TextureRegion(Resources.ShipTileStrongTexture, 20, 20, 20, 20));
        return shapeDrawer1;
    }

    // When using this we want to be able to call this method, pass text, and crawl speed.
    // sounds are the new stuff.
    // a lot of the super logic is half-baked I would say.

    /**
     * Updates the text bubble's location for rendering
     * @param textBoxLocation
     */
    public void update(Vector2 textBoxLocation){
        if(dead){return;} // Dont print text that's expired
        if(firstUpdate){
            begin = System.currentTimeMillis(); // First print will cause the beginning to be set
            firstUpdate = false;
        }

        deltaFromStart = System.currentTimeMillis() - begin;

        if(letterSounds != null){letterSounds();}

        lastChar = getLastChar();
        intermediateString = this.text.substring(0, lastChar);

        if(tentacle != null){ // Tentacle drawing/following
            tentacle.follow(target);// The order here is important. It causes the
            tentacle.inverseFollow(textBoxLocation);
            tentacle.draw(shapeDrawer);
        }
        setTextBoxLocation(textBoxLocation);
        print();
    }

    /**
     * Updates the text bubble's location for rendering
     */
    public void update(){
        if(dead){return;} // Dont print text that's expired
        if(firstUpdate){
            begin = System.currentTimeMillis(); // First print will cause the beginning to be set
            firstUpdate = false;
        }

        deltaFromStart = System.currentTimeMillis() - begin;

        if(letterSounds != null){letterSounds();}

        lastChar = getLastChar();
        intermediateString = this.text.substring(0, lastChar);

        if(tentacle != null){ // Tentacle drawing/following
            tentacle.follow(target);// The order here is important.
//            tentacle.inverseFollow(textBoxLocation);
            tentacle.draw(shapeDrawer);
        }
//        setLocation(textBoxLocation);
        print();
    }

    /**
     * Draws the text bubble in place
     */
    @Override
    public void render(){
        if(dead){return;} // Dont print text that's expired
        if(firstUpdate){
            begin = System.currentTimeMillis(); // First print will cause the beginning to be set
            firstUpdate = false;
        }

        deltaFromStart = System.currentTimeMillis() - begin;

        if(letterSounds != null){letterSounds();}

        lastChar = getLastChar();
        intermediateString = this.text.substring(0, lastChar);

        if(tentacle != null){ // Tentacle drawing/following
            tentacle.follow(target);// The order here is important. It causes the textbox to stay on top of the arrowTentacle
            tentacle.inverseFollow(textBoxLocation);
            tentacle.draw(shapeDrawer);
        }

        print();
    }

    /**
     * Creates placement sounds from list
     */
    private void letterSounds() {
        if(lastChar != Math.min((int) (deltaFromStart / millisecondsBetweenLetters), text.length())){
            letterSounds.get((int) generalUtil.getRandomNumber(0, letterSounds.size())).play(25);
        }
    }
}
