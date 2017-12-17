package es.carlosrolindez.ping.core.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.TimeUtils
import es.carlosrolindez.ping.core.utils.*


class Walls {


    companion object {
        private val TAG = Walls::class.java.name
    }


    enum class Side {
        UP, DOWN
    }

    private var collisionTime = 0L
    private var collisionSide = Side.UP



    fun setCollision(side : Side) {
        collisionTime = TimeUtils.nanoTime()
        collisionSide = side
    }



    fun render(batch : SpriteBatch) {

        batch.color = Color.GREEN

        val time = MathUtils.nanoToSec * (TimeUtils.nanoTime() - collisionTime)
        val regionUp = Assets.instance.paddleAsset.wallHitAnimation.getKeyFrame(
                if (collisionSide == Side.UP) time else 0f)
        val regionDown = Assets.instance.paddleAsset.wallHitAnimation.getKeyFrame(
                if (collisionSide == Side.DOWN) time else 0f)


        drawTextureRegion(batch, regionUp, -SCREEN_WIDTH/2f + BUTTON_WIDTH + BUTTON_MARGIN_X*2, SCREEN_HEIGHT/2f - MARGIN,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH, 0f, false, false)
        drawTextureRegion(batch, regionDown, -SCREEN_WIDTH/2f + BUTTON_WIDTH + BUTTON_MARGIN_X*2, -SCREEN_HEIGHT/2f + MARGIN - WALL_WIDTH,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH, 0f, false, false)
        batch.setColor(1f, 1f, 1f, 1f)

    }

}