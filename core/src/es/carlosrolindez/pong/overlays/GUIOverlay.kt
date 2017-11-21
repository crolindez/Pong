package es.carlosrolindez.pong.overlays


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.*


class GUIOverlay(private val gameScreen: PongScreen) : InputAdapter() {
    private val viewport: Viewport

    private var pointerPlayerLeftUp: Int = 0
    private var pointerPlayerRightUp: Int = 0
    private var pointerPlayerLeftDown: Int = 0
    private var pointerPlayerRightDown: Int = 0

    private val rectLeftUp = Rectangle(0f,SCREEN_HEIGHT/2 , BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT + 2*BUTTON_MARGIN_Y)
    private val rectRightUp = Rectangle(SCREEN_WIDTH  - BUTTON_WIDTH - 2*BUTTON_MARGIN_X,SCREEN_HEIGHT/2 , BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT+ 2*BUTTON_MARGIN_Y)
    private val rectLeftDown = Rectangle(0f,SCREEN_HEIGHT/2  - BUTTON_HEIGHT - 2* BUTTON_MARGIN_Y, BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT+ 2*BUTTON_MARGIN_Y)
    private val rectRightDown = Rectangle(SCREEN_WIDTH - BUTTON_WIDTH - 2*BUTTON_MARGIN_X,SCREEN_HEIGHT/2  - BUTTON_HEIGHT - 2*BUTTON_MARGIN_Y, BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, BUTTON_HEIGHT+ 2*BUTTON_MARGIN_Y)
    private val rectSettings = Rectangle(SCREEN_WIDTH /2 + SETTINGS_OFFSET_X - SETTING_BUTTON_WIDTH,BOTTOM_BUTTONS_OFFSET_Y - SETTING_BUTTON_HEIGHT/2, 2*SETTING_BUTTON_WIDTH, 2*SETTING_BUTTON_HEIGHT)
    private val rectMusic = Rectangle(SCREEN_WIDTH /2 + MUSIC_OFFSET_X - SETTING_BUTTON_WIDTH,BOTTOM_BUTTONS_OFFSET_Y - SETTING_BUTTON_HEIGHT/2, 2*SETTING_BUTTON_WIDTH, 2*SETTING_BUTTON_HEIGHT)
    private val rectSound = Rectangle(SCREEN_WIDTH /2 + SOUND_OFFSET_X - SETTING_BUTTON_WIDTH,BOTTOM_BUTTONS_OFFSET_Y - SETTING_BUTTON_HEIGHT/2, 2*SETTING_BUTTON_WIDTH, 2*SETTING_BUTTON_HEIGHT)
    private val rectAutoPlayer1 = Rectangle(0f,SCREEN_HEIGHT /2 - BUTTON_AUTO_HEIGHT - 2*BUTTON_AUTO_MARGIN_Y, 2*BUTTON_AUTO_MARGIN_X + BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT + 2*BUTTON_AUTO_MARGIN_Y)
    private val rectAutoPlayer2 = Rectangle(SCREEN_WIDTH -2*BUTTON_AUTO_MARGIN_X - BUTTON_AUTO_WIDTH, SCREEN_HEIGHT /2 - BUTTON_AUTO_HEIGHT - 2*BUTTON_AUTO_MARGIN_Y, 2*BUTTON_AUTO_MARGIN_X + BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT + 2*BUTTON_AUTO_MARGIN_Y)


    init {
        viewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
        Assets.instance.smokeParticles.emitters.first().setPosition(SCREEN_WIDTH/2, SCREEN_HEIGHT/2)
        Assets.instance.smokeParticles.start()
    }

    fun render(batch: SpriteBatch) {
        viewport.apply()

        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        Assets.instance.smokeParticles.draw(batch)

        batch.color = Color.SKY
        val region = Assets.instance.buttonAsset.background
        drawTextureRegion(batch, region, BUTTON_WIDTH + BUTTON_MARGIN_X*2, MARGIN,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, SCREEN_HEIGHT - 2* MARGIN, 0f, false, false)

        batch.setColor(1f, 1f, 1f, 1f)

        drawTextureRegion(batch,
                if (gameScreen.level.leftUpPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                BUTTON_MARGIN_X,
                SCREEN_HEIGHT/2 + BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (gameScreen.level.leftDownPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                BUTTON_MARGIN_X,
                SCREEN_HEIGHT/2  - BUTTON_HEIGHT - BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, true)

        drawTextureRegion(batch,
                if (gameScreen.level.rightUpPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                SCREEN_WIDTH  - BUTTON_WIDTH - BUTTON_MARGIN_X,
                SCREEN_HEIGHT/2 + BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (gameScreen.level.rightDownPressed) {
                    Assets.instance.buttonAsset.buttonPressed
                } else {
                    Assets.instance.buttonAsset.buttonReleased
                },
                SCREEN_WIDTH - BUTTON_WIDTH - BUTTON_MARGIN_X,
                SCREEN_HEIGHT/2  - BUTTON_HEIGHT - BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, true)

        Assets.instance.sevenFont.data.setScale(0.2f)
        Assets.instance.sevenFont.color= Color.WHITE //Color.FOREST
        Assets.instance.sevenFont.draw(batch,"PLAYER 1" ,PLAYER_TEXT_OFFSET_X, SCREEN_HEIGHT - PLAYER_TEXT_OFFSET_Y,
                0f, Align.left,false)
        Assets.instance.sevenFont.draw(batch,"PLAYER 2" , SCREEN_WIDTH - PLAYER_TEXT_OFFSET_X, SCREEN_HEIGHT - PLAYER_TEXT_OFFSET_Y,
                0f, Align.right,false)
        Assets.instance.sevenFont.draw(batch,gameScreen.scorePlayer1.toString() , SCREEN_WIDTH/2 - SCORE_TEXT_OFFSET, SCREEN_HEIGHT - PLAYER_TEXT_OFFSET_Y,
                0f, Align.right,false)
        Assets.instance.sevenFont.draw(batch,":" , SCREEN_WIDTH/2, SCREEN_HEIGHT - PLAYER_TEXT_OFFSET_Y,
                0f, Align.center,false)
        Assets.instance.sevenFont.draw(batch,gameScreen.scorePlayer2.toString() , SCREEN_WIDTH/2 + SCORE_TEXT_OFFSET, SCREEN_HEIGHT - PLAYER_TEXT_OFFSET_Y,
                0f, Align.left,false)



        if (gameScreen.level.player1.auto)
            batch.color = Color.GREEN
        else
            batch.color = Color.FOREST

        drawTextureRegion(batch,
                Assets.instance.buttonAsset.buttonAuto,
                BUTTON_AUTO_MARGIN_X,
                SCREEN_HEIGHT /2 - BUTTON_AUTO_HEIGHT - BUTTON_AUTO_MARGIN_Y,
                BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT,
                0f,
                false, false)

        if (gameScreen.level.player2.auto)
            batch.color = Color.GREEN
        else
            batch.color = Color.FOREST

        drawTextureRegion(batch,
                Assets.instance.buttonAsset.buttonAuto,
                SCREEN_WIDTH  - BUTTON_AUTO_WIDTH - BUTTON_AUTO_MARGIN_X,
                SCREEN_HEIGHT /2 - BUTTON_AUTO_HEIGHT - BUTTON_AUTO_MARGIN_Y,
                BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT,
                0f,
                false, false)


        batch.color = Color.GREEN

        drawTextureRegion(batch,
                Assets.instance.buttonAsset.buttonSettings,
                SCREEN_WIDTH /2 + SETTINGS_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (gameScreen.level.musicState) Assets.instance.buttonAsset.buttonMusicOn
                else Assets.instance.buttonAsset.buttonMusicOff,
                SCREEN_WIDTH /2 + MUSIC_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (gameScreen.level.soundState)Assets.instance.buttonAsset.buttonSoundOn
                else Assets.instance.buttonAsset.buttonSoundOff,
                SCREEN_WIDTH /2 + SOUND_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        batch.end()


    }


    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
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
        Assets.instance.smokeParticles.update(delta)
    }
}
