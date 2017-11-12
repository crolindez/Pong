package es.carlosrolindez.pong

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.pong.entities.Ball
import es.carlosrolindez.pong.entities.Paddle
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.Assets
import es.carlosrolindez.pong.utils.*



class Level(var pongScreen: PongScreen) {

    companion object {
        val TAG = Level::class.java.name
    }

    private val player1 = Paddle(this, Paddle.Side.LEFT)
    private val player2 = Paddle(this, Paddle.Side.RIGHT)
    private val ball = Ball(this)
    private val fieldRect = Rectangle( MARGIN,  MARGIN,
            SCREEN_WIDTH - 2*MARGIN, SCREEN_HEIGHT - 2*MARGIN )
    private val introTime: Long

    private val viewport: ExtendViewport
    private var renderer : ShapeRenderer


    internal var leftUpPressed: Boolean = false
    internal var rightUpPressed: Boolean = false
    internal var leftDownPressed: Boolean = false
    internal var rightDownPressed: Boolean = false


    init {
        viewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)

        introTime = TimeUtils.nanoTime()
        Assets.instance.startSound.play()

        renderer = ShapeRenderer()
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
    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
    }

    internal fun render(batch: SpriteBatch) {

        viewport.apply()

        batch.setProjectionMatrix(viewport.camera.combined)

        batch.begin()

        player1.render(batch)
        player2.render(batch)
        ball.render(batch)

//        batch.setColor(0f, 1f, 0f, 1f)
//        Assets.instance.sevenFont.getData().setScale(0.4f)
//        Assets.instance.sevenFont.draw(batch,"PILAR" , SCREEN_WIDTH/2,viewport.getWorldHeight(),0f, Align.center,false);
//        Assets.instance.sevenFont.getData().setScale(1f)
//        batch.setColor(1f, 1f, 1f, 1f)
        batch.end()

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.GRAY);
        renderer.rectLine(MARGIN, MARGIN-0.5f, SCREEN_WIDTH - MARGIN, MARGIN-0.5f,1f);
        renderer.rectLine(MARGIN, SCREEN_HEIGHT - MARGIN + 0.5f, SCREEN_WIDTH - MARGIN, SCREEN_HEIGHT - MARGIN + 0.5f, 1f);
        renderer.end();
    }

    internal fun dispose() {
        renderer.dispose()
    }

}
