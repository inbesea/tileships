package com.Interfaces;

import com.badlogic.gdx.audio.Sound;

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
    void setSounds(Sound[] sounds);
    // Will append
    void addSounds(Sound sound);
    void addSounds(Sound[] sounds);

    void setTextSpeed(int millisecondsBetweenWords);

    TextBoxInterface buildProduct();// Return whatever we made lol
}
