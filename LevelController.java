package com.g6.pavovival.actions;

public class LevelController {

    public int level = 1;               // 1 to 3
    public final int waveSize = 5;      // 5 minions per level
    public int spawnedInWave = 0;       // spawned in current wave
    public int killedInWave  = 0;       // killed in current wave
    public int totalKills    = 0;

    public boolean bossSpawned = false;

    // Spawner call when minion is spawned
    public void onMinionSpawned() { spawnedInWave++; }

    // Collision call when a minion dies
    public void onMinionKilled() {
        killedInWave++;
        totalKills++;
    }

    // add more minions or not (current wavr)
    public boolean canSpawnMoreThisWave() {
        return spawnedInWave < waveSize;
    }

    // wave cleared by kills
    public boolean isWaveCleared() {
        return killedInWave >= waveSize;
    }

    // Change level when wave cleared
    public void advanceLevelIfCleared() {
        if (isWaveCleared() && level < 3) { //level 1 > 2 > 3
            level++;
            resetWave();
        }
    }

    // After finishing level 3 (15 kills), boss appear
    public boolean shouldSpawnBoss() {
        return (level == 3) && isWaveCleared() && !bossSpawned;
    }

    // Reset counters for a new wave (after level change)
    public void resetWave() {
        spawnedInWave = 0;
        killedInWave  = 0;
    }
}
