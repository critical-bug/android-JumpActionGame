package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.graphics.Texture;

public class Star extends GameObject {
    private static final float STAR_WIDTH = 0.8f;
    private static final float STAR_HEIGHT = 0.8f;

    private enum State {None, Exist}
    private State mState;

    public Star(final Texture texture, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setSize(STAR_WIDTH, STAR_HEIGHT);
        mState = State.Exist;
    }

    public void get() {
        mState = State.None;
        setAlpha(0);
    }
}