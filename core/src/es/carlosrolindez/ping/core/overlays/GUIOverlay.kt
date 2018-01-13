package es.carlosrolindez.ping.core.overlays


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
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.net.Network
import es.carlosrolindez.ping.core.utils.*


class GUIOverlay(private val pingScreen: PingScreen) : InputAdapter() {
    private val viewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)
//    private val shapeRenderer = ShapeRenderer()


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
    private val rectHelp = Rectangle(HELP_OFFSET_X - SETTING_BUTTON_WIDTH,-SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y - SETTING_BUTTON_HEIGHT/2, 2*SETTING_BUTTON_WIDTH, 2*SETTING_BUTTON_HEIGHT)

    private val rectAutoPlayer1 = Rectangle(-SCREEN_WIDTH/2f,- BUTTON_AUTO_HEIGHT - BUTTON_AUTO_OFFSET_Y - BUTTON_AUTO_MARGIN_Y, BUTTON_AUTO_WIDTH + 2* + BUTTON_AUTO_MARGIN_X, BUTTON_AUTO_HEIGHT + 2* BUTTON_AUTO_MARGIN_Y )
    private val rectAutoPlayer2 = Rectangle(SCREEN_WIDTH/2f  - 2 * BUTTON_AUTO_MARGIN_X - BUTTON_AUTO_WIDTH, - BUTTON_AUTO_HEIGHT - BUTTON_AUTO_OFFSET_Y - BUTTON_AUTO_MARGIN_Y, BUTTON_AUTO_WIDTH + 2 * BUTTON_AUTO_MARGIN_X, BUTTON_AUTO_HEIGHT + 2* BUTTON_AUTO_MARGIN_Y)

    private val rectScreen = Rectangle(-SCREEN_WIDTH/2f + BUTTON_WIDTH+ 2* BUTTON_MARGIN_X, -SCREEN_HEIGHT/2f + MARGIN,SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, SCREEN_HEIGHT-2*MARGIN)
    private val rectPlay = Rectangle( - SETTING_BUTTON_WIDTH/2, - PLAY_OFFSET_Y- SETTING_BUTTON_HEIGHT/2, SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT)
    private val rectNetwork = Rectangle( SCREEN_WIDTH/2f - BUTTON_NETWORK_MARGIN_X - BUTTON_NETWORK_WIDTH, SCREEN_HEIGHT/2f - BUTTON_NETWORK_MARGIN_Y - BUTTON_NETWORK_HEIGHT, BUTTON_NETWORK_WIDTH + BUTTON_NETWORK_MARGIN_X, BUTTON_NETWORK_HEIGHT + BUTTON_NETWORK_MARGIN_Y)

    private var flashingTime  = 0f
    private var goalSide = 1

    init {
        viewport.camera.position.set(0f, 0f,0f)
        Assets.smokeParticles.emitters.first().setPosition(0f, 0f)
        Assets.smokeParticles.start()
    }

    internal fun render(batch: SpriteBatch) {
        viewport.apply()

        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        Assets.smokeParticles.draw(batch)

        // Playground
        batch.color = Color.SKY
        val region = Assets.buttonAsset.background
        drawTextureRegion(batch, region, -SCREEN_WIDTH/2f + BUTTON_WIDTH + BUTTON_MARGIN_X*2, -SCREEN_HEIGHT/2f + MARGIN,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, SCREEN_HEIGHT - 2* MARGIN, 0f, false, false)

        // Buttons
        batch.color = Color.GREEN
        drawTextureRegion(batch,
                if (pingScreen.level.leftUpPressed) {
                    Assets.buttonAsset.buttonPressed
                } else {
                    Assets.buttonAsset.buttonReleased
                },
                -SCREEN_WIDTH/2f + BUTTON_MARGIN_X,
                BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (pingScreen.level.leftDownPressed) {
                    Assets.buttonAsset.buttonPressed
                } else {
                    Assets.buttonAsset.buttonReleased
                },
                -SCREEN_WIDTH/2f + BUTTON_MARGIN_X,
                - BUTTON_HEIGHT - BUTTON_MARGIN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, true)

        if (Network.connection == null) {
            drawTextureRegion(batch,
                    if (pingScreen.level.rightUpPressed) {
                        Assets.buttonAsset.buttonPressed
                    } else {
                        Assets.buttonAsset.buttonReleased
                    },
                    SCREEN_WIDTH / 2f - BUTTON_WIDTH - BUTTON_MARGIN_X,
                    BUTTON_MARGIN_Y,
                    BUTTON_WIDTH, BUTTON_HEIGHT,
                    0f,
                    false, false)

            drawTextureRegion(batch,
                    if (pingScreen.level.rightDownPressed) {
                        Assets.buttonAsset.buttonPressed
                    } else {
                        Assets.buttonAsset.buttonReleased
                    },
                    SCREEN_WIDTH / 2f - BUTTON_WIDTH - BUTTON_MARGIN_X,
                    -BUTTON_HEIGHT - BUTTON_MARGIN_Y,
                    BUTTON_WIDTH, BUTTON_HEIGHT,
                    0f,
                    false, true)
        }

        // Players + score
        val opponentName : String =
                if (Network.connection != null)
                    pingScreen.opponentName
                else
                    GamePreferences.player2Name

        Assets.sevenFont.data.setScale(0.2f)
        Assets.sevenFont.color= Color.WHITE //Color.FOREST
        Assets.sevenFont.draw(batch,GamePreferences.player1Name ,-SCREEN_WIDTH/2f + PLAYER_TEXT_OFFSET_X, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.left,false)
        Assets.sevenFont.draw(batch,opponentName, SCREEN_WIDTH/2f - PLAYER_TEXT_OFFSET_X, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.right,false)

        Assets.sevenFont.draw(batch,":" , 0f, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.center,false)


        Assets.sevenFont.color = Color.WHITE
        Assets.sevenFont.draw(batch, pingScreen.scorePlayer1.toString() , -SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.right,false)
        Assets.sevenFont.draw(batch, pingScreen.scorePlayer2.toString() , SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                0f, Align.left,false)
        if (TimeUtils.nanoTime() * MathUtils.nanoToSec -flashingTime < FLASHING_TIME) {
            Assets.sevenFont.color = Color.GREEN
            if (goalSide==1) {
                Assets.sevenFont.draw(batch, pingScreen.scorePlayer1.toString() , -SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                        0f, Align.right,false)
            } else {
                Assets.sevenFont.draw(batch, pingScreen.scorePlayer2.toString() , SCORE_TEXT_OFFSET, SCREEN_HEIGHT/2f - PLAYER_TEXT_OFFSET_Y,
                        0f, Align.left,false)
            }

        }



        // Player 1 Auto
        if (pingScreen.level.player1.auto)
            batch.color = Color.GREEN
        else
            batch.color = Color.FOREST

        drawTextureRegion(batch,
                Assets.buttonAsset.buttonAuto,
                -SCREEN_WIDTH/2 + BUTTON_AUTO_MARGIN_X,
                - BUTTON_AUTO_HEIGHT - BUTTON_AUTO_OFFSET_Y,
                BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT,
                0f,
                false, false)

        // Player 2 Auto

        if (Network.connection == null) {
            if (pingScreen.level.player2.auto)
                batch.color = Color.GREEN
            else
                batch.color = Color.FOREST

            drawTextureRegion(batch,
                    Assets.buttonAsset.buttonAuto,
                    SCREEN_WIDTH / 2f - BUTTON_AUTO_WIDTH - BUTTON_AUTO_MARGIN_X,
                    -BUTTON_AUTO_HEIGHT - BUTTON_AUTO_OFFSET_Y,
                    BUTTON_AUTO_WIDTH, BUTTON_AUTO_HEIGHT,
                    0f,
                    false, false)
        }

        // Network button

        if (Network.connection == null) {
            batch.color = Color.GREEN

            drawTextureRegion(batch,
                    Assets.buttonAsset.buttonNetwork,
                    SCREEN_WIDTH / 2f - BUTTON_NETWORK_MARGIN_X / 2f - BUTTON_NETWORK_WIDTH,
                    SCREEN_HEIGHT / 2f - BUTTON_NETWORK_MARGIN_Y / 2f - BUTTON_NETWORK_HEIGHT,
                    BUTTON_NETWORK_WIDTH, BUTTON_NETWORK_HEIGHT,
                    0f,
                    false, false)
        }

        // Setting buttons
        batch.color = Color.GREEN

        drawTextureRegion(batch,
                Assets.buttonAsset.buttonSettings,
                SETTINGS_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                -SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (GamePreferences.music) Assets.buttonAsset.buttonMusicOn
                else Assets.buttonAsset.buttonMusicOff,
                MUSIC_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                -SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                if (GamePreferences.sound)Assets.buttonAsset.buttonSoundOn
                else Assets.buttonAsset.buttonSoundOff,
                SOUND_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                -SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,
                Assets.buttonAsset.buttonHelp,
                HELP_OFFSET_X - SETTING_BUTTON_WIDTH/2,
                -SCREEN_HEIGHT/2f + BOTTOM_BUTTONS_OFFSET_Y,
                SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                0f,
                false, false)

        Assets.lineFireworksParticles.draw(batch)
        Assets.circleFireworksParticles.draw(batch)

        if (pingScreen.gameover) {
            Assets.sevenFont.data.setScale(0.4f)
            Assets.sevenFont.color = Color.WHITE //Color.FOREST
            if (pingScreen.scorePlayer1> pingScreen.scorePlayer2)
                Assets.sevenFont.draw(batch, Assets.stringBundle.format("winnerMessage",GamePreferences.player1Name ), 0f, 0f + PLAY_OFFSET_Y,
                    0f, Align.center    , false)
            else {
                Assets.sevenFont.draw(batch, Assets.stringBundle.format("winnerMessage",
                        if (Network.connection != null)
                            pingScreen.opponentName
                        else
                            GamePreferences.player2Name),
                        0f, 0f + PLAY_OFFSET_Y, 0f, Align.center, false)
            }
            val bright = 0.9f + 0.1f * Math.sin(TimeUtils.nanoTime() * MathUtils.nanoToSec * Math.PI * 2f).toFloat()
            batch.color = Color(bright,bright,bright,1f)
            drawTextureRegion(batch,
                    Assets.buttonAsset.buttonPlay,
                    - SETTING_BUTTON_WIDTH/2,
                    - SETTING_BUTTON_HEIGHT/2 - PLAY_OFFSET_Y,
                    SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                    0f,
                    false, false)
        } else if (pingScreen.paused) {
            Assets.sevenFont.data.setScale(0.4f)
            Assets.sevenFont.color = Color.WHITE //Color.FOREST
            Assets.sevenFont.draw(batch, Assets.stringBundle.format("pausedMessage"), 0f, PLAY_OFFSET_Y,
                        0f, Align.center    , false)
            val bright = 0.9f + 0.1f * Math.sin(TimeUtils.nanoTime() * MathUtils.nanoToSec * Math.PI * 2f).toFloat()
            batch.color = Color(bright,bright,bright,1f)
            drawTextureRegion(batch,
                    Assets.buttonAsset.buttonPlay,
                    - SETTING_BUTTON_WIDTH/2,
                    - SETTING_BUTTON_HEIGHT/2 - PLAY_OFFSET_Y,
                    SETTING_BUTTON_WIDTH, SETTING_BUTTON_HEIGHT,
                    0f,
                    false, false)
        }

        batch.end()

/*        shapeRenderer.setProjectionMatrix(viewport.camera.combined)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.setColor(Color.WHITE)
        shapeRenderer.rect(rectAutoPlayer1)
        shapeRenderer.rect(rectAutoPlayer2)
        shapeRenderer.rect(rectSettings)
        shapeRenderer.rect(rectMusic)
        shapeRenderer.rect(rectSound)
        shapeRenderer.rect(rectNetwork)

        shapeRenderer.end()*/


    }

    private fun ShapeRenderer.rect(r : Rectangle) {
        this.rect(r.x, r.y, r.width, r.height)
    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(0f, 0f,0f)
    }

    internal fun dispose() {
//        shapeRenderer.dispose()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val position = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        when {
            rectLeftUp.contains(position) -> {
                pointerPlayerLeftUp = pointer
                pingScreen.level.leftUpPressed = true
            }
            rectRightUp.contains(position) && (Network.connection == null) -> {
                pointerPlayerRightUp = pointer
                pingScreen.level.rightUpPressed = true
            }
            rectLeftDown.contains(position) -> {
                pointerPlayerLeftDown = pointer
                pingScreen.level.leftDownPressed = true
            }
            rectRightDown.contains(position) && (Network.connection == null) -> {
                pointerPlayerRightDown = pointer
                pingScreen.level.rightDownPressed = true
            }
            rectSettings.contains(position) -> {
                if (Network.connection==null) {
                    pingScreen.paused = true
                } else {
                    Network.pause()
                }
                if (pingScreen.hasPriority(pingScreen.configurationDialog)) {
                    pingScreen.closeLessPriorityDialogs(pingScreen.configurationDialog)
                    pingScreen.configurationDialog.openDialog()
                }
            }
            rectMusic.contains(position) -> {
                pingScreen.level.switchMusic()
            }
            rectSound.contains(position) -> {
                pingScreen.level.switchSound()
            }
            rectAutoPlayer1.contains(position) -> {
                pingScreen.level.player1.switchAuto()
            }
            rectAutoPlayer2.contains(position) && (Network.connection == null) -> {
                pingScreen.level.player2.switchAuto()
            }
            rectNetwork.contains(position) && (Network.connection == null) -> {
                pingScreen.paused = true
                if (pingScreen.hasPriority(pingScreen.playerListDialog)) {
                    pingScreen.closeLessPriorityDialogs(pingScreen.playerListDialog)
                    pingScreen.playerListDialog.openDialog()
                }
            }
            rectPlay.contains(position) -> {
                if (pingScreen.gameover) {
                    if (Network.connection==null) {
                        pingScreen.level.initBall()
                        pingScreen.paused = false
                    } else {
                        Network.play()
                    }
                    Assets.lineFireworksParticles.reset()
                    Assets.circleFireworksParticles.reset()
                } else {
                    if (Network.connection==null) {
                        pingScreen.paused = !pingScreen.paused
                    } else {
                        if (pingScreen.paused)
                            Network.resume()
                        else
                            Network.pause()
                    }
                }
            }
            rectScreen.contains(position) -> {
                if (Network.connection==null) {
                    pingScreen.paused = true
                } else {
                    Network.pause()
                }
            }
            rectHelp.contains(position) -> {
                if (Network.connection==null) {
                    pingScreen.paused = true
                } else {
                    Network.pause()
                }
                if (pingScreen.hasPriority(pingScreen.helpDialog)) {
                    pingScreen.closeLessPriorityDialogs(pingScreen.helpDialog)
                    pingScreen.helpDialog.openDialog()
                }
            }

        }
        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val position = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))

        if (pointer == pointerPlayerLeftUp && rectLeftDown.contains(position) ) {
            pointerPlayerLeftDown = pointer
            pointerPlayerLeftUp = 0
            pingScreen.level.leftUpPressed = false
            pingScreen.level.leftDownPressed = true
        } else if (pointer == pointerPlayerLeftDown && rectLeftUp.contains(position) ) {
            pointerPlayerLeftUp = pointer
            pointerPlayerLeftDown = 0
            pingScreen.level.leftDownPressed = false
            pingScreen.level.leftUpPressed = true
        } else if (pointer == pointerPlayerRightUp && rectRightDown.contains(position) ) {
            pointerPlayerRightDown = pointer
            pointerPlayerRightUp = 0
            pingScreen.level.rightUpPressed = false
            pingScreen.level.rightDownPressed = true
        } else if (pointer == pointerPlayerRightDown && rectRightUp.contains(position) ) {
            pointerPlayerRightUp = pointer
            pointerPlayerRightDown = 0
            pingScreen.level.rightDownPressed = false
            pingScreen.level.rightUpPressed = true
        }
        return super.touchDragged(screenX, screenY, pointer)

    }


    fun update(delta: Float) {
        if (!Gdx.input.isTouched(pointerPlayerLeftDown)) {
            pointerPlayerLeftDown = 0
            pingScreen.level.leftDownPressed = false
        }
        if (!Gdx.input.isTouched(pointerPlayerLeftUp)) {
            pointerPlayerLeftUp = 0
            pingScreen.level.leftUpPressed = false
        }
        if (!Gdx.input.isTouched(pointerPlayerRightDown)) {
            pointerPlayerRightDown = 0
            pingScreen.level.rightDownPressed = false
        }
        if (!Gdx.input.isTouched(pointerPlayerRightUp)) {
            pointerPlayerRightUp = 0
            pingScreen.level.rightUpPressed = false
        }

        if (pingScreen.gameover) {
            if (Assets.lineFireworksParticles.isComplete && Assets.circleFireworksParticles.isComplete) {
                firework()
            }
            Assets.lineFireworksParticles.update(delta)
            Assets.circleFireworksParticles.update(delta)

        }

        Assets.smokeParticles.update(delta)
    }

    private fun firework() {
        val vector = Vector2(MathUtils.random(-SCREEN_WIDTH / 4, SCREEN_WIDTH / 4), MathUtils.random(-SCREEN_HEIGHT / 4, SCREEN_HEIGHT / 4))
        Assets.lineFireworksParticles.emitters.first().setPosition(vector.x,vector.y)
        Assets.circleFireworksParticles.emitters.first().setPosition(vector.x,vector.y)
        Assets.lineFireworksParticles.start()
        Assets.circleFireworksParticles.start()
        if (GamePreferences.sound)
            Assets.fireworkSound.play(1f * GamePreferences.volSound)
    }

    internal fun flashScore(side : Int) {
        flashingTime = TimeUtils.nanoTime() * MathUtils.nanoToSec
        goalSide = side
    }
}
