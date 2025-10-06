package com.g6.pavovival.actions;

import com.badlogic.gdx.utils.Array;
import com.g6.pavovival.entities.*;

public class Spawn {

    private float spawnTimer = 0f;
    private float spawnInterval = 1.1f;  // seconds between minions
    private int maxActiveMinions = 3;    // minions on the screen

    private final AssetController assets;
    private final LevelController levels;

    public Spawn(AssetController assets, LevelController levels) {
        this.assets = assets;
        this.levels = levels;
    }

    public void update(float dt, Array<Enemy> enemies, Pavo pavo) {
        // Do not spawn minions if boss spawn
        if (levels.shouldSpawnBoss()) return;

        // Count active minions
        int active = 0;
        for (Enemy e : enemies) if (e.alive && e instanceof Minion) active++;

        spawnTimer -= dt;
        boolean canSpawn = levels.canSpawnMoreThisWave() && active < maxActiveMinions && spawnTimer <= 0f;
        if (canSpawn) {
            // spawn from right side from ground
            Minion m = new Minion(assets.minionTexture, 900, 0);
            enemies.add(m);
            levels.onMinionSpawned();
            spawnTimer = spawnInterval;
        }

        // Minion: walk toward Pavo
        for (Enemy e : enemies)
            if (e instanceof Minion && e.alive) ((Minion) e).steerToward(pavo);
    }
}
