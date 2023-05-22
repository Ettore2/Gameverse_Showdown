package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ButtonPS extends ButtonC{

    private Table table;

    public ButtonPS(String text, Skin skin, Skin skin1 , Table table) {
        super(text, skin, skin1);

        this.table = table;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(isOver() || isOver){
            getStage().addActor(table);
        }else{
            getStage().getRoot().removeActor(table);
        }
    }
}
