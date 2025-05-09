package org.bitbucket.noahcrosby.shapes;

import org.bitbucket.noahcrosby.interfaces.Movable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Tentacle implements Movable {

    private final Line[] lines;
    private final Vector2 target;
    private final Vector2 lockPosition;
    private boolean locked;

    public Tentacle(int lineAmount, float lineLength) {
        lines = new Line[lineAmount];
        lines[0] = new Line(lineLength);
        for (int i = 1; i < lineAmount; i++) {
            lines[i] = new Line(lines[i - 1], lineLength);
        }
        target = new Vector2();
        lockPosition = new Vector2();
        locked = false;
    }

    public Tentacle(int lineAmount, float lineLength, float startingWidth, float finalWidth, Color color1, Color color2) {
        this(lineAmount, lineLength);
        for (int i = 0; i < lineAmount; i++) {
            lines[i].width = MathUtils.map(0, lineAmount - 1, startingWidth, finalWidth, i);
            lines[i].setColor(color1.cpy().lerp(color2, i / (lineAmount - 1f)));
        }
    }

    public void draw(ShapeDrawer shapeDrawer) {
        for (int i = 0; i < lines.length; i++) {
            lines[i].draw(shapeDrawer);
        }
    }

    @Override
    public void translateX(float amount) {
        for (int i = 0; i < lines.length; i++) {
            lines[i].translateX(amount);
        }
    }

    @Override
    public void translateY(float amount) {
        for (int i = 0; i < lines.length; i++) {
            lines[i].translateY(amount);
        }
    }

    @Override
    public float getX() {
        return lines[lines.length - 1].getA().x;
    }

    @Override
    public float getY() {
        return lines[lines.length - 1].getA().y;
    }

    /**
     * Updates the tentacle's location to follow the target position from the forward direction
     * @param target
     */
    public void follow(Vector2 target) {
        for (int i = 0; i < lines.length; i++) {
            target = i == 0 ? target : lines[i - 1].getA();
            lines[i].follow(target);
        }

        if (locked) {
            lines[lines.length - 1].setA(lockPosition);
            for (int i = lines.length - 2; i >= 0; i--) {
                lines[i].setA(lines[i + 1].getB());
            }
        }
    }

    /**
     * Gets the array of lines to follow the target position from the reverse direction
     * @param target
     */
    public void inverseFollow(Vector2 target){
        for (int i = lines.length - 1; i >= 0; i--) {
            target = i == lines.length - 1 ? target : lines[i + 1].getA();
            lines[i].follow(target);
        }
    }

    public void follow(float x, float y) {
        follow(target.set(x, y));
    }

    public Line[] getLines() {
        return lines;
    }

    public void lock() {
        lockPosition.set(lines[lines.length - 1].getA());
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public void toggleLock() {
        if (locked) unlock();
        else lock();
    }
}
