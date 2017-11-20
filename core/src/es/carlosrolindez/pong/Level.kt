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


class Level(var pongScreen: PongScreen) {

    companion object {
        val TAG: String = Level::class.java.name
    }

    enum class LevelState{ INITIAL_STATE, BEEPS_STATE, PLAYING_STATE }

    private val fieldRect = Rectangle( MARGIN,  MARGIN,
            SCREEN_WIDTH - 2*MARGIN, SCREEN_HEIGHT - 2*MARGIN )
    private val player1 = Paddle(this, Paddle.Side.LEFT)
    private val player2 = Paddle(this, Paddle.Side.RIGHT)

    internal var musicState = true
    internal var soundState = true

    internal val ball = Ball(this)
    internal val walls = Walls(this)

    private var initialTime  = TimeUtils.nanoTime() * MathUtils.nanoToSec
    private var state: LevelState = LevelState.INITIAL_STATE

    private val viewport: ExtendViewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)


    internal var leftUpPressed: Boolean = false
    internal var rightUpPressed: Boolean = false
    internal var leftDownPressed: Boolean = false
    internal var rightDownPressed: Boolean = false


    init {
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
        initBall()
    }

    private fun initBall() {
        ball.initState()
        initialTime = TimeUtils.nanoTime()  * MathUtils.nanoToSec // - (REINTRO_TIME / MathUtils.nanoToSec).toLong()
        state = LevelState.INITIAL_STATE
    }

    private fun relaunchBall() {
        initBall()
        state = LevelState.PLAYING_STATE
    }

    internal fun update(delta: Float) {

        when (state) {
            LevelState.INITIAL_STATE -> {
                if ( MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= LOADING_TIME) {
                    if (soundState) Assets.instance.startSound.play(0.5f)
                    state = LevelState.BEEPS_STATE
                }
            }

            LevelState.BEEPS_STATE -> {
                if (MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= INTRO_TIME) {
                    if (musicState) {
                        Assets.instance.music.play()
                        Assets.instance.music.isLooping = true
                        Assets.instance.music.volume = 0.2f
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
                        if (soundState) Assets.instance.hitSound.play(0.2f)
                    }

                    when (ball.checkGoal()) {
                        1 -> {
                            pongScreen.scorePlayer2++; relaunchBall()
                        }
                        2 -> {
                            pongScreen.scorePlayer1++; relaunchBall()
                        }
                    }
                }
            }
        }

    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
        walls.resize(width,height)

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
        soundState = !soundState
    }

    internal fun switchMusic() {
        musicState = !musicState
        if (musicState)
            Assets.instance.music.play()
        else
            Assets.instance.music.pause()

    }

    internal fun dispose() {
 //       walls.dispose()
    }

}
