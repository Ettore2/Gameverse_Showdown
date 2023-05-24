package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class Projectile implements GameObject{
    public static final String PROJECTILE_OLLIDER_TAG = "projectile collider";
    public static final String PROJECTILE_OBJ_TAG = "projectile obj";
    public static ModelInstance mario_projectile_instance = new ModelInstance(new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal(Character.CHARACTERS_MODELS_DIRECTORY + Character.CHARACTERS_MODELS_FIlE[0])));


    static class projectile_mario_prove extends Projectile{
        projectile_mario_prove(Character c) {
            super(c,"c05attackairn", 1,mario_projectile_instance,new Point2D.Float(0,-1.8f),new Point2D.Float(0,1.8f), 3, 2, 80, 5);
            velocity = new Vector2(c.facingDirection * 0.05f,0);
            knockback = new Vector2(c.facingDirection * 0.03f,0);

            addCollider(new CircularCollider(this,creator,0,0,PROJECTILE_OLLIDER_TAG,0.1f));
        }


    }



    //aggiungere tutti i modelli 3d di tutti i proiettili
    Character creator;
    String tag;
    Point2D.Float position, modelRelativePosition;
    ;
    Vector2 knockback, velocity;
    ModelInstance model;
    int lifeDamage, guardDamage;
    int frameOfExecution;//for future use
    int frameOfLife;
    int enemyRecoveryFrames;
    Vector<Collider2D> createdColliders;

    AnimationController controller;


    Projectile(@NotNull Character creator,String animation,int animationSpeed, ModelInstance model,Point2D.Float thisRelativePos,Point2D.Float modelRelativePos, int lifeDamage, int guardDamage, int frameOfLife, int enemyRecoveryFrames){
        this.creator = creator;
        this.createdColliders = new Vector<>();
        this.creator.existingCharacterProjectiles.add(this);
        this.model = model;
        this.lifeDamage = lifeDamage;
        this.guardDamage = guardDamage;
        this.frameOfLife = frameOfLife;
        this.velocity = new Vector2(0,0);
        this.position = new Point2D.Float(creator.get2DPosition().x + thisRelativePos.x, creator.get2DPosition().y + thisRelativePos.y);
        this.modelRelativePosition = modelRelativePos;
        this.enemyRecoveryFrames = enemyRecoveryFrames;

        controller = new AnimationController(model);
        controller.setAnimation(animation, -1);
        controller.current.speed = animationSpeed;


        this.frameOfExecution = 0;
    }


    //metodi da non overrydare
    public void execute(){
        frameOfExecution++;
        if(frameOfLife > 0){
            frameOfLife--;
            //controller.update(Gdx.graphics.getDeltaTime());
        }
        if(frameOfLife == 0){
            projectileDestruction();
        }else{
            move();
        }
    }
    public void addCollider(Collider2D col){
        this.createdColliders.add(col);
        creator.addCollider(col);
    }
    public void remoreCollider(Collider2D col){
        if(this.createdColliders.contains(col)){
            this.createdColliders.remove(col);
            creator.removeCollider(col);
        }

    }
    public void removeAllColliders(){
        for(Collider2D col : createdColliders){
            creator.removeCollider(col);
        }
    }


    //metodi da overrydare
    private void move() {
        this.position.x += velocity.x;
        this.position.y += velocity.y;
    }
    public void hit(@NotNull Character c){
        if(c.grounded){
            if(c.guarding){
                c.controller.setAnimation(c.guardHitAnimation,1);
                c.controller.current.time = 0;
                c.controller.current.speed = c.guardAnimationSpeed;
                c.currentGuardAmount -= guardDamage;
            }else{
                c.controller.setAnimation(c.normalHitAnimation,1);
                c.controller.current.time = 0;
                c.controller.current.speed = c.normalHitAnimationSpeed;
                c.currentLife -= lifeDamage;

                applyKnockBack(c);
            }
        }else{
            c.controller.setAnimation(c.airHitAnimation,1);
            c.controller.current.time = 0;
            c.controller.current.speed = c.airHitAnimationSpeed;
            c.currentLife -= lifeDamage;

            applyKnockBack(c);
        }

        if(c.currentLife < 0){
            c.currentLife = 0;
        }
        if(c.currentGuardAmount < 0){
            c.currentGuardAmount = 0;
        }

        c.currentAttackState = 0;
        c.lastAttackId = Character.ATTACK_NONE;

        projectileDestruction();
    }
    public void applyKnockBack(Character c) {
        c.currentXForce = knockback.x;
        c.currentYForce = knockback.y;
    }
    private void projectileDestruction() {
        removeAllColliders();
        creator.existingCharacterProjectiles.remove(this);

    }


    //interfacce
    public void collision(Collider2D myCollider, Collider2D otherCollider) {
        if(otherCollider.tag.equals(Character.BODY_COLLIDER_TAG)){
            projectileDestruction();
        }


    }//programmare che se esco da schermo devo cancellarmi
    public Point2D.Float get2DPosition() {
        //Point2D.Float posTmp = creator.get2DPosition();
        //return new Point2D.Float(posTmp.x + this.position.x, posTmp.y + this.position.y);
        return (Point2D.Float) this.position.clone();
    }
    public void setX2DPosition(float x) {
        this.position.x = x;

    }
    public void setY2DPosition(float y) {
        this.position.y = y;

    }
    public void set2DPosition(Point.Float p) {
        this.position = p;

    }
    public String getTag() {
        return tag;

    }
    public void setTag(String tag) {
        this.tag = tag;

    }
}
