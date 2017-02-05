package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.graphics.Texture;

public class Ufo extends GameObject {
    static final float UFO_WIDTH = 2.0f;
    private static final float UFO_HEIGHT = 1.3f;

    public Ufo(final Texture texture, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setSize(UFO_WIDTH, UFO_HEIGHT);
    }


}
