package es.carlosrolindez.pong.net

import com.badlogic.gdx.Gdx
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import es.carlosrolindez.pong.PongScreen
import es.carlosrolindez.pong.dialogs.AcceptDialog
import es.carlosrolindez.pong.utils.GamePreferences
import java.io.IOException
import java.nio.channels.ClosedSelectorException

class NetworkServer (private val pongScreen : PongScreen) {
    companion object {
        val TAG : String = NetworkServer::class.java.name
    }

    private var serverNet : Server? = null


    internal fun dispose() {
        try {
            Gdx.app.error(TAG, "Dispose stop")
            serverNet?.stop()
 //           serverNet?.close()
            Gdx.app.error(TAG, "Dispose dispose")
 //           serverNet?.dispose()
            serverNet = null
        } catch (e: IOException) {
            Gdx.app.error(TAG, "I catch you server dispose")
        }catch (e: ClosedSelectorException) {
            Gdx.app.error(TAG, "I catch you server dispose 2")
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
                        // TODO open accept windows
                        pongScreen.opponentName = genObject.clientName
                        Network.connection = connection
                        pongScreen.paused = true
                        pongScreen.acceptDialog.openDialog()

                    } else {
                        val message = Network.LoginRejected()
                        Gdx.app.error(TAG,"Connection with ${genObject.clientName} rejected")
                        connection.sendTCP(message)
                    }

                    return
                // Playing classes
				}  else Network.receivedPlayingMessage(genObject)

			}

            override fun disconnected(c: Connection?) {
                Gdx.app.error(TAG, "Disconnection")
                Network.connection = null
                pongScreen.level.initBall()
                pongScreen.paused = true
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
