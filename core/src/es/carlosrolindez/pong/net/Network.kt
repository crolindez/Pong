package es.carlosrolindez.pong.net

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.EndPoint
import com.esotericsoftware.minlog.Log
import com.esotericsoftware.minlog.Log.LEVEL_NONE
import es.carlosrolindez.pong.PongScreen
import es.carlosrolindez.pong.utils.BALL_INITIAL_VELOCITY_RANGE_MAX_Y
import es.carlosrolindez.pong.utils.BALL_INITIAL_VELOCITY_RANGE_MIN_Y
import es.carlosrolindez.pong.utils.BALL_INITIAL_VELOCITY_X

// This class is a convenient place to keep things common to both the client and server.
object Network {
    init {
        Log.set(LEVEL_NONE)
    }

    internal val TCP_PORT = 54722
    internal val UDP_PORT = 54723
    internal var connection : Connection? = null
    lateinit private var pongScreen : PongScreen

    // This registers objects that are going to be sent over the network.
    internal fun register(endPoint: EndPoint?,screen : PongScreen) {
        pongScreen = screen

        val kryo = endPoint?.kryo
        kryo?.register(Login::class.java)
        kryo?.register(LoginRejected::class.java)
        kryo?.register(LoginAccepted::class.java)

        kryo?.register(Play::class.java)
        kryo?.register(Goal::class.java)
        kryo?.register(NewBall::class.java)
        kryo?.register(Bounce::class.java)
        kryo?.register(PlayerPosition::class.java)

        kryo?.register(Pause::class.java)
        kryo?.register(Resume::class.java)

    }



    //  Connection classes

    internal class Login {
        var clientName = "Player"
    }

    internal class LoginRejected

    internal class LoginAccepted {
        internal var serverName = "Player"
    }

    // Playing classes

    internal class Play {
        var ballVelocityX : Float = BALL_INITIAL_VELOCITY_X
        var ballVelocityY : Float = MathUtils.randomSign() * MathUtils.random(BALL_INITIAL_VELOCITY_RANGE_MIN_Y, BALL_INITIAL_VELOCITY_RANGE_MAX_Y)
    }

    internal class Goal {
        var score : Int = 0
    }

    internal class NewBall {
        var ballVelocityX : Float = BALL_INITIAL_VELOCITY_X
        var ballVelocityY : Float = MathUtils.randomSign() * MathUtils.random(BALL_INITIAL_VELOCITY_RANGE_MIN_Y, BALL_INITIAL_VELOCITY_RANGE_MAX_Y)
    }

    internal class Bounce {
        var ballPreviousPositionX = 0f
        var ballPreviousPositionY = 0f
        var ballPositionX = 0f
        var ballPositionY = 0f
        var ballVelocityX = 0f
        var ballVelocityY = 0f
    }

    internal class PlayerPosition {
        var verticalPosition = 0f
    }

    // Management classes

    internal class Pause {
        var ballPreviousPositionX = 0f
        var ballPreviousPositionY = 0f
        var ballPositionX = 0f
        var ballPositionY = 0f
        var ballVelocityX = 0f
        var ballVelocityY = 0f
    }

    internal class Resume

    // Message Engine

    internal fun play() {
        val message = Play()
        connection?.sendTCP(message)
        Gdx.app.error(NetworkClient.TAG, "Sent Play")
        pongScreen.paused = false
        pongScreen.level.initBall(Vector2(message.ballVelocityX, message.ballVelocityY))
    }

    internal fun newBall() {
        val message = NewBall()
        connection?.sendTCP(message)
        Gdx.app.error(NetworkClient.TAG, "Sent NewBall")
        pongScreen.paused = false
        pongScreen.level.relaunchBall(Vector2(message.ballVelocityX, message.ballVelocityY))
    }

    internal fun goal() {
        val message = Goal()
        message.score = pongScreen.scorePlayer2
        connection?.sendTCP(message)
        Gdx.app.error(NetworkClient.TAG, "Sent Goal")
    }

    internal fun playerPosition () {
        val message = PlayerPosition()
        message.verticalPosition = pongScreen.level.player1.position.y
        connection?.sendTCP(message)
    }


    internal fun bounce(previousPosition : Vector2, position : Vector2, velocity : Vector2) {
        val message= Bounce()
        message.ballPreviousPositionX = previousPosition.x
        message.ballPreviousPositionY = previousPosition.y
        message.ballPositionX = position.x
        message.ballPositionY = position.y
        message.ballVelocityX = velocity.x
        message.ballVelocityY = velocity.y
        connection?.sendTCP(message)
        Gdx.app.error(NetworkClient.TAG, "Sent Bounce")
    }

    internal fun receivedPlayingMessage(genObject: Any) {
        when (genObject) {
            is Network.Play -> {
                val velocity = Vector2(-genObject.ballVelocityX, genObject.ballVelocityY)
                pongScreen.paused = false
                pongScreen.level.initBall(velocity)
                Gdx.app.error(NetworkClient.TAG, "Received Play")
            }
            is Network.NewBall -> {
                val velocity = Vector2(-genObject.ballVelocityX, genObject.ballVelocityY)
                pongScreen.paused = false
                pongScreen.level.relaunchBall(velocity)
                Gdx.app.error(NetworkClient.TAG, "Received NewBall")
            }
            is Network.Goal -> {
                pongScreen.scorePlayer1 = genObject.score
                pongScreen.gui.flashScore(1)
                Gdx.app.error(NetworkClient.TAG, "Received NewBall")
            }
            is Network.Bounce -> {
                pongScreen.level.ball.previousPosition.set(-genObject.ballPreviousPositionX, genObject.ballPreviousPositionY)
                pongScreen.level.ball.position.set(-genObject.ballPositionX, genObject.ballPositionY)
                pongScreen.level.ball.velocity.set(-genObject.ballVelocityX, genObject.ballVelocityY)
                pongScreen.level.player2.setCollision()
                Gdx.app.error(NetworkClient.TAG, "Received NewBall")
            }
            is Network.PlayerPosition -> {
                pongScreen.level.player2.position.y = genObject.verticalPosition
            }
        }
    }

}
