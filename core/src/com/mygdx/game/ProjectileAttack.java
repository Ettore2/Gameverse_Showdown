package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;

public abstract class ProjectileAttack extends Attack{
    public static final String PROJECTILE_ATTACK_OLLIDER_TAG = "projectile attack collider";
    public static final String PROJECTILE_ATTACK_OBJ_TAG = "projectile attack obj";
    final int KNOCKBACK_NORMAL = 0, KNOCKBACK_DIRECTIONAL_X = 1, KNOCKBACK_DIRECTIONAL_Y = 2, KNOCKBACK_DIRECTIONAL_XY = 3, KNOCKBACK_CUSTOM = -1;
    public static final int BOUNCE_NONE = -1;// qualunque cosa minore di 0

    static class projectile_mario_prove extends ProjectileAttack {
        projectile_mario_prove(Character c) {
            super(c,c.walkAnimation, 1,Character.MARIO_PROVE_PROJECTILE,new Point2D.Float(0,-1.8f),new Point2D.Float(0,1.8f), 3, 2, 80, 4);
            velocity = new Vector2(c.facingDirection * 0.05f,0);
            knockBack = new Vector2(c.facingDirection * 0.02f,0);

            addCollider(new CircularCollider(this,creator,0,0, PROJECTILE_ATTACK_OLLIDER_TAG,0.1f));
        }


    }



    //aggiungere tutti i modelli 3d di tutti i proiettili
    Point2D.Float modelRelativePosition;
    Vector2 velocity;
    ModelInstance model;
    int totalFramesOfExecution;
    int modelId;

    AnimationController controller;
    int projectileFacingDirection;
    float weight, XBounciness, YBounciness;


    ProjectileAttack(@NotNull Character creator, String animation, int animationSpeed, int modelId, Point2D.Float thisRelativePos, Point2D.Float modelRelativePos, int lifeDamage, int guardDamage, int totalFramesOfExecution, int enemyRecoveryFrames){
        super(creator, animation, animationSpeed, lifeDamage, guardDamage, enemyRecoveryFrames);

        this.relativePosition = thisRelativePos;
        this.modelRelativePosition = modelRelativePos;this.tag = PROJECTILE_ATTACK_OLLIDER_TAG;
        this.creator.existingCharacterProjectiles.add(this);

        this.modelId = modelId;
        this.totalFramesOfExecution = totalFramesOfExecution;
        this.velocity = new Vector2(0, 0);
        this.projectileFacingDirection = creator.facingDirection;
        this.weight = 0;
        this.XBounciness = BOUNCE_NONE;
        this.YBounciness = BOUNCE_NONE;


        this.model = Character.getProjectile(modelId);
        if(this.model != null){
            this.model.transform.setToRotation(0,1,0,90 * projectileFacingDirection);
            this.model.transform.setTranslation(ModelAbsoluteYPosition(),ModelAbsoluteYPosition(),0);

            controller = new AnimationController(model);
            controller.setAnimation(animation, -1);
            controller.current.speed = animationSpeed;
        }

    }


    //metodi da non overrydare
    public void execute(){
        super.execute();

        if(totalFramesOfExecution > framesOfExecution){
            move();
            this.velocity.y -= weight;

            if(this.model != null){
                this.model.transform.setToRotation(0,1,0,90 * projectileFacingDirection);
                this.model.transform.setTranslation(ModelAbsoluteXPosition(),ModelAbsoluteYPosition(),0);
                controller.update(Gdx.graphics.getDeltaTime());
            }

        }else{
            projectileMissDestruction();
        }
    }
    private void projectileDeletion(){
        removeAllColliders();
        creator.existingCharacterProjectiles.remove(this);
        if(model != null){
            Character.AVAILABLE_PROJECTILE_MODELS[modelId].add(model);//rendo di nuovo disponibile il modello
        }
    }
    public float ModelAbsoluteXPosition(){
        return this.relativePosition.x + modelRelativePosition.x;

    }
    public float ModelAbsoluteYPosition(){
        return this.relativePosition.y + modelRelativePosition.y;

    }
    public void applyKnockBack(@NotNull Character c){
        if(knockBackType == KNOCKBACK_NORMAL){
            KNOCKBACK_NORMAL(c);
        }
        if(knockBackType == KNOCKBACK_DIRECTIONAL_X){
            KNOCKBACK_DIRECTIONAL_X(c);
            System.out.println("ciao");
        }
        if(knockBackType == KNOCKBACK_DIRECTIONAL_Y){
            KNOCKBACK_DIRECTIONAL_Y(c);
            System.out.println("ciao");
        }
        if(knockBackType == KNOCKBACK_DIRECTIONAL_XY){
            KNOCKBACK_DIRECTIONAL_XY(c);
            System.out.println("ciao");
        }
        if(knockBackType == KNOCKBACK_CUSTOM){
            KNOCKBACK_CUSTOM(c);
            System.out.println("ciao");
        }
        System.out.println(c.currentXForce);

    }


    protected void KNOCKBACK_NORMAL(@NotNull Character c){
        c.currentXForce =  knockBack.x;
        c.currentYForce = knockBack.y;
    }
    protected void KNOCKBACK_DIRECTIONAL_X(@NotNull Character c){
        c.currentYForce = knockBack.y;
        if(c.get2DPosition().x >= get2DPosition().x){
            c.currentXForce = knockBack.x;
        }else{
            c.currentXForce = knockBack.x * -1;
        }
    }
    protected void KNOCKBACK_DIRECTIONAL_Y(@NotNull Character c){
        c.currentXForce = knockBack.x;
        if(c.currentBodyCollider.get2DPosition().y >= get2DPosition().y){
            c.currentYForce = knockBack.y;
        }else{
            c.currentYForce = knockBack.y * -1;
        }
    }
    protected void KNOCKBACK_DIRECTIONAL_XY(@NotNull Character c){
        if(c.get2DPosition().x >= get2DPosition().x){
            c.currentXForce = knockBack.x;
        }else{
            c.currentXForce = knockBack.x * -1;
        }

        if(c.currentBodyCollider.get2DPosition().y >= get2DPosition().y){
            c.currentYForce = knockBack.y;
        }else{
            c.currentYForce = knockBack.y * -1;
        }
    }
    private void KNOCKBACK_CUSTOM(@NotNull Character c){}


    //metodi da overrydare
    private void move() {
        this.relativePosition.x += velocity.x;
        this.relativePosition.y += velocity.y;
    }
    private void projectileHitDestruction() {
        projectileDeletion();

    }
    private void projectileMissDestruction() {
        projectileDeletion();

    }
    public void groundCollision(){
        if(YBounciness >= 0){
            this.velocity.y = -this.velocity.y * YBounciness;
        }else{
            projectileMissDestruction();
        }

    }
    private void lateralBoundsCollision() {
        if(XBounciness >= 0){
            this.velocity.x = -this.velocity.x * YBounciness;
        }else{
            projectileMissDestruction();
        }
    }


    //interfacce
    public void collision(Collider2D myCollider, Collider2D otherCollider) {
        if(otherCollider.tag.equals(Character.BODY_COLLIDER_TAG) && multipleHitsDelay == -1){
            projectileHitDestruction();
        }
        if(otherCollider.tag.equals(BattleStage.GROUND_TAG)){
            groundCollision();
        }
        if(otherCollider.tag.equals(BattleStage.LEFT_BOUND_TAG) || otherCollider.tag.equals(BattleStage.RIGHT_BOUND_TAG)){
            lateralBoundsCollision();
        }

        //potrei gestire collisioni tra proiettili e altri proiettili o proiettili e mosse

    }



}
