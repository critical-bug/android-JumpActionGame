package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class JumpActionGame extends Game {
    SpriteBatch batch;
    public final ActivityRequestHandler requestHandler;

    public JumpActionGame(final ActivityRequestHandler activityRequestHandler) {
        requestHandler = activityRequestHandler;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose () {
		batch.dispose();
	}
}
