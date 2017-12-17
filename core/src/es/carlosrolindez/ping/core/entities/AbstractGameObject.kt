package es.carlosrolindez.ping.core.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2


abstract class AbstractGameObject {

    var dimension = Vector2()
    var origin = Vector2()

    var position = Vector2()
    var previousPosition = Vector2()
    var velocity = Vector2()


    open fun update(delta: Float) {
        previousPosition.set(position)
        position.mulAdd(velocity, delta)
    }

    abstract fun render(batch: SpriteBatch)
}
