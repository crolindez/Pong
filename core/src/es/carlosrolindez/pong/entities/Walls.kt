package es.carlosrolindez.pong.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.utils.*


class Walls(private val level: Level){


    companion object {
        private val TAG = Walls::class.java.name
    }


    enum class Side {
        UP, DOWN
    }

    private var collisionTime = 0f
    private var collisionSide = Side.UP
//    private var renderer = ShapeRenderer()
    private var viewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)

    init {
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
    }


    fun setCollision(side : Side) {
        collisionTime = TimeUtils.nanoTime() * MathUtils.nanoToSec
        collisionSide = side
    }



    fun render(batch : SpriteBatch) {

        batch.color = Color.GREEN
        val region = Assets.instance.paddleAsset.wallHitAnimation.getKeyFrame(MathUtils.nanoToSec * TimeUtils.nanoTime() - collisionTime)
        drawTextureRegion(batch, region, BUTTON_WIDTH + BUTTON_MARGIN_X*2, SCREEN_HEIGHT - MARGIN,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH, 0f, false, false)
        drawTextureRegion(batch, region, BUTTON_WIDTH + BUTTON_MARGIN_X*2, MARGIN - WALL_WIDTH,
                SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH, 0f, false, false)
        batch.setColor(1f, 1f, 1f, 1f)

/*
        var colorUp = Color.FOREST
        var colorDown = Color.FOREST

        if (endCollisionTime > TimeUtils.nanoTime() * MathUtils.nanoToSec) {
            when (collisionSide) {
                Side.UP -> colorUp = Color.GREEN
                Side.DOWN -> colorDown = Color.GREEN
            }
        }



        renderer.color=colorUp
        renderer.rect(BUTTON_WIDTH + BUTTON_MARGIN_X*2 , SCREEN_HEIGHT - MARGIN,SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH)
        renderer.color=colorDown
        renderer.rect(BUTTON_WIDTH + BUTTON_MARGIN_X*2 , MARGIN - WALL_WIDTH,SCREEN_WIDTH - 2* BUTTON_WIDTH - 4 * BUTTON_MARGIN_X, WALL_WIDTH)
        renderer.color=Color.BLACK

        renderer.end()*/

    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)

    }

 //   fun dispose() {
  //      renderer.dispose()

  //  }





}