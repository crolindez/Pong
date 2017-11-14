package es.carlosrolindez.pong.entities


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.TimeUtils
import com.sun.org.apache.xpath.internal.operations.Bool
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.utils.*

class Ball(private val level: Level): AbstractGameObject() {


    companion object {
        private val TAG = Ball::class.java.name
    }

    private var endCollisionTime : Float

    init {

        dimension.set(BALL_WIDTH, BALL_HEIGHT)
        position.set( BALL_INITIAL_POSITION_X, BALL_INITIAL_POSITION_Y)
        origin.set(BALL_WIDTH /2, BALL_HEIGHT /2)
        velocity.set(50f,20f)
        endCollisionTime = 0f

    }

    private fun setCollision() {
        endCollisionTime = TimeUtils.nanoTime() * MathUtils.nanoToSec + FLASH_TIME
    }

    override fun render(batch: SpriteBatch) {
        val patch : NinePatch = when (endCollisionTime < TimeUtils.nanoTime() * MathUtils.nanoToSec) {
            true  -> Assets.instance.paddleAsset.ball
            false -> Assets.instance.paddleAsset.ball_hit
        }

        batch.color = Color.GREEN
        patch.draw(batch,position.x - dimension.x/2,position.y-dimension.y/2, dimension.x, dimension.y)
        batch.setColor(1f,1f,1f,1f)

    }

    fun checkGoal(fieldRect: Rectangle) :Boolean {
        if (position.x < fieldRect.x + BALL_WIDTH) return true
        if (position.x > fieldRect.x + fieldRect.width - BALL_WIDTH) return true
        return false
    }

    fun checkCollisionWall(fieldRect : Rectangle) : Boolean {
        val collision = /*checkCollisionWallLeft (fieldRect.x + BALL_WIDTH/2) or
                checkCollisionWallRight(fieldRect.x + fieldRect.width - BALL_WIDTH/2) or*/
                checkCollisionWallDown (fieldRect.y + BALL_HEIGHT/2) or
                checkCollisionWallUp   (fieldRect.y + fieldRect.height - BALL_HEIGHT/2)
        if (collision) setCollision()
        return collision
    }

    private fun checkCollisionWallLeft(x : Float) : Boolean {
        if (position.x<x && previousPosition.x>=x) { // collision left
            position.x = 2*x - position.x
            previousPosition.x = 2*x - previousPosition.x
            velocity.x *= -1
            return true
        }
        return false
    }

    private fun checkCollisionWallRight(x : Float) : Boolean {
        if (position.x>x && previousPosition.x<=x) { // collision right
            position.x = 2*x - position.x
            previousPosition.x = 2*x - previousPosition.x
            velocity.x *= -1
            return true
        }
        return false
    }

    private fun checkCollisionWallDown(y : Float) : Boolean {
        if (position.y<y && previousPosition.y>=y) { // collision down
            position.y = 2*y - position.y
            previousPosition.y = 2*y - previousPosition.y
            velocity.y *= -1
            return true
        }
        return false
    }

    private fun checkCollisionWallUp(y : Float) : Boolean {
        if (position.y>y && previousPosition.y<=y) { // collision up
            position.y = 2*y - position.y
            previousPosition.y = 2*y - previousPosition.y
            velocity.y *= -1
            return true
        }
        return false
    }

    fun checkCollisionPaddle(paddle : Paddle) : Boolean {
        val collision = checkCollisionLeft (paddle.position.x + PADDLE_WIDTH/2 + BALL_WIDTH/2,  paddle.position.y - PADDLE_HEIGHT/2 - BALL_HEIGHT/2,paddle.position.y + PADDLE_HEIGHT/2 + BALL_HEIGHT/2 ) or
                checkCollisionRight(paddle.position.x - PADDLE_WIDTH/2 - BALL_WIDTH/2,  paddle.position.y - PADDLE_HEIGHT/2 - BALL_HEIGHT/2,paddle.position.y + PADDLE_HEIGHT/2 + BALL_HEIGHT/2 ) or
                checkCollisionDown (paddle.position.y + PADDLE_HEIGHT/2 + BALL_HEIGHT/2,paddle.position.x - PADDLE_WIDTH/2 - BALL_WIDTH/2,  paddle.position.x + PADDLE_WIDTH/2 + BALL_WIDTH/2   ) or
                checkCollisionUp   (paddle.position.y - PADDLE_HEIGHT/2 - BALL_HEIGHT/2,paddle.position.x - PADDLE_WIDTH/2 - BALL_WIDTH/2,  paddle.position.x + PADDLE_WIDTH/2 + BALL_WIDTH/2   )
        if (collision) {
            setCollision()
            paddle.setCollision()
        }
        return collision
    }

    private fun checkCollisionLeft(x : Float, yMin : Float, yMax : Float) : Boolean {
        if (position.y in yMin..yMax && position.x<x && previousPosition.x>=x) { // collision paddle left
            position.x = 2*x - position.x
            previousPosition.x = 2*x - previousPosition.x
            velocity.x *= -1
            return true
        }
        return false
    }

    private fun checkCollisionRight(x : Float, yMin : Float, yMax : Float) : Boolean {
        if (position.y in yMin..yMax && position.x>x && previousPosition.x<=x) { // collision paddle left
            position.x = 2*x - position.x
            previousPosition.x = 2*x - previousPosition.x
            velocity.x *= -1
            return true
        }
        return false
    }

    private fun checkCollisionDown(y : Float, xMin : Float, xMax : Float) : Boolean {
        if (position.x in xMin..xMax && position.y<y && previousPosition.y>=y) { // collision paddle down
            position.y = 2*y - position.y
            previousPosition.y = 2*y - previousPosition.y
            velocity.y *= -1
            return true
        }
        return false
    }

    private fun checkCollisionUp(y : Float, xMin : Float, xMax : Float) : Boolean {
        if (position.x in xMin..xMax && position.y>y && previousPosition.y<=y) { // collision paddle up
            position.y = 2*y - position.y
            previousPosition.y = 2*y - previousPosition.y
            velocity.y *= -1
            return true
        }
        return false
    }


}