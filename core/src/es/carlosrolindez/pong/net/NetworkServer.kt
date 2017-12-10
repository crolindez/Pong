package es.carlosrolindez.pong.net

import com.badlogic.gdx.Gdx
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import es.carlosrolindez.pong.PongScreen
import es.carlosrolindez.pong.utils.GamePreferences
import java.io.IOException

class NetworkServer (private val pongScreen : PongScreen) {
    companion object {
        val TAG : String = NetworkServer::class.java.name
    }

    private var serverNet : Server? = null


    internal fun dispose() {
        try {
            serverNet?.stop()
 //           serverNet?.close()
            serverNet?.dispose()
            serverNet = null
        } catch (e: IOException) {
            Gdx.app.error(TAG, "I catch you server dispose")
        }
    }

	internal fun start()  {
        if (serverNet!=null) return

        serverNet = Server()
        Network.register(serverNet,pongScreen)

        serverNet?.addListener(object :  Listener() {
            override fun received(connection: Connection?, genObject: Any?) {
                if (connection == null || genObject == null) {
                    Gdx.app.error(TAG, "Warning:  received message without connection")
                    return
                }


                    //  Connection classes
				if (genObject is Network.Login) {

                    if (Network.connection == null) {
                        pongScreen.opponentName = genObject.clientName
                        Network.connection = connection
                        val message = Network.LoginAccepted()
                        message.serverName = GamePreferences.instance.player1Name
                        Gdx.app.error(TAG,"Connection with ${genObject.clientName} accepted")
                        connection.sendTCP(message)
                    } else {
                        // TODO open accept windows
                        val message = Network.LoginRejected()
                        Gdx.app.error(TAG,"Connection with ${genObject.clientName} rejected")
                        connection.sendTCP(message)
                        // TODO close accept windows
                    }

                    return
                // Playing classes
				}  else Network.receivedPlayingMessage(genObject)

			}

            override fun disconnected(c: Connection?) {
                Gdx.app.error(NetworkClient.TAG, "Disconnection")
    //            dispose()
            }


		})

        serverNet?.start()
        try {
            serverNet?.bind(Network.TCP_PORT, Network.UDP_PORT)
        } catch (ex : IOException) {
   //         dispose()
        }
	}
}
