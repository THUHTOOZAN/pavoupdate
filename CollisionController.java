package com.g6.pavovival.actions;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.g6.pavovival.entities.*;

public class CollisionController {
    private Rectangle r1 = new Rectangle(), r2 = new Rectangle();

    /** Player bullets hit enemies; report minion kills to LevelController. */
    public void playerBulletsVsEnemies(Array<Bullet> playerBullets, Array<Enemy> enemies, LevelController levels) {

        for (Bullet b : playerBullets) if (b.alive) {
            r1.set(b.bounds());
            for (Enemy e : enemies) if (e.alive) {
                r2.set(e.bounds());
                if (r1.overlaps(r2)) {
                    e.hit(b.damage());
                    b.alive = false;
                    // count only minion deaths for waves
                    if (!e.alive && e instanceof Minion) levels.onMinionKilled();
                    break;
                }
            }
        }
    }

    // boss bullets hit Pavo
    public void enemyBulletsVsPavo(Array<Bullet> enemyBullets, Pavo p) {
        r1.set(p.bounds());

        for (Bullet b : enemyBullets) if (b.alive) {
            r2.set(b.bounds());
            if (r1.overlaps(r2)) {
                b.alive = false;
                p.hp -= 1;
            }
        }
    }

    // minions touch Pavo (contact damage).
    public boolean enemiesVsPavo(Array<Enemy> enemies, Pavo p) {
        r1.set(p.bounds());
        for (Enemy e : enemies) if (e.alive) {
            r2.set(e.bounds());
            if (r1.overlaps(r2)) return true;
        }
        return false;
    }
}
