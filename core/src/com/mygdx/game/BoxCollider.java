package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;

public class BoxCollider extends Collider2D{
    public float width,height;

    //costruttori
    BoxCollider(@NotNull GameObject owner, @NotNull GameObject absoluteOwner, @NotNull Point2D.Float center, String tag, float width, float height) {
        super(SQUARE, owner, absoluteOwner, center, tag);
        this.width = width;
        this.height = height;
    }
    BoxCollider(@NotNull GameObject owner, @NotNull Point2D.Float center, String tag, float width, float height) {
        this(owner, owner, center, tag, width, height);
    }
    BoxCollider(@NotNull GameObject owner, @NotNull Point2D.Float center, float width, float height) {
        this(owner, owner, center, "", width, height);
    }

    BoxCollider(@NotNull GameObject owner, @NotNull GameObject absoluteOwner, float xCenter, float yCenter, String tag, float width, float height) {
        this(owner, absoluteOwner, new Point2D.Float(xCenter,yCenter), tag, width, height);

    }
    BoxCollider(@NotNull GameObject owner, float xCenter, float yCenter, String tag, float width, float height) {
        this(owner, owner, new Point2D.Float(xCenter,yCenter), tag, width, height);

    }
    BoxCollider(@NotNull GameObject owner, float xCenter, float yCenter, float width, float height) {
        this(owner, new Point2D.Float(xCenter,yCenter), width, height);

    }


    //altri metodi

    public float getLeftLate(){
            return this.get2DPosition().x-(this.width/2);
    }
    public float getRightLate(){
            return this.get2DPosition().x+(this.width/2);
    }
    public float getUpLate(){
        return this.get2DPosition().y+(this.height/2);
    }
    public float getDownLate(){
        return this.get2DPosition().y-(this.height/2);
    }
    public Point2D.Float pointDownLeft(){
        return new Point2D.Float(get2DPosition().x-(width/2),get2DPosition().y-(height/2));
    }
    public Point2D.Float pointUpLeft(){
        return new Point2D.Float(get2DPosition().x-(width/2),get2DPosition().y+(height/2));

    }
    public Point2D.Float pointDownRight(){
        return new Point2D.Float(get2DPosition().x+(width/2),get2DPosition().y-(height/2));

    }
    public Point2D.Float pointUpRight(){
        return new Point2D.Float(get2DPosition().x+(width/2),get2DPosition().y+(height/2));

    }

    @Override
    public boolean isColliding(Collider2D col) {
        if(col.type == SQUARE){
            BoxCollider boxCol=(BoxCollider)col;
            return ((this.getLeftLate()-boxCol.getRightLate())*(this.getRightLate()-boxCol.getLeftLate())<0 && (this.getDownLate()-boxCol.getUpLate())*(this.getUpLate()-boxCol.getDownLate())<0);
        }
        if(col.type == CIRCLE){

            //se il cerchio è dentro a il quadrato
            if(getLeftLate() <= col.get2DPosition().x && getRightLate() >= col.get2DPosition().x && getDownLate() <= col.get2DPosition().y && getUpLate() >= col.get2DPosition().y){
                return true;
            }

            boolean greaterThanD1 = false,greaterThanD2 = false;
            float m,q;

            //diagonale 1
            m = (pointUpLeft().y - pointDownRight().y) / (pointUpLeft().x - pointDownRight().x);
            q = pointDownRight().y - m*pointDownRight().x;
            if(col.get2DPosition().y >= m * col.get2DPosition().x + q){
                greaterThanD1 = true;
            }
            //diagonale 2
            m = (pointDownLeft().y - pointUpRight().y) / (pointDownLeft().x - pointUpRight().x);
            q = pointDownLeft().y - m*pointDownLeft().x;
            if(col.get2DPosition().y >= m * col.get2DPosition().x + q){
                greaterThanD2 = true;
            }

            //selezione casi
            Point2D.Float A = null,B = null,newCircleCenter = null;
            if(greaterThanD1 && greaterThanD2){
                A = pointUpLeft();
                B = pointUpRight();
                newCircleCenter = new Point.Float(col.get2DPosition().x, col.get2DPosition().y);
            }//cerchio è sopra
            if(greaterThanD1 && !greaterThanD2){
                A = new Point2D.Float(pointDownRight().y,pointDownRight().x);
                B = new Point2D.Float(pointUpRight().y,pointUpRight().x);
                newCircleCenter = new Point.Float(col.get2DPosition().y, col.get2DPosition().x);
            }//cerchio è a destra
            if(!greaterThanD1 && greaterThanD2){
                A = new Point2D.Float(pointDownLeft().y,pointDownLeft().x);
                B = new Point2D.Float(pointUpLeft().y,pointUpLeft().x);
                newCircleCenter = new Point.Float(col.get2DPosition().y, col.get2DPosition().x);
            }//cerchio è a sinistra
            if(!greaterThanD1 && !greaterThanD2){
                A = pointDownLeft();
                B = pointDownRight();
                newCircleCenter = new Point.Float(col.get2DPosition().x, col.get2DPosition().y);
            }//cerchio è sotto

            //x punto compresa tra AB
            if(A.x <= newCircleCenter.x && B.x >= newCircleCenter.x){
                return Math.abs(newCircleCenter.y-A.y) <= ((CircularCollider)col).radius;
            }else{//x punto non compresa tra AB
                float xTranslation;
                if(newCircleCenter.x<A.x){
                    xTranslation = Math.abs(A.x-newCircleCenter.x);
                }else{
                    xTranslation = Math.abs(B.x-newCircleCenter.x);
                }

                return Math.sqrt(Math.pow(xTranslation,2) + Math.pow(A.y - newCircleCenter.y,2)) <= ((CircularCollider)col).radius;
            }

        }

        return false;
    }
    public void draw(ShapeRenderer renderer){
        if(isVisible) {
            renderer.setColor(colorColliders);
            renderer.rect((get2DPosition().x - width/2 + 4.85f) * Gdx.graphics.getWidth()/9.7f , (get2DPosition().y - height/2 + 2.7f) * Gdx.graphics.getHeight()/5.4f,width * Gdx.graphics.getWidth()/9.7f, height*Gdx.graphics.getHeight()/5.4f);
        }

    }
    public void setDimensions(Point2D.Float center, float width, float height){
        this.center = center;
        this.width = width;
        this.height = height;

    }


    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}












