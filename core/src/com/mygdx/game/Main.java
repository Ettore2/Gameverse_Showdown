package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;

public class Main extends Game {
    /*TODO
    implementing random character selection

     */

    public static final boolean USE_CONTROLLERS = true;
    public static boolean EMULATING_CONTROLLER_1 = true;//per adesso tastiera sempre attiva

    public Controller c1,c2;
    public ControllerMapping mapC1,mapC2;

    final int frameXControllersCheck = 100;
    int controllersCheckCounter = 0;
    private String[] pathMusic = {"Beneath the Mask Smash", "Heartbeat Heartbreak P5", "Burn My Dread Inst"};
    private final int numberOfMusic = pathMusic.length;
    private float musicVolume;
    private Music bgm;
    private long id;

    @Override
    public void create () {

        c1 = null;
        c2 = null;

        //creo controllers
        if(USE_CONTROLLERS){
            if(Controllers.getControllers().size>0){
                c1=Controllers.getControllers().get(0);
                mapC1=c1.getMapping();
            }
            if(Controllers.getControllers().size>1){
                c2=Controllers.getControllers().get(1);
                mapC2=c2.getMapping();
            }

        }

        bgm = Gdx.audio.newMusic(Gdx.files.internal("BGM/" + pathMusic[0] + ".mp3"));

        musicVolume = 0.0f;//min 0.01 = 1% max 1.0 = 100%
        bgm.setVolume(musicVolume);
        bgm.play();
        bgm.setLooping(true);

        //this.setScreen(new ChooseCharactersScreen(this));
        this.setScreen(new BattleScreen(this,0,2,0,999999));
        //this.setScreen(new MenuScreen(this));
        //System.out.println(GameConstants.screenWidth+"   "+GameConstants.screenHeight);
    }

    @Override
    public void render () {
        super.render();//esegue render dell screen settato in this.setScreen()

        //cambio il controller che sto emulando (contiene sleep)
        //if(!USE_CONTROLLERS&&Gdx.input.isKeyPressed(Input.Keys.ENTER)){
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            EMULATING_CONTROLLER_1 =! EMULATING_CONTROLLER_1;
            System.out.println("!!!!!!!!!!!!ho scambiato il controller che stai emulando!!!!!!!!!!!!!!"); //debug

            //sleep
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        //controllo controllers
        if(controllersCheckCounter == 0 && USE_CONTROLLERS){
            //se i controllers sono disconnessi metto a null controllers e mappature
            if(c1 != null&&!c1.isConnected()){
                c1 = null;
                mapC1 = null;
            }
            if(c2 != null && !c2.isConnected()){
                c2 = null;
                mapC2 = null;
            }

            //riassegno valore a controllers e mappature se possibile
            if(c1 == null && Controllers.getControllers().size>0){
                if(c2!=Controllers.getControllers().get(0)){
                    c1=Controllers.getControllers().get(0);
                    mapC1=c1.getMapping();
                }else if(Controllers.getControllers().size>1&&c2!=Controllers.getControllers().get(1)){
                    c1=Controllers.getControllers().get(1);
                    mapC1=c1.getMapping();
                }
            }
            if(c2==null&&Controllers.getControllers().size>0){
                if(c1!=Controllers.getControllers().get(0)){
                    c2=Controllers.getControllers().get(0);
                    mapC2=c2.getMapping();
                }else if(Controllers.getControllers().size>1&&c1!=Controllers.getControllers().get(1)){
                    c2=Controllers.getControllers().get(1);
                    mapC2=c2.getMapping();
                }
            }
        }
        controllersCheckCounter=(controllersCheckCounter+1) % frameXControllersCheck;
    }

    @Override
    public void dispose () {
        super.dispose();
        bgm.dispose();
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
        return musicVolume;
    }
    public void stopMusic(){
        bgm.stop();
    }
    public void resumeMusic(){
        bgm.play();
    }

    public String[] getPathMusic() {
        return pathMusic;
    }

    public void setPathMusic(String[] pathMusic) {
        this.pathMusic = pathMusic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Music getSound() {
        return bgm;
    }

    public void setSound(Music sound) {
        this.bgm = sound;
    }

    public int getNumberOfMusic() {
        return numberOfMusic;
    }
}
