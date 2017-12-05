package es.carlosrolindez.pong.overlays


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.*


class GUIOverlay(private val gameScreen: PongScreen) : InputAdapter() {
    private val viewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)


    private var pointerPlayerLeftUp: Int = 0
    private var pointerPlayerRightUp: Int = 0
    private var pointerPlayerLeftDown: Int = 0
    private var pointerPlayerRightDown: Int = 0

    private val rectLeftUp = Rectangle(-SCREEN_WIDTH/2f,0f , BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT + 2*BUTTON_MARGIN_Y)
    private val rectRightUp = Rectangle(SCREEN_WIDTH/2f  - BUTTON_WIDTH - 2*BUTTON_MARGIN_X,0f , BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT+ 2*BUTTON_MARGIN_Y)
    private val rectLeftDown = Rectangle(-SCREEN_WIDTH/2f, - BUTTON_HEIGHT - 2* BUTTON_MARGIN_Y, BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT+ 2*BUTTON_MARGIN_Y)
    private val rectRightDown = Rectangle(SCREEN_WIDTH/2f - BUTTON_WIDTH - 2*BUTTON_MARGIN_X,- BUTTON_HEIGHT - 2*BUTTON_MARGIN_Y, BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT+ 2*BUTTON_MARGIN_Y)
    private val rectSettings = Rectangle( SETTINGS_OFFSET_X - SETTING_BUTTON_WIDTH,-SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y - SETTING_BUTTON_HEIGHT/2, 2*SETTING_BUTTON_WIDTH, 2*SETTING_BUTTON_HEIGHT)
    private val rectMusic = Rectangle(MUSIC_OFFSET_X - SETTING_BUTTON_WIDTH,-SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y - SETTING_BUTTON_HEIGHT/2, 2*SETTING_BUTTON_WIDTH, 2*SETTING_BUTTON_HEIGHT)
    private val rectSound = Rectangle(SOUND_OFFSET_X - SETTING_BUTTON_WIDTH,-SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y - SETTING_BUTTON_HEIGHT/2, 2*SETTING_BUTTON_WIDTH, 2*SETTING_BUTTON_HEIGHT)
    private val rectAutoPlayer1 = Rectangle(-SCREEN_WIDTH/2f,- BUTTON_AUTO_HEIGHT - 2*BUTTON_AUTO_MARGIN_Y, 2*BUTTON_AUTO_MARGIN_X + BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT + 2*BUTTON_AUTO_MARGIN_Y)
    private val rectAutoPlayer2 = Rectangle(SCREEN_WIDTH/2f -2*BUTTON_AUTO_MARGIN_X - BUTTON_AUTO_WIDTH, - BUTTON_AUTO_HEIGHT - 2*BUTTON_AUTO_MARGIN_Y, 2*BUTTON_AUTO_MARGIN_X + BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT + 2*BUTTON_AUTO_MARGIN_Y)
    private val rectScreen = Rectangle(-SCREEN_WIDTH/2f + BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, -SCREEN_HEIGHT/2f + MARGIN,SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, SCREEN_HEIGHT-2*MARGIN)
    private val rectPlay = Rectangle( - SETTING_BUTTON_WIDTH/2, - PLAY_OFFSET_Y- SETTING_BUTTON_HEIGHT/2, SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT)
    private val rectNetwork = Rectangle( SCREEN_WIDTH/2f - BUTTON_NETWORK_MARGIN_X/2f - BUTTON_NETWORK_WIDTH, SCREEN_HEIGHT/2f - BUTTON_NETWORK_MARGIN_Y/2f - BUTTON_NETWORK_HEIGHT, BUTTON_NETWORK_WIDTH, BUTTON_NETWORK_HEIGHT)

    private var flashingTime  = 0f
    private var goalSide = 1

    init {
        viewport.camera.position.set(0f, 0f,0f)
        Assets.instance.smokeParticles.emitters.first().setPosition(0f, 0f)
        Assets.instance.smokeParticles.start()
    }

    fun render(batch: SpriteBatch) {
        viewport.apply()

        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        Assets.instance.smokeParticles.draw(batch)

        batch.color = Color.SKY
        val region = Assets.instance.buttonAsset.background
        drawTextureRegion(batch, region, -SCREEN_WIDTH/2f + BUTTON_WIDTH + BUTTON_MARGIN_X*2, -SCREEN_HEIGHT/2f + MARGIN,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, SCREEN_HEIGHT - 2* MARGIN, 0f, false, false)

        batch.color = Color.GREEN

        drawTextureRegion(batch,
                if (gameScreen.level.leftUpPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                -SCREEN_WIDTH/2f + BUTTON_MARGIN_X,
                BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (gameScreen.level.leftDownPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                -SCREEN_WIDTH/2f + BUTTON_MARGIN_X,
                - BUTTON_HEIGHT - BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, true)

        drawTextureRegion(batch,
                if (gameScreen.level.rightUpPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                SCREEN_WIDTH/2f  - BUTTON_WIDTH - BUTTON_MARGIN_X,
                BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (gameScreen.level.rightDownPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                SCREEN_WIDTH/2f - BUTTON_WIDTH - BUTTON_MARGIN_X,
                - BUTTON_HEIGHT - BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, true)

        Assets.instance.sevenFont.data.setScale(0.2f)
        Assets.instance.sevenFont.color= Color.WHITE //Color.FOREST
        Assets.instance.sevenFont.draw(batch,GamePreferences.instance.player1Name ,-SCREEN_WIDTH/2f + PLAYER_TEXT_OFFSET_X, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.left,false)
        Assets.instance.sevenFont.draw(batch,GamePreferences.instance.player2Name, SCREEN_WIDTH/2f - PLAYER_TEXT_OFFSET_X, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.right,false)

        Assets.instance.sevenFont.draw(batch,":" , 0f, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.center,false)


        Assets.instance.sevenFont.color = Color.WHITE
        Assets.instance.sevenFont.draw(batch,gameScreen.scorePlayer1.toString() , -SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.right,false)
        Assets.instance.sevenFont.draw(batch,gameScreen.scorePlayer2.toString() , SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.left,false)
        if (TimeUtils.nanoTime() * MathUtils.nanoToSec -flashingTime < FLASHING_TIME) {
            Assets.instance.sevenFont.color = Color.GREEN
            if (goalSide==1) {
                Assets.instance.sevenFont.draw(batch,gameScreen.scorePlayer1.toString() , -SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                        0f, Align.right,false)
            } else {
                Assets.instance.sevenFont.draw(batch,gameScreen.scorePlayer2.toString() , SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                        0f, Align.left,false)
            }

        }




        if (gameScreen.level.player1.auto)
            batch.color = Color.GREEN
        else
            batch.color = Color.FOREST

        drawTextureRegion(batch,
                Assets.instance.buttonAsset.buttonAuto,
                -SCREEN_WIDTH/2 + BUTTON_AUTO_MARGIN_X,
                - BUTTON_AUTO_HEIGHT/2f - BUTTON_AUTO_MARGIN_Y,
                BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT,
                0f,
                false, false)

        if (gameScreen.level.player2.auto)
            batch.color = Color.GREEN
        else
            batch.color = Color.FOREST

        drawTextureRegion(batch,
                Assets.instance.buttonAsset.buttonAuto,
                SCREEN_WIDTH/2f  - BUTTON_AUTO_WIDTH - BUTTON_AUTO_MARGIN_X,
                - BUTTON_AUTO_HEIGHT/2f - BUTTON_AUTO_MARGIN_Y,
                BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT,
                0f,
                false, false)

        if (gameScreen.netClient.playing)
            batch.color = Color.GREEN
        else
            batch.color = Color.FOREST

        drawTextureRegion(batch,
                Assets.instance.buttonAsset.buttonNetwork,
                SCREEN_WIDTH/2f - BUTTON_NETWORK_MARGIN_X/2f - BUTTON_NETWORK_WIDTH,
                SCREEN_HEIGHT/2f - BUTTON_NETWORK_MARGIN_Y/2f - BUTTON_NETWORK_HEIGHT,
                BUTTON_NETWORK_WIDTH, BUTTON_NETWORK_HEIGHT,
                0f,
                false, false)

        batch.color = Color.GREEN

        drawTextureRegion(batch,
                Assets.instance.buttonAsset.buttonSettings,
                SETTINGS_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                -SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (GamePreferences.instance.music) Assets.instance.buttonAsset.buttonMusicOn
                else Assets.instance.buttonAsset.buttonMusicOff,
                MUSIC_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                -SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (GamePreferences.instance.sound)Assets.instance.buttonAsset.buttonSoundOn
                else Assets.instance.buttonAsset.buttonSoundOff,
                SOUND_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                -SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        Assets.instance.lineFireworksParticles.draw(batch)
        Assets.instance.circleFireworksParticles.draw(batch)

        if (gameScreen.gameover) {
            Assets.instance.sevenFont.data.setScale(0.4f)
            Assets.instance.sevenFont.color = Color.WHITE //Color.FOREST
            if (gameScreen.scorePlayer1>gameScreen.scorePlayer2)
                Assets.instance.sevenFont.draw(batch, GamePreferences.instance.player1Name + WINNER_MESSAGE, 0f, 0f + PLAY_OFFSET_Y,
                    0f, Align.center    , false)
            else
                Assets.instance.sevenFont.draw(batch, GamePreferences.instance.player2Name + WINNER_MESSAGE, 0f, 0f + PLAY_OFFSET_Y,
                        0f, Align.center    , false)
            val bright = 0.9f + 0.1f * Math.sin(TimeUtils.nanoTime() * MathUtils.nanoToSec * Math.PI * 2f).toFloat()
            batch.color = Color(bright,bright,bright,1f)
            drawTextureRegion(batch,
                    Assets.instance.buttonAsset.buttonPlay,
                    - SETTING_BUTTON_WIDTH/2,
                    - SETTING_BUTTON_HEIGHT/2 - PLAY_OFFSET_Y,
                    SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                    0f,
                    false, false)
        } else if (gameScreen.paused) {
            Assets.instance.sevenFont.data.setScale(0.4f)
            Assets.instance.sevenFont.color = Color.WHITE //Color.FOREST
            Assets.instance.sevenFont.draw(batch, PAUSED_MESSAGE, 0f, PLAY_OFFSET_Y,
                        0f, Align.center    , false)
            val bright = 0.9f + 0.1f * Math.sin(TimeUtils.nanoTime() * MathUtils.nanoToSec * Math.PI * 2f).toFloat()
            batch.color = Color(bright,bright,bright,1f)
            drawTextureRegion(batch,
                    Assets.instance.buttonAsset.buttonPlay,
                    - SETTING_BUTTON_WIDTH/2,
                    - SETTING_BUTTON_HEIGHT/2 - PLAY_OFFSET_Y,
                    SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                    0f,
                    false, false)
        }

        batch.end()


    }


    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(0f, 0f,0f)
    }

    fun dispose() {
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val position = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        when {
            rectLeftUp.contains(position) -> {
                pointerPlayerLeftUp = pointer
                gameScreen.level.leftUpPressed = true
            }
            rectRightUp.contains(position) -> {
                pointerPlayerRightUp = pointer
                gameScreen.level.rightUpPressed = true
            }
            rectLeftDown.contains(position) -> {
                pointerPlayerLeftDown = pointer
                gameScreen.level.leftDownPressed = true
            }
            rectRightDown.contains(position) -> {
                pointerPlayerRightDown = pointer
                gameScreen.level.rightDownPressed = true
            }
            rectSettings.contains(position) -> {
                gameScreen.paused = true
                gameScreen.configurationStage.openDialog()
            }
            rectMusic.contains(position) -> {
                gameScreen.level.switchMusic()
            }
            rectSound.contains(position) -> {
                gameScreen.level.switchSound()
            }
            rectAutoPlayer1.contains(position) -> {
                gameScreen.level.player1.switchAuto()
            }
            rectAutoPlayer2.contains(position) -> {
                gameScreen.level.player2.switchAuto()
            }
            rectNetwork.contains(position) -> {
                gameScreen.paused = true
                gameScreen.netClient.playing = ! gameScreen.netClient.playing
                gameScreen.networkStage.openDialog()
            }
            rectPlay.contains(position) -> {
                if (gameScreen.gameover) {
                    gameScreen.level.initBall()
                    gameScreen.paused = false
                    Assets.instance.lineFireworksParticles.reset()
                    Assets.instance.circleFireworksParticles.reset()

                } else {
                    gameScreen.paused = !gameScreen.paused
                }
            }
            rectScreen.contains(position) -> {
                gameScreen.paused = true
            }

        }
        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val position = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))

        if (pointer == pointerPlayerLeftUp && rectLeftDown.contains(position) ) {
            pointerPlayerLeftDown = pointer
            pointerPlayerLeftUp = 0
            gameScreen.level.leftUpPressed = false
            gameScreen.level.leftDownPressed = true
        } else if (pointer == pointerPlayerLeftDown && rectLeftUp.contains(position) ) {
            pointerPlayerLeftUp = pointer
            pointerPlayerLeftDown = 0
            gameScreen.level.leftDownPressed = false
            gameScreen.level.leftUpPressed = true
        } else if (pointer == pointerPlayerRightUp && rectRightDown.contains(position) ) {
            pointerPlayerRightDown = pointer
            pointerPlayerRightUp = 0
            gameScreen.level.rightUpPressed = false
            gameScreen.level.rightDownPressed = true
        } else if (pointer == pointerPlayerRightDown && rectRightUp.contains(position) ) {
            pointerPlayerRightUp = pointer
            pointerPlayerRightDown = 0
            gameScreen.level.rightDownPressed = false
            gameScreen.level.rightUpPressed = true
        }
        return super.touchDragged(screenX, screenY, pointer)

    }


    fun update(delta: Float) {
        if (!Gdx.input.isTouched(pointerPlayerLeftDown)) {
            pointerPlayerLeftDown = 0
            gameScreen.level.leftDownPressed = false
        }
        if (!Gdx.input.isTouched(pointerPlayerLeftUp)) {
            pointerPlayerLeftUp = 0
            gameScreen.level.leftUpPressed = false
        }
        if (!Gdx.input.isTouched(pointerPlayerRightDown)) {
            pointerPlayerRightDown = 0
            gameScreen.level.rightDownPressed = false
        }
        if (!Gdx.input.isTouched(pointerPlayerRightUp)) {
            pointerPlayerRightUp = 0
            gameScreen.level.rightUpPressed = false
        }

        if (gameScreen.gameover) {
            if (Assets.instance.lineFireworksParticles.isComplete && Assets.instance.circleFireworksParticles.isComplete) {
                firework()
            }
            Assets.instance.lineFireworksParticles.update(delta)
            Assets.instance.circleFireworksParticles.update(delta)

        }

        Assets.instance.smokeParticles.update(delta)
    }

    private fun firework() {
        val vector = Vector2(MathUtils.random(-SCREEN_WIDTH / 4, SCREEN_WIDTH / 4), MathUtils.random(-SCREEN_HEIGHT / 4, SCREEN_HEIGHT / 4))
        Assets.instance.lineFireworksParticles.emitters.first().setPosition(vector.x,vector.y)
        Assets.instance.circleFireworksParticles.emitters.first().setPosition(vector.x,vector.y)
        Assets.instance.lineFireworksParticles.start()
        Assets.instance.circleFireworksParticles.start()
        if (GamePreferences.instance.sound)
            Assets.instance.fireworkSound.play(1f * GamePreferences.instance.volSound)
    }

    internal fun flashScore(side : Int) {
        flashingTime = TimeUtils.nanoTime() * MathUtils.nanoToSec
        goalSide = side
    }
}
