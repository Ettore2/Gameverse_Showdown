package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Vector;

public class ChooseCharactersScreen extends GameState{

    private static final String[] pathCharacters = {"Img/Characters/Mario/Mario", "Img/Characters/Donkey Kong/Donkey Kong", "Img/Characters/QuestionMark/QuestionMark"};
    private int c1_positionX, c1_positionY, c1_positionBtnX, c1_tmpPosition;
    private int c1_positionIsTakenX, c1_positionIsTakenY;
    private int c2_positionX, c2_positionY;
    private int c2_positionIsTakenX, c2_positionIsTakenY;
    private boolean checkLblCharacters, checkBtnCustomsConfirms;
    private Image backgroundImage;
    private Stage stage;
    private Camera camera;
    private FitViewport viewPort;
    private Label lblG1, lblG2;
    private Table tableCharacters;
    private ImageC[][] lblCharacters;
    private ButtonC[] btnConfirmsCustom;
    private Skin skinOrange, skinDarkOrange, skinBlue, skinRed;

    private Texture menuBox1, menuBox2;
    private Image boxImage1, boxImage2;
    private ProgressBar[] statsBar1, statsBar2;

    Character c1, c2;
    Vector<Character> characters;

    private Environment environment;
    private PerspectiveCamera camera3D; //(camera2D è gia presente in classe stage)
    private ModelBatch modelBatch;

    public ChooseCharactersScreen(Main game) {
        super(game);
        setInputsDelay(0.15f);

        camera = new OrthographicCamera();
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage(viewPort);

        Gdx.input.setInputProcessor(stage);

        skinOrange = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_ORANGE));
        skinDarkOrange = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_ORANGEDARK));
        skinBlue = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_BLUE));
        skinRed = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_RED));

        //select character

        lblG1 = new Label("Player 1", skinOrange);
        lblG2 = new Label("Player 2", skinOrange);

        lblG1.setPosition(GameConstants.screenWidth * 0.066f,GameConstants.screenHeight * 0.915f);
        lblG2.setPosition(GameConstants.screenWidth - (GameConstants.screenWidth * 0.066f) - lblG1.getWidth(), GameConstants.screenHeight * 0.915f);

        //setup ambiente 3D
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, 0, -1, -5.5f));

        modelBatch = new ModelBatch();

        // Create a perspective camera with some sensible defaults
        camera3D = new PerspectiveCamera(50, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera3D.position.set(0, 3f, 5.5f);
        camera3D.lookAt(0, 2, 0);
        camera3D.near = 1f;
        camera3D.far = 300f;
        camera3D.update();
        UBJsonReader jsonReader = new UBJsonReader();
        // Import and instantiate our model (called "myModel.g3dj")
        ModelBuilder modelBuilder = new ModelBuilder();

        //table choose characters
        tableCharacters = new Table();
        tableCharacters.defaults().size((float) (GameConstants.screenWidth * 0.1), (float) (GameConstants.screenHeight * 0.142));
        tableCharacters.setPosition(GameConstants.screenWidth / 2f, GameConstants.screenHeight / 2.5f);

        lblCharacters = new ImageC[3][6];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                if(i == 0 && j >= 0 && j <= Character.AVILABLE_CHARACTERS){
                    lblCharacters[i][j] = new ImageC(new Texture(pathCharacters[j] + ".png"), pathCharacters[j] + ".png", pathCharacters[j] + "_isOver.png", pathCharacters[j] + "_isOverC2.png",j);
                    tableCharacters.add(lblCharacters[i][j]).pad(0, 0, 25, 25);
                    //System.out.println(j);
                }else{
                    lblCharacters[i][j] = new ImageC(new Texture(pathCharacters[2] + ".png"), pathCharacters[2] + ".png", pathCharacters[2] + "_isOver.png", pathCharacters[2] + "_isOverC2.png");
                    tableCharacters.add(lblCharacters[i][j]).pad(0, 0, 25, 25);
                }
            }
            tableCharacters.row();
        }

        //btn custom and confirms
        Table tableBtn = new Table();
        tableBtn.defaults().size((float) (GameConstants.screenWidth * 0.21), (float) (GameConstants.screenHeight * 0.12));
        tableBtn.setPosition(GameConstants.screenWidth / 2.02f, GameConstants.screenHeight * 0.09f);

        btnConfirmsCustom = new ButtonC[2];

        btnConfirmsCustom[0] = new ButtonC("Customs", skinOrange, skinDarkOrange);
        btnConfirmsCustom[1] = new ButtonC("Confirms", skinOrange, skinDarkOrange, true);

        tableBtn.add(btnConfirmsCustom[0]).pad(0, 0,0, 25);
        tableBtn.add(btnConfirmsCustom[1]);

        //background
        Texture background = new Texture(GameConstants.BACKGROUND_CHOOSECHARACTERS);
        backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);

        //Box statistic of characters

        menuBox1 = new Texture(GameConstants.STATSBOX_TEXTURE_RED);
        menuBox2 = new Texture(GameConstants.STATSBOX_TEXTURE_BLUE);

        //width 1680 height 913
        //width 3008 height 1639

        boxImage1 = new Image(menuBox1);
        boxImage1.setSize(GameConstants.screenWidth * 0.14f, GameConstants.screenHeight * 0.45f);
        boxImage1.setPosition(GameConstants.screenWidth * 0.016f, GameConstants.screenHeight * 0.183f);

        boxImage2 = new Image(menuBox2);
        boxImage2.setSize(GameConstants.screenWidth * 0.14f, GameConstants.screenHeight * 0.45f);
        boxImage2.setPosition(GameConstants.screenWidth * 0.84f, GameConstants.screenHeight * 0.183f);

        statsBar1 = new ProgressBar[4];
        statsBar2 = new ProgressBar[4];

        for(int i = 0; i < 4; i++){
            statsBar1[i] = new ProgressBar(0f, 5f, 1f, false, skinRed);
            statsBar1[i].setSize(GameConstants.screenWidth * 0.104f, GameConstants.screenHeight * 0.009f);

            statsBar2[i] = new ProgressBar(0f, 5f, 1f, false, skinBlue);
            statsBar2[i].setSize(GameConstants.screenWidth * 0.104f, GameConstants.screenHeight * 0.009f);
        }


        statsBar1[0].setPosition(GameConstants.screenWidth * 0.03f, GameConstants.screenHeight * 0.531f);
        statsBar1[1].setPosition(GameConstants.screenWidth * 0.03f, GameConstants.screenHeight * 0.426f);
        statsBar1[2].setPosition(GameConstants.screenWidth * 0.03f, GameConstants.screenHeight * 0.316f);
        statsBar1[3].setPosition(GameConstants.screenWidth * 0.03f, GameConstants.screenHeight * 0.216f);

        statsBar2[0].setPosition(GameConstants.screenWidth * 0.855f, GameConstants.screenHeight * 0.531f);
        statsBar2[1].setPosition(GameConstants.screenWidth * 0.855f, GameConstants.screenHeight * 0.426f);
        statsBar2[2].setPosition(GameConstants.screenWidth * 0.855f, GameConstants.screenHeight * 0.316f);
        statsBar2[3].setPosition(GameConstants.screenWidth * 0.855f, GameConstants.screenHeight * 0.216f);


        //variable for logic

        checkLblCharacters = true;
        checkBtnCustomsConfirms = false;

        c1_positionX = 0;
        c1_positionY = 0;

        c1_positionBtnX = 0;
        c1_tmpPosition = 0;

        c2_positionX = 1;
        c2_positionY = 0;

        c1_positionIsTakenX = -1;
        c1_positionIsTakenY = -1;

        c2_positionIsTakenX = -1;
        c2_positionIsTakenY = -1;

        lblCharacters[0][1].isOverC2 = true;
        lblCharacters[0][0].isOver = true;

        //add component to stage

        stage.addActor(backgroundImage);
        stage.addActor(tableCharacters);

        stage.addActor(lblG1);
        stage.addActor(lblG2);

        stage.addActor(tableBtn);

        stage.addActor(boxImage1);
        stage.addActor(boxImage2);


        for (int i = 0; i < 4; i++) {
            stage.addActor(statsBar1[i]);
            stage.addActor(statsBar2[i]);
        }

        characters = new Vector<>();
        for(int i = 0; i< Character.AVILABLE_CHARACTERS; i++){
            characters.add(new Character(camera3D, i,0,0,0,0));
            characters.get(i).setIdleAnimation();
        }

        updateC1(lblCharacters[c1_positionY][c1_positionX].id);
        updateC2(lblCharacters[c2_positionY][c2_positionX].id);
    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);//codice aggiunto

        stage.act(delta);
        stage.draw();

        if(c1_positionIsTakenY != -1 && c1_positionIsTakenX != -1 && c2_positionIsTakenY != -1 && c2_positionIsTakenX != -1){
            btnConfirmsCustom[1].disabled = false;
        }else{
            btnConfirmsCustom[1].disabled = true;
        }

        modelBatch.begin(camera3D);

        if(c1 != null){
            c1.controller.update(Gdx.graphics.getDeltaTime());
            modelBatch.render(c1, environment);
        }
        if(c2 != null){
            c2.controller.update(Gdx.graphics.getDeltaTime());
            modelBatch.render(c2, environment);
        }
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height,true);
        backgroundImage.setSize(viewPort.getWorldWidth(), viewPort.getWorldHeight());
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateC1(int id){

        if(id >= 0 && id < Character.AVILABLE_CHARACTERS){
            c1 = characters.get(id);
            c1.transform.setToRotation(0,1,0,80);
            c1.transform.setTranslation(-3.8f,3f,0);
            statsBar1[0].setValue(c1.getHealthStat());
            statsBar1[1].setValue(c1.getAttackStat());
            statsBar1[2].setValue(c1.getAgilityStat());
            statsBar1[3].setValue(c1.getDefenseStat());
        }else{
            c1 = null;
            statsBar1[0].setValue(0);
            statsBar1[1].setValue(0);
            statsBar1[2].setValue(0);
            statsBar1[3].setValue(0);
        }
    }
    public void updateC2(int id){

        if(id >= 0 && id < Character.AVILABLE_CHARACTERS){
            c2 = characters.get(id);
            c2.transform.setToRotation(0,1,0,-10);
            c2.transform.setTranslation(3.8f,3f,0);
            statsBar2[0].setValue(c2.getHealthStat());
            statsBar2[1].setValue(c2.getAttackStat());
            statsBar2[2].setValue(c2.getAgilityStat());
            statsBar2[3].setValue(c2.getDefenseStat());
        }else{
            c2 = null;
            statsBar2[0].setValue(0);
            statsBar2[1].setValue(0);
            statsBar2[2].setValue(0);
            statsBar2[3].setValue(0);
        }
    }

    @Override
    public void c1_buttonDpadRight() {//d
        if((c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) || (c1_positionIsTakenY != -1 && c1_positionIsTakenX != -1 && c2_positionIsTakenY != -1 && c2_positionIsTakenX != -1)){
            if (c1_positionX + 1 < lblCharacters[0].length && checkLblCharacters) {
                if (lblCharacters[c1_positionY][c1_positionX + 1].isOverC2 && c1_positionX + 2 <= 5) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionX += 2;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                } else if (!lblCharacters[c1_positionY][c1_positionX + 1].isOverC2) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionX++;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                }
                if (c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) {
                    updateC1(lblCharacters[c1_positionY][c1_positionX].id);
                }

            } else if (c1_positionBtnX + 1 < btnConfirmsCustom.length && checkBtnCustomsConfirms && !btnConfirmsCustom[c1_positionBtnX + 1].disabled) {
                btnConfirmsCustom[c1_positionBtnX].isOver = false;
                c1_positionBtnX++;
                btnConfirmsCustom[c1_positionBtnX].isOver = true;
            }
        }
    }

    @Override
    public void c1_buttonDpadLeft() {//a
        if((c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) || (c1_positionIsTakenY != -1 && c1_positionIsTakenX != -1 && c2_positionIsTakenY != -1 && c2_positionIsTakenX != -1)) {
            if (c1_positionX - 1 >= 0 && checkLblCharacters) {
                if (lblCharacters[c1_positionY][c1_positionX - 1].isOverC2 && c1_positionX - 2 >= 0) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionX -= 2;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                } else if (!lblCharacters[c1_positionY][c1_positionX - 1].isOverC2) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionX--;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                }
                if (c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) {
                    updateC1(lblCharacters[c1_positionY][c1_positionX].id);
                }
            } else if (c1_positionBtnX - 1 >= 0 && checkBtnCustomsConfirms) {
                btnConfirmsCustom[c1_positionBtnX].isOver = false;
                c1_positionBtnX--;
                btnConfirmsCustom[c1_positionBtnX].isOver = true;
            }
        }
    }

    @Override
    public void c1_buttonDpadUp() {//w
        if((c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) || (c1_positionIsTakenY != -1 && c1_positionIsTakenX != -1 && c2_positionIsTakenY != -1 && c2_positionIsTakenX != -1)) {
            if (c1_positionY - 1 >= 0 && checkLblCharacters) {
                if (lblCharacters[c1_positionY - 1][c1_positionX].isOverC2 && c1_positionY - 2 >= 0) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionY -= 2;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                } else if (!lblCharacters[c1_positionY - 1][c1_positionX].isOverC2) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionY--;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                }
                if (c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) {
                    updateC1(lblCharacters[c1_positionY][c1_positionX].id);
                }
            } else if (checkBtnCustomsConfirms) {

                checkLblCharacters = true;
                checkBtnCustomsConfirms = false;
                btnConfirmsCustom[c1_positionBtnX].isOver = false;

                if (c1_positionBtnX != c1_tmpPosition && c1_positionX >= 0 && c1_positionX <= 2) {// se era 1 e ora è 0
                    c1_positionX = 4;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;
                } else if (c1_positionBtnX != c1_tmpPosition && c1_positionX > 2 && c1_positionX <= 5) {//se era 0 e ora è 1
                    c1_positionX = 1;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;
                } else {
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;
                }


                c1_positionBtnX = 0;
            }
        }
    }

    @Override
    public void c1_buttonDpadDown() {//s
        if((c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) || (c1_positionIsTakenY != -1 && c1_positionIsTakenX != -1 && c2_positionIsTakenY != -1 && c2_positionIsTakenX != -1)) {
            if (c1_positionY + 1 < lblCharacters.length && checkLblCharacters) {
                if (lblCharacters[c1_positionY + 1][c1_positionX].isOverC2 && c1_positionY + 2 < lblCharacters.length) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionY += 2;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                } else if (!lblCharacters[c1_positionY + 1][c1_positionX].isOverC2) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    c1_positionY++;
                    lblCharacters[c1_positionY][c1_positionX].isOver = true;

                }
                if (c1_positionIsTakenY == -1 && c1_positionIsTakenX == -1) {
                    updateC1(lblCharacters[c1_positionY][c1_positionX].id);
                }
            } else if (c1_positionY + 1 >= lblCharacters.length && checkLblCharacters) {

                if (c1_positionX >= 0 && c1_positionX <= 2 && !btnConfirmsCustom[1].disabled) {

                    System.out.println("ok");

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    btnConfirmsCustom[0].isOver = true;
                    checkLblCharacters = false;
                    checkBtnCustomsConfirms = true;
                    c1_positionBtnX = 0;
                    c1_tmpPosition = 0;

                } else if (c1_positionX > 2 && c1_positionX <= 5 && !btnConfirmsCustom[1].disabled) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    btnConfirmsCustom[1].isOver = true;
                    checkLblCharacters = false;
                    checkBtnCustomsConfirms = true;
                    c1_positionBtnX = 1;
                    c1_tmpPosition = 1;

                } else if (btnConfirmsCustom[1].disabled) {

                    lblCharacters[c1_positionY][c1_positionX].isOver = false;
                    btnConfirmsCustom[0].isOver = true;
                    checkLblCharacters = false;
                    checkBtnCustomsConfirms = true;
                    c1_positionBtnX = 0;
                    c1_tmpPosition = 0;
                }
            }
        }

    }

    @Override
    public void c1_buttonA() {//k
        if(lblCharacters[c1_positionY][c1_positionX].id != -1){
            lblCharacters[c1_positionY][c1_positionX].isTaken = true;
            c1_positionIsTakenX = c1_positionX;
            c1_positionIsTakenY = c1_positionY;
        }else if(btnConfirmsCustom[1].isOver && !btnConfirmsCustom[1].disabled){
            game.setScreen(new BattleScreen(game,0,lblCharacters[c1_positionIsTakenY][c1_positionIsTakenX].id,lblCharacters[c2_positionIsTakenY][c2_positionIsTakenX].id,180));
        }
    }

    @Override
    public void c1_buttonB() {//L == o
        if(c1_positionIsTakenX != -1 && c1_positionIsTakenY != -1){
            lblCharacters[c1_positionIsTakenY][c1_positionIsTakenX].isTaken = false;

            lblCharacters[c1_positionY][c1_positionX].isOver = false;

            checkLblCharacters = true;
            checkBtnCustomsConfirms = false;

            c1_positionX = c1_positionIsTakenX;
            c1_positionY = c1_positionIsTakenY;

            lblCharacters[c1_positionY][c1_positionX].isOver = true;

            btnConfirmsCustom[c1_positionBtnX].isOver = false;

            c1_positionIsTakenX = -1;
            c1_positionIsTakenY = -1;
            updateC1(lblCharacters[c1_positionY][c1_positionX].id);
        }else if(c1_positionIsTakenX == -1 && c1_positionIsTakenY == -1){
            changeScreen(MENU);
        }
    }

    @Override
    public void c1_buttonBack() {//I == triangolo

    }

    @Override
    public void c1_buttonY() {//J == quadrato

    }

    @Override
    public void c1_buttonX() {

    }

    @Override
    public void c1_buttonStart() {

    }

    @Override
    public void c1_buttonL1() {

    }

    @Override
    public void c1_buttonR1() {

    }

    @Override
    public void c1_buttonLeftStick() {

    }

    @Override
    public void c1_buttonRightStick() {

    }

    @Override
    public void c1_axisLeftX(float val) {
        if(val>0){
            c1_buttonDpadRight();
        }else{
            c1_buttonDpadLeft();
        }
    }

    @Override
    public void c1_axisLeftY(float val) {
        if(val>0){
            c1_buttonDpadDown();
        }else{
            c1_buttonDpadUp();
        }
    }

    @Override
    public void c1_axisRightX(float val) {

    }
    @Override
    public void c1_axisRightY(float val) {

    }

//---------------------------------------------------------------------------------------------------------------------

    @Override
    public void c2_buttonDpadRight() {//d
        if(c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
            if (c2_positionX + 1 < lblCharacters[0].length && checkLblCharacters) {
                if (lblCharacters[c2_positionY][c2_positionX + 1].isOver && c2_positionX + 2 <= 5) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionX += 2;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                } else if (!lblCharacters[c2_positionY][c2_positionX + 1].isOver) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionX++;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                }
                if (c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
                    updateC2(lblCharacters[c2_positionY][c2_positionX].id);
                }

            }
        }
    }

    @Override
    public void c2_buttonDpadLeft() {//a
        if(c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
            if (c2_positionX - 1 >= 0 && checkLblCharacters) {
                if (lblCharacters[c2_positionY][c2_positionX - 1].isOver && c2_positionX - 2 >= 0) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionX -= 2;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                } else if (!lblCharacters[c2_positionY][c2_positionX - 1].isOver) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionX--;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                }
                if (c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
                    updateC2(lblCharacters[c2_positionY][c2_positionX].id);
                }
            }
        }
    }

    @Override
    public void c2_buttonDpadUp() {//w
        if(c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
            if (c2_positionY - 1 >= 0 && checkLblCharacters) {
                if (lblCharacters[c2_positionY - 1][c2_positionX].isOver && c2_positionY - 2 >= 0) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionY -= 2;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                } else if (!lblCharacters[c2_positionY - 1][c2_positionX].isOver) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionY--;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                }
                if (c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
                    updateC2(lblCharacters[c2_positionY][c2_positionX].id);
                }
            }
        }
    }

    @Override
    public void c2_buttonDpadDown() {//s
        if(c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
            if (c2_positionY + 1 < lblCharacters.length && checkLblCharacters) {
                if (lblCharacters[c2_positionY + 1][c2_positionX].isOver && c2_positionY + 2 < lblCharacters.length) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionY += 2;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                } else if (!lblCharacters[c2_positionY + 1][c2_positionX].isOver) {

                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = false;
                    c2_positionY++;
                    lblCharacters[c2_positionY][c2_positionX].isOverC2 = true;

                }
                if (c2_positionIsTakenY == -1 && c2_positionIsTakenX == -1) {
                    updateC2(lblCharacters[c2_positionY][c2_positionX].id);
                }
            }
        }
    }

    @Override
    public void c2_buttonA() {//k

        if(lblCharacters[c2_positionY][c2_positionX].id != -1){
            lblCharacters[c2_positionY][c2_positionX].isTaken = true;
            c2_positionIsTakenX = c2_positionX;
            c2_positionIsTakenY = c2_positionY;
            //System.out.println("c2 take position: x = " + c2_positionIsTakenX + " y = " + c2_positionIsTakenY);
        }
    }

    @Override
    public void c2_buttonB() {//l
        if(c2_positionIsTakenX != -1 && c2_positionIsTakenY != -1){
            lblCharacters[c2_positionIsTakenY][c2_positionIsTakenX].isTaken = false;
            c2_positionIsTakenX = -1;
            c2_positionIsTakenY = -1;
            updateC2(lblCharacters[c2_positionY][c2_positionX].id);
            lblCharacters[c1_positionY][c1_positionX].isOver = false;
            lblCharacters[c1_positionIsTakenY][c1_positionIsTakenX].isOver = true;
            c1_positionY = c1_positionIsTakenY;
            c1_positionX = c1_positionIsTakenX;
        }
    }

    @Override
    public void c2_buttonBack() {

    }

    @Override
    public void c2_buttonY() {

    }

    @Override
    public void c2_buttonX() {

    }

    @Override
    public void c2_buttonStart() {

    }

    @Override
    public void c2_buttonL1() {

    }

    @Override
    public void c2_buttonR1() {

    }

    @Override
    public void c2_buttonLeftStick() {

    }

    @Override
    public void c2_buttonRightStick() {

    }

    @Override
    public void c2_axisLeftX(float val) {
        if(val>0){
            c2_buttonDpadRight();
        }else{
            c2_buttonDpadLeft();
        }
    }

    @Override
    public void c2_axisLeftY(float val) {
        if(val>0){
            c2_buttonDpadUp();
        }else{
            c2_buttonDpadDown();
        }
    }

    @Override
    public void c2_axisRightX(float val) {

    }

    @Override
    public void c2_axisRightY(float val) {

    }

}
