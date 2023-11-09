package org.bitbucket.noahcrosby.Shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import org.bitbucket.noahcrosby.Interfaces.Movable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.util.AngleUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class Line implements Movable {

    private final Vector2 a, b;
    private float angleDeg;
    private final float length;
    private final Color color;
    public float width = 5f;

    private static ShapeRenderer debugRenderer = new ShapeRenderer();


    public Line(float ax, float ay, float bx, float by) {
        a = new Vector2(ax, ay);
        b = new Vector2(bx, by);
        angleDeg = AngleUtils.degreesBetweenPoints(b, a);
        length = a.dst(b);
        color = WHITE.cpy();
    }

    public Line(float x, float y, float length, float angle, boolean degrees) {
        a = new Vector2(x, y);
        b = new Vector2();
        this.length = length;
        angleDeg = degrees ? angle : (angle * MathUtils.radDeg);
        calculateB();
        color = WHITE.cpy();
    }

    public Line(float x, float y, float length) {
        this(x, y, length, 0f, true);
    }

    public Line(float length) {
        this(0f, 0f, length);
    }

    public Line(Line line, float length, float angleDeg) {
        this(line.b.x, line.b.y, length, angleDeg, true);
    }

    public Line(Line line, float length) {
        this(line, length, 0f);
    }

    private void calculateB() {
        float deltaX = length, deltaY = length;
        deltaX *= MathUtils.cosDeg(angleDeg);
        deltaY *= MathUtils.sinDeg(angleDeg);
        b.set(a.x + deltaX, a.y + deltaY);
    }

    public void draw(ShapeDrawer shapeDrawer) {
        shapeDrawer.line(a, b, color, width);
    }

    /**
     * Draws a line
     * pass in orthoCamera.combined
     * @param start
     * @param end
     * @param lineWidth
     * @param color
     * @param projectionMatrix
     */
    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    /**
     * Draws a line
     * pass in orthoCamera.combined
     * @param start
     * @param end
     * @param projectionMatrix
     */
    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public static void drawRectangle(Vector2 leftBottPosition, int x, int y, Color color, boolean fill , Matrix4 projectionMatrix) {
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        if(fill)debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        else debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.rect(leftBottPosition.x, leftBottPosition.y, x, y);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public static void drawRectangle(Vector2 leftBottPosition, int x, int y, Color edge, Color fill, Matrix4 projectionMatrix) {
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(fill);
        debugRenderer.rect(leftBottPosition.x, leftBottPosition.y, x, y);
        debugRenderer.end();

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(edge);
        debugRenderer.rect(leftBottPosition.x, leftBottPosition.y, x, y);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public static void drawFilledCircle(Vector2 center, float radius, Color color, Matrix4 projectionMatrix) {
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(color);
        debugRenderer.circle(center.x, center.y, radius);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    @Override
    public void translateX(float amount) {
        a.x += amount;
        b.x += amount;
    }

    @Override
    public void translateY(float amount) {
        a.y += amount;
        b.y += amount;
    }

    @Override
    public float getX() {
        return a.x;
    }

    @Override
    public float getY() {
        return a.y;
    }

    public void follow(Vector2 target) {
        Vector2 dir = target.cpy().sub(a);
        setRotation(dir.angleDeg());
        dir.setLength(length);
        dir.scl(-1f);
        setA(target.cpy().add(dir));
    }

    public Vector2 getA() {
        return a;
    }

    public Vector2 getB() {
        return b;
    }

    public void setA(Vector2 position) {
        a.set(position);
        calculateB();
    }

    public float getAngleDeg() {
        return angleDeg;
    }

    public void rotate(float degrees) {
        angleDeg += degrees;
        if (angleDeg >= 360f) {
            angleDeg = angleDeg % 360f;
        }
        calculateB();
    }

    public void setRotation(float degrees) {
        rotate(degrees - angleDeg);
    }

    public void setColor(Color newColor) {
        color.set(newColor);
    }
}
