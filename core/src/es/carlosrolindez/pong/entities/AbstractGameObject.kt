package es.carlosrolindez.pong.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

/**
 * Created by Carlos on 20/09/2017.
 */

abstract class AbstractGameObject {

    var dimension = Vector2()
    var origin = Vector2()
    var scale = Vector2(1f,1f)
    var rotation = 0

    var position = Vector2()
    var previousPosition = Vector2()
    var velocity = Vector2()


    open fun update(delta: Float) {
        previousPosition.set(position)
        position.mulAdd(velocity, delta)
    }

    abstract fun render(batch: SpriteBatch)
}
