package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Arrays;

public abstract class GameState implements Screen{
    private static final float SOGLIA_LETTURA_ASSI=0.6f;

    public static final int MENU = 0;//LAUNCHER
    public static final int CHOOSE_CHARACTERS = 1;//BTN START
    public static final int PREFERENCES = 2;//BTN PREFERENCES
    public static final int EXIT = 3;//BTN EXIT
    public static final int BATTLESCREEN = 4;
    final Main game;
    int loadingCounter;
    boolean haveLoadedAssets;
    ProgressBar loadingProgressBar;
    Image backgroundLoadingIcon;


    float[] buttonsDelays; //memorizza i valori a cui settate i timers quando si preme un tasto
    float[] c1ButtonsTimers; //memorizza i valori dei timers dei tasti del controller 1
    float[] c2ButtonsTimers; //memorizza i valori dei timers dei tasti del controller 2

    float[] axisDelays;//memorizza i valori a cui settate i timers quando si usa un asse
    float[] c1AxisTimers;//memorizza i valori dei timers degli assi del controller 1
    float[] c2AxisTimers;//memorizza i valori dei timers degli assi del controller 2

    //se il timer dell'input desiderato è > 0 non eseguo l'input
    //ogni volta che accetto un input setto il time dell'input al corrispondente delay


    public GameState(Main game){
        this.game=game;

        //cose loading
        backgroundLoadingIcon = new Image(new Texture(GameConstants.BACKGROUND_LOADING_GENERAL));
        backgroundLoadingIcon.setBounds(0,0, GameConstants.screenWidth, GameConstants.screenHeight);

        game.stopMusic();
        haveLoadedAssets = false;
        loadingCounter = 0;
        loadingProgressBar = new ProgressBar(0,100,1,false,new Skin(Gdx.files.internal(GameConstants.SKIN_PROGRESSBAR)));
        loadingProgressBar.setSize(Gdx.graphics.getWidth()/2f,Gdx.graphics.getWidth()/20f);
        loadingProgressBar.setPosition((Gdx.graphics.getWidth() - loadingProgressBar.getWidth())/2f,(Gdx.graphics.getHeight() - loadingProgressBar.getHeight())/5f);


        buttonsDelays = new float[16];
        c1ButtonsTimers = new float[16];
        c2ButtonsTimers = new float[16];

        axisDelays = new float[4];
        c1AxisTimers = new float[4];
        c2AxisTimers = new float[4];


        setInputsDelay(0);//setto delay di default

        //setto timers iniziali a zero
        Arrays.fill(c1ButtonsTimers, 0);
        Arrays.fill(c2ButtonsTimers, 0);
        Arrays.fill(c1AxisTimers, 0);
        Arrays.fill(c2AxisTimers, 0);

    }


    public void changeScreen(int screen){
        System.out.println(screen);
        Screen newScreen = null;

        switch (screen) {
            case MENU:
                newScreen = new MenuScreen(game);
                break;

            case CHOOSE_CHARACTERS:
                newScreen = new ChooseCharactersScreen(game);
                break;

            case PREFERENCES:
                newScreen = new PreferencesScreen(game);
                break;

            case BATTLESCREEN:
                newScreen = new BattleScreen(game,0,0,0,100);
                break;

            default:
                break;
        }

        if (newScreen != null) {

            Screen oldScreen = game.getScreen();
            System.out.println(oldScreen);
            if (oldScreen != null) {
                oldScreen.dispose();
            }

            game.setScreen(newScreen);
            System.out.println(game.getScreen());
        }

    }

    //(i float sono secondi)
    public void setInputsDelay(float timeOfAllDelays){
        Arrays.fill(axisDelays, timeOfAllDelays);
        Arrays.fill(buttonsDelays, timeOfAllDelays);

    }//setta TUTTI i delay degli inputs al valore dato
    public void setInputsDelay(float axisDelayTime, float menusButtonsDelayTime, float actionsButtonsDelayTime){
        Arrays.fill(axisDelays, axisDelayTime);

        //non posso usare i val di game.c1 o game.c2 perché i potrebbero essere null
        buttonsDelays[4] = menusButtonsDelayTime;//back (select)
        buttonsDelays[6] = menusButtonsDelayTime;//start

        buttonsDelays[11] = menusButtonsDelayTime;//up arrow
        buttonsDelays[12] = menusButtonsDelayTime;//down arrow
        buttonsDelays[13] = menusButtonsDelayTime;//left arrow
        buttonsDelays[14] = menusButtonsDelayTime;//right arrow

        buttonsDelays[0] = actionsButtonsDelayTime;//A
        buttonsDelays[1] = actionsButtonsDelayTime;//B
        buttonsDelays[2] = actionsButtonsDelayTime;//X
        buttonsDelays[3] = actionsButtonsDelayTime;//Y
        buttonsDelays[7] = actionsButtonsDelayTime;//L1
        buttonsDelays[8] = actionsButtonsDelayTime;//R1
        buttonsDelays[9] = actionsButtonsDelayTime;//L3 (pressione analogico L)
        buttonsDelays[10] = actionsButtonsDelayTime;//R3 (pressione analogico R)

    }

    @Override
    public void render(float delta) {

        //decremento timers di disattivazione inputs
        for(int i = 0; i < c1ButtonsTimers.length; i++){
            if(c1ButtonsTimers[i] > 0) c1ButtonsTimers[i] -= delta;
            if(c2ButtonsTimers[i] > 0) c2ButtonsTimers[i] -= delta;
        }
        for(int i = 0; i < c1AxisTimers.length; i++){
            if(c1AxisTimers[i] > 0) c1AxisTimers[i] -= delta;
            if(c2AxisTimers[i] > 0) c2AxisTimers[i] -= delta;
        }

        if(Main.USE_CONTROLLERS){
            //controller 1
            if(game.c1 != null && game.c1.isConnected()){
                if(c1ButtonsTimers[game.mapC1.buttonDpadRight] <= 0 && game.c1.getButton(game.mapC1.buttonDpadRight)){
                    c1_buttonDpadRight();
                    c1ButtonsTimers[game.mapC1.buttonDpadRight] = buttonsDelays[game.mapC1.buttonDpadRight];
                }
                if(c1ButtonsTimers[game.mapC1.buttonDpadLeft] <= 0 && game.c1.getButton(game.mapC1.buttonDpadLeft)){
                    c1_buttonDpadLeft();
                    c1ButtonsTimers[game.mapC1.buttonDpadLeft] = buttonsDelays[game.mapC1.buttonDpadLeft];
                }
                if(c1ButtonsTimers[game.mapC1.buttonDpadUp] <= 0 && game.c1.getButton(game.mapC1.buttonDpadUp)){
                    c1_buttonDpadUp();
                    c1ButtonsTimers[game.mapC1.buttonDpadUp] = buttonsDelays[game.mapC1.buttonDpadUp];
                }
                if(c1ButtonsTimers[game.mapC1.buttonDpadDown] <= 0 && game.c1.getButton(game.mapC1.buttonDpadDown)){
                    c1_buttonDpadDown();
                    c1ButtonsTimers[game.mapC1.buttonDpadDown] = buttonsDelays[game.mapC1.buttonDpadDown];
                }
                if(c1ButtonsTimers[game.mapC1.buttonA] <= 0 && game.c1.getButton(game.mapC1.buttonA)){
                    c1_buttonA();
                    c1ButtonsTimers[game.mapC1.buttonA] = buttonsDelays[game.mapC1.buttonA];
                }
                if(c1ButtonsTimers[game.mapC1.buttonB] <= 0 && game.c1.getButton(game.mapC1.buttonB)){
                    c1_buttonB();
                    c1ButtonsTimers[game.mapC1.buttonB] = buttonsDelays[game.mapC1.buttonB];
                }
                if(c1ButtonsTimers[game.mapC1.buttonBack] <= 0 && game.c1.getButton(game.mapC1.buttonBack)){
                    c1_buttonBack();
                    c1ButtonsTimers[game.mapC1.buttonBack] = buttonsDelays[game.mapC1.buttonBack];
                }
                if(c1ButtonsTimers[game.mapC1.buttonY] <= 0 && game.c1.getButton(game.mapC1.buttonY)){
                    c1_buttonY();
                    c1ButtonsTimers[game.mapC1.buttonY] = buttonsDelays[game.mapC1.buttonY];
                }
                if(c1ButtonsTimers[game.mapC1.buttonX] <= 0 && game.c1.getButton(game.mapC1.buttonX)){
                    c1_buttonX();
                    c1ButtonsTimers[game.mapC1.buttonX] = buttonsDelays[game.mapC1.buttonX];
                }
                if(c1ButtonsTimers[game.mapC1.buttonStart] <= 0 && game.c1.getButton(game.mapC1.buttonStart)){
                    c1_buttonStart();
                    c1ButtonsTimers[game.mapC1.buttonStart] = buttonsDelays[game.mapC1.buttonStart];
                }
                if(c1ButtonsTimers[game.mapC1.buttonL1] <= 0 && game.c1.getButton(game.mapC1.buttonL1)){
                    c1_buttonL1();
                    c1ButtonsTimers[game.mapC1.buttonL1] = buttonsDelays[game.mapC1.buttonL1];
                }
                if(c1ButtonsTimers[game.mapC1.buttonR1] <= 0 && game.c1.getButton(game.mapC1.buttonR1)){
                    c1_buttonR1();
                    c1ButtonsTimers[game.mapC1.buttonR1] = buttonsDelays[game.mapC1.buttonR1];
                }
                if(c1ButtonsTimers[game.mapC1.buttonLeftStick] <= 0 && game.c1.getButton(game.mapC1.buttonLeftStick)){
                    c1_buttonLeftStick();
                    c1ButtonsTimers[game.mapC1.buttonLeftStick] = buttonsDelays[game.mapC1.buttonLeftStick];

                }
                if(c1ButtonsTimers[game.mapC1.buttonRightStick] <= 0 && game.c1.getButton(game.mapC1.buttonRightStick)){
                    c1_buttonRightStick();
                    c1ButtonsTimers[game.mapC1.buttonRightStick] = buttonsDelays[game.mapC1.buttonRightStick];
                }
                if(c1AxisTimers[game.mapC1.axisLeftX] <= 0 && Math.abs(game.c1.getAxis(game.mapC1.axisLeftX))>=SOGLIA_LETTURA_ASSI){
                    c1_axisLeftX(game.c1.getAxis(game.mapC1.axisLeftX));
                    c1AxisTimers[game.mapC1.axisLeftX] = buttonsDelays[game.mapC1.axisLeftX];
                }
                if(c1AxisTimers[game.mapC1.axisRightX] <= 0 && Math.abs(game.c1.getAxis(game.mapC1.axisRightX))>=SOGLIA_LETTURA_ASSI){
                    c1_axisRightX(game.c1.getAxis(game.mapC1.axisRightX));
                    c1AxisTimers[game.mapC1.axisRightX] = buttonsDelays[game.mapC1.axisRightX];
                }
                if(c1AxisTimers[game.mapC1.axisLeftY] <= 0 && Math.abs(game.c1.getAxis(game.mapC1.axisLeftY))>=SOGLIA_LETTURA_ASSI){
                    c1_axisLeftY(game.c1.getAxis(game.mapC1.axisLeftY));
                    c1AxisTimers[game.mapC1.axisLeftY] = buttonsDelays[game.mapC1.axisLeftY];
                }
                if(c1AxisTimers[game.mapC1.axisRightY] <= 0 && Math.abs(game.c1.getAxis(game.mapC1.axisRightY))>=SOGLIA_LETTURA_ASSI){
                    c1_axisRightY(game.c1.getAxis(game.mapC1.axisRightY));
                    c1AxisTimers[game.mapC1.axisRightY] = buttonsDelays[game.mapC1.axisRightY];
                }

            }


            //controller 2
            if(game.c2!=null&&game.c2.isConnected()) {
                if(c2ButtonsTimers[game.mapC2.buttonDpadRight] <= 0 && game.c2.getButton(game.mapC2.buttonDpadRight)) {
                    c2_buttonDpadRight();
                    c2ButtonsTimers[game.mapC2.buttonDpadRight] = buttonsDelays[game.mapC2.buttonDpadRight];
                }
                if(c2ButtonsTimers[game.mapC2.buttonDpadLeft] <= 0 && game.c2.getButton(game.mapC2.buttonDpadLeft)) {
                    c2_buttonDpadLeft();
                    c2ButtonsTimers[game.mapC2.buttonDpadLeft] = buttonsDelays[game.mapC2.buttonDpadLeft];
                }
                if(c2ButtonsTimers[game.mapC2.buttonDpadUp] <= 0 && game.c2.getButton(game.mapC2.buttonDpadUp)) {
                    c2_buttonDpadUp();
                    c2ButtonsTimers[game.mapC2.buttonDpadUp] = buttonsDelays[game.mapC2.buttonDpadUp];
                }
                if(c2ButtonsTimers[game.mapC2.buttonDpadDown] <= 0 && game.c2.getButton(game.mapC2.buttonDpadDown)) {
                    c2_buttonDpadDown();
                    c2ButtonsTimers[game.mapC2.buttonDpadDown] = buttonsDelays[game.mapC2.buttonDpadDown];
                }
                if(c2ButtonsTimers[game.mapC2.buttonA] <= 0 && game.c2.getButton(game.mapC2.buttonA)) {
                    c2_buttonA();
                    c2ButtonsTimers[game.mapC2.buttonA] = buttonsDelays[game.mapC2.buttonA];
                }
                if(c2ButtonsTimers[game.mapC2.buttonB] <= 0 && game.c2.getButton(game.mapC2.buttonB)) {
                    c2_buttonB();
                    c2ButtonsTimers[game.mapC2.buttonB] = buttonsDelays[game.mapC2.buttonB];
                }
                if(c2ButtonsTimers[game.mapC2.buttonBack] <= 0 && game.c2.getButton(game.mapC2.buttonBack)) {
                    c2_buttonBack();
                    c2ButtonsTimers[game.mapC2.buttonBack] = buttonsDelays[game.mapC2.buttonBack];
                }
                if(c2ButtonsTimers[game.mapC2.buttonY] <= 0 && game.c2.getButton(game.mapC2.buttonY)) {
                    c2_buttonY();
                    c2ButtonsTimers[game.mapC2.buttonY] = buttonsDelays[game.mapC2.buttonY];
                }
                if(c2ButtonsTimers[game.mapC2.buttonX] <= 0 && game.c2.getButton(game.mapC2.buttonX)) {
                    c2_buttonX();
                    c2ButtonsTimers[game.mapC2.buttonX] = buttonsDelays[game.mapC2.buttonX];
                }
                if(c2ButtonsTimers[game.mapC2.buttonStart] <= 0 && game.c2.getButton(game.mapC2.buttonStart)) {
                    c2_buttonStart();
                    c2ButtonsTimers[game.mapC2.buttonStart] = buttonsDelays[game.mapC2.buttonStart];
                }
                if(c2ButtonsTimers[game.mapC2.buttonL1] <= 0 && game.c2.getButton(game.mapC2.buttonL1)) {
                    c2_buttonL1();
                    c2ButtonsTimers[game.mapC2.buttonL1] = buttonsDelays[game.mapC2.buttonL1];
                }
                if(c2ButtonsTimers[game.mapC2.buttonR1] <= 0 && game.c2.getButton(game.mapC2.buttonR1)) {
                    c2_buttonR1();
                    c2ButtonsTimers[game.mapC2.buttonR1] = buttonsDelays[game.mapC2.buttonR1];
                }
                if(c2ButtonsTimers[game.mapC2.buttonLeftStick] <= 0 && game.c2.getButton(game.mapC2.buttonLeftStick)) {
                    c2_buttonLeftStick();
                    c2ButtonsTimers[game.mapC2.buttonLeftStick] = buttonsDelays[game.mapC2.buttonLeftStick];

                }
                if(c2ButtonsTimers[game.mapC2.buttonRightStick] <= 0 && game.c2.getButton(game.mapC2.buttonRightStick)) {
                    c2_buttonRightStick();
                    c2ButtonsTimers[game.mapC2.buttonRightStick] = buttonsDelays[game.mapC2.buttonRightStick];
                }
                if(c2AxisTimers[game.mapC2.axisLeftX] <= 0 && Math.abs(game.c2.getAxis(game.mapC2.axisLeftX))>=SOGLIA_LETTURA_ASSI){
                    c2_axisLeftX(game.c2.getAxis(game.mapC2.axisLeftX));
                    c2AxisTimers[game.mapC2.axisLeftX] = buttonsDelays[game.mapC2.axisLeftX];
                }
                if(c2AxisTimers[game.mapC2.axisRightX] <= 0 && Math.abs(game.c2.getAxis(game.mapC2.axisRightX))>=SOGLIA_LETTURA_ASSI){
                    c2_axisRightX(game.c2.getAxis(game.mapC2.axisRightX));
                    c2AxisTimers[game.mapC2.axisRightX] = buttonsDelays[game.mapC2.axisRightX];
                }
                if(c2AxisTimers[game.mapC2.axisLeftY] <= 0 && Math.abs(game.c2.getAxis(game.mapC2.axisLeftY))>=SOGLIA_LETTURA_ASSI){
                    c2_axisLeftY(game.c2.getAxis(game.mapC2.axisLeftY));
                    c2AxisTimers[game.mapC2.axisLeftY] = buttonsDelays[game.mapC2.axisLeftY];
                }
                if(c2AxisTimers[game.mapC2.axisRightY] <= 0 && Math.abs(game.c2.getAxis(game.mapC2.axisRightY))>=SOGLIA_LETTURA_ASSI){
                    c2_axisRightY(game.c2.getAxis(game.mapC2.axisRightY));
                    c2AxisTimers[game.mapC2.axisRightY] = buttonsDelays[game.mapC2.axisRightY];
                }
            }

        }

        //if(!Main.USE_CONTROLLERS){
        if(true){
            if(Main.EMULATING_CONTROLLER_1){
                //controller 1
                if(c1ButtonsTimers[14] <= 0 && Gdx.input.isKeyPressed(Input.Keys.D)){
                    c1_buttonDpadRight();
                    c1ButtonsTimers[14] = buttonsDelays[14];
                }
                if(c1ButtonsTimers[13] <= 0 && Gdx.input.isKeyPressed(Input.Keys.A)){
                    c1_buttonDpadLeft();
                    c1ButtonsTimers[13] = buttonsDelays[13];
                }
                if(c1ButtonsTimers[11] <= 0 && Gdx.input.isKeyPressed(Input.Keys.W)){
                    c1_buttonDpadUp();
                    c1ButtonsTimers[11] = buttonsDelays[11];
                }
                if(c1ButtonsTimers[12] <= 0 && Gdx.input.isKeyPressed(Input.Keys.S)){
                    c1_buttonDpadDown();
                    c1ButtonsTimers[12] = buttonsDelays[12];
                }
                if(c1ButtonsTimers[0] <= 0 && Gdx.input.isKeyPressed(Input.Keys.K)){
                    c1_buttonA();
                    c1ButtonsTimers[0] = buttonsDelays[0];
                }
                if(c1ButtonsTimers[1] <= 0 && Gdx.input.isKeyPressed(Input.Keys.L)){
                    c1_buttonB();
                    c1ButtonsTimers[1] = buttonsDelays[1];
                }
                if(c1ButtonsTimers[4] <= 0 && Gdx.input.isKeyPressed(Input.Keys.Q)){
                    c1_buttonBack();
                    c1ButtonsTimers[4] = buttonsDelays[4];
                }
                if(c1ButtonsTimers[3] <= 0 && Gdx.input.isKeyPressed(Input.Keys.I)){
                    c1_buttonY();
                    c1ButtonsTimers[3] = buttonsDelays[3];
                }
                if(c1ButtonsTimers[2] <= 0 && Gdx.input.isKeyPressed(Input.Keys.J)){
                    c1_buttonX();
                    c1ButtonsTimers[2] = buttonsDelays[2];
                }
                if(c1ButtonsTimers[6] <= 0 && Gdx.input.isKeyPressed(Input.Keys.E)){
                    c1_buttonStart();
                    c1ButtonsTimers[6] = buttonsDelays[6];
                }
                if(c1ButtonsTimers[7] <= 0 && Gdx.input.isKeyPressed(Input.Keys.R)){
                    c1_buttonL1();
                    c1ButtonsTimers[7] = buttonsDelays[7];
                }
                if(c1ButtonsTimers[8] <= 0 && Gdx.input.isKeyPressed(Input.Keys.F)){
                    c1_buttonR1();
                    c1ButtonsTimers[8] = buttonsDelays[8];
                }
                if(c1ButtonsTimers[9] <= 0 && Gdx.input.isKeyPressed(Input.Keys.T)){
                    c1_buttonLeftStick();
                    c1ButtonsTimers[9] = buttonsDelays[9];

                }
                if(c1ButtonsTimers[10] <= 0 && Gdx.input.isKeyPressed(Input.Keys.G)){
                    c1_buttonRightStick();
                    c1ButtonsTimers[10] = buttonsDelays[10];
                }
                if(c1AxisTimers[0] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_1)||Gdx.input.isKeyPressed(Input.Keys.NUM_2))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
                        c1_axisLeftX(1);
                    }else{
                        c1_axisLeftX(-1);
                    }
                    c1AxisTimers[0] = buttonsDelays[0];
                }
                if(c1AxisTimers[2] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_5)||Gdx.input.isKeyPressed(Input.Keys.NUM_6))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_6)){
                        c1_axisRightX(1);
                    }else{
                        c1_axisRightX(-1);
                    }
                    c1AxisTimers[2] = buttonsDelays[2];
                }
                if(c1AxisTimers[1] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_3)||Gdx.input.isKeyPressed(Input.Keys.NUM_4))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
                        c1_axisLeftY(1);
                    }else{
                        c1_axisLeftY(-1);
                    }
                    c1AxisTimers[1] = buttonsDelays[1];
                }
                if(c1AxisTimers[3] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_7)||Gdx.input.isKeyPressed(Input.Keys.NUM_8))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_8)){
                        c1_axisRightY(1);
                    }else{
                        c1_axisRightY(-1);
                    }
                    c1AxisTimers[3] = buttonsDelays[3];
                }

            }else{
                //controller 2
                if(c2ButtonsTimers[14] <= 0 && Gdx.input.isKeyPressed(Input.Keys.D)){
                    c2_buttonDpadRight();
                    c2ButtonsTimers[14] = buttonsDelays[14];
                }
                if(c2ButtonsTimers[13] <= 0 && Gdx.input.isKeyPressed(Input.Keys.A)){
                    c2_buttonDpadLeft();
                    c2ButtonsTimers[13] = buttonsDelays[13];
                }
                if(c2ButtonsTimers[11] <= 0 && Gdx.input.isKeyPressed(Input.Keys.W)){
                    c2_buttonDpadUp();
                    c2ButtonsTimers[11] = buttonsDelays[11];
                }
                if(c2ButtonsTimers[12] <= 0 && Gdx.input.isKeyPressed(Input.Keys.S)){
                    c2_buttonDpadDown();
                    c2ButtonsTimers[12] = buttonsDelays[12];
                }
                if(c2ButtonsTimers[0] <= 0 && Gdx.input.isKeyPressed(Input.Keys.K)){
                    c2_buttonA();
                    c2ButtonsTimers[0] = buttonsDelays[0];
                }
                if(c2ButtonsTimers[1] <= 0 && Gdx.input.isKeyPressed(Input.Keys.L)){
                    c2_buttonB();
                    c2ButtonsTimers[1] = buttonsDelays[1];
                }
                if(c2ButtonsTimers[4] <= 0 && Gdx.input.isKeyPressed(Input.Keys.Q)){
                    c2_buttonBack();
                    c2ButtonsTimers[4] = buttonsDelays[4];
                }
                if(c2ButtonsTimers[3] <= 0 && Gdx.input.isKeyPressed(Input.Keys.I)){
                    c2_buttonY();
                    c2ButtonsTimers[3] = buttonsDelays[3];
                }
                if(c2ButtonsTimers[2] <= 0 && Gdx.input.isKeyPressed(Input.Keys.J)){
                    c2_buttonX();
                    c2ButtonsTimers[2] = buttonsDelays[2];
                }
                if(c2ButtonsTimers[6] <= 0 && Gdx.input.isKeyPressed(Input.Keys.E)){
                    c2_buttonStart();
                    c2ButtonsTimers[6] = buttonsDelays[6];
                }
                if(c2ButtonsTimers[7] <= 0 && Gdx.input.isKeyPressed(Input.Keys.R)){
                    c2_buttonL1();
                    c2ButtonsTimers[7] = buttonsDelays[7];
                }
                if(c2ButtonsTimers[8] <= 0 && Gdx.input.isKeyPressed(Input.Keys.F)){
                    c2_buttonR1();
                    c2ButtonsTimers[8] = buttonsDelays[8];
                }
                if(c2ButtonsTimers[9] <= 0 && Gdx.input.isKeyPressed(Input.Keys.T)){
                    c2_buttonLeftStick();
                    c2ButtonsTimers[9] = buttonsDelays[9];

                }
                if(c2ButtonsTimers[10] <= 0 && Gdx.input.isKeyPressed(Input.Keys.G)){
                    c2_buttonRightStick();
                    c2ButtonsTimers[10] = buttonsDelays[10];
                }
                if(c2AxisTimers[0] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_1)||Gdx.input.isKeyPressed(Input.Keys.NUM_2))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
                        c2_axisLeftX(1);
                    }else{
                        c2_axisLeftX(-1);
                    }
                    c2AxisTimers[0] = buttonsDelays[0];
                }
                if(c2AxisTimers[2] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_5)||Gdx.input.isKeyPressed(Input.Keys.NUM_6))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_6)){
                        c2_axisRightX(1);
                    }else{
                        c2_axisRightX(-1);
                    }
                    c2AxisTimers[2] = buttonsDelays[2];
                }
                if(c2AxisTimers[1] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_3)||Gdx.input.isKeyPressed(Input.Keys.NUM_4))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
                        c2_axisLeftY(1);
                    }else{
                        c2_axisLeftY(-1);
                    }
                    c2AxisTimers[1] = buttonsDelays[1];
                }
                if(c2AxisTimers[3] <= 0 && (Gdx.input.isKeyPressed(Input.Keys.NUM_7)||Gdx.input.isKeyPressed(Input.Keys.NUM_8))){
                    if(Gdx.input.isKeyPressed(Input.Keys.NUM_8)){
                        c2_axisRightY(1);
                    }else{
                        c2_axisRightY(-1);
                    }
                    c2AxisTimers[3] = buttonsDelays[3];
                }
            }
        }

        //per tasto ps: c1.getButton(5)


        //System.out.print("render ");
        if(haveLoadedAssets){
            normalExecution(delta);
            //System.out.println("normalExecution");
        }else{
            loadingExecution(delta);
            //System.out.println("loadingExecution");

            if(haveLoadedAssets){
                game.resumeMusic();
            }
        }


    }
    public void normalExecution(float delta){

    }
    public void loadingExecution(float delta){
        haveLoadedAssets = true;

    }

    public abstract void c1_buttonDpadRight();
    public abstract void c1_buttonDpadLeft();
    public abstract void c1_buttonDpadUp();
    public abstract void c1_buttonDpadDown();
    public abstract void c1_buttonA();
    public abstract void c1_buttonB();
    public abstract void c1_buttonBack();
    public abstract void c1_buttonY();
    public abstract void c1_buttonX();
    public abstract void c1_buttonStart();
    public abstract void c1_buttonL1();
    public abstract void c1_buttonR1();
    public abstract void c1_buttonLeftStick();
    public abstract void c1_buttonRightStick();
    public abstract void c1_axisLeftX(float val);
    public abstract void c1_axisRightX(float val);
    public abstract void c1_axisLeftY(float val);
    public abstract void c1_axisRightY(float val);

    public abstract void c2_buttonDpadRight();
    public abstract void c2_buttonDpadLeft();
    public abstract void c2_buttonDpadUp();
    public abstract void c2_buttonDpadDown();
    public abstract void c2_buttonA();
    public abstract void c2_buttonB();
    public abstract void c2_buttonBack();
    public abstract void c2_buttonY();
    public abstract void c2_buttonX();
    public abstract void c2_buttonStart();
    public abstract void c2_buttonL1();
    public abstract void c2_buttonR1();
    public abstract void c2_buttonLeftStick();
    public abstract void c2_buttonRightStick();
    public abstract void c2_axisLeftX(float val);
    public abstract void c2_axisRightX(float val);
    public abstract void c2_axisLeftY(float val);
    public abstract void c2_axisRightY(float val);



}
