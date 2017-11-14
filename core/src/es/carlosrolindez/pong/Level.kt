package es.carlosrolindez.pong

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.MathUtils.randomSign
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.pong.entities.Ball
import es.carlosrolindez.pong.entities.Paddle
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.*


class Level(var pongScreen: PongScreen) {

    companion object {
        val TAG: String = Level::class.java.name
    }

    private val player1 = Paddle(this, Paddle.Side.LEFT)
    private val player2 = Paddle(this, Paddle.Side.RIGHT)
    internal val ball = Ball(this)
    private val fieldRect = Rectangle( MARGIN,  MARGIN,
            SCREEN_WIDTH - 2*MARGIN, SCREEN_HEIGHT - 2*MARGIN )
    private var introTime: Long

    private val viewport: ExtendViewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)


    internal var leftUpPressed: Boolean = false
    internal var rightUpPressed: Boolean = false
    internal var leftDownPressed: Boolean = false
    internal var rightDownPressed: Boolean = false


    init {
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)


        Assets.instance.startSound.play()
        initBall()
        introTime = TimeUtils.nanoTime()
    }

    private fun initBall() {
        ball.position.set(SCREEN_WIDTH /2, SCREEN_HEIGHT /2)
        ball.velocity.set(randomSign()*50f,randomSign()*random(10f,30f))
        introTime = TimeUtils.nanoTime() - (REINTRO_TIME / MathUtils.nanoToSec).toLong()
    }

    internal fun update(delta: Float) {
        if (MathUtils.nanoToSec * (TimeUtils.nanoTime() - introTime)  < INTRO_TIME) return
        if (!Assets.instance.music.isPlaying) {
            Assets.instance.music.play()
            Assets.instance.music.isLooping = true
            Assets.instance.music.volume = 1f
        }

        player1.update(delta,leftUpPressed,leftDownPressed)
        player2.update(delta,rightUpPressed,rightDownPressed)
        ball.update(delta)


        if (ball.checkCollisionWall(fieldRect) or
                ball.checkCollisionPaddle(player1) or
                ball.checkCollisionPaddle(player2))
            Assets.instance.hitSound.play()

        if (ball.checkGoal(fieldRect)) initBall()
    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
    }

    internal fun render(batch: SpriteBatch) {

        viewport.apply()

        batch.projectionMatrix = viewport.camera.combined

        batch.begin()

        player1.render(batch)
        player2.render(batch)
        ball.render(batch)

        batch.end()


    }

    internal fun dispose() {

    }

}
