package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ProgressBarC extends ProgressBar {

    private Sound pbSound;
    private ProgressBarStyle pbStyle, pbStyle1;
    private Skin skin;
    private String styleName, styleName1;
    protected boolean isOver = false;
    private boolean isOverSoundPb = false;

    public ProgressBarC(float min, float max, float stepSize, boolean vertical, Skin skin) {
        super(min, max, stepSize, vertical, skin);

        pbSound = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));
    }

    public ProgressBarC(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName, String styleName1) {
        super(min, max, stepSize, vertical, skin, styleName);

        pbSound = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));

        this.skin = skin;
        this.styleName = styleName;
        this.styleName1 = styleName1;

        pbStyle = new ProgressBarStyle(skin.get(styleName,ProgressBarStyle.class));
        pbStyle1 = new ProgressBarStyle(skin.get(styleName, ProgressBarStyle.class));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isOver){
            setStyle(skin.get(styleName1,ProgressBarStyle.class));
            if(isOverSoundPb == true) {
                long id = pbSound.play(1f);
                isOverSoundPb = false;
            }
        }else{
            setStyle(skin.get(styleName,ProgressBarStyle.class));
            isOverSoundPb = true;
        }
    }
}
