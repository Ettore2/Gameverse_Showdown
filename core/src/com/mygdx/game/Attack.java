package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class Attack implements GameObject{
    //ricordo di dare tag a colliders (lo posso fare da costruttore)
    //usare metodi per settare attributi di attacchi
    static final String ATTACK_COLLIDER_TAG = "attack collider";
    static final String ATTACK_OBJ_TAG = "attack obj";
    //settare absolute owner

    /*
    c01attacks3s calcio in mezzo
    c02attackhi3 montante
    c02attacklw3 spazzata con la gamba
    c03attacks4s manata in mezzo (brutta)
    c04attackhi4 testata
    c04attacklw4 breakdance
    c05attackairn calcio in aria
     */

    //MARIO
    static class Attack_mario_grounded_x_0 extends Attack{
        Attack_mario_grounded_x_0(Character c) {
            super(c,"c00attack11",1.6f,8,10,7,20,16,17,25,20);
            setknockback(0.025f,0);
        }
        public void firstActiveFrame(){
            super.firstActiveFrame();

            BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.27f,-1.78f, ATTACK_COLLIDER_TAG,0.3f,0.1f);
            addCollider(colTmp);
        }
    }//pugno 1 (combo 0)
    static class Attack_mario_grounded_x_1 extends Attack{
        Attack_mario_grounded_x_1(Character c) {
            super(c,"c00attack12",1.6f,10,12,5,15,21,24,30,36);
            setknockback(0.025f,0);
        }
        public void firstActiveFrame(){
            super.firstActiveFrame();

            BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.43f,-1.76f, ATTACK_COLLIDER_TAG,0.4f,0.13f);
            addCollider(colTmp);
        }
    }//pugno 2 (combo 1)
    static class Attack_mario_grounded_x_2 extends Attack{
        Attack_mario_grounded_x_2(Character c) {
            super(c,"c00attack13",1.6f,13,15,7,25,6,8,18,30);
            setknockback(0.132f,0.141f);
        }
        public void firstActiveFrame(){
            super.firstActiveFrame();

            BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.3f,-1.95f, ATTACK_COLLIDER_TAG,0.1f,0.13f);
            addCollider(colTmp);

        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 4){
                ((BoxCollider)createdColliders.get(0)).setDimensions(new Point2D.Float(creator.facingDirection * 0.4f,-1.8f),0.14f,0.22f);
            }
            if(nFrame == 8){
                ((BoxCollider)createdColliders.get(0)).setDimensions(new Point2D.Float(creator.facingDirection * 0.4f,-1.5f),0.2f,0.35f);
            }
            if(nFrame == 16){
                removeAllColliders();
            }
        }
    }//calcio (combo 2)
    static class Attack_mario_grounded_y_0 extends Attack{
        Attack_mario_grounded_y_0(Character c) {
            super(c,"c04attackhi4",2f,17,30,7,26,30,30,37,32);
            setknockback(0.1f,0);
        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);
            if(nFrame == 4){
                CircularCollider colTmp = new CircularCollider(this, creator, creator.facingDirection * -0.3f,-1.65f, ATTACK_COLLIDER_TAG,0.14f);
                addCollider(colTmp);
            }
            if(nFrame == 5){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.2f,-1.55f));
            }
            if(nFrame == 11){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.1f,-1.43f));
                ((CircularCollider)createdColliders.get(0)).radius = 0.16f;
            }
            if(nFrame == 16){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.2f,-1.42f));
            }
            if(nFrame == 18){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.35f,-1.55f));
                ((CircularCollider)createdColliders.get(0)).radius = 0.18f;
            }
            if(nFrame == 20){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.448f,-1.7f));
                ((CircularCollider)createdColliders.get(0)).radius = 0.2f;
            }

        }
    }//testata (guard breaker)
    static class Attack_mario_grounded_b_0 extends Attack{
        Attack_mario_grounded_b_0(Character c) {
            super(c,"c02attackhi3",2f,6,5,10,16,10,17,26,24);
            setknockback(0.05f,0.18f);
        }
        public void firstActiveFrame() {
            super.firstActiveFrame();
            addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.17f,-1.5f, ATTACK_COLLIDER_TAG,0.12f));
        }

        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 5){
                createdColliders.get(0).setY2DPosition(-1.3f);
            }
            if(nFrame == 8){
                createdColliders.get(0).setY2DPosition(-1f);
            }
            if(nFrame == 11){
                createdColliders.get(0).setX2DPosition(creator.facingDirection * 0.10f);
                ((CircularCollider) createdColliders.get(0)).radius=0.14f;
            }
            if(nFrame == 13){
                createdColliders.get(0).setX2DPosition(creator.facingDirection * 0.04f);
            }
        }
    }//montante
    static class Attack_mario_aerial_x_0 extends Attack{
        Attack_mario_aerial_x_0(Character c) {
            super(c,"c05attackairn",4f,6,6,9,17,10,15,21,10);
            setknockback(0.13f,0.08f);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void firstActiveFrame() {
            super.firstActiveFrame();
            addCollider(new BoxCollider(this, creator, creator.facingDirection * 0.19f,-1.8f, ATTACK_COLLIDER_TAG,0.25f,0.14f));
        }
    }//calcio aereo
    static class Attack_mario_aerial_b_0 extends Attack{
        Attack_mario_aerial_b_0(Character c) {
            super(c,"c05attackairf",2.5f,12,19,11,31,18,22,25,16);
            setknockback(0.09f,-0.16f);
            this.setAllowMovementDuringActiveFrames(true);
        }

        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 16){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.39f,-1.65f, ATTACK_COLLIDER_TAG,0.12f));
            }
            if(nFrame == 17){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.49f,-1.82f));
            }
            if(nFrame == 20){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.3f,-2f));
                ((CircularCollider)createdColliders.get(0)).radius = 0.14f;
            }
            if(nFrame == 22){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.15f,-2.08f));
            }
            if(nFrame == 24){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.02f,-2f));
                ((CircularCollider)createdColliders.get(0)).radius = 0.15f;
            }
        }
    }//pugno aereo
    static class Attack_mario_aerial_y_0 extends Attack{
        Attack_mario_aerial_y_0(Character c) {
            super(c,"c05attackairhi",1.4f,7,7,9,35,9,10,21,13);
            setknockback(0,0.143f);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);
            if(nFrame == 8){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.38f,-1.15f, ATTACK_COLLIDER_TAG,0.11f));
            }
            if(nFrame == 9){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.22f,-1.115f, ATTACK_COLLIDER_TAG,0.11f));
            }
            if(nFrame == 10){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.07f,-1.110f));
            }
            if(nFrame == 11){
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * -0.08f,-1.115f));
            }
            if(nFrame == 12){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.18f,-1.110f));
            }
            if(nFrame == 13){
                remoreCollider(createdColliders.get(0));
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.28f,-1.13f));
            }
            if(nFrame == 14){
                removeAllColliders();
            }

        }
    }//calcio alto aereo

    static class Attack_mario_projectile_prove extends Attack{
        Attack_mario_projectile_prove(Character c) {
            super(c,"c05attackairhi",1.4f,7,7,18,1,17,17,17,0);
            this.setAllowMovementDuringActiveFrames(false);
        }
        public void firstActiveFrame() {
            super.firstActiveFrame();

            //instanzio il proietile
            new Projectile.projectile_mario_prove(this.creator);
        }
    }


    //BONKEY KONG
    static class Attack_donkeyKong_grounded_x_0 extends Attack{
        Attack_donkeyKong_grounded_x_0(Character c) {
            super(c,"c00attack11",1.6f,13,15,7,20,16,21,27,26);
            setknockback(0.028f,0);
        }

        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 7){
                BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.4f,-1.7f, ATTACK_COLLIDER_TAG,1f,0.34f);
                addCollider(colTmp);
            }

        }
    }//pugno 1 (combo 0)
    static class Attack_donkeyKong_grounded_x_1 extends Attack{
        Attack_donkeyKong_grounded_x_1(Character c) {
            super(c,"c00attack12",1.7f,19,50,11,30,32,36,39,36);
            setknockback(0.175f,0.19f);
        }
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 4){
                BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.65f,-1.6f, ATTACK_COLLIDER_TAG,0.8f,1.3f);
                addCollider(colTmp);
            }
            if(nFrame == 18){
                removeAllColliders();
            }

        }
    }//pugno 2 (combo 1) (guard breaker)
    static class Attack_donkeyKong_grounded_y_0 extends Attack{
        Attack_donkeyKong_grounded_y_0(Character c) {
            super(c,"c01attacks3hi",2.5f,6,4,2,26,13,21,23,21);
            setknockback(0.089f,0);
        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);
            if(nFrame == 4){
                BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.3f,-1.65f, ATTACK_COLLIDER_TAG,0.65f,0.36f);
                addCollider(colTmp);
            }
            if(nFrame > 4){
                createdColliders.get(0).setX2DPosition((float) (creator.facingDirection *( 0.3f + 0.34 * (nFrame - 4))));
            }
            if(nFrame == 7){
                removeAllColliders();
            }

        }
    }//schiaffo orizzontale (ranged poke)
    static class Attack_donkeyKong_grounded_b_0 extends Attack{
        Attack_donkeyKong_grounded_b_0(Character c) {
            super(c,"c02attackhi3",2f,9,14,7,17,13,17,20,27);
            setknockback(0.14f,0.14f);
        }

        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 6){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.63f,-1.2f, ATTACK_COLLIDER_TAG,0.235f));
            }
            if(nFrame == 7){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.63f,-1f));
            }
            if(nFrame == 9){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.12f,-0.6f, ATTACK_COLLIDER_TAG,0.235f));
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.55f,-0.6f));
            }
            if(nFrame == 10){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.2f,-0.6f));
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * -0.5f,-0.6f));
            }
            if(nFrame == 11){
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * -0.5f,-0.6f));
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.7f,-0.86f));
            }
            if(nFrame == 12){
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * -1f,-1.35f));
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -1.1f,-1.6f));
            }
            if(nFrame == 13){
                remoreCollider(createdColliders.get(0));
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -1.1f,-2f));
            }
            if(nFrame == 15){
                removeAllColliders();
            }

        }
        public void applyKnockBack(Character c){
            c.currentYForce = knockback.y;
            if(c.get2DPosition().x * this.creator.facingDirection >= this.creator.get2DPosition().x * this.creator.facingDirection){
                c.currentXForce = this.creator.facingDirection * knockback.x;
            }else{
                c.currentXForce = this.creator.facingDirection * knockback.x * -1;
            }
        }
    }//manata alta
    static class Attack_donkeyKong_aerial_x_0 extends Attack{
        Attack_donkeyKong_aerial_x_0(Character c) {
            super(c,"c05attackairn",2f,7,12,3,30,10,15,19,10);
            //c05attackairn
            setknockback(0.13f,0.08f);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 10){
                addCollider(new CircularCollider(this, creator, creator.facingDirection *  0.42f,-1.76f, ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection *  0.25f,-1.66f, ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection *  0.1f,-1.56f, ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.1f,-1.46f, ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.25f,-1.36f, ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.42f,-1.26f, ATTACK_COLLIDER_TAG,0.21f));
            }

        }

        public void applyKnockBack(Character c){
            c.currentYForce = knockback.y;
            if(c.get2DPosition().x * this.creator.facingDirection >= this.creator.get2DPosition().x * this.creator.facingDirection){
                c.currentXForce = this.creator.facingDirection * knockback.x;
            }else{
                c.currentXForce = this.creator.facingDirection * knockback.x * -1;
            }
        }
    }//tornado aereo
    static class Attack_donkeyKong_aerial_b_0 extends Attack{
        Attack_donkeyKong_aerial_b_0(Character c) {
            super(c,"c05attackairf",2.5f,13,23,3,38,19,21,25,16);
            setknockback(0.06f,-0.2f);
            this.setAllowMovementDuringActiveFrames(true);
        }

        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);


            if(nFrame == 19){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.78f,-1f, ATTACK_COLLIDER_TAG,0.21f));
            }
            if(nFrame == 20){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.88f,-1.54f));
            }
            if(nFrame == 21){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.68f,-1.9f));
            }
            if(nFrame == 22){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.3f,-2.4f));
            }
            if(nFrame == 22){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.2f,-2.4f));
            }
            if(nFrame == 24){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.07f,-2.35f));
            }
            if(nFrame == 25){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.1f,-2.35f));
            }
            if(nFrame == 26){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.17f,-2.35f));
                ((CircularCollider)createdColliders.get(0)).radius = 0.225f;
            }
            if(nFrame == 30){
                removeAllColliders();
            }
        }
    }//pugno basso aereo
    static class Attack_donkeyKong_aerial_y_0 extends Attack{
        Attack_donkeyKong_aerial_y_0(Character c) {
            super(c,"c05attackairhi",2f,8,9,3,68,14,18,20,21);
            setknockback(0.15f,0);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);
            if(nFrame == 11){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.6f,-1.3f, ATTACK_COLLIDER_TAG,0.2f));
            }
            if(nFrame == 12){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.7f,-1.75f));
            }
            if(nFrame == 24){
                removeAllColliders();
            }

        }
    }//testata aerea


    Character creator;
    String tag;
    Point2D.Float position;
    String animationName;
    float animationSpeed;
    int lifeDamage, guardDamage;
    Vector2 knockback;
    int frameCounter;//tiene conto di per quanti frame l'attacco è stato eseguito
    int startUpFrames;//frames inizializzazione dell'attacco (vulnerabilità) (artificiale rispetto ad animazione)
    int activeFrames;//frame di durata dell'attacco (comparsa hitBoxes non coincide con frame 0 peré animazione ha già startup frames)
    int lifeHitRecoveryFrames;//frame di "stun" finale se l'attacco colpisce nemico non bloccante (artificiale rispetto ad animazione)
    int guardHitRecoveryFrames;//frame di "stun" finale se l'attacco colpisce nemico bloccante (artificiale rispetto ad animazione)
    int missRecoveryFrames;//frame di "stun" finale se l'attacco manca il nemico (artificiale rispetto ad animazione)
    int enemyRecoveryFrames;//frame di stun per nemico che viene colpito da questo attacco
    boolean currentlyEnableMovement, enableMovement;
    Vector<Collider2D> createdColliders;
    boolean haveHitted;


    //frame debug
    float lastTime;

    //costruttori
    private Attack(@NotNull Character c,@NotNull String animationName, float animationSpeed, int lifeDamage, int guardDamage, int startUpFrames, int activeFrames, int lifeHitRecoveryFrames, int guardHitRecoveryFrames, int missRecoveryFrames, int enemyRecoveryFrames){
        this.position = new Point2D.Float(0,0);
        this.tag = ATTACK_OBJ_TAG;

        this.creator = c;
        this.animationName = animationName;
        this.animationSpeed = animationSpeed;
        this.lifeDamage =  lifeDamage;
        this.guardDamage =  guardDamage;
        this.startUpFrames = startUpFrames;
        this.activeFrames = activeFrames;
        this.lifeHitRecoveryFrames = lifeHitRecoveryFrames;
        this.guardHitRecoveryFrames = guardHitRecoveryFrames;
        this.missRecoveryFrames = missRecoveryFrames;
        this.enemyRecoveryFrames = enemyRecoveryFrames;

        //parametri default
        currentlyEnableMovement = false;
        frameCounter = 0;
        haveHitted = false;
        createdColliders = new Vector<>();
        knockback = new Vector2(0,0);
        this.setAllowMovementDuringActiveFrames(false);
    }


    //metodo da usare per ottenere attacchi
    public static Attack getAttack(Character c,int attackId,int attackState){
        if(c.id == Character.MARIO_ID){
            switch (attackId){
                case Character.ATTACK_1_GROUNDED:
                    switch (attackState){
                        case 0:
                            return new Attack_mario_grounded_x_0(c);
                        case 1:
                            return new Attack_mario_grounded_x_1(c);
                        case 2:
                            return new Attack_mario_grounded_x_2(c);
                    }
                case Character.ATTACK_2_GROUNDED:
                    return new Attack_mario_grounded_b_0(c);
                case Character.ATTACK_3_GROUNDED:
                    return new Attack_mario_grounded_y_0(c);
                case Character.ATTACK_1_AIRBORN:
                    return new Attack_mario_aerial_x_0(c);
                case Character.ATTACK_2_AIRBORN:
                    return new Attack_mario_aerial_b_0(c);
                case Character.ATTACK_3_AIRBORN:
                    return new Attack_mario_aerial_y_0(c);
            }
        }

        if(c.id == Character.DONKEY_ID){
            switch (attackId){
                case Character.ATTACK_1_GROUNDED:
                    switch (attackState){
                        case 0:
                            return new Attack_donkeyKong_grounded_x_0(c);
                        case 1:
                            return new Attack_donkeyKong_grounded_x_1(c);
                    }
                case Character.ATTACK_2_GROUNDED:
                    return new Attack_donkeyKong_grounded_b_0(c);
                case Character.ATTACK_3_GROUNDED:
                    return new Attack_donkeyKong_grounded_y_0(c);
                case Character.ATTACK_1_AIRBORN:
                    return new Attack_donkeyKong_aerial_x_0(c);
                case Character.ATTACK_2_AIRBORN:
                    return new Attack_donkeyKong_aerial_b_0(c);
                case Character.ATTACK_3_AIRBORN:
                    return new Attack_donkeyKong_aerial_y_0(c);
            }
        }


        return null;
    }


    //metodi da non averrydare
    public void execute(){
        //System.out.print("frame: "+frameCounter+" "); //debug
        creator.autoComboTimer=Character.AUTOCOMBO_TIME_TOLLERANCE;// valore temporaneo per non far cancellare lo stato


        if(frameCounter == 0){
            attackStart();//avvio attacco
        }//codice start (1 volta a inizio attacco)

        if(frameCounter < startUpFrames){//startup
            startupFrames(frameCounter);
        }//stratup frames

        if(frameCounter >= startUpFrames && frameCounter < startUpFrames+activeFrames){//active
            activeFrames(frameCounter-startUpFrames);

            if(!creator.controller.current.animation.id.equals(this.animationName)){
                creator.controller.setAnimation(this.animationName,1);
                creator.controller.current.speed = this.animationSpeed;
            }//mi assicuro di star mostrando l'animazione
        }//active frames

        if(frameCounter >= startUpFrames+activeFrames){//recovery
            if(haveHitted && frameCounter < startUpFrames+activeFrames+ lifeHitRecoveryFrames){
                hitRecoveryFrames(frameCounter-(startUpFrames+activeFrames));
            }
            if(!haveHitted && frameCounter<startUpFrames+activeFrames+missRecoveryFrames){
                missRecoveryFrames(frameCounter-(startUpFrames+activeFrames));
            }
        }//recovery frames

        if(haveHitted){
            if(frameCounter == startUpFrames+activeFrames+ lifeHitRecoveryFrames){
                attackHitEnd();//concludo attacco
                creator.currentAttackId=Character.ATTACK_NONE;
            }//fine hit
        }else{
            if(frameCounter == startUpFrames+activeFrames+missRecoveryFrames){
                attackMissEnd();//concludo attacco
                creator.currentAttackId=Character.ATTACK_NONE;
            }//fine miss
        }//cofice fine (1 volta a fine attacco)


        frameCounter++;//commento per fare frame debug


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
    public void setknockback(Vector2 knockback){
        this.knockback = knockback;

    }
    public void setknockback(float xVal, float yVal){
        setknockback(new Vector2(xVal, yVal));

    }
    public void setAllowMovementDuringActiveFrames(boolean allowMovement){
        enableMovement = allowMovement;

    }



    //metodi che si possono averrydare (chiamando super)
    public void attackStart(){
        creator.jump=false;//sovrascrivo inputs di salto
        creator.moveDirection = Character.MOVE_STOP;
        currentlyEnableMovement = false;
        creator.controller.setAnimation(creator.idleAnimation,-1);

        this.haveHitted = false;
        //System.out.println("start"); //debug
    }
    public void startupFrames(int nFrame){
        currentlyEnableMovement = enableMovement;
        if(creator.grounded){
            creator.controller.setAnimation(creator.idleAnimation,-1);
            creator.controller.current.speed = creator.idleAnimationSpeed;
        }else{
            creator.controller.setAnimation(creator.fallingAnimation,-1);
            creator.controller.current.speed = creator.fallingAnimationSpeed;
        }

    }
    public void firstActiveFrame(){
        creator.controller.setAnimation(animationName);
        creator.controller.current.speed = animationSpeed;
        creator.controller.current.time = 0;

    }
    public void activeFrames(int nFrame){
        creator.canMove= currentlyEnableMovement;
        if(nFrame == 0){
            firstActiveFrame();
        }
    }
    public void firstRecoveryFrame(){
        removeAllColliders();
        currentlyEnableMovement = false;

    }
    public void hitRecoveryFrames(int nFrame){
        creator.canMove=false;
        //System.out.println("hitRecovery"); //debug
        if(creator.controller.current.loopCount == 0){//permetto ad animazione active frames di prendere parte di recovery frames
            creator.controller.setAnimation(creator.idleAnimation,-1);
        }

        if(nFrame == 0){
            firstRecoveryFrame();
        }
    }
    public void missRecoveryFrames(int nFrame){
        creator.canMove = false;
        //System.out.println("missRecovery"); //debug
        if(creator.controller.current.loopCount == 0) {//permetto ad animazione active frames di prendere parte di recovery frames
            creator.controller.setAnimation(creator.idleAnimation,-1);
        }

        if(nFrame == 0){
            firstRecoveryFrame();
        }
    }
    public void attackHitEnd(){
        creator.autoComboTimer = creator.AUTOCOMBO_TIME_TOLLERANCE;
        creator.lastAttackId = creator.currentAttackId;
        creator.endedAttackThisExecution = true;
        creator.canMove = true;
        //System.out.println("hitEnd"); //debug
        creator.currentAttackId = Character.ATTACK_NONE;
        frameCounter = 0;
        haveHitted = false;

        removeAllColliders();
        createdColliders = new Vector<>();
    }
    public void attackMissEnd(){
        creator.autoComboTimer = creator.AUTOCOMBO_TIME_TOLLERANCE;
        creator.lastAttackId = creator.currentAttackId;
        creator.endedAttackThisExecution = true;
        creator.canMove = true;
        //System.out.println("misEnd"); //debug
        creator.currentAttackId = Character.ATTACK_NONE;
        frameCounter = 0;
        haveHitted = false;

        removeAllColliders();
        createdColliders = new Vector<>();
    }
    public void hit(Character c){
        if(!haveHitted){//fa danno 1 volta
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

            haveHitted = true;

            c.currentAttackState = 0;
            c.lastAttackId = Character.ATTACK_NONE;
        }//fatto 1 sola volta

        //continua a infliggere stun -> combo costanti indipendentemente al frame in cui è avvenuta la hit
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
    public void applyKnockBack(Character c){
        c.currentXForce = this.creator.facingDirection * knockback.x;
        c.currentYForce = knockback.y;
    }
    public void interrupt(){
        removeAllColliders();
        creator.currentAttackState = 0;
        frameCounter = 0;
        haveHitted = false;

        createdColliders = new Vector<>();
    }




    //metodi interfacce
    public void collision(Collider2D myCollider, Collider2D otherCollider) {
        //creator.collision(myCollider, otherCollider);
    }
    public Point2D.Float get2DPosition() {
        Point2D.Float posTmp = creator.get2DPosition();
        return new Point2D.Float(posTmp.x + this.position.x, posTmp.y + this.position.y);
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
