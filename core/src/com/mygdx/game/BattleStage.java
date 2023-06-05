package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.awt.geom.Point2D;
import java.util.Vector;

public class BattleStage extends GenericGameObject{
    public static final String STAGE_TAG="stage";
    public static final String GROUND_TAG="ground", LEFT_BOUND_TAG="leftBound", RIGHT_BOUND_TAG="rightBound";
    final String STAGES_PHOTOS_DIRECTORY = "Stages/";
    final float BOUNDS_SIZE = 3;

    final String[] STAGES_NAMES = {"stage mario"};
    final float[] STAGES_HEIGHTS = {-1.999f};
    final String[] STAGES_PHOTOS = {"Stage 1/sfondo 1 tot.png"};
    final Point2D.Float[] STAGES_C1_SPAWNS = {new Point2D.Float(-1.5f,1.4f)};
    final Point2D.Float[] STAGES_C2_SPAWNS = {new Point2D.Float(1.5f,1.4f)};



    String tag;
    final int stageId;
    BoxCollider groundCollider,leftCollider,rightCollider;
    Image img;
    Vector<Collider2D> existingColliders;


    BattleStage(int stageId, Vector<Collider2D> existingColliders){
        super(0,0,"");
        this.stageId = stageId;
        this.existingColliders = existingColliders;

        constructor();

    }


    private void constructor(){
        this.tag=STAGE_TAG;
        img = new Image(new Texture(Gdx.files.internal(STAGES_PHOTOS_DIRECTORY + STAGES_PHOTOS[stageId])));
        img.setPosition(0, -120);


        //setto colliders stages
        groundCollider = new BoxCollider(this,0,STAGES_HEIGHTS[stageId]-BOUNDS_SIZE/2,Gdx.graphics.getWidth(),BOUNDS_SIZE);
        groundCollider.setTag(GROUND_TAG);
        groundCollider.useRelativePosition = false;
        groundCollider.isVisible = false;
        existingColliders.add(groundCollider);

        leftCollider = new BoxCollider(this,-9.7f/2-BOUNDS_SIZE/2,0,BOUNDS_SIZE,1000);
        leftCollider.setTag(LEFT_BOUND_TAG);
        leftCollider.useRelativePosition = false;
        //leftCollider.isVisible = false;
        existingColliders.add(leftCollider);

        rightCollider = new BoxCollider(this,9.7f/2+BOUNDS_SIZE/2,0,BOUNDS_SIZE,1000);
        rightCollider.setTag(RIGHT_BOUND_TAG);
        rightCollider.useRelativePosition = false;
        //rightCollider.isVisible = false;
        existingColliders.add(rightCollider);

        colliders.add(groundCollider);
        colliders.add(leftCollider);
        colliders.add(rightCollider);
    }

    public void draw(Batch batch, float parentAlfa){
        img.draw(batch, parentAlfa);


    }
    public Point2D.Float getC1spawnPoint(){
        return STAGES_C1_SPAWNS[this.stageId];

    }
    public Point2D.Float getC2spawnPoint(){
        return STAGES_C2_SPAWNS[this.stageId];

    }





}
