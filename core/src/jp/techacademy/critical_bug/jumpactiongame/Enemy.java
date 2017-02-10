package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.graphics.Texture;

public class Enemy extends GameObject {
    static final float WIDTH = 0.5f;
    static final float HEIGHT = 0.5f;

    private static final float VELOCITY = 2.0f;

    Enemy(final Texture texture, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setSize(WIDTH, HEIGHT);
        velocity.x = VELOCITY;
    }

    void update(float deltaTime) {
        setX(getX() + velocity.x * deltaTime);

        if (getX() < WIDTH / 2) {
            velocity.x = -velocity.x;
            setX(WIDTH / 2);
        }
        if (getX() > GameScreen.WORLD_WIDTH - WIDTH / 2) {
            velocity.x = -velocity.x;
            setX(GameScreen.WORLD_WIDTH - WIDTH / 2);
        }
    }

}
