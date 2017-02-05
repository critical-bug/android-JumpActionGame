package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.graphics.Texture;

class Star extends GameObject {
    private static final float STAR_WIDTH = 0.8f;
    static final float STAR_HEIGHT = 0.8f;

    enum State {None, Exist}
    State mState;

    Star(final Texture texture, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setSize(STAR_WIDTH, STAR_HEIGHT);
        mState = State.Exist;
    }

    void get() {
        mState = State.None;
        setAlpha(0);
    }
}
