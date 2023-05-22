package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;

public abstract class Collider2D implements HaveTag, HavePosition2D{
    public static boolean DEFAULT_VISIBILITY = false;
    public static final int SQUARE = 0, CIRCLE = 1;
    public static final Color colorColliders = new Color(0f,1f,0f,1f);//trasparenza non funziona


    public final int type;
    protected String tag;
    protected Point2D.Float center;
    public boolean isActive,useRelativePosition,isVisible;
    protected GameObject owner;

    Collider2D(int type, @NotNull GameObject owner, @NotNull Point2D.Float center, String tag){
        this.center = center;
        this.type = type;
        this.owner = owner;
        this.tag = tag;

        isActive = true;
        useRelativePosition = true;
        isVisible = DEFAULT_VISIBILITY;
    }
    Collider2D(int type,@NotNull GameObject owner,float xCenter, float yCenter,String tag){
        this(type, owner, new Point2D.Float(xCenter, yCenter),tag);

    }
    Collider2D(int type, @NotNull GameObject owner, @NotNull Point2D.Float center){
        this(type, owner, center,  "");
    }
    Collider2D(int type,@NotNull GameObject owner,float xCenter, float yCenter){
        this(type, owner, new Point2D.Float(xCenter, yCenter), "");

    }

    //setters
    public void setOwner(@NotNull GameObject owner) {
        this.owner = owner;

    }

    //getters
    public HavePosition2D getOwner() {
        return owner;

    }

    //altri metodi

    public Point2D.Float get2DPosition() {
        if(useRelativePosition){
            return new Point2D.Float(this.center.x + owner.get2DPosition().x, this.center.y + owner.get2DPosition().y);
        }else{
            return new Point2D.Float(this.center.x, this.center.y);
        }

    }
    public void set2DPosition(Point2D.Float p){
        this.center = p;

    }

    public void setX2DPosition(float x) {
        this.center.x = x;

    }
    public void setY2DPosition(float y) {
        this.center.y = y;

    }
    public boolean isColliding(Collider2D col){
        System.out.println("metodo isColliding collide2D");
        return false;
    }
    public void collision(Collider2D col){
        owner.collision(this,col);
    }

    public abstract void draw(ShapeRenderer renderer);


}
