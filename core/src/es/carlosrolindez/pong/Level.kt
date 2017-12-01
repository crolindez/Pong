package es.carlosrolindez.pong

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.pong.entities.Ball
import es.carlosrolindez.pong.entities.Paddle
import es.carlosrolindez.pong.entities.Walls
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.*


class Level(private var pongScreen: PongScreen) {

    companion object {
        val TAG: String = Level::class.java.name
    }

    enum class LevelState{ INITIAL_STATE, BEEPS_STATE, PLAYING_STATE, GAME_OVER }

    private val fieldRect = Rectangle( MARGIN - SCREEN_WIDTH/2f,  MARGIN - SCREEN_HEIGHT/2f,
            SCREEN_WIDTH - 2*MARGIN, SCREEN_HEIGHT - 2*MARGIN )
    internal val player1 = Paddle(this, Paddle.Side.LEFT)
    internal val player2 = Paddle(this, Paddle.Side.RIGHT)

    internal val ball = Ball(this)
    internal val walls = Walls()

    private var initialTime  = TimeUtils.nanoTime() * MathUtils.nanoToSec
    private var state: LevelState = LevelState.INITIAL_STATE

    private val viewport: ExtendViewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)

    internal var leftUpPressed: Boolean = false
    internal var rightUpPressed: Boolean = false
    internal var leftDownPressed: Boolean = false
    internal var rightDownPressed: Boolean = false

    init {
        viewport.camera.position.set(0f,0f,0f)
        initBall()
    }

    internal fun initBall() {
        ball.initState()
        player1.initState()
        player2.initState()
        initialTime = TimeUtils.nanoTime()  * MathUtils.nanoToSec
        state = LevelState.INITIAL_STATE
        pongScreen.scorePlayer1 = 0
        pongScreen.scorePlayer2 = 0
    }

    private fun relaunchBall() {
        ball.initState()
        initialTime = TimeUtils.nanoTime()  * MathUtils.nanoToSec
        state = LevelState.PLAYING_STATE
    }

    internal fun update(delta: Float) {

        when (state) {
            LevelState.INITIAL_STATE -> {
                if ( MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= LOADING_TIME) {
                    if (GamePreferences.instance.sound) Assets.instance.startSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
                    state = LevelState.BEEPS_STATE
                }
            }

            LevelState.BEEPS_STATE -> {
                if (MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= INTRO_TIME) {
                    if (GamePreferences.instance.music) {
                        Assets.instance.music.play()
                        Assets.instance.music.isLooping = true
                        Assets.instance.music.volume = MUSIC_VOLUME * GamePreferences.instance.volMusic
                    }

                    state = LevelState.PLAYING_STATE
                }
            }
            LevelState.PLAYING_STATE -> {
                if (MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= REINTRO_TIME) {
                    player1.update(delta, leftUpPressed, leftDownPressed)
                    player2.update(delta, rightUpPressed, rightDownPressed)
                    ball.update(delta)

                    if (ball.checkCollisionWall(fieldRect) or
                            ball.checkCollisionPaddle(player1) or
                            ball.checkCollisionPaddle(player2)) {
                        if (GamePreferences.instance.sound) Assets.instance.hitSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
                    }

                    when (ball.checkGoal()) {
                        1 -> {
                            if (GamePreferences.instance.sound)
                                Assets.instance.goalSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
                            pongScreen.scorePlayer2++; relaunchBall()
                            initialTime  = TimeUtils.nanoTime() * MathUtils.nanoToSec
                            pongScreen.gui.flashScore(2)
                        }
                        2 -> {
                            if (GamePreferences.instance.sound)
                                Assets.instance.goalSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
                            pongScreen.scorePlayer1++; relaunchBall()
                            initialTime  = TimeUtils.nanoTime() * MathUtils.nanoToSec
                            pongScreen.gui.flashScore(1)
                        }
                    }

                    if (pongScreen.gameover) {
                        state = LevelState.GAME_OVER
                    }
                }
            }
            LevelState.GAME_OVER -> {

            }
        }

    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(0f, 0f,0f)
    }

    internal fun render(batch: SpriteBatch) {

        viewport.apply()

        batch.projectionMatrix = viewport.camera.combined

        batch.begin()

        player1.render(batch)
        player2.render(batch)
        ball.render(batch)
        walls.render(batch)
        batch.end()



    }

    internal fun switchSound() {
        setSound(!GamePreferences.instance.sound)
    }

    internal fun setSound(value : Boolean)  {
        GamePreferences.instance.sound = value
        GamePreferences.instance.save()
    }

    internal fun setVolumeSound(value : Float)  {
        GamePreferences.instance.volSound = value
        GamePreferences.instance.save()
    }

    internal fun switchMusic(){
        setMusic(!GamePreferences.instance.music)
    }

    internal fun setMusic(value: Boolean) {
        GamePreferences.instance.music = value
        GamePreferences.instance.save()
        if (GamePreferences.instance.music)
            Assets.instance.music.play()
        else
            Assets.instance.music.pause()

    }

    internal fun setVolumeMusic(value : Float)  {
        GamePreferences.instance.volMusic = value
        Assets.instance.music.volume = MUSIC_VOLUME * GamePreferences.instance.volMusic
        GamePreferences.instance.save()
    }

    internal fun dispose() {
 //       walls.dispose()
    }

}
