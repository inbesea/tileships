package org.bitbucket.noahcrosby.Interfaces;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public interface TextBoxBuilderInterface extends BuilderInterface {
    void reset();
    void setText(String text);
    public void setSpeechArrowTarget(Vector2 target); // Sets the speech bubble anchor point
    void setSpeechArrowWidths(int beginningWidth, int endWidth);
    void setSpeechArrowSegments(int segments);
    void setSpeechArrowSegmentSizes(int segmentSizes);
    void setTimeout(long timeout);

    // Will remove the previously set sounds.
    void setSounds(Sound sound);
    void setSounds(ArrayList<Sound> sounds);
    // Will append
    void addSounds(Sound sound);
    void addSounds(ArrayList<Sound> sounds);
    void setTextSpeed(int millisecondsBetweenWords);

    void stretchyArrow(boolean arrowIsStretchy);
    void setAddArrow(boolean setArrow);
    TextBoxInterface buildProduct();// Return whatever we made lol

    void setCountdownAfterCrawl(boolean countdownAfterCrawl);
}
