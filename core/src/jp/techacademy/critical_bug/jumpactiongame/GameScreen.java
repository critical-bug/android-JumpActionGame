package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends ScreenAdapter {
    private static final float CAMERA_HEIGHT = 15f;
    private static final float CAMERA_WIDTH = 10f;
    public static final float GRAVITY = -12f;
    public static final float WORLD_WIDTH = 10f;
    public static final float WORLD_HEIGHT = CAMERA_HEIGHT * 20; // 20画面分登れば終了;
    private final JumpActionGame mGame;
    private final Sprite mBg;
    private final OrthographicCamera mCamera;
    private final FitViewport mViewPort;
    private final Random mRandom;
    private final ArrayList<Step> mSteps;
    private final ArrayList<Star> mStars;
    private final GameState mGameState;
    private final Ufo mUfo;
    private final Player mPlayer;

    enum GameState {Ready,Playing,Gameover};

    public GameScreen(final JumpActionGame game) {
        mGame = game;
        Texture bgTexture = new Texture("back.png");
        // TextureRegionで切り出す時の原点は左上
        mBg = new Sprite( new TextureRegion(bgTexture, 0, 0, 540, 810));
        mBg.setSize(CAMERA_WIDTH, CAMERA_HEIGHT);
        mBg.setPosition(0, 0);

        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        mViewPort = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT);

        mRandom = new Random();
        mSteps = new ArrayList<Step>();
        mStars = new ArrayList<Star>();
        mGameState = GameState.Ready;

        float y = createStage();

        mPlayer = new Player(new Texture("uma.png"), 0, 0, 72, 72);
        mPlayer.setPosition(WORLD_WIDTH / 2 - mPlayer.getWidth() / 2, Step.STEP_HEIGHT);

        mUfo = new Ufo(new Texture("ufo.png"), 0, 0, 120, 74);
        mUfo.setPosition(WORLD_WIDTH / 2 - Ufo.UFO_WIDTH / 2, y);
    }

    private float createStage() {
        Texture stepTexture = new Texture("step.png");
        Texture starTexture = new Texture("star.png");

        // StepとStarをゴールの高さまで配置していく
        float y = 0;

        float maxJumpHeight = Player.PLAYER_JUMP_VELOCITY * Player.PLAYER_JUMP_VELOCITY / (2 * -GRAVITY);
        while (y < WORLD_HEIGHT - 5) {
            Step.Type type = mRandom.nextFloat() > 0.8f ? Step.Type.Moving : Step.Type.Static;
            float x = mRandom.nextFloat() * (WORLD_WIDTH - Step.STEP_WIDTH);

            Step step = new Step(type, stepTexture, 0, 0, 144, 36);
            step.setPosition(x, y);
            mSteps.add(step);

            if (mRandom.nextFloat() > 0.6f) {
                Star star = new Star(starTexture, 0, 0, 72, 72);
                star.setPosition(step.getX() + mRandom.nextFloat(), step.getY() + Star.STAR_HEIGHT + mRandom.nextFloat() * 3);
                mStars.add(star);
            }

            y += (maxJumpHeight - 0.5f);
            y -= mRandom.nextFloat() * (maxJumpHeight / 3);
        }
        return y;
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        // カメラの座標をアップデート（計算）し、スプライトの表示に反映させる
        mCamera.update();
        mGame.batch.setProjectionMatrix(mCamera.combined);

        mGame.batch.begin();
        // 原点は左下
        mBg.setPosition(mCamera.position.x - CAMERA_WIDTH / 2, mCamera.position.y - CAMERA_HEIGHT / 2);
        mBg.draw(mGame.batch);
        for (final Step step : mSteps) {
            step.draw(mGame.batch);
        }
        for (final Star star : mStars) {
            star.draw(mGame.batch);
        }
        mUfo.draw(mGame.batch);
        mGame.batch.end();
    }

    private void update(final float delta) {
        switch (mGameState) {
            case Ready:
                // TODO
                break;
            case Playing:
                // TODO
                break;
            case Gameover:
                // TODO
                break;
        }
    }

    @Override
    public void resize(final int width, final int height) {
        mViewPort.update(width, height);
    }
}
