package com.Interfaces;

import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

public interface TextBoxBuilderInterface {
    void reset();
    void setText(String text);
    void setSpeechArrow(int beginning, int end); // Will need where it points and hangs to
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
}
