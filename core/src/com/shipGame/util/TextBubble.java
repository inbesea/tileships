package com.shipGame.util;

import com.Interfaces.TextBoxInterface;
import com.Shapes.Tentacle;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shipGame.TileShipGame;

import java.util.Objects;

public class TextBubble  implements TextBoxInterface {

   /*TODO This might need to be a builder object.
   We need to be able to fade out the text, end the text visually right away,
   begin fade out/end after crawling the whole text, or begin the countdown right away,
   One this is true, this needs polish
   * */
    Long begin;
    Long deltaFromStart;
    Long timeoutCount;
    boolean timeoutAfterCrawl = true;
    int millisecondsBetweenLetters;
    int lastChar;
    Long timeout = -1L;
    boolean dead = false;
    String text;
    String intermediateString = "";
    // starts full opacity
    private float opacity = 1f;
    private float fadespeed = 0f;
    protected boolean firstUpdate = true;
    protected Vector2 location;

    public TextBubble(String text, int millisecondsBetweenLetters, Long timeout,
                      boolean timeoutAfterCrawl, float fadeoutSpeed, Vector2 location){
        begin = System.currentTimeMillis();
        this.text = text;
        this.millisecondsBetweenLetters = millisecondsBetweenLetters;
        this.location = location;
    }

    /**
     * Sets the location of the text bubble, and draws it.
     * @param location
     */
    public void update(Vector2 location){
        // TODO: possibly remove this. What is it doing?? It could be used to update values, but not draw the textbox.
        if(dead){return;} // Dont print text that's expired
        if(firstUpdate){
            runFirstUpdate();
        }

        deltaFromStart = System.currentTimeMillis() - begin;

        lastChar = getLastChar();
        intermediateString = this.text.substring(0, lastChar);

        if(timingOut()){ // Begin fading out, or timeout.

        }
        setLocation(location);
        print();
    }

    @Override
    public void render() {
        if(dead){return;} // Dont print text that's expired
        if(firstUpdate){
            runFirstUpdate();
        }

        deltaFromStart = System.currentTimeMillis() - begin;

        lastChar = getLastChar();
        intermediateString = this.text.substring(0, lastChar);

        if(timingOut()){ // Begin fading out, or timeout.

        }

        print();
    }

    private void runFirstUpdate(){
        begin = System.currentTimeMillis(); // First print will cause the beginning to be set
        firstUpdate = false;
    }

    /**
     * Returns true if the textbox is expired
     *
     * @return
     */
    private boolean timingOut() {
        // Handles timeouts after crawl, and before based on string leng, and
        // firstupdate. Should be good.
        if(timeoutAfterCrawl && millisecondsBetweenLetters >= 0){
            if(Objects.equals(intermediateString, text)){
                return true; // Crawl has completed, and timeout begins
            } else {
                return false; // Crawl still developing
            }
        } else if(!timeoutAfterCrawl && firstUpdate){
            return true; // Timeout after first update
        } else {
            return false; // Update might not be called yet
        }
    }

    /**
     * Gets the current char counted to.
     * This is based on the delta from start and crawl speed.
     *
     * @return - number of chars to display based on time since beginning.
     */
    protected int getLastChar(){
        if(begin == null)begin = System.currentTimeMillis(); // First print will cause the beginning to be set

        return Math.min((int) (deltaFromStart / millisecondsBetweenLetters), text.length());
    }

    /**
     * Render the text bubble at the given location
     */
    protected void print(){
        TileShipGame.font.draw(TileShipGame.batch, intermediateString, this.location.x, this.location.y);
    }
    
    private void fadeout(){
        if(timeout >= 0){
            if(timeout < deltaFromStart){
                this.opacity -= fadespeed;
                // Remove invisible textbox.
                if(this.opacity == 0){
                    kill();
                }
            }
        }
    }
    
    protected void stop(){
        if(timeout >= 0){
            if(timeout < deltaFromStart){
                kill();
            }
        }
    }

    public void setFadespeed(float fadespeed){
        this.fadespeed = fadespeed;
    }

    @Override
    public void kill() {
        dead = true;
    }

    public void setLocation(Vector2 location){
        this.location = location;
    }
}
