package org.bitbucket.noahcrosby.shipGame.EntitySystems;

import com.badlogic.ashley.core.Engine;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectableUpdateSystemTest {
    Engine engine;
    @Before
    public void setup(){
        engine = new Engine();
    }

    @AfterEach
    void tearDown(){}

    @Test
    void testUpdateWorks(){

    }

}
