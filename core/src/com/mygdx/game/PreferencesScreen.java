package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

//width 1680 height 913
public class PreferencesScreen extends GameState{

    public static final int CONTROLS = 0, AUDIO = 1, BACK = 2;//General menu table
    public static final int MUSIC_BACK = 0, MUSIC_FORTH = 1, VOLUME_DOWN = 0, VOLUME_UP = 1;//Audio menu table

    private boolean checkAudio, checkControls;
    private int positionY, positionSubTableX, positionSubTableY, positionMusic;
    private String[] pathMusics;
    private Sound sound, sound1;

    private Label lblMusic, lblMusicVolume;
    private ProgressBarC pbVolume;
    private ButtonC[] btnGeneralMenu;
    private ButtonC[][] btnChooseControl;
    private ButtonC btnMusic;
    private Table tableMenu, tableControls, tableAudio;
    private Stage stage;
    private Camera camera;
    private FitViewport viewPort;
    private Image backgroundImage, titleImage;
    private Skin skin, skin1, skin2;

    public PreferencesScreen(Main game) {
        super(game);
        setInputsDelay(0.1f);

        /*TODO:
         * Creo tutti i pulsanti per table audio:
         * 1. remove sound game (flag)
         * 2. cerco di capire come adattare a schermo più piccolo
         * 3. volume effect
         */
        camera = new OrthographicCamera();
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage(viewPort);

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_YELOW));
        skin1 = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_DARKYELLOW));
        skin2 = new Skin(Gdx.files.internal("skin/SkinProgressBar/skin.json"));

        //title

        Texture title = new Texture(GameConstants.IMAGE_TITLE_PREFERENCES);
        titleImage = new Image(title);
        titleImage.setScale(1.1f);
        titleImage.setPosition((float) (GameConstants.screenWidth * 0.05), (float) (GameConstants.screenHeight * 0.8));

        //lblPreferences = new Label("Preferences", skin, "title");
        //lblPreferences.setPosition((float) (GameConstants.screenWidth * 0.109),GameConstants.centerY + (GameConstants.row_height * 2) + GameConstants.row_height2);

        //table menu
        tableMenu = new Table();
        tableMenu.defaults().size((float) (GameConstants.screenWidth * 0.267), (float) (GameConstants.screenHeight * 0.142));

        tableMenu.setPosition((float) (GameConstants.screenWidth * 0.242), (float) (GameConstants.screenHeight * 0.59));


        //table controls
        tableControls = new Table();

        //table Audio

        //init component for table audio
        lblMusic = new Label("Music", skin, "text");
        lblMusicVolume = new Label("Volume music", skin, "text");

        btnChooseControl = new ButtonC[2][2];

        btnChooseControl[0][MUSIC_BACK] = new ButtonC("<", skin, skin1);
        btnChooseControl[0][MUSIC_FORTH] = new ButtonC(">", skin, skin1);

        btnChooseControl[1][VOLUME_DOWN] = new ButtonC("-", skin, skin1);
        btnChooseControl[1][VOLUME_UP] = new ButtonC("+", skin, skin1);

        pbVolume = new ProgressBarC(0, 100, 1f, false, skin2, "default-horizontal", "horizontal-isOver");
        pbVolume.setValue(game.getMusicVolume() * 100);

        btnMusic = new ButtonC("Beneath the Mask Smash", skin, skin1, "small");

        //init table audio
        tableAudio = new Table();//width 1680 height 913
        tableAudio.defaults().size((float) (GameConstants.screenWidth * 0.1), (float) (GameConstants.screenHeight * 0.142));
        tableAudio.setPosition((float) (GameConstants.screenWidth * 0.655), (float) (GameConstants.screenHeight * 0.557));

        //add component to table audio
        tableAudio.add(lblMusic).pad(0,0,0,(float) (GameConstants.screenWidth * 0.065));
        tableAudio.add(btnChooseControl[0][MUSIC_BACK]).size((float) (GameConstants.screenWidth * 0.06),(float) (GameConstants.screenHeight * 0.11)).pad(0, 0, 0, 25);
        tableAudio.add(btnMusic).size((float) (GameConstants.screenWidth * 0.208), (float) (GameConstants.screenHeight * 0.1));
        tableAudio.add(btnChooseControl[0][MUSIC_FORTH]).size((float) (GameConstants.screenWidth * 0.06),(float) (GameConstants.screenHeight * 0.11)).pad(0, 25, 0, 0);

        tableAudio.row().pad((float) (GameConstants.screenHeight * 0.05), 0 ,0, 0);

        tableAudio.add(lblMusicVolume).pad(0, 0, 0,(float) (GameConstants.screenWidth * 0.065));
        tableAudio.add(btnChooseControl[1][VOLUME_DOWN]).size((float) (GameConstants.screenWidth * 0.06),(float) (GameConstants.screenHeight * 0.11)).pad(0, 0, 0, 25);
        tableAudio.add(pbVolume).size((float) (GameConstants.screenWidth * 0.208), (float) (GameConstants.screenHeight * 0.1)).pad(0,0,0,0);
        tableAudio.add(btnChooseControl[1][VOLUME_UP]).size((float) (GameConstants.screenWidth * 0.06),(float) (GameConstants.screenHeight * 0.11)).pad(0, 25, 0, 0);

        tableAudio.row();

        //button for table menu

        btnGeneralMenu = new ButtonC[3];

        btnGeneralMenu[CONTROLS] = new ButtonPS("Controls", skin, skin1, tableControls);
        btnGeneralMenu[AUDIO] = new ButtonPS("Audio", skin, skin1, tableAudio);
        btnGeneralMenu[BACK] = new ButtonC("Back", skin, skin1);

        tableMenu.row().pad((float) (GameConstants.screenHeight * 0.25), 0, 0, 0);
        tableMenu.add(btnGeneralMenu[CONTROLS]).fillX().uniform();
        tableMenu.row().pad((float) (GameConstants.screenHeight * 0.055), 0, (float) (GameConstants.screenHeight * 0.055), 0);
        tableMenu.add(btnGeneralMenu[AUDIO]).fillX().uniform();
        tableMenu.row().pad(0, 0,0, 0);
        tableMenu.add(btnGeneralMenu[BACK]).fillX().uniform();

        //background
        Texture background = new Texture(GameConstants.BACKGROUND_PREFERENCES);
        backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);

        //sound

        //declare general variable
        btnGeneralMenu[CONTROLS].isOver = true;
        positionY = 0;

        positionSubTableX = 0;
        positionSubTableY = 0;

        positionMusic = 0;

        checkAudio = false;
        checkControls = false;

        pathMusics = game.getPathMusic();

        //sound = Gdx.audio.newSound(Gdx.files.internal("BGM/" + pathMusics[0] + ".mp3"));
        //sound1 = Gdx.audio.newSound(Gdx.files.internal("BGM/" + pathMusics[1] + ".mp3"));

        stage.addActor(backgroundImage);
        stage.addActor(titleImage);
        stage.addActor(tableMenu);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height,true);
        backgroundImage.setSize(viewPort.getWorldWidth(), viewPort.getWorldHeight());
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        //titleImage.setScaling(Scaling.fit);
    }

    @Override
    public void show() {

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

    @Override
    public void c1_buttonDpadRight() {//d
        if (positionSubTableX + 1 == 1 && checkAudio){

            btnChooseControl[positionSubTableY][positionSubTableX].isOver = false;
            positionSubTableX++;
            btnChooseControl[positionSubTableY][positionSubTableX].isOver = true;

            if(btnChooseControl[1][VOLUME_UP].isOver && game.getMusicVolume() + 0.01f <= 100){

                game.setMusicVolume(game.getMusicVolume() + 0.01f);
                pbVolume.setValue(game.getMusicVolume() * 100);
                game.getSound().setVolume(game.getMusicVolume());

            }else if(btnChooseControl[0][MUSIC_FORTH].isOver){
                positionMusic++;
                positionMusic = positionMusic % game.getNumberOfMusic();
                btnMusic.setText(pathMusics[positionMusic]);

                game.getSound().stop();
                game.setSound(Gdx.audio.newMusic(Gdx.files.internal("BGM/" + btnMusic.getText() + ".mp3")));
                game.getSound().setVolume(game.getMusicVolume());
                game.getSound().play();
            }

        }else if(positionSubTableX + 1 == 0 && checkAudio) {

            btnChooseControl[positionSubTableY][positionSubTableX + 1].isOver = false;
            positionSubTableX++;
            btnChooseControl[positionSubTableY][positionSubTableX + 1].isOver = true;

            if(btnChooseControl[1][VOLUME_UP].isOver && game.getMusicVolume() + 0.01f <= 100){

                game.setMusicVolume(game.getMusicVolume() + 0.01f);
                pbVolume.setValue(game.getMusicVolume() * 100);
                game.getSound().setVolume(game.getMusicVolume());

            }else if(btnChooseControl[0][MUSIC_FORTH].isOver){
                positionMusic++;
                positionMusic = positionMusic % game.getNumberOfMusic();
                btnMusic.setText(pathMusics[positionMusic]);

                game.getSound().stop();
                game.setSound(Gdx.audio.newMusic(Gdx.files.internal("BGM/" + btnMusic.getText() + ".mp3")));
                game.getSound().setVolume(game.getMusicVolume());
                game.getSound().play();
            }

        }else if(btnChooseControl[1][VOLUME_UP].isOver && game.getMusicVolume() + 0.01f <= 100){

            game.setMusicVolume(game.getMusicVolume() + 0.01f);
            pbVolume.setValue(game.getMusicVolume() * 100);
            game.getSound().setVolume(game.getMusicVolume());

        }else if(btnChooseControl[0][MUSIC_FORTH].isOver){
            positionMusic++;
            positionMusic = positionMusic % game.getNumberOfMusic();
            btnMusic.setText(pathMusics[positionMusic]);

            game.getSound().stop();
            game.setSound(Gdx.audio.newMusic(Gdx.files.internal("BGM/" + btnMusic.getText() + ".mp3")));
            game.getSound().setVolume(game.getMusicVolume());
            game.getSound().play();
        }
    }



    @Override
    public void c1_buttonDpadLeft() {//a
        if (positionSubTableX - 1 == 0 && checkAudio){

            btnChooseControl[positionSubTableY][positionSubTableX].isOver = false;
            positionSubTableX--;
            btnChooseControl[positionSubTableY][positionSubTableX].isOver = true;

            if(btnChooseControl[1][VOLUME_DOWN].isOver && game.getMusicVolume() - 0.01f >= 0){

                game.setMusicVolume(game.getMusicVolume() - 0.01f);
                pbVolume.setValue(game.getMusicVolume() * 100);
                game.getSound().setVolume(game.getMusicVolume());

            }else if(btnChooseControl[0][MUSIC_BACK].isOver){
                if (positionMusic - 1 == -1) {
                    positionMusic = 1;
                }else{
                    positionMusic--;
                }
                btnMusic.setText(pathMusics[positionMusic]);

                game.getSound().stop();
                game.setSound(Gdx.audio.newMusic(Gdx.files.internal("BGM/" + btnMusic.getText() + ".mp3")));
                game.getSound().setVolume(game.getMusicVolume());
                game.getSound().play();
            }

        } else if (positionSubTableX - 1 == -1 && checkAudio) {

            btnChooseControl[positionSubTableY][positionSubTableX + 1].isOver = false;
            positionSubTableX--;
            btnChooseControl[positionSubTableY][positionSubTableX + 1].isOver = true;

            if(btnChooseControl[1][VOLUME_DOWN].isOver && game.getMusicVolume() - 0.01f >= 0){

                game.setMusicVolume(game.getMusicVolume() - 0.01f);
                pbVolume.setValue(game.getMusicVolume() * 100);
                game.getSound().setVolume(game.getMusicVolume());

            }else if(btnChooseControl[0][MUSIC_BACK].isOver){
                if (positionMusic - 1 == -1) {
                    positionMusic = 1;
                }else{
                    positionMusic--;
                }
                btnMusic.setText(pathMusics[positionMusic]);

                game.getSound().stop();
                game.setSound(Gdx.audio.newMusic(Gdx.files.internal("BGM/" + btnMusic.getText() + ".mp3")));
                game.getSound().setVolume(game.getMusicVolume());
                game.getSound().play();
            }

        }else if(btnChooseControl[1][VOLUME_DOWN].isOver && game.getMusicVolume() - 0.01f >= 0){

            game.setMusicVolume(game.getMusicVolume() - 0.01f);
            pbVolume.setValue(game.getMusicVolume() * 100);
            game.getSound().setVolume(game.getMusicVolume());

        }else if(btnChooseControl[0][MUSIC_BACK].isOver){
            if (positionMusic - 1 == -1) {
                positionMusic = 1;
            }else{
                positionMusic--;
            }
            btnMusic.setText(pathMusics[positionMusic]);

            game.getSound().stop();
            game.setSound(Gdx.audio.newMusic(Gdx.files.internal("BGM/" + btnMusic.getText() + ".mp3")));
            game.getSound().setVolume(game.getMusicVolume());
            game.getSound().play();
        }
    }

    @Override
    public void c1_buttonDpadUp() {//w
        System.out.println("arrowUp");

        if(positionY - 1 >= 0 && !checkControls && !checkAudio){

            btnGeneralMenu[positionY].isOver = false;
            positionY--;
            btnGeneralMenu[positionY].isOver = true;

        } else if (positionSubTableY - 1 == 0 && checkAudio) {

            btnChooseControl[positionSubTableY][VOLUME_DOWN].isOver = false;
            btnChooseControl[positionSubTableY][VOLUME_UP].isOver = false;

            positionSubTableX = 0;

            pbVolume.isOver = false;
            positionSubTableY--;
            btnMusic.isOver = true;

        }
    }

    @Override
    public void c1_buttonDpadDown() {//s
        //System.out.println("arrowDown");
        if(positionY + 1 < btnGeneralMenu.length && !checkControls && !checkAudio){

            btnGeneralMenu[positionY].isOver = false;
            positionY++;
            btnGeneralMenu[positionY].isOver = true;

        }else if(positionSubTableY + 1 == 1 && checkAudio){

            btnChooseControl[positionSubTableY][MUSIC_BACK].isOver = false;
            btnChooseControl[positionSubTableY][MUSIC_FORTH].isOver = false;

            positionSubTableX = 0;

            btnMusic.isOver = false;
            positionSubTableY++;
            pbVolume.isOver = true;

        }
    }

    @Override
    public void c1_buttonA() {//K == X
        if(btnGeneralMenu[BACK].isOver){
            changeScreen(GameState.MENU);
        }else if(btnGeneralMenu[AUDIO].isOver && !checkAudio){

            checkAudio = true;
            btnMusic.isOver = true;
            btnGeneralMenu[AUDIO].fakeIsOver = true;

        }
    }

    @Override
    public void c1_buttonB() {//L == O
        if(!checkControls && !checkAudio){
            changeScreen(GameState.MENU);
        } else if (positionSubTableX == 1 && checkAudio) {
            btnChooseControl[positionSubTableY][positionSubTableX].isOver = false;
            btnGeneralMenu[AUDIO].fakeIsOver = false;
            checkAudio = false;

            positionSubTableY = 0;
            positionSubTableX = 0;
        } else if (positionSubTableX == -1 && checkAudio) {
            btnChooseControl[positionSubTableY][positionSubTableX + 1].isOver = false;
            btnGeneralMenu[AUDIO].fakeIsOver = false;
            checkAudio = false;

            positionSubTableY = 0;
            positionSubTableX = 0;
        } else if (positionSubTableX == 0 && checkAudio) {
            btnChooseControl[positionSubTableY][positionSubTableX].isOver = false;
            btnGeneralMenu[AUDIO].fakeIsOver = false;
            checkAudio = false;

            positionSubTableY = 0;
            positionSubTableX = 0;
        }
    }

    @Override
    public void c1_buttonY() {//I == triangolo

    }

    @Override
    public void c1_buttonX() {//J == quadrato

    }

    @Override
    public void c1_buttonStart() {

    }

    @Override
    public void c1_buttonBack() {

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
        if (val > 0) {
            c1_buttonDpadRight();
        } else {
            c1_buttonDpadLeft();
        }
    }

    @Override
    public void c1_axisRightX(float val) {

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
    public void c1_axisRightY(float val) {

    }

    @Override
    public void c2_buttonDpadRight() {

    }

    @Override
    public void c2_buttonDpadLeft() {

    }

    @Override
    public void c2_buttonDpadUp() {

    }

    @Override
    public void c2_buttonDpadDown() {

    }

    @Override
    public void c2_buttonA() {

    }

    @Override
    public void c2_buttonB() {

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

    }

    @Override
    public void c2_axisRightX(float val) {

    }

    @Override
    public void c2_axisLeftY(float val) {

    }

    @Override
    public void c2_axisRightY(float val) {

    }

}
