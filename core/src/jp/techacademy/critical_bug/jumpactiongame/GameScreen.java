package jp.techacademy.critical_bug.jumpactiongame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Random;

class GameScreen extends ScreenAdapter {
    private static final float CAMERA_HEIGHT = 15f;
    private static final float CAMERA_WIDTH = 10f;
    static final float GRAVITY = -12f;
    static final float WORLD_WIDTH = 10f;
    static final float WORLD_HEIGHT = CAMERA_HEIGHT * 20; // 20画面分登れば終了;
    private static final float GUI_WIDTH = 320;
    private static final float GUI_HEIGHT = 480;
    private final JumpActionGame mGame;
    private final Sprite mBg;
    private final OrthographicCamera mCamera;
    private final FitViewport mViewPort;
    private final Random mRandom;
    private final ArrayList<Step> mSteps;
    private final ArrayList<Star> mStars;
    private final Ufo mUfo;
    private final Player mPlayer;
    private final Vector3 mTouchPoint;
    private final OrthographicCamera mGuiCamera;
    private final FitViewport mGuiViewPort;
    private final BitmapFont mFont;
    private final Preferences mPrefs;
    private GameState mGameState;
    private float mHeightSoFar;
    private int mScore;
    private int mHighScore;

    enum GameState {Ready,Playing,Gameover}

    GameScreen(final JumpActionGame game) {
        mGame = game;
        Texture bgTexture = new Texture("back.png");
        // TextureRegionで切り出す時の原点は左上
        mBg = new Sprite(new TextureRegion(bgTexture, 0, 0, 540, 810));
        mBg.setSize(CAMERA_WIDTH, CAMERA_HEIGHT);
        mBg.setPosition(0, 0);

        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        mViewPort = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT);

        // GUI用のカメラを設定する
        mGuiCamera = new OrthographicCamera();
        mGuiCamera.setToOrtho(false, GUI_WIDTH, GUI_HEIGHT);
        mGuiViewPort = new FitViewport(GUI_WIDTH, GUI_HEIGHT, mGuiCamera);

        mRandom = new Random();
        mSteps = new ArrayList<Step>();
        mStars = new ArrayList<Star>();
        mGameState = GameState.Ready;
        mTouchPoint = new Vector3();

        mFont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        mFont.getData().setScale(0.8f);
        mScore = 0;
        mPrefs = Gdx.app.getPreferences("jp.techacademy.critical_bug.jumpactiongame");
        mHighScore = mPrefs.getInteger("HIGHSCORE", 0);

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

        if (mPlayer.getY() > mCamera.position.y) {
            mCamera.position.y = mPlayer.getY();
        }

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
        mPlayer.draw(mGame.batch);

        mGame.batch.end();

        mGuiCamera.update();
        mGame.batch.setProjectionMatrix(mGuiCamera.combined);
        mGame.batch.begin();
        mFont.draw(mGame.batch, "HighScore: " + mHighScore, 16, GUI_HEIGHT - 15);
        mFont.draw(mGame.batch, "Score: " + mScore, 16, GUI_HEIGHT - 35);
        mGame.batch.end();
    }

    private void update(final float delta) {
        switch (mGameState) {
            case Ready:
                updateReady();
                break;
            case Playing:
                updatePlaying(delta);
                break;
            case Gameover:
                updateGameOver();
                break;
        }
    }

    private void updateGameOver() {

    }

    private void updatePlaying(final float delta) {
        float accel = 0;
        if (Gdx.input.isTouched()) {
            mGuiViewPort.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle left = new Rectangle(0, 0, GUI_WIDTH / 2, GUI_HEIGHT);
            Rectangle right = new Rectangle(GUI_WIDTH / 2, 0, GUI_WIDTH / 2, GUI_HEIGHT);
            if (left.contains(mTouchPoint.x, mTouchPoint.y)) {
                accel = 5.0f;
            }
            if (right.contains(mTouchPoint.x, mTouchPoint.y)) {
                accel = -5.0f;
            }
        }

        for (final Step step : mSteps) {
            step.update(delta);
        }

        // Player ?
        if (mPlayer.getY() <= 0.5f) {
            mPlayer.hitStep();
        }
        mPlayer.update(delta, accel);
        mHeightSoFar = Math.max(mPlayer.getY(), mHeightSoFar);

        checkCollision();

        checkGameOver();
    }

    private void checkGameOver() {
        if (mHeightSoFar - CAMERA_HEIGHT / 2 > mPlayer.getY()) {
            Gdx.app.log("JumpActionGame", "GAMEOVER");
            mGameState = GameState.Gameover;
        }
    }

    /**
     * 当たり判定して mGameState や物体の位置をアップデートする
     */
    private void checkCollision() {
        // UFOに当たったらゴール
        if (mPlayer.getBoundingRectangle().overlaps(mUfo.getBoundingRectangle())) {
            Gdx.app.log("JumpActionGame", "CLEAR");
            mGameState = GameState.Gameover;
            return;
        }
        // starに当たったら回収
        for (final Star star : mStars) {
            if (star.mState == Star.State.None) {
                continue;
            }
            if (mPlayer.getBoundingRectangle().overlaps(star.getBoundingRectangle())) {
                star.get();
                mScore++;
                if (mScore > mHighScore) {
                    mHighScore = mScore;
                    mPrefs.putInteger("HIGHSCORE", mHighScore);
                    mPrefs.flush();
                }
                break;
            }
        }
        // 上昇中はStepとの当たり判定を確認しない
        if (mPlayer.velocity.y > 0) {
            return;
        }
        for (final Step step : mSteps) {
            if (step.mState == Step.State.Vanish) {
                continue;
            }
            if (mPlayer.getY() > step.getY()) {
                if (mPlayer.getBoundingRectangle().overlaps(step.getBoundingRectangle())) {
                    mPlayer.hitStep();
                    // 踏み台と当たった場合 適当な確率で踏み台を消す
                    if (mRandom.nextFloat() > 0.5f) {
                        step.vanish();
                    }
                    break;
                }
            }
        }
    }

    private void updateReady() {
        // タッチでゲーム開始
        if (Gdx.input.justTouched()) {
            mGameState = GameState.Playing;
        }
    }

    @Override
    public void resize(final int width, final int height) {
        mViewPort.update(width, height);
        mGuiViewPort.update(width, height);
    }
}
