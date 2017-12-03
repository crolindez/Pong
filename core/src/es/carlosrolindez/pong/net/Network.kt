package es.carlosrolindez.pong.net

import com.esotericsoftware.kryonet.EndPoint
import com.esotericsoftware.minlog.Log
import com.esotericsoftware.minlog.Log.LEVEL_NONE

// This class is a convenient place to keep things common to both the client and server.
object Network {
    init {
        Log.set(LEVEL_NONE);
    }

    internal val TCP_PORT = 54722
    internal val UDP_PORT = 54723

    // This registers objects that are going to be sent over the network.
    internal fun register(endPoint: EndPoint?) {
        val kryo = endPoint?.kryo
        kryo?.register(Login::class.java)
        kryo?.register(RegisterOk::class.java)
        kryo?.register(RegisterNok::class.java)
  /*      kryo.register(RegistrationRequired::class.java)
        kryo.register(Register::class.java)
        kryo.register(AddCharacter::class.java)
        kryo.register(UpdateCharacter::class.java)
        kryo.register(RemoveCharacter::class.java)
        kryo.register(Char::class.java)
        kryo.register(MoveCharacter::class.java)*/
    }

    class Login {
        internal var clientName: String? = null
    }

    class RegisterOk {
        internal var serverName: String? = null
    }

    class RegisterNok
}
