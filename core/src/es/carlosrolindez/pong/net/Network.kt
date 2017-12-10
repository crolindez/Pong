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
        Log.set(LEVEL_NONE);
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
        var ballPositionX = 0f
        var ballPositionY = 0f
        var ballVelocityX = 0f
        var ballVelocityY = 0f
    }

    internal class PlayerPosition {
        val verticalPosition = 0f
    }

    // Managment classes

    internal class Pause {
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
        Gdx.app.error(NetworkClient.TAG, " Sent Play")
        pongScreen.paused = false
        pongScreen.level.initBall(Vector2(message.ballVelocityX, message.ballVelocityY))
    }

    internal fun receivedPlayingMessage(connection: Connection, genObject: Any) {
        if (genObject is Network.Play) {           // Play
            var velocity = Vector2(-genObject.ballVelocityX, genObject.ballVelocityY)
            pongScreen.paused = false
            pongScreen.level.initBall(velocity)
            Gdx.app.error(NetworkClient.TAG, " Received Play")
        }
    }


}
