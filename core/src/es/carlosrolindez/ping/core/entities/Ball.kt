package es.carlosrolindez.core.entities



import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils


class Ball(private val level: es.carlosrolindez.core.Level): AbstractGameObject() {


    companion object {
        private val TAG = Ball::class.java.name
    }

    private var collisionTime : Float
    private var initialTime : Float

    init {

        dimension.set(BALL_WIDTH, BALL_HEIGHT)
        position.set( BALL_INITIAL_POSITION_X, BALL_INITIAL_POSITION_Y)
        origin.set(BALL_WIDTH /2, BALL_HEIGHT /2)
        velocity.set(BALL_INITIAL_VELOCITY_X, BALL_INITIAL_VELOCITY_RANGE_MIN_Y)
        collisionTime = 0f
        initialTime = 0f

    }

    fun initState(velocity : Vector2 ) {
        position.set(BALL_INITIAL_POSITION_X, BALL_INITIAL_POSITION_Y)
        previousPosition.set(BALL_INITIAL_POSITION_X, BALL_INITIAL_POSITION_Y)
        this.velocity = velocity
        initialTime = TimeUtils.nanoTime() * MathUtils.nanoToSec
    }

    private fun setCollision() {
        collisionTime = TimeUtils.nanoTime() * MathUtils.nanoToSec
    }

    override fun render(batch: SpriteBatch) {

        batch.color = Color.GREEN
        val region = Assets.instance.paddleAsset.ballHitAnimation.getKeyFrame(MathUtils.nanoToSec * TimeUtils.nanoTime() - collisionTime)
        drawTextureRegion(batch, region, position.x - dimension.x/2, position.y-dimension.y/2,
                dimension.x, dimension.y, 0f, false, false)

        batch.setColor(1f, 1f, 1f, 1f)

    }

    fun checkGoal() :Int {
        if (position.x < BALL_WIDTH/2f + BUTTON_WIDTH + BUTTON_MARGIN_X*2f - SCREEN_WIDTH/2f) return 1
        if (position.x > SCREEN_WIDTH/2f - BALL_WIDTH/2f - BUTTON_WIDTH - BUTTON_MARGIN_X*2f) return 2
        return 0
    }

    fun checkCollisionWall(fieldRect : Rectangle) : Boolean {
        val collision =
                checkCollisionWallDown (fieldRect.y + BALL_HEIGHT/2) or
                checkCollisionWallUp   (fieldRect.y + fieldRect.height - BALL_HEIGHT/2)
        if (collision)
            setCollision()
        return collision
    }


    private fun checkCollisionWallDown(y : Float) : Boolean {
        if (position.y<y && previousPosition.y>=y) { // collision down
            position.y = 2*y - position.y
            previousPosition.y = 2*y - previousPosition.y
            velocity.y *= -1
            level.walls.setCollision(Walls.Side.DOWN)
            return true
        }
        return false
    }

    private fun checkCollisionWallUp(y : Float) : Boolean {
        if (position.y>y && previousPosition.y<=y) { // collision up
            position.y = 2*y - position.y
            previousPosition.y = 2*y - previousPosition.y
            velocity.y *= -1
            level.walls.setCollision(Walls.Side.UP)
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
            val deltaTime = (TimeUtils.nanoTime() * MathUtils.nanoToSec - initialTime)*1f
            val delta = MathUtils.random(-deltaTime, deltaTime)
            velocity.y += delta
            return true
        }
        return false
    }

    private fun checkCollisionRight(x : Float, yMin : Float, yMax : Float) : Boolean {
        if (position.y in yMin..yMax && position.x>x && previousPosition.x<=x) { // collision paddle left
            position.x = 2*x - position.x
            previousPosition.x = 2*x - previousPosition.x
            velocity.x *= -1
            val deltaTime = (TimeUtils.nanoTime() * MathUtils.nanoToSec - initialTime)*1f
            val delta = MathUtils.random(-deltaTime, deltaTime)
            velocity.y += delta
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