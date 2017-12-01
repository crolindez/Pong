package es.carlosrolindez.pong.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.utils.*


class Walls(){


    companion object {
        private val TAG = Walls::class.java.name
    }


    enum class Side {
        UP, DOWN
    }

    private var collisionTime = 0f
    private var collisionSide = Side.UP



    fun setCollision(side : Side) {
        collisionTime = TimeUtils.nanoTime() * MathUtils.nanoToSec
        collisionSide = side
    }



    fun render(batch : SpriteBatch) {

        batch.color = Color.GREEN

        val regionUp = Assets.instance.paddleAsset.wallHitAnimation.getKeyFrame(
                if (collisionSide == Side.UP) (MathUtils.nanoToSec * TimeUtils.nanoTime() - collisionTime) else 0f)
        val regionDown = Assets.instance.paddleAsset.wallHitAnimation.getKeyFrame(
                if (collisionSide == Side.DOWN) (MathUtils.nanoToSec * TimeUtils.nanoTime() - collisionTime) else 0f)


        drawTextureRegion(batch, regionUp, -SCREEN_WIDTH/2f + BUTTON_WIDTH + BUTTON_MARGIN_X*2, SCREEN_HEIGHT/2f - MARGIN,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH, 0f, false, false)
        drawTextureRegion(batch, regionDown, -SCREEN_WIDTH/2f + BUTTON_WIDTH + BUTTON_MARGIN_X*2, -SCREEN_HEIGHT/2f + MARGIN - WALL_WIDTH,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH, 0f, false, false)
        batch.setColor(1f, 1f, 1f, 1f)

    }

}