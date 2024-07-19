package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

// TODO: Pull functionality into a bucket class that can be inherited
// Probably could make the tracked thing generic making it even more extensible
public class FuelTank {
    Double fuelCount = 0d;
    Double fuelCapacity = 5d;

    public FuelTank(Double fuel) {
        this.fuelCount = fuel;
    }

    public FuelTank(Double fuel, Double fuelCapacity) {
        this.fuelCount = fuel;
        this.fuelCapacity = fuelCapacity;
    }

    public Double getFuelCapacity() {
        if(fuelCapacity == null) {
            return Double.POSITIVE_INFINITY;
        }
        return fuelCapacity;
    }

    public void setFuelCapacity(Double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public void setFuelCount(Double fuelCount) {
        this.fuelCount = fuelCount;
    }

    public Double getFuelCount() {
        return fuelCount;
    }

    public void addFuel(Double amount) {
        fuelCount += amount;
        generalUtil.clamp(fuelCount, fuelCapacity, 0d);
    }

    public void consumeFuel(Double amount) {
        fuelCount -= amount;
        generalUtil.clamp(fuelCount, 0d, fuelCapacity);
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
