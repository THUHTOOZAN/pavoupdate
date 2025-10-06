package com.g6.pavovival.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetController {
    public Texture lvOne, lvTwo, lvThree;
    public Texture pavoTexture, minionTexture, bossyTexture;
    public Texture bulletTexture, bossBulletTexture;
    public BitmapFont font;

    public void load() {
        // backgrounds
        lvOne   = new Texture(Gdx.files.internal("lvOne.png"));
        lvTwo   = new Texture(Gdx.files.internal("lvTwo.png"));
        lvThree = new Texture(Gdx.files.internal("lvThree.png"));

        // characters (pavo, minions, boss)
        pavoTexture = new Texture(Gdx.files.internal("pavo.png"));
        minionTexture   = new Texture(Gdx.files.internal("minion.png"));
        bossyTexture= new Texture(Gdx.files.internal("bossy.png")); // keep the filename you used

        // bullets (pavo + boss)
        bulletTexture   = new Texture(Gdx.files.internal("pavo_bullet.png"));
        bossBulletTexture   = new Texture(Gdx.files.internal("boss_bullet.png"));

        font = new BitmapFont();
    }

    public void dispose() {
        lvOne.dispose(); lvTwo.dispose(); lvThree.dispose();
        pavoTexture.dispose(); minionTexture.dispose(); bossyTexture.dispose();
        bulletTexture.dispose(); bossBulletTexture.dispose();
        font.dispose();
    }
}
