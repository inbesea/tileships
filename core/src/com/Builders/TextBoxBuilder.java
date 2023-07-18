package com.Builders;

import com.Interfaces.TextBoxBuilderInterface;
import com.Interfaces.TextBoxInterface;
import com.Shapes.Tentacle;
import com.badlogic.gdx.audio.Sound;
import com.shipGame.util.SoundTextBubble;
import com.shipGame.util.TextBubble;

public class TextBoxBuilder implements TextBoxBuilderInterface {

    protected String text;
    protected Tentacle arrow;
    protected
    TextBubble textBubble;


    @Override
    public void reset() {
        text = "";
        arrow = null;
    }

    @Override
    public void setText(String text) {

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

    }

    @Override
    public void setSounds(Sound sound) {

    }

    @Override
    public void setSounds(Sound[] sounds) {

    }

    @Override
    public void addSounds(Sound sound) {

    }

    @Override
    public void addSounds(Sound[] sounds) {

    }

    @Override
    public void setTextSpeed(int millisecondsBetweenWords) {

    }

    @Override
    public TextBoxInterface buildProduct() {
        if(checkSoundboxCriteria()){
//            return new SoundTextBubble();
        }
        else{

        }
        return null;
    }

    private boolean checkSoundboxCriteria() {
        return true;
    }
}
