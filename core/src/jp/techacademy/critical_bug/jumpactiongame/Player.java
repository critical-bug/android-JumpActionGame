package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.graphics.Texture;

public class Player extends GameObject {
    private static final float PLAYER_WIDTH = 1.0f;
    private static final float PLAYER_HEIGHT = 1.0f;
    private static final float PLAYER_MOVE_VELOCITY = 20.0f;
    private static final float PLAYER_JUMP_VELOCITY = 11.0f;
    private State mState;
    private enum State {Jump, Fall};

    public Player(final Texture texture, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setSize(PLAYER_WIDTH, PLAYER_HEIGHT);
        mState = State.Fall;
    }

    public void update(float delta, float accelX) {
        // 重力をプレイヤーの速度に加算し、速度から位置を計算する
        velocity.add(0, GameScreen.GRAVITY * delta);
        velocity.x = -accelX / 10 * PLAYER_MOVE_VELOCITY;
        setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);

        // y方向の速度が正（＝上方向）のときにSTATEがPLAYER_STATE_JUMPでなければPLAYER_STATE_JUMPにする
        if (velocity.y > 0) {
            if (mState != State.Jump) {
                mState = State.Jump;
            }
        }

        // y方向の速度が負（＝下方向）のときにSTATEがPLAYER_STATE_FALLでなければPLAYER_STATE_FALLにする
        if (velocity.y < 0) {
            if (mState != State.Fall) {
                mState = State.Fall;
            }
        }

        // 画面の端まで来たら反対側に移動させる
        if (getX() + PLAYER_WIDTH / 2 < 0) {
            setX(GameScreen.WORLD_WIDTH - PLAYER_WIDTH / 2);
        } else if (getX() + PLAYER_WIDTH / 2 > GameScreen.WORLD_WIDTH) {
            setX(0);
        }
    }

    public void hitStep() {
        velocity.y = PLAYER_JUMP_VELOCITY;
        mState = State.Jump;
    }
}
