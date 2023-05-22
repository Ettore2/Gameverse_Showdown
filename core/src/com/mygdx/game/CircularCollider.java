package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;

public class CircularCollider extends Collider2D{
    float radius;
    private ShapeRenderer renderer;

    //costruttori
    CircularCollider(@NotNull GameObject owner, @NotNull Point2D.Float center, String tag, float radius) {
        super(CIRCLE, owner, center, tag);
        this.radius = radius;
    }
    CircularCollider(@NotNull GameObject owner, float xCenter, float yCenter, String tag, float radius) {
        this(owner, new Point2D.Float(xCenter, yCenter), tag, radius);

    }
    CircularCollider(@NotNull GameObject owner, @NotNull Point2D.Float center, float radius) {
        super(CIRCLE, owner, center);
        this.radius = radius;
    }
    CircularCollider(@NotNull GameObject owner, float xCenter, float yCenter, float radius) {
        this(owner, new Point2D.Float(xCenter, yCenter), radius);

    }


    //altri metodi
    public void draw(ShapeRenderer renderer){
        if(isVisible){
            renderer.setColor(colorColliders);
            renderer.circle((get2DPosition().x + 4.85f)* Gdx.graphics.getWidth()/9.7f , (get2DPosition().y + 2.7f)*Gdx.graphics.getHeight()/5.4f,radius*Gdx.graphics.getWidth()/9.7f);
        }

    }

    public boolean isColliding(Collider2D col) {
        if(col.type == SQUARE){
            return col.isColliding(this);
        }
        if(col.type == CIRCLE){
            return this.get2DPosition().distance(col.get2DPosition()) <= this.radius + ((CircularCollider)col).radius;
        }

        return false;
    }
    public void setDimensions(Point2D.Float center, float radius){
        this.center = center;
        this.radius = radius;

    }


    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}
