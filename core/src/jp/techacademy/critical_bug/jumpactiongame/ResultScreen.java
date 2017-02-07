package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

public class ResultScreen extends ScreenAdapter {
    private static final float GUI_WIDTH = 320;
    private static final float GUI_HEIGHT = 480;
    private final JumpActionGame mGame;
    private final int mScore;
    private final Sprite mBg;
    private final OrthographicCamera mGuiCamera;
    private final BitmapFont mFont;

    public ResultScreen(final JumpActionGame game, final int score) {
        mGame = game;
        if (mGame.requestHandler != null) {
            mGame.requestHandler.showAds(true);
        }
        mScore = score;

        Texture bgTexture = new Texture("resultback.png");
        mBg = new Sprite(new TextureRegion(bgTexture, 0, 0, 540, 810));
        mBg.setSize(GUI_WIDTH, GUI_HEIGHT);
        mBg.setPosition(0, 0);

        mGuiCamera = new OrthographicCamera();
        mGuiCamera.setToOrtho(false, GUI_WIDTH, GUI_HEIGHT);

        mFont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // カメラの座標をアップデート（計算）し、スプライトの表示に反映させる
        mGuiCamera.update();
        mGame.batch.setProjectionMatrix(mGuiCamera.combined);

        mGame.batch.begin();
        mBg.draw(mGame.batch);
        mFont.draw(mGame.batch, "Score: " + mScore, 0, GUI_HEIGHT / 2 + 40, GUI_WIDTH, Align.center, false);
        mFont.draw(mGame.batch, "Retry?", 0, GUI_HEIGHT / 2 - 40, GUI_WIDTH, Align.center, false);
        mGame.batch.end();

        if (Gdx.input.justTouched()) {
            if (mGame.requestHandler != null) {
                mGame.requestHandler.showAds(false);
            }
            mGame.setScreen(new GameScreen(mGame));
        }
    }
}
