package org.bitbucket.noahcrosby.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class Text {

    private final BitmapFont font;
    private String text;
    private boolean verticallyCentered;
    private float yOffset;
    private int hAlign;
    private String truncate;
    private final Rectangle bounds;

    public Text(BitmapFont font) {
        this.font = new BitmapFont(font.getData().fontFile);
        text = "";
        hAlign = Align.left;
        bounds = new Rectangle();
        bounds.width = 500f;
    }

    public void draw(SpriteBatch spriteBatch) {
        font.draw(
            spriteBatch,
            text,
            bounds.x,
            bounds.y + yOffset,
            0,
            text.length(),
            bounds.width,
            hAlign,
            true,
            truncate
        );
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
            bounds.x,
            bounds.y + yOffset,
            bounds.width,
            -bounds.height
        );
    }

    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    // Position related
    public float getX() {
        return bounds.x;
    }

    public void setX(float x) {
        translateX(x - getX());
    }

    public void translateX(float amount) {
        bounds.x += amount;
    }

    public float getY() {
        return bounds.y;
    }

    public void setY(float y) {
        translateY(y - getY());
    }

    public void translateY(float amount) {
        bounds.y += amount;
    }

    public void translate(float xAmount, float yAmount) {
        translateX(xAmount);
        translateY(yAmount);
    }

    public void translate(Vector2 amount) {
        translate(amount.x, amount.y);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    /**
     *  Avoids creating a new Vector2 each time this is called, you need to provide a Vector2
     *  You need to provide a Vector2 to store the resulting position though
     * @param result the vector provided to store the position
     * @return the position vector for concatenation of methods
     */
    public Vector2 getPosition(Vector2 result) {
        return result.set(getX(), getY());
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }

    // Font/Text related
    public BitmapFont getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateBounds();
    }

    public int getHAlign() {
        return hAlign;
    }

    public void hAlignLeft() {
        hAlign = Align.left;
    }

    public void hAlignCenter() {
        hAlign = Align.center;
    }

    public void hAlignRight() {
        hAlign = Align.right;
    }

    public void setVerticallyCentered(boolean centered) {
        verticallyCentered = centered;
        yOffset = centered ? bounds.height / 2f : 0f;
    }

    public String getTruncate() {
        return truncate;
    }

    public void setTruncate(String truncate) {
        this.truncate = truncate;
        updateBounds();
    }

    public float getScale() {
        return font.getScaleX();
    }

    public void setScale(float scale) {
        font.getData().setScale(scale);
        updateBounds();
    }

    public float getWidth() {
        return bounds.width;
    }

    public void setWidth(float width) {
        bounds.width = width;
    }

    private void updateBounds() {
        bounds.height = TextUtils.getTextHeight(this);
        yOffset = verticallyCentered ? bounds.height / 2f : 0f;
    }

    public Color getColor() {
        return font.getColor();
    }

    public void setColor(Color color) {
        font.setColor(color);
    }
}
