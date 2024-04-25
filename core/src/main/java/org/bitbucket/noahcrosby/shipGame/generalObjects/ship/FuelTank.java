package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

// TODO: Pull functionality into a bucket class that can be inherited
// Probably could make the tracked thing generic making it even more extensible
public class FuelTank {
    Integer fuelCount = 0;
    Integer fuelCapacity = 5;

    public FuelTank(Integer fuel) {
        this.fuelCount = fuel;
    }

    public FuelTank(Integer fuel, Integer fuelCapacity) {
        this.fuelCount = fuel;
        this.fuelCapacity = fuelCapacity;
    }

    public Integer getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(Integer fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public void setFuelCount(Integer fuelCount) {
        this.fuelCount = fuelCount;
    }

    public Integer getFuelCount() {
        return fuelCount;
    }

    public void addFuel(Integer amount) {
        fuelCount += amount;
        generalUtil.clamp(fuelCount, 0, fuelCapacity);
    }

    public void consumeFuel(Integer amount) {
        fuelCount -= amount;
        generalUtil.clamp(fuelCount, 0, fuelCapacity);
    }

    /**
     * Fills the tank to it's capacity
     */
    public void fill() {
        fuelCount = fuelCapacity;
    }

    public boolean isFull() {
        return fuelCount == fuelCapacity;
    }
}
