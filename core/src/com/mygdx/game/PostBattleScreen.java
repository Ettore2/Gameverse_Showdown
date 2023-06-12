package com.mygdx.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.awt.geom.Point2D;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class PostBattleScreen extends GameState{
    Character p1, p2;
    int battleEndState;
    PerspectiveCamera camera3D;
    Environment environment;
    private Stage stageBackGround;
    private Batch stageBackGroundBatch;
    private ModelBatch modelBatch;
    Image imgBackGround, imgOutputText;



    public PostBattleScreen(Main game, Character p1, Character p2, int battleEndState, PerspectiveCamera camera3D, Environment environment) {
        super(game);

        this.p1 = p1;
        this.p2 = p2;
        this.battleEndState = battleEndState;
        this.camera3D = camera3D;
        camera3D.position.set(0, 3, 3);
        camera3D.lookAt(0, 3, 0);
        camera3D.update();
        this.environment = environment;


        this.stageBackGround = new Stage();
        this.stageBackGroundBatch = stageBackGround.getBatch();
        this.modelBatch = new ModelBatch();

        imgBackGround = new Image(new Texture(GameConstants.BACKGROUND_LOADING_GENERAL));
        stageBackGround.addActor(imgBackGround);
        p1.setFeetAt(new Point2D.Float(-1.7f,0));
        p1.setYRotation(80);
        p2.setFeetAt(new Point2D.Float(1.7f,0));
        p2.setYRotation(-10);

        String imgBackGroundFileName = "Img/PostBattleScreen/Draw.png";
        p1.controller.setAnimation(p1.loseAnimation, -1);
        p1.controller.current.speed = p1.loseAnimationSpeed;
        p2.controller.setAnimation(p2.loseAnimation, -1);
        p2.controller.current.speed = p2.loseAnimationSpeed;

        if(battleEndState == BattleScreen.STATE_WIN_P1){
            p1.controller.setAnimation(p1.winAnimation, 1);
            p1.controller.current.speed = p1.winAnimationSpeed;
            imgBackGroundFileName = "Img/PostBattleScreen/P1WINS.png";
        }
        if(battleEndState == BattleScreen.STATE_WIN_P2) {
            p2.controller.setAnimation(p2.winAnimation, 1);
            p2.controller.current.speed = p2.winAnimationSpeed;
            imgBackGroundFileName = "Img/PostBattleScreen/P2WINS.png";
        }


        imgOutputText = new Image(new Texture(imgBackGroundFileName));
        imgOutputText.setPosition(stageBackGround.getCamera().position.x - (imgOutputText.getWidth()*imgOutputText.getScaleX()/2),stageBackGround.getCamera().position.y - GameConstants.screenHeight * 0.342f);;
        stageBackGround.addActor(imgOutputText);
    }

    private void quit(){
        game.setScreen(new ChooseCharactersScreen(game));
        this.dispose();
    }
    @Override
    public void normalExecution(float delta) {

        System.out.println(p1.controller.current.loopCount);
        if(p1.controller.current.loopCount == 0){
            p1.controller.setAnimation(p1.winAnimationIdle, -1);
            p1.controller.current.speed = p1.winAnimationIdleSpeed;
        }
        if(p2.controller.current.loopCount == 0){
            p2.controller.setAnimation(p2.winAnimationIdle, -1);
            p2.controller.current.speed = p2.winAnimationIdleSpeed;
        }

        //disegno grafica 2D
        stageBackGround.act();
        stageBackGround.draw();

        //disegno grafica 3D
        modelBatch.begin(camera3D);//begin
        //System.out.println(Character.AVAILABLE_PROJECTILE_MODELS[0].size());
        if (p1 != null) {
            p1.controller.update(delta);
            modelBatch.render(p1);
        }
        if (p2 != null) {
            p2.controller.update(delta);
            modelBatch.render(p2);
        }
        modelBatch.end();//end

    }




    //pulsanti--------------------------------------------------------------
    public void c1_buttonDpadRight() {

    }
    public void c1_buttonDpadLeft() {

    }
    public void c1_buttonDpadUp() {

    }
    public void c1_buttonDpadDown() {

    }
    public void c1_buttonA() {
        quit();
    }
    public void c1_buttonB() {
        quit();
    }
    public void c1_buttonBack() {
        quit();
    }
    public void c1_buttonY() {
        quit();
    }
    public void c1_buttonX() {
        quit();
    }
    public void c1_buttonStart() {
        quit();
    }
    public void c1_buttonL1() {
        quit();
    }
    public void c1_buttonR1() {
        quit();
    }
    public void c1_buttonLeftStick() {
        quit();
    }
    public void c1_buttonRightStick() {
        quit();
    }
    public void c1_axisLeftX(float val) {

    }
    public void c1_axisRightX(float val) {

    }
    public void c1_axisLeftY(float val) {

    }
    public void c1_axisRightY(float val) {

    }


    public void c2_buttonDpadRight() {

    }
    public void c2_buttonDpadLeft() {

    }
    public void c2_buttonDpadUp() {

    }
    public void c2_buttonDpadDown() {

    }
    public void c2_buttonA() {
        quit();
    }
    public void c2_buttonB() {
        quit();
    }
    public void c2_buttonBack() {
        quit();
    }
    public void c2_buttonY() {
        quit();
    }
    public void c2_buttonX() {
        quit();
    }
    public void c2_buttonStart() {
        quit();
    }
    public void c2_buttonL1() {
        quit();
    }
    public void c2_buttonR1() {
        quit();
    }
    public void c2_buttonLeftStick() {
        quit();
    }
    public void c2_buttonRightStick() {
        quit();
    }
    public void c2_axisLeftX(float val) {

    }
    public void c2_axisRightX(float val) {

    }
    public void c2_axisLeftY(float val) {

    }
    public void c2_axisRightY(float val) {

    }











    //altri override
    public void show() {}
    public void resize(int width, int height) {}
    public void pause() {}
    public void resume() {}
    public void hide() {}
    public void dispose() {}
}
