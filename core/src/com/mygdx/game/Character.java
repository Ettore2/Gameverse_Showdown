package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;

import java.awt.geom.Point2D;
import java.util.Vector;

public class Character extends ModelInstance implements GameObject{
    public static final int AVILABLE_CHARACTERS = 3;

    final static float JUMP_SPAM_BLOCKER = 0.33f;//per evitare spam accidentale
    public static final int GUARD_HIT_STUN_FRAMES = 20;
    public static final int GUARD_START_FRAMES_DELAY = 6;
    public static final int GUARD_EXIT_FRAMES_DELAY = 7;
    public static final int GUARD_BREAKE_STUNN_FRAMES = 50;
    public static final float GUARD_BREAKE_X_CNOCKBACK = 0.002f;
    public static final int GUARD_NORMAL_REGENERATION_ACTIVATION_DELAY = 200;//dopo quanto
    public static final int GUARD_BREAK_REGENERATION_ACTIVATION_DELAY = 360;//dopo quanto (tenere >= GUARD_NORMAL_REGENERATION_DELAY)
    public static final int FRAMES_FOR_GUARD_REGENERATION_AMMOUNT = 3;// ogni quanto
    public static final int GUARD_REGENARATION_AMMOUNT = 1;//di quanto
    public static final float MAX_STATS = 5;
    public static final String PLAYER_TAG = "player";
    public static final String BODY_COLLIDER_TAG = "body collider";

    public static float groundHeight = 0;
    public static final int MOVE_SX = -1, MOVE_STOP = 0, MOVE_DX = 1;//non modificare
    public static final int FACING_SX = -1, FACING_DX = 1;//non modificare
    public static final int ATTACK_NONE = -1, ATTACK_1_GROUNDED = 0, ATTACK_2_GROUNDED = 1, ATTACK_3_GROUNDED = 2, ATTACK_1_AIRBORN = 3, ATTACK_2_AIRBORN = 4, ATTACK_3_AIRBORN = 5;
    public static final int ATTACK_BUTTON_X = 0, ATTACK_BUTTON_Y = 2, ATTACK_BUTTON_B = 1;
    public static final String CHARACTERS_MODELS_DIRECTORY = "Characters3D/";
    public static final String[] CHARACTERS_MODELS_FIlE = {"Mario/Mario.g3dj","Donkey Kong/DK.g3dj","Sonic/Sonic.g3dj"};
    public static final String[] CHARACTERS_NAMES = {"Mario","Donkey Kong","Sonic"};
    final static float AUTOCOMBO_TIME_TOLLERANCE = 0.25f;
    public static final int MARIO_ID = 0, DONKEY_ID = 1, SONIC_ID = 2;

    static final Vector<ModelInstance>[] AVAILABLE_PROJECTILE_MODELS = new Vector[1];
    public static final int MARIO_PROVE_PROJECTILE = 0;

    //cose "logiche"
    public boolean puppet;
    public final int id;
    public String tag;
    private float attackStat, defenseStat, agilityStat, healthStat;
    //attackPerfectStatsVal, defensePerfectStatsVal servono a porporzionare la barra delle statistiche:
    // se un personaggio ha attackPerfectStatsVal vita avrà nelle statistiche la vita al massimo
    public Character enemy;
    int numberOfJumps, availableJumps;
    int maxLife, currentLife;
    int maxGuardAmount, currentGuardAmount;
    float groundedMoveSpeed, airborneMoveSpeed, weight;//se 2 personaggi si scontrano si fa la differenza di velocità X massa per capire chi spinge di piu
    float jumpForce,jumpDeceleration;
    float falAcceleration, fastFallAcceleration;
    float currentXForce, currentYForce;
    private boolean moveLeft,moveRight;
    public boolean canMoveRight, canMoveLeft;
    int moveDirection;
    int facingDirection;
    boolean tryingToGuard;
    int guardStartFramesCounter, guardExitFramesCounter;
    int guardRegenerationFramesCounter, guardRegenerationRateoFramesCounter;
    boolean grounded,canMove, guarding, haveArmor;// haveArmor ti rende impossibile de stunnare
    int currentStunFrames;
    boolean crouching;
    boolean jump;
    Attack[][] attacks;
    int inputtedAttackIndex, currentAttackId, lastAttackId, currentAttackState;
    boolean attackedLastFrame;
    float autoComboTimer;
    boolean endedAttackThisExecution; //per evitare di avere un pixel di animazione camminata facendo auto combo
    float jumpTimer;
    boolean jumpedThisExecution; //per evitare che resetti i jump a 2 prima di staccarsi dal terreno
    Vector<Projectile> existingCharacterProjectiles;



    PerspectiveCamera camera3D;//per aggiustare prospettiva

    BoxCollider bodyCol;
    CircularCollider headCol;
    private float originalBodyColliderRelativeX, originalHeadColliderRelativeX;
    private Vector<Collider2D> personalColliders;
    private Vector<Collider2D> existingColliders;

    Attack meleeHitsToExecute;
    Vector<Projectile> projectileHitsToExecute;



    //cose grafiche
    public AnimationController controller;
    String idleAnimation, walkAnimation, firstJumpAnimation, secondJumpAnimation;
    String fallingAnimation, guardAnimation, normalHitAnimation, airHitAnimation, guardHitAnimation;

    float idleAnimationSpeed, walkAnimationSpeed, firstJumpAnimationSpeed, secondJumpAnimationSpeed;
    float fallingAnimationSpeed, guardAnimationSpeed;
    float normalHitAnimationSpeed, guardHitAnimationSpeed, airHitAnimationSpeed;



    //eventualmente create i timer per gestire tutti questi stati

    public Character(PerspectiveCamera camera3D, int characterId, float xPos, float yPos, float zPos, float axesX, float axesY, float axesZ, float degrees,Vector<Collider2D> existingColliders){
        //crusha se l'idi non esiste
        super(new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal(CHARACTERS_MODELS_DIRECTORY + CHARACTERS_MODELS_FIlE[characterId])));
        puppet = false;
        id = characterId;
        this.camera3D = camera3D;
        this.tag = PLAYER_TAG;
        transform.setToRotation(axesX,axesY,axesZ,degrees);//impostante che sia prima di posizione
        transform.setTranslation(xPos,yPos,zPos);
        controller = new AnimationController(this);
        attacks = new Attack[6][1]; // [id attacco] [stato attacco (combo)]
        currentAttackId = ATTACK_NONE;
        lastAttackId = ATTACK_NONE;
        attackedLastFrame = false;
        autoComboTimer = 0f;
        jump = false;
        canMove = true;
        this.personalColliders = new Vector<>();
        this.existingColliders = existingColliders;
        facingDirection = FACING_DX;//solo per inizializzare
        currentStunFrames = 0;
        haveArmor = false;
        tryingToGuard = false;
        guardStartFramesCounter = 0;
        guardExitFramesCounter = 0;
        guardRegenerationFramesCounter = 0;
        guarding = false;
        canMoveRight = true;
        canMoveLeft = true;
        inputtedAttackIndex = ATTACK_NONE;

        projectileHitsToExecute = new Vector<>();

        idleAnimationSpeed = 1;
        walkAnimationSpeed = 1;
        firstJumpAnimationSpeed = 1;
        secondJumpAnimationSpeed = 1;
        fallingAnimationSpeed = 1;
        guardAnimationSpeed = 1;
        normalHitAnimationSpeed = 1;
        guardHitAnimationSpeed = 1;
        airHitAnimationSpeed = 1;

        currentXForce = 0;
        currentYForce = 0;

        bodyCol = new BoxCollider(this, new Point2D.Float(0, 0),0,0);
        bodyCol.useRelativePosition = true;
        bodyCol.setTag(BODY_COLLIDER_TAG);
        existingColliders.add(bodyCol);
        headCol = new CircularCollider(this, new Point2D.Float(0, 0),0);
        headCol.setTag(BODY_COLLIDER_TAG);
        headCol.useRelativePosition = true;
        existingColliders.add(headCol);

        existingCharacterProjectiles = new Vector<>();

        switch (characterId){
            case MARIO_ID:
                new_Mario();
                break;
            case DONKEY_ID:
                new_Donkey();
                break;
            case SONIC_ID:
                new_Sonic();
                break;
        }

        originalBodyColliderRelativeX = this.bodyCol.center.x;
        originalHeadColliderRelativeX = this.headCol.center.x;

    }
    public Character(PerspectiveCamera camera3D, int characterId, float xPos, float yPos, float zPos, float yRotation){
        this(camera3D, characterId, xPos, yPos, zPos, 0, 0, 0, 0, new Vector<Collider2D>());
        transform.setToRotation(0,1,0,yRotation);
        transform.setTranslation(xPos, yPos, zPos);
    }
    public Character(PerspectiveCamera camera3D, int characterId, float xPos, float yPos, float zPos, float yRotation,Vector<Collider2D> existingColliders){
        this(camera3D, characterId, xPos, yPos, zPos, 0, 0, 0, 0, existingColliders);
        transform.setToRotation(0,1,0,yRotation);
        transform.setTranslation(xPos, yPos, zPos);
    }

    public static ModelInstance getProjectile(int projectileId) {
        if(projectileId >= 0 && AVAILABLE_PROJECTILE_MODELS.length > projectileId && AVAILABLE_PROJECTILE_MODELS[projectileId]!=null && AVAILABLE_PROJECTILE_MODELS[projectileId].size() > 0){
            return AVAILABLE_PROJECTILE_MODELS[projectileId].remove(0);
        }
        System.out.println(false);
        return null;
    }

    private void new_Mario(){
        //debug proiettili


        attackStat = 2;
        defenseStat = 3;
        agilityStat = 2;
        healthStat = 3;

        groundedMoveSpeed = 0.042f;
        airborneMoveSpeed = 0.03f;
        idleAnimation = "a00wait1";
        walkAnimation = "a01walk";
        firstJumpAnimation = "a03jumpf";
        secondJumpAnimation = "a03jumpaerialf";
        fallingAnimation = "a04fall";
        normalHitAnimation = "f00damagehi1";
        airHitAnimation = "f01damageair1";
        guardHitAnimation = "b01guarddamage";
        guardAnimation = "b00guard";

        idleAnimationSpeed = 1;
        walkAnimationSpeed = 2.2f;
        firstJumpAnimationSpeed = 1;
        secondJumpAnimationSpeed = 4.6f;
        fallingAnimationSpeed = 1;
        guardAnimationSpeed = 6f;
        normalHitAnimationSpeed = 1;
        guardHitAnimationSpeed = 1;
        airHitAnimationSpeed = 1;


        attacks[ATTACK_1_GROUNDED]=new Attack[3];
        attacks[ATTACK_1_GROUNDED][0] = Attack.getAttack(this,ATTACK_1_GROUNDED,0);
        attacks[ATTACK_1_GROUNDED][1] = Attack.getAttack(this,ATTACK_1_GROUNDED,1);
        attacks[ATTACK_1_GROUNDED][2] = Attack.getAttack(this,ATTACK_1_GROUNDED,2);
        attacks[ATTACK_2_GROUNDED][0] = Attack.getAttack(this,ATTACK_2_GROUNDED,0);
        attacks[ATTACK_3_GROUNDED][0] = Attack.getAttack(this,ATTACK_3_GROUNDED,0);
        attacks[ATTACK_1_AIRBORN][0] = Attack.getAttack(this,ATTACK_1_AIRBORN,0);
        attacks[ATTACK_2_AIRBORN][0] = Attack.getAttack(this,ATTACK_2_AIRBORN,0);
        attacks[ATTACK_3_AIRBORN][0] = Attack.getAttack(this,ATTACK_3_AIRBORN,0);

        //attacks[ATTACK_2_GROUNDED][0] = new Attack.Attack_mario_projectile_prove(this);

        controller.setAnimation(idleAnimation);//importante

        maxLife = 200;
        currentLife = maxLife;
        maxGuardAmount = 30;
        currentGuardAmount = maxGuardAmount;
        numberOfJumps = 2;
        availableJumps = numberOfJumps;
        jumpForce = 0.145f;
        jumpDeceleration = 0.0070f;
        falAcceleration = 0.0083f;
        fastFallAcceleration = 0.0067f;
        weight = 0.007f;

        Vector3 positionTmp = new Vector3();
        transform.getTranslation(positionTmp);
        bodyCol.setDimensions(new Point2D.Float(0, -1.8f),0.35f,0.65f);
        headCol.setDimensions(new Point2D.Float(0, -1.48f),0.175f);


        //bodyCol.isVisible = false; //debug
        //headCol.isVisible = false; //debug
    }
    private void new_Donkey(){
        attackStat = 3;
        defenseStat = 4;
        agilityStat = 1;
        healthStat = 5;

        groundedMoveSpeed = 0.033f;
        airborneMoveSpeed = 0.027f;
        idleAnimation = "a00wait1";
        walkAnimation = "a01walk";
        firstJumpAnimation = "a03jumpf";
        secondJumpAnimation = "a03jumpaerialf";
        fallingAnimation = "a04fall";
        normalHitAnimation = "f00damagehi1";
        airHitAnimation = "f01damageair1";
        guardHitAnimation = "b01guarddamage";
        guardAnimation = "b00guard";

        idleAnimationSpeed = 1;
        walkAnimationSpeed = 1.5f;
        firstJumpAnimationSpeed = 1;
        secondJumpAnimationSpeed = 4.6f;
        fallingAnimationSpeed = 1;
        guardAnimationSpeed = 6f;
        normalHitAnimationSpeed = 1;
        guardHitAnimationSpeed = 1;
        airHitAnimationSpeed = 1;

        attacks[ATTACK_1_GROUNDED]=new Attack[2];
        attacks[ATTACK_1_GROUNDED][0] = Attack.getAttack(this,ATTACK_1_GROUNDED,0);
        attacks[ATTACK_1_GROUNDED][1] = Attack.getAttack(this,ATTACK_1_GROUNDED,1);
        attacks[ATTACK_2_GROUNDED][0] = Attack.getAttack(this,ATTACK_2_GROUNDED,0);
        attacks[ATTACK_3_GROUNDED][0] = Attack.getAttack(this,ATTACK_3_GROUNDED,0);
        attacks[ATTACK_1_AIRBORN][0] = Attack.getAttack(this,ATTACK_1_AIRBORN,0);
        attacks[ATTACK_2_AIRBORN][0] = Attack.getAttack(this,ATTACK_2_AIRBORN,0);
        attacks[ATTACK_3_AIRBORN][0] = Attack.getAttack(this,ATTACK_3_AIRBORN,0);

        controller.setAnimation(idleAnimation);//importante

        maxLife = 320;
        currentLife = maxLife;
        maxGuardAmount = 50;
        currentGuardAmount = maxGuardAmount;
        numberOfJumps = 2;
        availableJumps = numberOfJumps;
        jumpForce = 0.13f;
        jumpDeceleration = 0.008f;
        falAcceleration = 0.0084f;
        fastFallAcceleration = 0.0091f;
        weight = 0.0086f;

        Vector3 positionTmp = new Vector3();
        transform.getTranslation(positionTmp);
        bodyCol.setDimensions(new Point2D.Float(0, -1.6f),0.8f,1f);
        headCol.setDimensions(new Point2D.Float(0.38f, -1.325f),0.27f);


        //bodyCol.isVisible = false; //debug
        //headCol.isVisible = false; //debug
    }
    private void new_Sonic(){
        attackStat = 3;
        defenseStat = 2;
        agilityStat = 5;
        healthStat = 2;

        groundedMoveSpeed = 0.063f;
        airborneMoveSpeed = 0.038f;
        idleAnimation = "smush_blender_import|smush_blender_import a00wait1.nuanmb";
        walkAnimation = "smush_blender_import|smush_blender_import SONICsmush_blender_import a01walkfastMODIF";
        firstJumpAnimation = "smush_blender_import|smush_blender_import a03jumpb.nuanmb";
        secondJumpAnimation = "smush_blender_import|smush_blender_import a03jumpaerialb.nuanmb";
        fallingAnimation = "smush_blender_import|smush_blender_import a04fallaerial.nuanmb";
        normalHitAnimation = "smush_blender_import|smush_blender_import f00damagehi1.nuanmb";
        airHitAnimation = "smush_blender_import|smush_blender_import f01damageair1.nuanmb";
        guardHitAnimation = "smush_blender_import|smush_blender_import b01guarddamage.nuanmb";
        guardAnimation = "smush_blender_import|smush_blender_import b00guard.nuanmb";

        idleAnimationSpeed = 1;
        walkAnimationSpeed = 1.7f;
        firstJumpAnimationSpeed = 1;
        secondJumpAnimationSpeed = 4.6f;
        fallingAnimationSpeed = 1;
        guardAnimationSpeed = 6f;
        normalHitAnimationSpeed = 1;
        guardHitAnimationSpeed = 1;
        airHitAnimationSpeed = 1;

        attacks[ATTACK_1_GROUNDED]=new Attack[3];
        attacks[ATTACK_1_GROUNDED][0] = Attack.getAttack(this,ATTACK_1_GROUNDED,0);
        attacks[ATTACK_1_GROUNDED][1] = Attack.getAttack(this,ATTACK_1_GROUNDED,1);
        attacks[ATTACK_1_GROUNDED][2] = Attack.getAttack(this,ATTACK_1_GROUNDED,2);
        attacks[ATTACK_2_GROUNDED][0] = Attack.getAttack(this,ATTACK_2_GROUNDED,0);
        attacks[ATTACK_3_GROUNDED][0] = Attack.getAttack(this,ATTACK_3_GROUNDED,0);
        attacks[ATTACK_1_AIRBORN][0] = Attack.getAttack(this,ATTACK_1_AIRBORN,0);
        attacks[ATTACK_2_AIRBORN][0] = Attack.getAttack(this,ATTACK_2_AIRBORN,0);
        attacks[ATTACK_3_AIRBORN][0] = Attack.getAttack(this,ATTACK_3_AIRBORN,0);

        controller.setAnimation(idleAnimation);//importante

        maxLife = 170;
        currentLife = maxLife;
        maxGuardAmount = 20;
        currentGuardAmount = maxGuardAmount;
        numberOfJumps = 2;
        availableJumps = numberOfJumps;
        jumpForce = 0.162f;
        jumpDeceleration = 0.0080f;
        falAcceleration = 0.01f;
        fastFallAcceleration = 0.0140f;
        weight = 0.0058f;

        Vector3 positionTmp = new Vector3();
        transform.getTranslation(positionTmp);
        bodyCol.setDimensions(new Point2D.Float(0, -1.8f),0.35f,0.65f);
        headCol.setDimensions(new Point2D.Float(0, -1.48f),0.175f);


        //bodyCol.isVisible = false; //debug
        //headCol.isVisible = false; //debug
    }


    //getters
    public float getHealthStat() {
        return healthStat;

    }
    public float getDefenseStat() {
        return defenseStat;

    }
    public float getAgilityStat() {
        return agilityStat;

    }
    public float getAttackStat() {
        return attackStat;

    }


    public void lodProjectiles(){
        if(id == MARIO_ID){
            if(AVAILABLE_PROJECTILE_MODELS[MARIO_PROVE_PROJECTILE] == null){
                AVAILABLE_PROJECTILE_MODELS[MARIO_PROVE_PROJECTILE] = new Vector<>();
            }
            for(int i = 0; i < 4; i++){
                AVAILABLE_PROJECTILE_MODELS[MARIO_PROVE_PROJECTILE].add( new ModelInstance(new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal(Character.CHARACTERS_MODELS_DIRECTORY + Character.CHARACTERS_MODELS_FIlE[0]))));
            }
        }



    }

    public void executeInputs(){
        /*
        Vector3 positionTmp = new Vector3();
        transform.getTranslation(positionTmp);
        transform.setTranslation(positionTmp.x+airborneMoveSpeed*moveDirection,positionTmp.y,positionTmp.z);

        per qualche motivo sto codice è necessario per gli spostamenti sull'a sse delle x
        non posso usare transform.translate();
         */

        if(!puppet){//se sono in un campo di battaglia
            //consumazione inputs a fine metodo

            //se setto la stessa animazione con stessi loop l'animazione non cambia (non interrompo fluidità)
            //l'ordine del codice è importante
            float deltaTime = Gdx.graphics.getDeltaTime();//shortcut

            jumpedThisExecution = false;//se no prima di alzarsi da terra resetta i jump disponibili
            endedAttackThisExecution = false;//se no ho 1 frame di camminata in mezzo ad auto combo

            if(inputtedAttackIndex != ATTACK_NONE){

                boolean attackAccepted = false;
                if(!isAttacking() && !isStunned() && !guarding && inputtedAttackIndex >=0 && inputtedAttackIndex <=2){//controllo anche chiamate errate
                    if(grounded){
                        if(lastAttackId != inputtedAttackIndex){
                            currentAttackState = 0;
                        }
                        if(attacks[inputtedAttackIndex][currentAttackState] != null){//controllo per attacchi mancanti
                            currentAttackId = inputtedAttackIndex;
                            attackAccepted = true;
                        }
                    }else{
                        if(lastAttackId != 3 + inputtedAttackIndex){
                            currentAttackState = 0;
                        }
                        if(attacks[3+ inputtedAttackIndex][currentAttackState] != null){//controllo per attacchi mancanti
                            currentAttackId = 3 + inputtedAttackIndex;
                            attackAccepted = true;
                        }
                    }
                }

                //aumento stato attacco e setto tempo tolleranza auto combo
                if(attackAccepted){
                    if(lastAttackId == currentAttackId && attacks[currentAttackId].length>currentAttackState+1){
                        currentAttackState++;
                        autoComboTimer = AUTOCOMBO_TIME_TOLLERANCE;// valore temporaneo per non far cancellare lo stato
                    }else{
                        currentAttackState = 0;
                    }

                }
            }

            //interpreto tasti di movimento e compilo moveDirection
            if((moveLeft && moveRight) || (!moveLeft && !moveRight)){//se premo tutte 2 le direzioni o nessuna
                moveDirection = MOVE_STOP;
            }else{
                if(moveRight){
                    moveDirection = MOVE_SX;
                }else{
                    moveDirection = MOVE_DX;
                }

            }
            //System.out.println(moveLeft+"\t"+moveRight); //debug


            //setto idle animation grounded
            if(!guarding && !isStunned() && grounded && (moveDirection == MOVE_STOP || controller.current.loopCount == 0 || (isAttacking() && controller.current.animation.id.equals(walkAnimation))) && (!isAttacking() || controller.current.loopCount == 0 || controller.current.animation.id.equals(walkAnimation))){
                //controller.setAnimation(idleAnimation,-1);
                controller.setAnimation(idleAnimation,-1);
                controller.current.speed = idleAnimationSpeed;

            }

            //setto idle animation airborne
            if(!guarding && !isStunned() && !grounded && (currentYForce <= 0 || controller.current.loopCount == 0) && (!isAttacking() || controller.current.loopCount == 0)){
                controller.setAnimation(fallingAnimation,-1);
                controller.current.speed = fallingAnimationSpeed;
                controller.current.time=100;
            }

            //movimento asse x (consumazione input a fine metodo)
            if(!guarding && !isStunned()&& moveDirection != MOVE_STOP && !endedAttackThisExecution && (!isAttacking() || attacks[currentAttackId][currentAttackState].currentlyEnableMovement)){
                //salvo posizione
                Vector3 positionTmp = new Vector3();
                transform.getTranslation(positionTmp);

                //rotazione (cancella posizione)
                if(!isAttacking()){
                    transform.setToRotation(0,1,0,moveDirection*90 + (90-(float)Math.toDegrees(Math.atan2(camera3D.position.z - positionTmp.z, camera3D.position.x - positionTmp.x))));
                    facingDirection = moveDirection;
                }

                if(grounded) {
                    if(!((moveDirection == MOVE_SX && !canMoveLeft) || (moveDirection == MOVE_DX && !canMoveRight))){
                            transform.setTranslation(positionTmp.x + groundedMoveSpeed * moveDirection, positionTmp.y, positionTmp.z);
                    }else{
                        transform.setTranslation(positionTmp.x, positionTmp.y, positionTmp.z);
                    }
                    if(!isAttacking()){
                        controller.setAnimation(walkAnimation);
                        controller.current.speed = walkAnimationSpeed;
                    }


                }else{
                    if(!((moveDirection == MOVE_SX && !canMoveLeft) || (moveDirection == MOVE_DX && !canMoveRight))) {
                        transform.setTranslation(positionTmp.x + airborneMoveSpeed * moveDirection, positionTmp.y, positionTmp.z);
                    }else{
                        transform.setTranslation(positionTmp.x, positionTmp.y, positionTmp.z);
                    }
                }
            }

            //knockback asse x
            if(isStunned()){
                Vector3 positionTmp = new Vector3();
                transform.getTranslation(positionTmp);
                transform.setTranslation(positionTmp.x + currentXForce,positionTmp.y,positionTmp.z);

                int xForceSing = (int)(currentXForce / Math.abs(currentXForce));
                currentXForce = (Math.abs(currentXForce) - weight);
                if(currentXForce < 0){
                    currentXForce = 0;
                }else{
                    currentXForce *= xForceSing;
                }


            }else{
                currentXForce = 0;
            }

            //knockback asse y
            if(isStunned()){
                Vector3 positionTmp = new Vector3();
                transform.getTranslation(positionTmp);
                transform.setTranslation(positionTmp.x,positionTmp.y + currentYForce,positionTmp.z);
                if(!grounded){
                    currentYForce -= weight + 0.001;// +0.001 da traiettoria a parabola
                }
            }

            //rotazione di default verso avversario
            if((!isStunned() && moveDirection == MOVE_STOP && enemy != null && !isAttacking() && canMove) || (isAttacking() && !attackedLastFrame) || guarding){
                Vector3 positionTmp1 = new Vector3();
                transform.getTranslation(positionTmp1);
                Vector3 positionTmp2 = new Vector3();
                enemy.transform.getTranslation(positionTmp2);

                //rotazione (compensa il cambio di prospettiva della telecamera 3d)
                if(positionTmp1.x < positionTmp2.x){
                    transform.setToRotation(0,1,0,90 + (90-(float)Math.toDegrees(Math.atan2(camera3D.position.z - positionTmp1.z, camera3D.position.x - positionTmp1.x))));
                    facingDirection = FACING_DX;
                }else{
                    transform.setToRotation(0,1,0,-90 + (90-(float)Math.toDegrees(Math.atan2(camera3D.position.z - positionTmp1.z, camera3D.position.x - positionTmp1.x))));
                    facingDirection = FACING_SX;
                }

                transform.setTranslation(positionTmp1);
            }

            //attacchi (sovrascrivono inputs di salto) (setto attackedLastFrame in fondo al metodo)
            //(dopo movimento per avere il puntamento automatico)
            if(currentAttackId != ATTACK_NONE){
                attacks[currentAttackId][currentAttackState].execute();
            }//eseguo attacco frame per frame
            if(autoComboTimer>0){
                autoComboTimer-=deltaTime;
            }else if(lastAttackId != ATTACK_NONE && currentAttackId == ATTACK_NONE){
                lastAttackId = ATTACK_NONE;
                currentAttackState = 0;
            }//gestione timer auto combo

            //caduta
            if(!isStunned() && !grounded && currentYForce <= 0){
                Vector3 positionTmp = new Vector3();
                transform.getTranslation(positionTmp);
                transform.setTranslation(positionTmp.x,positionTmp.y + currentYForce,positionTmp.z);
                currentYForce -= falAcceleration;
            }

            //salto
            if(!guarding && !isStunned() && !isAttacking() && jump && availableJumps > 0){
                jump = false;
                currentYForce = jumpForce;
                jumpTimer = JUMP_SPAM_BLOCKER;
                availableJumps--;
                jumpedThisExecution = true;
            }//setto valori "logica" di jump
            if(!isStunned() && currentYForce > 0){
                Vector3 positionTmp = new Vector3();
                transform.getTranslation(positionTmp);
                transform.setTranslation(positionTmp.x,positionTmp.y + currentYForce,positionTmp.z);
                currentYForce -= jumpDeceleration;

                //altrimenti ogni tanto mantiene animazione di camminamento
                if(availableJumps == 1 && !(controller.current.animation.id.equals(firstJumpAnimation)) && !isAttacking()){
                    controller.setAnimation(firstJumpAnimation);
                    controller.current.speed = firstJumpAnimationSpeed;
                }
                if(availableJumps == 0 && !(controller.current.animation.id.equals(secondJumpAnimation)) && !isAttacking()){
                    controller.setAnimation(secondJumpAnimation);
                    controller.current.speed = secondJumpAnimationSpeed;

                }
            }//grafica di jump e spostamento
            if(jumpTimer > 0){
                jumpTimer -= deltaTime;
            }//per evitare spam involontario di salti

            //abbassarsi
            if(crouching){
                if(!grounded){
                    //cancello forza salto
                    if(currentYForce > 0){
                        currentYForce = 0;
                    }
                    //fastFall
                    currentYForce -= fastFallAcceleration;
                }

                crouching = false;
            }

            //guardia (guard break gestito da classe attacco)
            if(guarding){//uscita da guardia
                if(!isStunned() && !tryingToGuard && guardExitFramesCounter < GUARD_EXIT_FRAMES_DELAY){
                    guardExitFramesCounter++;
                }else{
                    if(!isStunned()){
                        controller.setAnimation(guardAnimation,-1);
                        controller.current.speed = guardAnimationSpeed;
                        guarding = true;
                    }

                    guardExitFramesCounter = 0;
                }
                if(!isStunned() && !tryingToGuard && guardExitFramesCounter == GUARD_EXIT_FRAMES_DELAY){
                    guarding = false;
                }

            }else{//entrata in guardia
                if(grounded && !isAttacking() && !isStunned() && tryingToGuard && guardStartFramesCounter < GUARD_START_FRAMES_DELAY && currentGuardAmount > 0) {
                    guardStartFramesCounter++;

                }else{
                    guardStartFramesCounter = 0;
                }
                if(grounded && !isAttacking() && !isStunned() && tryingToGuard && guardStartFramesCounter == GUARD_START_FRAMES_DELAY){
                    controller.setAnimation(guardAnimation,-1);
                    controller.current.speed = guardAnimationSpeed;
                    guarding = true;
                }

            }
            if(currentGuardAmount < maxGuardAmount){
                //aumento contatore guardia
                if(guardRegenerationFramesCounter < GUARD_BREAK_REGENERATION_ACTIVATION_DELAY){
                    guardRegenerationFramesCounter++;
                }

                //controllo se posso rigenerare mana
                if(currentGuardAmount == 0 && guardRegenerationFramesCounter == GUARD_BREAK_REGENERATION_ACTIVATION_DELAY || currentGuardAmount != 0 && guardRegenerationFramesCounter >= GUARD_NORMAL_REGENERATION_ACTIVATION_DELAY){//guardia rotta
                    guardRegenerationRateoFramesCounter++;

                    if(guardRegenerationRateoFramesCounter == FRAMES_FOR_GUARD_REGENERATION_AMMOUNT){
                        currentGuardAmount  += GUARD_REGENARATION_AMMOUNT;
                        guardRegenerationRateoFramesCounter = 0;
                    }

                }




            }else{
                guardRegenerationFramesCounter = 0;
                guardRegenerationRateoFramesCounter = 0;
                if(currentGuardAmount > maxGuardAmount){
                    currentGuardAmount = maxGuardAmount;
                }//preventivo

            }

            //modifica di animazione di stun (per quando cambi da grounded o no e viceversa mid stun)
            if(isStunned()){
                if(grounded){
                    if(!guarding && !controller.current.animation.id.equals(normalHitAnimation)){
                        float animationTime = controller.current.time;

                        controller.setAnimation(normalHitAnimation);
                        controller.current.speed = normalHitAnimationSpeed;
                        controller.current.time = animationTime;
                    }
                }else if(!controller.current.animation.id.equals(airHitAnimation)){
                    float animationTime = controller.current.time;

                    controller.setAnimation(airHitAnimation);
                    controller.current.speed = airHitAnimationSpeed;
                    controller.current.time = animationTime;
                }
            }

            //esecuzione proiettili spawnati
            for(int i =0 ; i < existingCharacterProjectiles.size(); i++){
                existingCharacterProjectiles.get(i).execute();

            }


            //prendo posizione corrente
            Vector3 positionTmp = new Vector3();
            transform.getTranslation(positionTmp);


            bodyCol.setX2DPosition(originalBodyColliderRelativeX * facingDirection);
            headCol.setX2DPosition(originalHeadColliderRelativeX * facingDirection);

            //"consumo" input
            inputtedAttackIndex = ATTACK_NONE;
            grounded = false;
            moveLeft = false;
            moveRight = false;
            tryingToGuard = false;
            canMoveRight = true;
            canMoveLeft = true;
            jump = false;

            attackedLastFrame = false;
            if(isAttacking()){
                attackedLastFrame = true;
            }

            if(isStunned()){
                currentStunFrames--;
            }


        }else{
            controller.setAnimation(idleAnimation, -1);
        }
    }


    public boolean isAttacking(){
        //System.out.println(currentAttackId != ATTACK_NONE);//debug
        return currentAttackId != ATTACK_NONE;
    }
    public void setAttack(int index){
        inputtedAttackIndex = index;
    }
    public Attack getCurrentAttack(){
        if(isAttacking()){
            return attacks[currentAttackId][currentAttackState];
        }
        return null;
    }//versione salva, eventualmente copiare info in box collider
    public boolean tryToJump(){
        //System.out.println(jumpTimer);
        if(jumpTimer <= 0 && availableJumps > 0 && !isAttacking() && !isStunned()){
            jump = true;
            return true;
        }
        return false;
    }
    public void setMoveLeft(boolean val){
        moveLeft = val;

    }
    public void setMoveRight(boolean val){
        moveRight = val;

    }
    public boolean isStunned(){
        return this.currentStunFrames > 0;

    }
    public void drawColliders(ShapeRenderer r){
        if(bodyCol.isVisible){
            r.rect((bodyCol.get2DPosition().x - bodyCol.width/2 + 4.85f)*Gdx.graphics.getWidth()/9.7f , (bodyCol.get2DPosition().y - bodyCol.height/2 + 2.7f)*Gdx.graphics.getHeight()/5.4f,bodyCol.width*Gdx.graphics.getWidth()/9.7f, bodyCol.height*Gdx.graphics.getHeight()/5.4f);
        }
        if(headCol.isVisible){
            r.circle((headCol.get2DPosition().x + 4.85f)*Gdx.graphics.getWidth()/9.7f , (headCol.get2DPosition().y + 2.7f)*Gdx.graphics.getHeight()/5.4f,headCol.radius*Gdx.graphics.getWidth()/9.7f);
        }

        for(Collider2D col : personalColliders){
            if(col.isVisible)col.draw(r);
        }
    }
    public void addCollider(Collider2D col){
        this.personalColliders.add(col);
        this.existingColliders.add(col);

    }
    public void removeCollider(Collider2D col){
        if(this.personalColliders.contains(col)){
            this.personalColliders.remove(col);
            this.existingColliders.remove(col);
        }
    }
    public void resolveHits(){//chiamato in ogni frame in cui collido con un attacco
        if(meleeHitsToExecute != null){

            meleeHitsToExecute.hit(this);

            //se non ho armatura setto l'attacco a null
            if(!haveArmor && this.currentAttackId != ATTACK_NONE){
                this.attacks[currentAttackId][currentAttackState].interrupt();
                this.currentAttackState = 0;
                this.currentAttackId = ATTACK_NONE;
            }

            meleeHitsToExecute = null;// se l'attacco sta ancora hittando hit sara ancora diversa da nul nel prossimo frame
        }//attacchi fisici

        for(Projectile projectile: projectileHitsToExecute){
            projectile.hit(this);

            //se non ho armatura setto l'attacco a null
            if(!haveArmor && this.currentAttackId != ATTACK_NONE){
                this.attacks[currentAttackId][currentAttackState].interrupt();
                this.currentAttackState = 0;
                this.currentAttackId = ATTACK_NONE;
            }

        }//proiettili
        projectileHitsToExecute.clear();
    }
    public void setIdleAnimation(){
        this.controller.setAnimation(idleAnimation,-1);
        controller.current.speed = idleAnimationSpeed;
    }



    public Point2D.Float get2DPosition(){
        Vector3 pos= new Vector3();
        transform.getTranslation(pos);
        return new Point2D.Float(pos.x,pos.y);
    }
    public void set2DPosition(Point2D.Float p){
        transform.setTranslation(p.x, p.y, 0);

    }

    public void setX2DPosition(float x) {
        Vector3 pos= new Vector3();
        transform.getTranslation(pos);

        transform.setTranslation(x, pos.y, 0);

    }
    public void setY2DPosition(float y) {
        Vector3 pos= new Vector3();
        transform.getTranslation(pos);

        transform.setTranslation(pos.x, y, 0);

    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;

    }
    public void collision(Collider2D myCollider, Collider2D otherCollider) {

        //se la collisione è avvenuta tra il mio corpo e qualcosa
        if(myCollider.getTag().equals(BODY_COLLIDER_TAG)){
            if(otherCollider.getTag().equals(Attack.ATTACK_COLLIDER_TAG)) {
                meleeHitsToExecute = (Attack)(otherCollider.owner);
            }
            if(otherCollider.getTag().equals(BattleStage.RIGHT_BOUND_TAG) || otherCollider.getTag().equals(BattleStage.LEFT_BOUND_TAG)){

                if(otherCollider.getTag().equals(BattleStage.RIGHT_BOUND_TAG)) {
                    canMoveRight = false;
                    if(isStunned() || moveDirection == MOVE_DX){
                        //setta o.1f fuori da collisione
                        if(headCol.radius + originalHeadColliderRelativeX * facingDirection > bodyCol.width / 2 + originalBodyColliderRelativeX * facingDirection){
                            setX2DPosition(otherCollider.get2DPosition().x - ((BoxCollider)otherCollider).width/2 - headCol.radius - originalHeadColliderRelativeX * facingDirection + 0.01f);
                            System.out.println(originalHeadColliderRelativeX * facingDirection);
                        }else{
                            setX2DPosition(otherCollider.get2DPosition().x - ((BoxCollider)otherCollider).width/2 - bodyCol.width/2 - originalBodyColliderRelativeX * facingDirection + 0.01f);
                            System.out.println(originalBodyColliderRelativeX * facingDirection);
                        }

                    }

                }
                if(otherCollider.getTag().equals(BattleStage.LEFT_BOUND_TAG)) {
                    canMoveLeft = false;
                    if(isStunned() || moveDirection == MOVE_SX) {
                        //setta o.1f fuori da collisione
                        if(headCol.radius + originalHeadColliderRelativeX * facingDirection > bodyCol.width / 2 + originalBodyColliderRelativeX * facingDirection){
                            setX2DPosition(otherCollider.get2DPosition().x + ((BoxCollider)otherCollider).width/2 + headCol.radius + originalHeadColliderRelativeX * facingDirection - 0.1f);
                        }else{
                            setX2DPosition(otherCollider.get2DPosition().x + ((BoxCollider) otherCollider).width / 2 + bodyCol.width / 2 + originalBodyColliderRelativeX * facingDirection - 0.1f);
                        }

                    }
                }

                if(grounded){
                    if((otherCollider.getTag().equals(BattleStage.RIGHT_BOUND_TAG) && currentXForce > 0) || (otherCollider.getTag().equals(BattleStage.LEFT_BOUND_TAG) && currentXForce < 0)){
                        currentXForce = 0;
                    }
                }else{
                    currentXForce *= -1;
                    System.out.println("inverto");
                }
            }
            if(otherCollider.getTag().equals(BattleStage.GROUND_TAG)){
                grounded = true;
                if(!isStunned()){
                    if(!jumpedThisExecution){
                        availableJumps = numberOfJumps;
                        currentYForce = 0;
                    }

                    Vector3 positionTmp = new Vector3();
                    transform.getTranslation(positionTmp);
                    transform.setTranslation(positionTmp.x,otherCollider.get2DPosition().y + ((BoxCollider)otherCollider).height / 2 - bodyCol.center.y + bodyCol.height / 2 - 0.01f,positionTmp.z);

                }else{
                    if(currentYForce < 0){
                        Vector3 positionTmp = new Vector3();
                        transform.getTranslation(positionTmp);
                        transform.setTranslation(positionTmp.x,otherCollider.get2DPosition().y + ((BoxCollider)otherCollider).height / 2 - bodyCol.center.y + bodyCol.height / 2 + 0.01f,positionTmp.z);
                        currentYForce = - currentYForce / 2;
                    }
                }
            }
            if(otherCollider.getTag().equals(Projectile.PROJECTILE_OLLIDER_TAG)){
                projectileHitsToExecute.add((Projectile)otherCollider.owner);
            }


        }
    }
}





















