package es.carlosrolindez.pong.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.TimeUtils
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.net.Network
import es.carlosrolindez.pong.utils.*


class Paddle(private val level: Level,private val side:Side):AbstractGameObject() {

    companion object {
        private val TAG = Paddle::class.java.name
    }

    enum class Side {
        LEFT, RIGHT
    }

    private var collisionTime : Float
    internal var auto : Boolean = false
        private set


    init {

        dimension.set(PADDLE_WIDTH, PADDLE_HEIGHT)
        initState()
        origin.set(PADDLE_WIDTH/2, PADDLE_HEIGHT/2)

        collisionTime = 0f

    }

    fun initState() {
        position.set(when (side) {
            Side.LEFT -> PADDLE_POSITION_LEFT
            Side.RIGHT -> PADDLE_POSITION_RIGHT
        },                PADDLE_INITIAL_POSITION_Y)
    }

    fun setCollision() {
        collisionTime = TimeUtils.nanoTime() * MathUtils.nanoToSec
    }

    fun switchAuto() {
        auto = !auto
    }

    fun update(delta: Float,buttonUp: Boolean, buttonDown: Boolean) {

        if (side==Side.LEFT) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) || buttonUp || (auto && position.y < level.ball.position.y)) {
                position.y += PADDLE_VELOCITY * delta
                if (position.y > SCREEN_HEIGHT/2f - PADDLE_HEIGHT / 2f - MARGIN)
                    position.y = SCREEN_HEIGHT/2f - PADDLE_HEIGHT / 2f - MARGIN

                if (Network.connection != null)
                    Network.playerPosition()
            }
            if (Gdx.input.isKeyPressed(Input.Keys.Z) || buttonDown || (auto && position.y > level.ball.position.y)) {
                position.y -= PADDLE_VELOCITY * delta
                if (position.y < -SCREEN_HEIGHT/2f + PADDLE_HEIGHT / 2f + MARGIN)
                    position.y = -SCREEN_HEIGHT/2f + PADDLE_HEIGHT / 2f + MARGIN

                if (Network.connection != null)
                    Network.playerPosition()
            }
        } else if (Network.connection == null){
            if (Gdx.input.isKeyPressed(Input.Keys.K) || buttonUp || (auto && position.y < level.ball.position.y)) {
                position.y += PADDLE_VELOCITY * delta
                if (position.y > SCREEN_HEIGHT/2f - PADDLE_HEIGHT / 2f - MARGIN)
                    position.y = SCREEN_HEIGHT/2f - PADDLE_HEIGHT / 2f - MARGIN

            }
            if (Gdx.input.isKeyPressed(Input.Keys.M) || buttonDown || (auto && position.y > level.ball.position.y)) {
                position.y -= PADDLE_VELOCITY * delta
                if (position.y < -SCREEN_HEIGHT/2f + PADDLE_HEIGHT / 2f + MARGIN)
                    position.y = -SCREEN_HEIGHT/2f + PADDLE_HEIGHT / 2f + MARGIN

            }
        }

        super.update(delta)
    }

    override fun render(batch: SpriteBatch) {

        batch.color = Color.GREEN
        val region = Assets.instance.paddleAsset.paddleHitAnimation.getKeyFrame(MathUtils.nanoToSec * TimeUtils.nanoTime() - collisionTime)
        drawTextureRegion(batch, region, position.x - dimension.x / 2, position.y - dimension.y / 2,
                dimension.x, dimension.y, 0f, false, false)
        batch.setColor(1f, 1f, 1f, 1f)
    }


}
