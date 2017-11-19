package es.carlosrolindez.pong.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.TimeUtils
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.utils.*


class Paddle(private val level: Level,private val side:Side):AbstractGameObject() {


    companion object {
        private val TAG = Paddle::class.java.name
    }


    enum class Side {
        LEFT, RIGHT
    }

    private var endCollisionTime : Float
    private var collisionTime : Long

    init {

        dimension.set(PADDLE_WIDTH, PADDLE_HEIGHT)
        position.set(when (side) {
                        Side.LEFT -> PADDLE_POSITION_LEFT
                        Side.RIGHT -> PADDLE_POSITION_RIGHT
                    },                PADDLE_INITIAL_POSITION_Y)
        origin.set(PADDLE_WIDTH/2, PADDLE_HEIGHT/2)

        endCollisionTime = 0f
        collisionTime = 0L

    }

    fun setCollision() {
        collisionTime = TimeUtils.nanoTime()
        endCollisionTime = collisionTime * MathUtils.nanoToSec + FLASH_TIME
    }

    fun update(delta: Float,buttonUp: Boolean, buttonDown: Boolean) {


        if (side==Side.LEFT) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) || buttonUp) {
                position.y += PADDLE_VELOCITY * delta
                if (position.y > SCREEN_HEIGHT - PADDLE_HEIGHT / 2 - MARGIN)
                    position.y = SCREEN_HEIGHT - PADDLE_HEIGHT / 2 - MARGIN

            }
            if (Gdx.input.isKeyPressed(Input.Keys.Z) || buttonDown) {
                position.y -= PADDLE_VELOCITY * delta
                if (position.y < PADDLE_HEIGHT / 2 + MARGIN)
                    position.y = PADDLE_HEIGHT / 2 + MARGIN

            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.K) || buttonUp) {
                position.y += PADDLE_VELOCITY * delta
                if (position.y > SCREEN_HEIGHT - PADDLE_HEIGHT / 2 - MARGIN)
                    position.y = SCREEN_HEIGHT - PADDLE_HEIGHT / 2 - MARGIN

            }
            if (Gdx.input.isKeyPressed(Input.Keys.M) || buttonDown) {
                position.y -= PADDLE_VELOCITY * delta
                if (position.y < PADDLE_HEIGHT / 2 + MARGIN)
                    position.y = PADDLE_HEIGHT / 2 + MARGIN

            }
        }

        super.update(delta)
    }

    override fun render(batch: SpriteBatch) {

        batch.color = Color.GREEN
        val region = Assets.instance.paddleAsset.paddleHitAnimation.getKeyFrame(MathUtils.nanoToSec * (TimeUtils.nanoTime() - collisionTime))
        drawTextureRegion(batch, region, position.x - dimension.x / 2, position.y - dimension.y / 2,
                dimension.x, dimension.y, 0f, false, false)
        batch.setColor(1f, 1f, 1f, 1f)
    }


}
