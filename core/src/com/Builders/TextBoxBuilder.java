package com.Builders;

import com.Interfaces.TextBoxBuilderInterface;
import com.Interfaces.TextBoxInterface;
import com.Shapes.Tentacle;
import com.badlogic.gdx.audio.Sound;
import com.shipGame.util.SoundTextBubble;
import com.shipGame.util.TextBubble;

import java.util.ArrayList;
import java.util.Arrays;

public class TextBoxBuilder implements TextBoxBuilderInterface {

    protected String text;
    protected Tentacle arrow;
    protected boolean createArrow;
    protected
    TextBubble textBubble;
    private Sound sound;
    private ArrayList<Sound> sounds;
    int millisecondsBetweenWords = -1;
    long timeout;


    @Override
    public void reset() {
        text = "";
        arrow = null;
        sounds = new ArrayList<>();
        millisecondsBetweenWords = 300;
        createArrow = false;
        timeout = 7000;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setSpeechArrow(int beginning, int end) {

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
    public TextBoxInterface buildProduct() {
        if(checkSoundboxCriteria()){
            return new SoundTextBubble(text, millisecondsBetweenWords, sounds);
        } else {
            return new TextBubble(text, millisecondsBetweenWords);
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
