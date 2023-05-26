package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class Projectile implements GameObject{
    public static final String PROJECTILE_OLLIDER_TAG = "projectile collider";
    public static final String PROJECTILE_OBJ_TAG = "projectile obj";

    static class projectile_mario_prove extends Projectile{
        projectile_mario_prove(Character c) {
            super(c,c.walkAnimation, 1,Character.MARIO_PROVE_PROJECTILE,new Point2D.Float(0,-1.8f),new Point2D.Float(0,1.8f), 3, 2, 80, 4);
            velocity = new Vector2(c.facingDirection * 0.05f,0);
            knockback = new Vector2(c.facingDirection * 0.02f,0);

            addCollider(new CircularCollider(this,creator,0,0,PROJECTILE_OLLIDER_TAG,0.1f));
        }


    }



    //aggiungere tutti i modelli 3d di tutti i proiettili
    Character creator;
    String tag;
    Point2D.Float position, modelRelativePosition;
    Vector2 knockback, velocity;
    ModelInstance model;
    int lifeDamage, guardDamage;
    int frameOfExecution;//for future use
    int frameOfLife;
    int enemyRecoveryFrames, modelId;
    Vector<Collider2D> createdColliders;

    AnimationController controller;
    int facingDirection;
    boolean haveHitted;


    Projectile(@NotNull Character creator,String animation,int animationSpeed, int modelId,Point2D.Float thisRelativePos,Point2D.Float modelRelativePos, int lifeDamage, int guardDamage, int frameOfLife, int enemyRecoveryFrames){
        this.creator = creator;
        this.createdColliders = new Vector<>();
        this.creator.existingCharacterProjectiles.add(this);
        this.modelId = modelId;
        this.lifeDamage = lifeDamage;
        this.guardDamage = guardDamage;
        this.frameOfLife = frameOfLife;
        this.velocity = new Vector2(0, 0);
        this.position = new Point2D.Float(creator.get2DPosition().x + thisRelativePos.x, creator.get2DPosition().y + thisRelativePos.y);
        this.modelRelativePosition = modelRelativePos;
        this.enemyRecoveryFrames = enemyRecoveryFrames;
        this.facingDirection = creator.facingDirection;

        haveHitted = false;


        this.model = Character.getProjectile(modelId);
        if(this.model != null){
            this.model.transform.setToRotation(0,1,0,90 * facingDirection);
            this.model.transform.setTranslation(ModelAbsoluteYPosition(),ModelAbsoluteYPosition(),0);

            controller = new AnimationController(model);
            controller.setAnimation(animation, -1);
            controller.current.speed = animationSpeed;
        }



        this.frameOfExecution = 0;
    }


    //metodi da non overrydare
    public void execute(){
        frameOfExecution++;
        if(frameOfLife > 0){
            frameOfLife--;
            move();
            if(this.model != null){
                this.model.transform.setToRotation(0,1,0,90 * facingDirection);
                this.model.transform.setTranslation(ModelAbsoluteXPosition(),ModelAbsoluteYPosition(),0);
                controller.update(Gdx.graphics.getDeltaTime());
            }

        }
        if(frameOfLife == 0){
            projectileDestruction();
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
    public float ModelAbsoluteXPosition(){
        return this.position.x + modelRelativePosition.x;

    }
    public float ModelAbsoluteYPosition(){
        return this.position.y + modelRelativePosition.y;

    }


    //metodi da overrydare
    private void move() {
        this.position.x += velocity.x;
        this.position.y += velocity.y;
    }
    public void hit(@NotNull Character c){
        if(!haveHitted) {//fa danno 1 volta
            if (c.grounded) {
                if (c.guarding) {
                    c.controller.setAnimation(c.guardHitAnimation, 1);
                    c.controller.current.time = 0;
                    c.controller.current.speed = c.guardAnimationSpeed;
                    c.currentGuardAmount -= guardDamage;
                } else {
                    c.controller.setAnimation(c.normalHitAnimation, 1);
                    c.controller.current.time = 0;
                    c.controller.current.speed = c.normalHitAnimationSpeed;
                    c.currentLife -= lifeDamage;

                    applyKnockBack(c);
                }
            } else {
                c.controller.setAnimation(c.airHitAnimation, 1);
                c.controller.current.time = 0;
                c.controller.current.speed = c.airHitAnimationSpeed;
                c.currentLife -= lifeDamage;

                applyKnockBack(c);
            }

            if (c.currentLife < 0) {
                c.currentLife = 0;
            }
            if (c.currentGuardAmount < 0) {
                c.currentGuardAmount = 0;
            }

            c.currentAttackState = 0;
            c.lastAttackId = Character.ATTACK_NONE;
        }

        if(c.guarding){
            c.currentStunFrames = Character.GUARD_HIT_STUN_FRAMES;
        }else{
            c.currentStunFrames = this.enemyRecoveryFrames;
        }

        if(c.guarding && c.currentGuardAmount == 0){//se ho rotto la guardia
            c.currentStunFrames = Character.GUARD_BREAKE_STUNN_FRAMES;
            c.currentXForce = this.creator.facingDirection * Character.GUARD_BREAKE_X_CNOCKBACK;
            c.guarding = false;
            c.guardRegenerationFramesCounter = 0;
            c.guardRegenerationRateoFramesCounter = 0;
        }//se ho rotto la guardia in questa esecuzione

    }
    public void applyKnockBack(Character c) {
        c.currentXForce = knockback.x;
        c.currentYForce = knockback.y;
    }
    private void projectileDestruction() {
        removeAllColliders();
        creator.existingCharacterProjectiles.remove(this);
        if(model != null){
            Character.AVAILABLE_PROJECTILE_MODELS[modelId].add(model);//rendo di nuovo disponibile il modello
        }


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
