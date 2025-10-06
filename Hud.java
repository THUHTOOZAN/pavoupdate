package com.g6.pavovival.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.g6.pavovival.actions.AssetController;

public class Hud {

    private AssetController assets;

    public Hud (AssetController assets) {
        this.assets = assets;
    }

    public void draw (SpriteBatch batch, int level, int minions, int hp, int bossyHP) {

        assets.font.draw(batch, "STAGE : " + level, 10, 520);
        assets.font.draw(batch, "MINIONS : " + minions, 10, 520);
        assets.font.draw(batch, "HP : " + hp, 240, 520);

        if (bossyHP > 0) assets.font.draw(batch, "FINAL BOSS HP : " + bossyHP, 340, 520);
    }
}
