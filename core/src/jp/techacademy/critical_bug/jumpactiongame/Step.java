package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.graphics.Texture;

/**
 * 足場は初期位置で静止しているタイプと左右に移動するタイプが存在する
 */
public class Step extends GameObject {

    private static final float STEP_WIDTH = 2.0f;
    private static final float STEP_HEIGHT = 0.5f;
    private enum State {Vanish};
    private enum Type {Static, Moving};
    private static final float STEP_VELOCITY = 2.0f;
    private State mState;
    private final Type mType;

    public Step(Type type, final Texture texture, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setSize(STEP_WIDTH, STEP_HEIGHT);
        mType = type;
        if (mType == Type.Moving) {
            velocity.x = STEP_VELOCITY;
        }
    }

    public void update(float deltaTime) {
        switch (mType) {
            case Moving:
            setX(getX() + velocity.x * deltaTime);

            if (getX() < STEP_WIDTH / 2) {
                velocity.x = -velocity.x;
                setX(STEP_WIDTH / 2);
            }
            if (getX() > GameScreen.WORLD_WIDTH - STEP_WIDTH / 2) {
                velocity.x = -velocity.x;
                setX(GameScreen.WORLD_WIDTH - STEP_WIDTH / 2);
            }
        }
    }

    public void vanish() {
        mState = State.Vanish;
        setAlpha(0);
        velocity.x = 0;
    }
}
