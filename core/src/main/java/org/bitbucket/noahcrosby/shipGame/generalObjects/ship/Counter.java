package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

// TODO: Pull functionality into a bucket class that can be inherited
// Probably could make the tracked thing generic making it even more extensible

/**
 * Generic counter class that tracks an int value that can be added and subtracted from. A capacity value
 * limits how much count can be equal to. 
 */
public class Counter {
    Double count = 0d;
    Double capacity = 5d;

    public Counter(Double count) {
        this.count = count;
    }

    public Counter(Double count, Double capacity) {
        this.count = count;
        this.capacity = capacity;
    }

    public Double getCapacity() {
        if(capacity == null) {
            return Double.POSITIVE_INFINITY;
        }
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getCount() {
        return count;
    }

    public void add(Double amount) {
        count += amount;
        generalUtil.clamp(count, capacity, 0d);
    }

    public void remove(Double amount) {
        count -= amount;
        generalUtil.clamp(count, 0d, capacity);
    }

    /**
     * Fills the tank to it's capacity
     */
    public void fill() {
        count = capacity;
    }

    public boolean isFull() {
        return count == capacity;
    }
}
