package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ImageC extends Image{
    private Sound pbSound;
    private Skin skin;
    private Texture textureDefault, textureIsOver, textureIsOverC2;
    private Drawable drawable, drawableIsOver, drawableIsOverC2;
    private String style, styleIsOver, styleIsOverC2;
    protected boolean isOver = false, isOverC2 = false, isTaken = false, isTakenC2 = false;
    protected int id;
    private boolean isOverSoundImg = false;

    public ImageC(Texture texture, String style, String styleIsOver, String styleIsOverC2) {
        super(texture);

        this.style = style;
        this.styleIsOver = styleIsOver;
        this.styleIsOverC2 = styleIsOverC2;

        id = -1;

        textureDefault = new Texture(Gdx.files.internal(style));
        textureIsOver = new Texture(Gdx.files.internal(styleIsOver));
        textureIsOverC2 = new Texture(Gdx.files.internal(styleIsOverC2));

        drawable = new TextureRegionDrawable(new TextureRegion(textureDefault));
        drawableIsOver = new TextureRegionDrawable(new TextureRegion(textureIsOver));
        drawableIsOverC2 = new TextureRegionDrawable(new TextureRegion(textureIsOverC2));

        pbSound = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));
    }

    public ImageC(Texture texture, String style, String styleIsOver, String styleIsOverC2, int id) {
        super(texture);

        this.style = style;
        this.styleIsOver = styleIsOver;
        this.styleIsOverC2 = styleIsOverC2;

        this.id = id;

        textureDefault = new Texture(Gdx.files.internal(style));
        textureIsOver = new Texture(Gdx.files.internal(styleIsOver));
        textureIsOverC2 = new Texture(Gdx.files.internal(styleIsOverC2));

        drawable = new TextureRegionDrawable(new TextureRegion(textureDefault));
        drawableIsOver = new TextureRegionDrawable(new TextureRegion(textureIsOver));
        drawableIsOverC2 = new TextureRegionDrawable(new TextureRegion(textureIsOverC2));

        pbSound = Gdx.audio.newSound(Gdx.files.internal(GameConstants.MUSIC_BTN));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isOver || isOverC2){
            if(isOver){
                setDrawable(drawableIsOver);
            }else{
                setDrawable(drawableIsOverC2);
            }

            if(isOverSoundImg == true) {
                long id = pbSound.play(1f);
                isOverSoundImg = false;
            }
        }else{
            setDrawable(drawable);
            isOverSoundImg = true;
        }
    }
}
