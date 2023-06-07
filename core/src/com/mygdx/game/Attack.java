package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class Attack implements GameObject{
    static final String ATTACK_COLLIDER_TAG = "attack collider";
    static final String ATTACK_OBJ_TAG = "attack obj";
    static final int HITTED_NONE = 0,HITTED_LIFE = 1,HITTED_GUARD = 2;



    Character creator;
    String tag;
    Point2D.Float relativePosition;
    String animationName;
    float animationSpeed;
    int lifeDamage, guardDamage;
    Vector2 knockBack;
    int framesOfExecution;
    int enemyRecoveryFrames;//frame di stun per nemico che viene colpito da questo attacco;
    Vector<Collider2D> createdColliders;
    boolean applyContinuousKnockBack;
    int lastThingHitted;
    int knockBackType,multipleHitsDelay, framesToNextHit;//fra quanti frame puoi hittare di nuovo (-1 = non puoi)


    protected Attack(@NotNull Character c, @NotNull String animationName, float animationSpeed, int lifeDamage, int guardDamage, int enemyRecoveryFrames){
        this.relativePosition = new Point2D.Float(0,0);
        this.tag = ATTACK_COLLIDER_TAG;

        this.creator = c;
        this.animationName = animationName;
        this.animationSpeed = animationSpeed;
        this.lifeDamage =  lifeDamage;
        this.guardDamage =  guardDamage;
        this.enemyRecoveryFrames = enemyRecoveryFrames;

        //parametri default
        framesOfExecution = 0;
        createdColliders = new Vector<>();
        knockBack = new Vector2(0,0);
        applyContinuousKnockBack = false;
        multipleHitsDelay = -1;
        framesToNextHit = 0;
        lastThingHitted = HITTED_NONE;

    }



    public void execute(){
        framesOfExecution++;//commento per fare frame debug

        if(framesToNextHit > 0){
            framesToNextHit--;
        }


    }


    public boolean canHit(){
        return framesToNextHit == 0;
    }
    public void hit(@NotNull Character c){

        //continua a infliggere stun -> combo costanti indipendentemente al frame in cui è avvenuta la hit
        if(c.guarding){
            c.currentStunFrames = Character.GUARD_HIT_STUN_FRAMES;
        }else{
            c.currentStunFrames = this.enemyRecoveryFrames;
        }

        //continua a infliggere knocback -> combo costanti indipendentemente al frame in cui è avvenuta la hit
        if(!c.guarding && (applyContinuousKnockBack || canHit())){
            applyKnockBack(c);
        }

        if(canHit()){//fa danno 1 volta
            if(c.grounded){
                if(c.guarding){
                    c.controller.setAnimation(c.guardHitAnimation,1);
                    c.controller.current.time = 0;
                    c.controller.current.speed = c.guardAnimationSpeed;
                    c.currentGuardAmount -= guardDamage;
                    lastThingHitted = HITTED_GUARD;
                }else{
                    c.controller.setAnimation(c.normalHitAnimation,1);
                    c.controller.current.time = 0;
                    c.controller.current.speed = c.normalHitAnimationSpeed;
                    c.currentLife -= lifeDamage;
                    lastThingHitted = HITTED_LIFE;

                }
            }else{
                c.controller.setAnimation(c.airHitAnimation,1);
                c.controller.current.time = 0;
                c.controller.current.speed = c.airHitAnimationSpeed;
                c.currentLife -= lifeDamage;
                lastThingHitted = HITTED_LIFE;

            }

            if(c.currentLife < 0){
                c.currentLife = 0;
            }
            if(c.currentGuardAmount < 0){
                c.currentGuardAmount = 0;
            }

            framesToNextHit = multipleHitsDelay;

            c.currentAttackState = 0;
            c.lastAttackId = Character.ATTACK_NONE;
        }//fatto 1 sola volta


        if(c.guarding && c.currentGuardAmount == 0){//se ho rotto la guardia
            c.currentStunFrames = Character.GUARD_BREAKE_STUNN_FRAMES;
            c.currentXForce = this.creator.facingDirection * Character.GUARD_BREAKE_X_CNOCKBACK;
            c.guarding = false;
            c.guardRegenerationFramesCounter = 0;
            c.guardRegenerationRateoFramesCounter = 0;
        }//se ho rotto la guardia in questa esecuzione

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
    public void setKnockBack(Vector2 knockBack, int KnockBackType){
        this.knockBack = knockBack;
        this.knockBackType = KnockBackType;
    }
    public void setKnockBack(Vector2 knockBack){
        setKnockBack(knockBack,0);

    }
    public void setKnockBack(float xVal, float yVal){
        setKnockBack(new Vector2(xVal, yVal));

    }
    public void setKnockBack(float xVal, float yVal, int KnockBackType){
        setKnockBack(new Vector2(xVal, yVal), KnockBackType);

    }
    public abstract void applyKnockBack(Character c);





    public void collision(Collider2D myCollider, Collider2D otherCollider) {
        //creator.collision(myCollider, otherCollider);
    }
    public Point2D.Float get2DPosition() {
        Point2D.Float posTmp = creator.get2DPosition();
        return new Point2D.Float(posTmp.x + this.relativePosition.x, posTmp.y + this.relativePosition.y);
    }
    public void setX2DPosition(float x) {
        this.relativePosition.x = x;

    }
    public void setY2DPosition(float y) {
        this.relativePosition.y = y;

    }
    public void set2DPosition(Point.Float p) {
        this.relativePosition = p;

    }
    public String getTag() {
        return tag;

    }
    public void setTag(String tag) {
        this.tag = tag;

    }
}
