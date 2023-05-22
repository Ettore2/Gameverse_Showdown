package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public class GenericGameObject extends Actor implements GameObject{
    Point2D.Float center;
    String tag;
    Vector<Collider2D> colliders;



    public GenericGameObject(Point2D.Float p, String tag){
        this.center = p;
        this.tag = tag;
        colliders=new Vector<>();
    }
    public GenericGameObject(Point2D.Float p){
        this(p, "");
    }
    public GenericGameObject(float x, float y, String tag){
        this(new Point2D.Float(x, y), tag);
    }
    public GenericGameObject(float x, float y){
        this(new Point2D.Float(x, y));
    }



    public void act(float deltaTime){

    }

    public void drawColliders(ShapeRenderer renderer){
        for(Collider2D col : colliders){
            if(col.type == Collider2D.SQUARE){
                ((BoxCollider)col).draw(renderer);
            }
            if(col.type == Collider2D.CIRCLE){
                ((CircularCollider)col).draw(renderer);
            }
        }

    }
    public void draw(Batch batch, float parentAlfa){}//metodo di actor

    public Point2D.Float get2DPosition() {
        return center;

    }
    public void set2DPosition(Point.Float p) {
        this.center = p;

    }

    public void setX2DPosition(float x) {
        this.center.x = x;
    }

    public void setY2DPosition(float y) {
        this.center.y = y;

    }

    public String getTag() {
        return tag;

    }
    public void setTag(String tag) {
        this.tag = tag;

    }


    public void collision(Collider2D myCollider, Collider2D otherCollider) {

    }
}
