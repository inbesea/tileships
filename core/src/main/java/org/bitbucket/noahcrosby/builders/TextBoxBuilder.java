package org.bitbucket.noahcrosby.builders;

import org.bitbucket.noahcrosby.interfaces.TextBoxBuilderInterface;
import org.bitbucket.noahcrosby.interfaces.TextBoxInterface;
import org.bitbucket.noahcrosby.shapes.Tentacle;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.util.SoundTextBubble;
import org.bitbucket.noahcrosby.shipGame.util.TextBubble;

import java.util.ArrayList;

public class TextBoxBuilder implements TextBoxBuilderInterface {

    //Message text
    protected String text;
    // Should an arrow be created
    protected boolean createArrow;
    protected TextBubble textBubble;
    private Sound sound;
    private ArrayList<Sound> sounds;
    int millisecondsBetweenWords = -1;
    long timeout;
    Vector2 target;
    Vector2 textBoxLocation;
    // Determines when the textbox begins fading out, before or after all text is printed.
    boolean timeoutAfterCrawl;
    float fadeoutSpeed;

    protected Tentacle arrow;
    protected int lineSegments;
    protected int lineSize;
    protected int startingTentacleWidth;
    protected int finalTentacleWidth;
    protected Color beginColor;
    protected Color endColor;


    @Override
    public void reset() {
        /*Default is , no arrow, , , no target*/
        text = "";//no text
        sounds = new ArrayList<>();//no sounds
        millisecondsBetweenWords = 300; // 300ms text crawl
        createArrow = false; // no arrow
        timeout = 7000; // 7000ms timeout
        target = null;
        textBoxLocation = new Vector2(0, 0);
        timeoutAfterCrawl = true;
        fadeoutSpeed = 0.1f;

        arrow = null;
        lineSegments = 40;
        lineSize = 2;
        startingTentacleWidth = 1;
        finalTentacleWidth = 15;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setSpeechArrowTarget(Vector2 target) {
        this.target = target;
    }

    public void setPosition(Vector2 position){
        this.textBoxLocation = position;
    }

    @Override
    public void setSpeechArrowWidths(int beginningWidth, int endWidth) {

    }

    @Override
    public void setSpeechArrowSegments(int segments) {

    }

    @Override
    public void setSpeechArrowSegmentSizes(int segmentSizes) {

    }


    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setSounds(Sound sound) {
        this.sounds = new ArrayList<>(sounds);
    }

    @Override
    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override
    public void addSounds(Sound sound) {
        this.sounds.add(sound);
    }

    @Override
    public void addSounds(ArrayList<Sound> sounds) {
        this.sounds.addAll(sounds);
    }

    @Override
    public void setAddArrow(boolean setArrow) {
        this.createArrow = setArrow;
    }

    @Override
    public void setTextSpeed(int millisecondsBetweenWords) {
        this.millisecondsBetweenWords = millisecondsBetweenWords;
    }

    @Override
    public void stretchyArrow(boolean arrowIsStretchy) {
        // TODO : add logic to allow the textboxs' arrow to extend out, connecting both sides.
        // Difficult!
        System.out.println("TODO : implement stretchy Arrows in textboxbuilder!");
    }

    /**
     * Set the timeout after crawling is finished.
     * @param timeoutAfterCrawl
     */
    @Override
    public void setCountdownAfterCrawl(boolean timeoutAfterCrawl) {
        this.timeoutAfterCrawl = timeoutAfterCrawl;
    }

    /**
     * Builds instance of TextBubble/SoundTextBubble
     * @return
     */
    @Override
    public TextBoxInterface buildProduct() {
        if(checkSoundboxCriteria()){
            return new SoundTextBubble(text, millisecondsBetweenWords, timeout,
                    timeoutAfterCrawl, fadeoutSpeed, sounds,target, arrow);
        } else {
            return new TextBubble(text, millisecondsBetweenWords, timeout,
                    timeoutAfterCrawl, fadeoutSpeed, textBoxLocation);
        }
    }

    private boolean checkSoundboxCriteria() {
        if(sounds != null && !sounds.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
