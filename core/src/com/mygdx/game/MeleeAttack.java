package com.mygdx.game;

import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class MeleeAttack extends Attack{
    //ricordo di dare tag a colliders (lo posso fare da costruttore)
    //usare metodi per settare attributi di attacchi
    static final String MELEE_ATTACK_COLLIDER_TAG = "melee attack collider";
    static final String MELEE_ATTACK_OBJ_TAG = "melee attack obj";
    static final int KNOCKBACK_NORMAL = 0, KNOCKBACK_DIRECTIONAL_X = 1, KNOCKBACK_DIRECTIONAL_Y = 2, KNOCKBACK_DIRECTIONAL_XY = 3, KNOCKBACK_ACTIVE_X = 4, KNOCKBACK_ACTIVE_Y = 5, KNOCKBACK_ACTIVE_XY = 6, KNOCKBACK_CUSTOM = -1;
    static final int HITTED_NONE = 0,HITTED_LIFE = 1,HITTED_GUARD = 2;
    //settare absolute owner in collider creati

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
    static class Attack_mario_grounded_x_0 extends MeleeAttack {
        Attack_mario_grounded_x_0(Character c) {
            super(c,"c00attack11",1.6f,8,10,7,20,16,17,25,20);
            setKnockBack(0.025f,0);
        }
        public void firstActiveFrame(){
            super.firstActiveFrame();

            BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.27f,-1.78f, MELEE_ATTACK_COLLIDER_TAG,0.3f,0.1f);
            addCollider(colTmp);
        }
    }//pugno 1 (combo 0)
    static class Attack_mario_grounded_x_1 extends MeleeAttack {
        Attack_mario_grounded_x_1(Character c) {
            super(c,"c00attack12",1.6f,10,12,5,15,21,24,30,36);
            setKnockBack(0.025f,0);
        }
        public void firstActiveFrame(){
            super.firstActiveFrame();

            BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.43f,-1.76f, MELEE_ATTACK_COLLIDER_TAG,0.4f,0.13f);
            addCollider(colTmp);
        }
    }//pugno 2 (combo 1)
    static class Attack_mario_grounded_x_2 extends MeleeAttack {
        Attack_mario_grounded_x_2(Character c) {
            super(c,"c00attack13",1.6f,13,15,7,25,6,8,18,30);
            setKnockBack(0.132f,0.141f);
        }
        public void firstActiveFrame(){
            super.firstActiveFrame();

            BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.3f,-1.95f, MELEE_ATTACK_COLLIDER_TAG,0.1f,0.13f);
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
    static class Attack_mario_grounded_y_0 extends MeleeAttack {
        Attack_mario_grounded_y_0(Character c) {
            super(c,"c02attackhi3",2f,6,5,10,16,10,17,26,24);
            setKnockBack(0.05f,0.18f);
        }
        public void firstActiveFrame() {
            super.firstActiveFrame();
            addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.17f,-1.5f, MELEE_ATTACK_COLLIDER_TAG,0.12f));
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
    static class Attack_mario_grounded_b_0 extends MeleeAttack {
        Attack_mario_grounded_b_0(Character c) {
            super(c,"c04attackhi4",2f,17,30,7,26,30,30,37,32);
            setKnockBack(0.1f,0);
        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);
            if(nFrame == 4){
                CircularCollider colTmp = new CircularCollider(this, creator, creator.facingDirection * -0.3f,-1.65f, MELEE_ATTACK_COLLIDER_TAG,0.14f);
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
    static class Attack_mario_aerial_x_0 extends MeleeAttack {
        Attack_mario_aerial_x_0(Character c) {
            super(c,"c05attackairn",4f,6,6,9,17,10,15,21,10);
            setKnockBack(0.13f,0.08f);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void firstActiveFrame() {
            super.firstActiveFrame();
            addCollider(new BoxCollider(this, creator, creator.facingDirection * 0.19f,-1.8f, MELEE_ATTACK_COLLIDER_TAG,0.25f,0.14f));
        }
    }//calcio aereo
    static class Attack_mario_aerial_b_0 extends MeleeAttack {
        Attack_mario_aerial_b_0(Character c) {
            super(c,"c05attackairf",2.5f,12,19,11,31,18,22,25,16);
            setKnockBack(0.09f,-0.16f);
            this.setAllowMovementDuringActiveFrames(true);
        }

        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 16){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.39f,-1.65f, MELEE_ATTACK_COLLIDER_TAG,0.12f));
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
    static class Attack_mario_aerial_y_0 extends MeleeAttack {
        Attack_mario_aerial_y_0(Character c) {
            super(c,"c05attackairhi",1.4f,7,7,9,35,9,10,21,13);
            setKnockBack(0,0.143f);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);
            if(nFrame == 8){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.38f,-1.15f, MELEE_ATTACK_COLLIDER_TAG,0.11f));
            }
            if(nFrame == 9){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.22f,-1.115f, MELEE_ATTACK_COLLIDER_TAG,0.11f));
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

    static class Attack_mario_projectile_prove extends MeleeAttack {
        Attack_mario_projectile_prove(Character c) {
            super(c,"c05attackairhi",1f,7,7,18,1,17,17,17,0);
            this.setAllowMovementDuringActiveFrames(false);
        }
        public void firstActiveFrame() {
            super.firstActiveFrame();

            //instanzio il proietile
            new ProjectileAttack.projectile_mario_prove(this.creator);
        }
    }


    //BONKEY KONG
    static class Attack_donkeyKong_grounded_x_0 extends MeleeAttack {
        Attack_donkeyKong_grounded_x_0(Character c) {
            super(c,"c00attack11",1.6f,13,15,7,20,16,21,27,26);
            setKnockBack(0.028f,0);
        }

        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 7){
                BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.4f,-1.7f, MELEE_ATTACK_COLLIDER_TAG,1f,0.34f);
                addCollider(colTmp);
            }

        }
    }//pugno 1 (combo 0)
    static class Attack_donkeyKong_grounded_x_1 extends MeleeAttack {
        Attack_donkeyKong_grounded_x_1(Character c) {
            super(c,"c00attack12",1.7f,19,50,11,30,32,36,39,36);
            setKnockBack(0.175f,0.19f);
        }
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 4){
                BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.65f,-1.6f, MELEE_ATTACK_COLLIDER_TAG,0.8f,1.3f);
                addCollider(colTmp);
            }
            if(nFrame == 18){
                removeAllColliders();
            }

        }
    }//pugno 2 (combo 1) (guard breaker)
    static class Attack_donkeyKong_grounded_y_0 extends MeleeAttack {
        Attack_donkeyKong_grounded_y_0(Character c) {
            super(c,"c02attackhi3",2f,9,14,7,17,13,17,20,27);
            setKnockBack(0.14f,0.14f, KNOCKBACK_DIRECTIONAL_X);
        }

        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 6){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.63f,-1.2f, MELEE_ATTACK_COLLIDER_TAG,0.235f));
            }
            if(nFrame == 7){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.63f,-1f));
            }
            if(nFrame == 9){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.12f,-0.6f, MELEE_ATTACK_COLLIDER_TAG,0.235f));
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
    }//manata alta
    static class Attack_donkeyKong_grounded_b_0 extends MeleeAttack {
        Attack_donkeyKong_grounded_b_0(Character c) {
            super(c,"c01attacks3hi",2.5f,6,4,2,26,13,21,23,21);
            setKnockBack(0.089f,0);
        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);
            if(nFrame == 4){
                BoxCollider colTmp = new BoxCollider(this, creator, creator.facingDirection * 0.3f,-1.65f, MELEE_ATTACK_COLLIDER_TAG,0.65f,0.36f);
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
    static class Attack_donkeyKong_aerial_x_0 extends MeleeAttack {
        Attack_donkeyKong_aerial_x_0(Character c) {
            super(c,"c05attackairn",2f,7,12,3,30,10,15,19,10);
            //c05attackairn
            setKnockBack(0.13f,0.08f, KNOCKBACK_DIRECTIONAL_X);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 10){
                addCollider(new CircularCollider(this, creator, creator.facingDirection *  0.42f,-1.76f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection *  0.25f,-1.66f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection *  0.1f,-1.56f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.1f,-1.46f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.25f,-1.36f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.42f,-1.26f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
            }

        }
    }//tornado aereo
    static class Attack_donkeyKong_aerial_b_0 extends MeleeAttack {
        Attack_donkeyKong_aerial_b_0(Character c) {
            super(c,"c05attackairf",2.5f,13,23,3,38,19,21,25,16);
            setKnockBack(0.06f,-0.2f);
            this.setAllowMovementDuringActiveFrames(true);
        }

        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);


            if(nFrame == 19){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.78f,-1f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
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
    static class Attack_donkeyKong_aerial_y_0 extends MeleeAttack {
        Attack_donkeyKong_aerial_y_0(Character c) {
            super(c,"c05attackairhi",2f,8,9,3,68,14,18,20,21);
            setKnockBack(0.15f,0);
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);
            if(nFrame == 11){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.6f,-1.3f, MELEE_ATTACK_COLLIDER_TAG,0.2f));
            }
            if(nFrame == 12){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.7f,-1.75f));
            }
            if(nFrame == 24){
                removeAllColliders();
            }

        }
    }//testata aerea


    //SONIC
    static class Attack_sonic_grounded_x_0 extends MeleeAttack {
        Attack_sonic_grounded_x_0(Character c) {
            super(c,"smush_blender_import|smush_blender_import c00attack11.nuanmb",2f,5,7,4,7,20,22,24,32);
            setKnockBack(0.01f,0);
        }

        @Override
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 3){
                addCollider(new BoxCollider(this,creator.facingDirection * 0.63f,- 1.72f, MELEE_ATTACK_COLLIDER_TAG,0.13f,0.13f));
            }
        }
    }//pugno 1 (combo 0)
    static class Attack_sonic_grounded_x_1 extends MeleeAttack {
        Attack_sonic_grounded_x_1(Character c) {
            super(c,"smush_blender_import|smush_blender_import c00attack12.nuanmb",2f,6,9,5,7,23,23,23,36);
            setKnockBack(0.017f,0);
        }

        @Override
        public void firstActiveFrame() {
            super.firstActiveFrame();

            addCollider(new BoxCollider(this,creator.facingDirection * 0.61f,- 1.72f, MELEE_ATTACK_COLLIDER_TAG,0.16f,0.13f));
        }
    }//pugno 2 (combo 1)
    static class Attack_sonic_grounded_x_2 extends MeleeAttack {
        Attack_sonic_grounded_x_2(Character c) {
            super(c,"smush_blender_import|smush_blender_import c00attack13.nuanmb",2f,13,18,7,15,27,30,33,31);
            setKnockBack(0.10f,0);
        }
        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 3){
                addCollider(new BoxCollider(this, creator, creator.facingDirection * 0.283f,-2.06f, MELEE_ATTACK_COLLIDER_TAG,0.18f,0.26f));
            }
            if(nFrame == 4){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.76f,-1.65f));
                ((BoxCollider)createdColliders.get(0)).width = 0.3f;
            }
            if(nFrame > 4 && nFrame < 9){
                ((BoxCollider)createdColliders.get(0)).width = ((BoxCollider)createdColliders.get(0)).width * 0.9f;
                ((BoxCollider)createdColliders.get(0)).height = ((BoxCollider)createdColliders.get(0)).height * 0.9f;
            }
        }
    }//calcio (combo 2)
    static class Attack_sonic_grounded_y_0 extends MeleeAttack {
        Attack_sonic_grounded_y_0(Character c) {
            super(c,"smush_blender_import|smush_blender_import c02attackhi3.nuanmb",2f,7,9,6,18,40,42,44,28);
            setKnockBack(0.13f,0.18f);
        }

        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);

            if(nFrame == 8){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.2f,-1.15f, MELEE_ATTACK_COLLIDER_TAG,0.145f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.3f,-1f, MELEE_ATTACK_COLLIDER_TAG,0.13f));
            }

            if(nFrame == 15){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.178f,-1f));
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * 0.13f,-0.83f));
            }


        }
    }//calci alti
    static class Attack_sonic_grounded_b_0 extends MeleeAttack {
        Attack_sonic_grounded_b_0(Character c) {
            super(c,"smush_blender_import|smush_blender_import c01attacks3hi.nuanmb",2.4f,7,21,2,14,29, 22,37,33);
            setKnockBack(0.1f,0.05f);
        }

        @Override
        public void firstActiveFrame() {
            super.firstActiveFrame();

            creator.setCollidersConfiguration(new BoxCollider(creator,0.2f,-1.90f, Character.BODY_COLLIDER_TAG,0.6f,0.5f), new CircularCollider(creator,0.2f,-1.90f, Character.BODY_COLLIDER_TAG,0));
        }

        public void activeFrames(int nFrame){
            super.activeFrames(nFrame);
            if(nFrame == 8){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.76f,-1.485f, MELEE_ATTACK_COLLIDER_TAG,0.19f));
            }
            if(nFrame == 12){
                ((CircularCollider)createdColliders.get(0)).radius = 0.15f;
            }
        }
    }//calcio (guard breaker)
    static class Attack_sonic_aerial_x_0 extends MeleeAttack {
        Attack_sonic_aerial_x_0(Character c) {
            super(c,"smush_blender_import|smush_blender_import c05attackairn.nuanmb",2.2f,2,3,6,46,11,14,24,14);
            setKnockBack(0.025f,0.02f, KNOCKBACK_DIRECTIONAL_X);
            multipleHitsDelay = 8;
            this.setAllowMovementDuringActiveFrames(true);
        }
        public void firstActiveFrame() {
            super.firstActiveFrame();
            addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.15f,-1.7f, MELEE_ATTACK_COLLIDER_TAG,0.27f));
            creator.setCollidersConfiguration(new BoxCollider(creator,0.15f,-1.7f,Character.BODY_COLLIDER_TAG,0,0), new CircularCollider(creator, 0.15f,-1.7f, Character.BODY_COLLIDER_TAG,0.27f));
        }
    }//spin aereo //sistemare hurtBox
    static class Attack_sonic_aerial_b_0 extends MeleeAttack {
        Attack_sonic_aerial_b_0(Character c) {
            super(c,"smush_blender_import|smush_blender_import c05attackairf.nuanmb",2.5f,6,12,5,31,18,24,28,22);
            setKnockBack(0.10f,0);
            this.setAllowMovementDuringActiveFrames(true);
        }

        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);

            if(nFrame == 5){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.45f,-1.7f, MELEE_ATTACK_COLLIDER_TAG,0.21f));
                creator.setCollidersConfiguration(new BoxCollider(creator,0.2f,-1.7f, Character.BODY_COLLIDER_TAG,0.75f,0.265f), new CircularCollider(creator, 0.45f,-1.7f, Character.BODY_COLLIDER_TAG,0.21f));
            }
            if(nFrame == 15){
                removeAllColliders();
            }
        }
    }//testata rotante aerea //sistemare hurtBox
    static class Attack_sonic_aerial_y_0 extends MeleeAttack {
        Attack_sonic_aerial_y_0(Character c) {
            super(c,"smush_blender_import|smush_blender_import c05attackairhi.nuanmb",2.2f,5,6,9,33,9,10,21,13);
            setKnockBack(0.11f,0.149f, KNOCKBACK_ACTIVE_X);
            applyContinuousKnockBack = true;
            this.setAllowMovementDuringActiveFrames(true);

        }
        public void activeFrames(int nFrame) {
            super.activeFrames(nFrame);
            if(nFrame == 5){
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.43f,-1.66f, MELEE_ATTACK_COLLIDER_TAG,0.12f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.41f,-1.66f, MELEE_ATTACK_COLLIDER_TAG,0.12f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * 0.55f,-1.66f, MELEE_ATTACK_COLLIDER_TAG,0.09f));
                addCollider(new CircularCollider(this, creator, creator.facingDirection * -0.53f,-1.66f, MELEE_ATTACK_COLLIDER_TAG,0.09f));
            }

            if(nFrame == 9){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.41f,-1.64f));
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * -0.40f,-1.64f));
                createdColliders.get(2).set2DPosition(new Point2D.Float(creator.facingDirection * 0.52f,-1.58f));
                createdColliders.get(3).set2DPosition(new Point2D.Float(creator.facingDirection * -0.51f,-1.58f));
            }
            if(nFrame == 13){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.36f,-1.57f));
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * -0.37f,-1.56f));
                createdColliders.get(2).set2DPosition(new Point2D.Float(creator.facingDirection * 0.47f,-1.50f));
                createdColliders.get(3).set2DPosition(new Point2D.Float(creator.facingDirection * -0.46f,-1.49f));
            }
            if(nFrame == 14){
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * 0.20f,-1.37f));
                createdColliders.get(1).set2DPosition(new Point2D.Float(creator.facingDirection * -0.20f,-1.36f));
                createdColliders.get(2).set2DPosition(new Point2D.Float(creator.facingDirection * 0.30f,-1.27f));
                createdColliders.get(3).set2DPosition(new Point2D.Float(creator.facingDirection * -0.30f,-1.26f));
            }
            if(nFrame == 15){
                remoreCollider(createdColliders.get(0));
                remoreCollider(createdColliders.get(0));
                remoreCollider(createdColliders.get(0));
                createdColliders.get(0).set2DPosition(new Point2D.Float(creator.facingDirection * -0.028f,-1.176f));
                ((CircularCollider)createdColliders.get(0)).radius = 0.18f;
            }


        }

        @Override
        public void applyKnockBack(Character c) {
            float posX,posY;
            if(this.framesOfExecution < startUpFrames + 15){
                if(c.get2DPosition().x * creator.facingDirection > this.creator.get2DPosition().x * creator.facingDirection){
                    posX = createdColliders.get(0).get2DPosition().x - c.idleBodyCol.center.x;
                    posY = createdColliders.get(0).get2DPosition().y - c.idleBodyCol.center.y;
                    c.set2DPosition(new Point2D.Float(posX, posY));
                }else{
                    posX = createdColliders.get(1).get2DPosition().x - c.idleBodyCol.center.x;
                    posY = createdColliders.get(1).get2DPosition().y - c.idleBodyCol.center.y;
                    c.set2DPosition(new Point2D.Float(posX, posY));
                }
            }else{
                posX = createdColliders.get(0).get2DPosition().x - c.idleBodyCol.center.x;
                posY = createdColliders.get(0).get2DPosition().y - c.idleBodyCol.center.y;
                c.set2DPosition(new Point2D.Float(posX, posY));
                super.applyKnockBack(c);
            }

        }
    }//sforbiciata alta aerea

    int startUpFrames;//frames inizializzazione dell'attacco (vulnerabilità) (artificiale rispetto ad animazione)
    int activeFrames;//frame di durata dell'attacco (comparsa hitBoxes non coincide con frame 0 peré animazione ha già startup frames)
    int lifeHitRecoveryFrames;//frame di "stun" finale se l'attacco colpisce nemico non bloccante (artificiale rispetto ad animazione)
    int guardHitRecoveryFrames;//frame di "stun" finale se l'attacco colpisce nemico bloccante (artificiale rispetto ad animazione)
    int missRecoveryFrames;//frame di "stun" finale se l'attacco manca il nemico (artificiale rispetto ad animazione)
    boolean currentlyEnableMovement, enableMovement;


    //costruttori
    private MeleeAttack(@NotNull Character c, @NotNull String animationName, float animationSpeed, int lifeDamage, int guardDamage, int startUpFrames, int activeFrames, int lifeHitRecoveryFrames, int guardHitRecoveryFrames, int missRecoveryFrames, int enemyRecoveryFrames){
        super(c, animationName, animationSpeed, lifeDamage, guardDamage, enemyRecoveryFrames);

        this.relativePosition = new Point2D.Float(0,0);
        this.tag = MELEE_ATTACK_OBJ_TAG;

        this.startUpFrames = startUpFrames;
        this.activeFrames = activeFrames;
        this.lifeHitRecoveryFrames = lifeHitRecoveryFrames;
        this.guardHitRecoveryFrames = guardHitRecoveryFrames;
        this.missRecoveryFrames = missRecoveryFrames;

        //parametri default
        currentlyEnableMovement = false;
        applyContinuousKnockBack = false;
        this.setAllowMovementDuringActiveFrames(false);
    }


    //metodo da usare per ottenere attacchi
    public static MeleeAttack getAttack(Character c, int attackId, int attackState){
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

        if(c.id == Character.SONIC_ID){
            switch (attackId){
                case Character.ATTACK_1_GROUNDED:
                    switch (attackState){
                        case 0:
                            return new Attack_sonic_grounded_x_0(c);
                        case 1:
                            return new Attack_sonic_grounded_x_1(c);
                        case 2:
                            return new Attack_sonic_grounded_x_2(c);
                    }
                case Character.ATTACK_2_GROUNDED:
                    return new Attack_sonic_grounded_b_0(c);
                case Character.ATTACK_3_GROUNDED:
                    return new Attack_sonic_grounded_y_0(c);
                case Character.ATTACK_1_AIRBORN:
                    return new Attack_sonic_aerial_x_0(c);
                case Character.ATTACK_2_AIRBORN:
                    return new Attack_sonic_aerial_b_0(c);
                case Character.ATTACK_3_AIRBORN:
                    return new Attack_sonic_aerial_y_0(c);
            }
        }


        return null;
    }


    //metodi da non averrydare
    public void execute(){
        super.execute();


        creator.autoComboTimer = Character.AUTOCOMBO_TIME_TOLLERANCE;// valore temporaneo per non far cancellare lo stato

        if(framesOfExecution == 0){
            attackStart();//avvio attacco
        }//codice start (1 volta a inizio attacco)

        if(framesOfExecution < startUpFrames){//startup
            startupFrames(framesOfExecution);
        }//stratup frames

        if(framesOfExecution >= startUpFrames && framesOfExecution < startUpFrames+activeFrames){//active
            activeFrames(framesOfExecution -startUpFrames);

            if(!creator.controller.current.animation.id.equals(this.animationName)){
                creator.controller.setAnimation(this.animationName,1);
                creator.controller.current.speed = this.animationSpeed;
            }//mi assicuro di star mostrando l'animazione
        }//active frames

        if(framesOfExecution >= startUpFrames+activeFrames){//recovery
            if(lastThingHitted == HITTED_LIFE && framesOfExecution < startUpFrames+activeFrames+ lifeHitRecoveryFrames){
                hitRecoveryFrames(framesOfExecution -(startUpFrames+activeFrames));
            }
            if(lastThingHitted == HITTED_GUARD && framesOfExecution < startUpFrames+activeFrames+ guardHitRecoveryFrames){
                hitRecoveryFrames(framesOfExecution -(startUpFrames+activeFrames));
            }
            if(lastThingHitted == HITTED_NONE && framesOfExecution <startUpFrames+activeFrames+missRecoveryFrames){
                missRecoveryFrames(framesOfExecution -(startUpFrames+activeFrames));
            }
        }//recovery frames

        //codice fine attacco (1 volta a fine attacco)
        if(lastThingHitted == HITTED_LIFE && framesOfExecution == startUpFrames+activeFrames+ lifeHitRecoveryFrames){
            attackHitEnd();//concludo attacco
            creator.currentAttackId=Character.ATTACK_NONE;
        }
        if(lastThingHitted == HITTED_GUARD && framesOfExecution == startUpFrames+activeFrames+ guardHitRecoveryFrames){
            attackHitEnd();//concludo attacco
            creator.currentAttackId=Character.ATTACK_NONE;
        }
        if(lastThingHitted == HITTED_NONE && framesOfExecution == startUpFrames+activeFrames+missRecoveryFrames){
            attackMissEnd();//concludo attacco
            creator.currentAttackId=Character.ATTACK_NONE;
        }


    }
    public void setAllowMovementDuringActiveFrames(boolean allowMovement){
        enableMovement = allowMovement;

    }
    public void applyKnockBack(@NotNull Character c){
        if(knockBackType == KNOCKBACK_NORMAL){
            KNOCKBACK_NORMAL(c);
        }
        if(knockBackType == KNOCKBACK_DIRECTIONAL_X){
            KNOCKBACK_DIRECTIONAL_X(c);
        }
        if(knockBackType == KNOCKBACK_DIRECTIONAL_Y){
            KNOCKBACK_DIRECTIONAL_Y(c);
        }
        if(knockBackType == KNOCKBACK_DIRECTIONAL_XY){
            KNOCKBACK_DIRECTIONAL_XY(c);
        }
        if(knockBackType == KNOCKBACK_ACTIVE_X){
            KNOCKBACK_ACTIVE_X(c);
        }
        if(knockBackType == KNOCKBACK_ACTIVE_Y){
            KNOCKBACK_ACTIVE_Y(c);
        }
        if(knockBackType == KNOCKBACK_ACTIVE_XY){
            KNOCKBACK_ACTIVE_XY(c);
        }
        if(knockBackType == KNOCKBACK_CUSTOM){
            KNOCKBACK_CUSTOM(c);
        }

    }
    protected void KNOCKBACK_NORMAL(@NotNull Character c){
        c.currentXForce = creator.facingDirection * knockBack.x;
        c.currentYForce = knockBack.y;}
    protected void KNOCKBACK_DIRECTIONAL_X(@NotNull Character c){
        c.currentYForce = knockBack.y;
        if(c.get2DPosition().x * creator.facingDirection >= creator.get2DPosition().x * creator.facingDirection){
            c.currentXForce = creator.facingDirection * knockBack.x;
        }else{
            c.currentXForce = creator.facingDirection * knockBack.x * -1;
        }}
    protected void KNOCKBACK_DIRECTIONAL_Y(@NotNull Character c){
        c.currentXForce = knockBack.x;
        if(c.currentBodyCollider.get2DPosition().y >= creator.currentBodyCollider.get2DPosition().y){
            c.currentYForce = knockBack.y;
        }else{
            c.currentYForce = knockBack.y * -1;
        }}
    protected void KNOCKBACK_DIRECTIONAL_XY(@NotNull Character c){
        if(c.get2DPosition().x * creator.facingDirection >= creator.get2DPosition().x * creator.facingDirection){
            c.currentXForce = creator.facingDirection * knockBack.x;
        }else{
            c.currentXForce = creator.facingDirection * knockBack.x * -1;
        }

        if(c.currentBodyCollider.get2DPosition().y >= creator.currentBodyCollider.get2DPosition().y){
            c.currentYForce = knockBack.y;
        }else{
            c.currentYForce = knockBack.y * -1;
        }}
    protected void KNOCKBACK_ACTIVE_X(@NotNull Character c){
        if(creator.moveDirection == Character.MOVE_STOP){
            c.currentXForce = creator.facingDirection * knockBack.x;
        }else{
            c.currentXForce = creator.moveDirection * knockBack.x;
        }
        c.currentYForce = knockBack.y;}
    protected void KNOCKBACK_ACTIVE_Y(@NotNull Character c){
        if(creator.lookDirection == Character.LOOK_NONE){
            c.currentYForce =  knockBack.y;
        }else{
            c.currentYForce = knockBack.y * creator.lookDirection;
        }
        c.currentXForce = knockBack.x;}
    protected void KNOCKBACK_ACTIVE_XY(@NotNull Character c){
        if(creator.moveDirection == Character.MOVE_STOP){
            c.currentXForce = creator.facingDirection * knockBack.x;
        }else{
            c.currentXForce = creator.moveDirection * knockBack.x;
        }

        if(creator.lookDirection == Character.LOOK_NONE){
            c.currentYForce =  knockBack.y;
        }else{
            c.currentYForce = knockBack.y * creator.lookDirection;
        }}
    protected void KNOCKBACK_CUSTOM(@NotNull Character c){}



    //metodi che si possono overrydare (chiamando super)
    public void attackStart(){
        creator.jump=false;//sovrascrivo inputs di salto
        creator.moveDirection = Character.MOVE_STOP;
        currentlyEnableMovement = false;
        creator.controller.setAnimation(creator.idleAnimation,-1);

        this.framesToNextHit = 0;
        lastThingHitted = HITTED_NONE;
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
        creator.setCollidersConfiguration(creator.idleBodyCol, creator.idleHeadCol);
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
        creator.currentAttackId = Character.ATTACK_NONE;
        framesOfExecution = 0;
        this.framesToNextHit = 0;

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
        framesOfExecution = 0;
        this.framesToNextHit = 0;

        removeAllColliders();
        createdColliders = new Vector<>();
    }
    public void interrupt(){
        removeAllColliders();
        creator.currentAttackState = 0;
        framesOfExecution = 0;
        this.framesToNextHit = 0;

        createdColliders = new Vector<>();
    }



    //metodi interfacce
    public void collision(Collider2D myCollider, Collider2D otherCollider) {
        //creator.collision(myCollider, otherCollider);
    }
}
