package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ButtonC extends TextButton {
    private Skin skin1;
    private Sound btnChange;
    private String styleName;
    private TextButtonStyle customStyle, customStyle1;
    private Skin skinDisabled;
    protected boolean isOver = false, fakeIsOver = false, disabled = false;
    private boolean isOverSoundBtn = true;

    public ButtonC(String text, Skin skin, Skin skin1) {
        super(text, skin);

        this.skin1 = skin1;

        btnChange = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));
    }

    public ButtonC(String text, Skin skin, Skin skin1, boolean disabled) {
        super(text, skin);

        this.skin1 = skin1;
        this.disabled = disabled;

        skinDisabled = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_DISABLED));

        btnChange = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));
    }
    public ButtonC(String text, Skin skin, Skin skin1, String styleName) {
        super(text, skin, styleName);

        this.skin1 = skin1;
        this.styleName = styleName;

        customStyle = new TextButtonStyle(getSkin().get(styleName, TextButtonStyle.class));
        customStyle1 = new TextButtonStyle(skin1.get(styleName, TextButtonStyle.class));

        btnChange = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));
    }

    public ButtonC(String text, Skin skin, Skin skin1, String styleName, boolean disabled) {
        super(text, skin, styleName);

        this.skin1 = skin1;
        this.styleName = styleName;

        this.disabled = disabled;

        skinDisabled = new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_DISABLED));

        customStyle = new TextButtonStyle(getSkin().get(styleName, TextButtonStyle.class));
        customStyle1 = new TextButtonStyle(skin1.get(styleName, TextButtonStyle.class));

        btnChange = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isOver() || isOver && !disabled){
            if(fakeIsOver){
                if(styleName != null){
                    setStyle(customStyle);
                }else{
                    setStyle(getSkin().get(TextButtonStyle.class));
                }
            }else{
                if(styleName != null){
                    setStyle(customStyle1);
                }else{
                    setStyle(skin1.get(TextButtonStyle.class));
                }
            }
            if(isOverSoundBtn == true){
                long id = btnChange.play(1f);
                isOverSoundBtn = false;
            }
        } else if (!disabled){

            if(styleName != null){
                setStyle(customStyle);
            }else{
                setStyle(getSkin().get(TextButtonStyle.class));
            }
            isOverSoundBtn = true;
        } else if (disabled){
            setStyle(new TextButtonStyle(skinDisabled.get(TextButtonStyle.class)));
        }
    }

}
