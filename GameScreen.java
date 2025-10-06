package com.g6.pavovival.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import com.g6.pavovival.Main;
import com.g6.pavovival.actions.*;
import com.g6.pavovival.effects.Hud;
import com.g6.pavovival.entities.*;

public class GameScreen implements Screen {
    private final Main game;

    private Pavo pavo;
    private Array<Enemy> enemies = new Array<>();
    private Array<Bullet> playerBullets = new Array<>();
    private Array<Bullet> enemyBullets  = new Array<>();

    private LevelController levels = new LevelController();
    private Spawn spawner;
    private CollisionController collisions = new CollisionController();
    private InputController input = new InputController();
    private Hud hud;

    private Boss boss = null;

    public enum State { PLAYING, PAUSED, WIN, GAME_OVER }
    private State state = State.PLAYING;

    public GameScreen(Main game) {
        this.game = game;
        this.pavo = new Pavo(game.assets.pavoTexture, 40, 0);
        this.spawner = new Spawn(game.assets, levels);
        this.hud = new Hud(game.assets);
    }

    @Override
    public void render(float delta) {
        float dt = Math.min(delta, 1/30f);

        // inputs are pause, movement, jump
        input.handle(pavo);
        if (input.paused && state == State.PLAYING) state = State.PAUSED;
        else if (!input.paused && state == State.PAUSED) state = State.PLAYING;

        if (state == State.PLAYING) {
            // Player
            pavo.update(dt);

            // minions for current wave (5)
            spawner.update(dt, enemies, pavo);

            // level up if 5 minions killed (not spawned)
            levels.advanceLevelIfCleared();

            // boss after Level 3 cleared
            if (levels.shouldSpawnBoss() && boss == null) {
                boss = new Boss(game.assets.bossyTexture, 820, 0);
                levels.bossSpawned = true;
                enemies.add(boss);
            }

            // Enemy auto
            for (Enemy e : enemies) if (e.alive) {
                if (e instanceof Boss) {
                    Boss b = (Boss) e;
                    b.pursue(pavo);
                    b.maybeShoot(enemyBullets, game.assets.bossBulletTexture, pavo);
                }
                e.update(dt);
            }

            // pavo shoot fire wave from hand fan (input key > F)
            if (Gdx.input.isKeyJustPressed(Input.Keys.F) && pavo.canShoot()) {
                Bullet b = Bullet.playerBullet(
                    game.assets.bulletTexture,
                    pavo.x + (pavo.facing == 1 ? pavo.w : -8),
                    pavo.y + pavo.h * 0.6f,
                    pavo.facing
                );
                playerBullets.add(b);
                pavo.didShoot();
            }

            // Update bullets
            for (int i = 0; i < playerBullets.size; i++)
                playerBullets.get(i).update(dt);
            for (int i = 0; i < enemyBullets.size;  i++)
                enemyBullets.get(i).update(dt);

            // Collisions
            collisions.playerBulletsVsEnemies(playerBullets, enemies, levels);
            collisions.enemyBulletsVsPavo(enemyBullets, pavo);
            if (collisions.enemiesVsPavo(enemies, pavo)) {
                pavo.hp -= 1;
                pavo.x = Math.max(0, pavo.x - 50);
                if (pavo.hp <= 0) state = State.GAME_OVER;
            }

            // remove used bullets and killed enemies
            for (int i = playerBullets.size - 1; i >= 0; i--)
                if (!playerBullets.get(i).alive) playerBullets.removeIndex(i);
            for (int i = enemyBullets.size - 1; i >= 0; i--)
                if (!enemyBullets.get(i).alive)  enemyBullets.removeIndex(i);
            for (int i = enemies.size - 1; i >= 0; i--)
                if (!enemies.get(i).alive)        enemies.removeIndex(i);

            // Win check
            if (boss != null && !boss.alive) state = State.WIN;
        }
        // Render
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        drawBackground();
        pavo.render(game.batch);
        for (Enemy e : enemies) e.render(game.batch);
        for (int i = 0; i < playerBullets.size; i++) playerBullets.get(i).render(game.batch);
        for (int i = 0; i < enemyBullets.size;  i++) enemyBullets.get(i).render(game.batch);

        int bossHP = boss != null && boss.alive ? boss.hp : -1;
        hud.draw(game.batch, levels.level, levels.totalKills, pavo.hp, bossHP);

        if (state == State.PAUSED)
            game.assets.font.draw(game.batch, "PAUSED", 430, 300);
        if (state == State.WIN)
            game.assets.font.draw(game.batch, "YOU WIN!", 420, 300);
        if (state == State.GAME_OVER)
            game.assets.font.draw(game.batch, "GAME OVER", 410, 300);
        game.batch.end();
    }

    private void drawBackground() {

        Texture bg = game.assets.lvOne;

        if (levels.level == 2)
            bg = game.assets.lvTwo;
        if (levels.level == 3)
            bg = game.assets.lvThree;
        game.batch.draw(bg, 0, 0, 960, 540);
    }

    @Override public void show() {

    }
    @Override public void resize(int width, int height) {

    }
    @Override public void pause() {

    }
    @Override public void resume() {

    }
    @Override public void hide() {

    }
    @Override public void dispose() {

    }
}
