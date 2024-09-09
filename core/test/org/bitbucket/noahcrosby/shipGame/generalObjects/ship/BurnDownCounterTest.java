package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BurnDownCounterTest {

    protected BurnDownCounter counter;

    @BeforeEach
    void setUp() {
        counter = new BurnDownCounter(2d,12d,2d);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void check() {

    }

    @Test
    void isBurnedOut() {
    }

    @Test
    void burnOutChanceTest(){
        assertEquals(0.17f, counter.burnoutChance());
        assertEquals(0.33f, new BurnDownCounter(1d,3d,2d).burnoutChance());
    }
}
