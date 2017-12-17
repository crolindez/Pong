package es.carlosrolindez.core.net

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import es.carlosrolindez.ping.PingScreen
import java.io.IOException
import java.nio.channels.ClosedSelectorException

class NetworkServer (private val pingScreen: PingScreen) {
    companion object {
        val TAG : String = NetworkServer::class.java.name
    }

    private var serverNet : Server? = null


    internal fun dispose() {
        try {
            serverNet?.stop()
            serverNet = null
        } catch (e: IOException) {
        }catch (e: ClosedSelectorException) {
        }


    }

	internal fun start()  {
        if (serverNet!=null) return

        serverNet = Server()
        Network.register(serverNet, pingScreen)

        serverNet?.addListener(object :  Listener() {
            override fun received(connection: Connection?, genObject: Any?) {
                if (connection == null || genObject == null) {
                    return
                }


                    //  Connection classes
				if (genObject is Network.Login) {

                    if (Network.connection == null) {
                        // TODO open accept windows
                        pingScreen.opponentName = genObject.clientName
                        Network.connection = connection
                        pingScreen.paused = true
                        pingScreen.acceptDialog.openDialog()

                    } else {
                        val message = Network.LoginRejected()
                        connection.sendTCP(message)
                    }

                    return
                // Playing classes
				}  else Network.receivedPlayingMessage(genObject)

			}

            override fun disconnected(c: Connection?) {
                Network.connection = null
                pingScreen.level.initBall()
                pingScreen.paused = true
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
