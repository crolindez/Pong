package es.carlosrolindez.pong.net

import com.badlogic.gdx.Gdx
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Listener.ThreadedListener
import es.carlosrolindez.pong.PongScreen
import es.carlosrolindez.pong.net.Network.receivedPlayingMessage
import es.carlosrolindez.pong.utils.GamePreferences
import java.io.IOException
import java.net.InetAddress
import java.nio.channels.ClosedSelectorException


class NetworkClient(private val pongScreen : PongScreen) {
    companion object {
        val TAG = NetworkClient::class.java.name
    }

    private var clientNet : Client? = null
//    internal var serverAddress: InetAddress? = null


    init {
        clientNet = Client()
        Network.register(clientNet,pongScreen)
    }


    internal fun dispose() {
        try {
            Gdx.app.error(TAG, "dispose")
            clientNet?.stop()
 //           clientNet?.close()
            clientNet?.dispose()
            Gdx.app.error(TAG, "dispose2")
            clientNet = null
            Network.connection = null

        } catch (e: IOException) {
            Gdx.app.error(TAG, "I catch you client dispose")
        }

    }




	internal fun start() {
        Gdx.app.error(TAG, "start")
        clientNet?.start()



        clientNet?.addListener(ThreadedListener(object : Listener() {

            override fun connected(connection: Connection?) {
                Gdx.app.error(TAG, "Connection")
            }

            override fun received(connection: Connection?, genObject: Any?) {

                if (connection == null || genObject == null) {
                    Gdx.app.error(NetworkServer.TAG, "Warning:  received message without connection")
                    return
                }

                //  Connection classes
                if (genObject is Network.LoginRejected) {           // Login Rejected
                    // TODO close waiting windows
                    connection?.close()
                    Network.connection = null
                    Gdx.app.error(TAG, "Server rejected connection")
                    return
                } else if (genObject is Network.LoginAccepted) {    // Login Accepted
                    pongScreen.opponentName = genObject.serverName
                    Network.connection = connection
                    Gdx.app.error(NetworkServer.TAG,"Connection with ${genObject.serverName} accepted")
                    Network.play()

                // Playing classes
                } else receivedPlayingMessage(connection,genObject)

            }

            override fun disconnected(connection: Connection?) {
                Gdx.app.error(TAG, "disconnection")

            }

        }))
    }


    internal fun getServerList() : kotlin.collections.List<InetAddress> {
        val serverList = mutableListOf<InetAddress>()
        val inetAddresses = clientNet?.discoverHosts(Network.UDP_PORT, 1000)

        if (inetAddresses == null || inetAddresses.isEmpty()) {
            Gdx.app.error(TAG, "InetAddress NOT found")
        } else {
            for (serverAddress : InetAddress in inetAddresses) {
                if (!serverList.contains(serverAddress) &&  !serverAddress.isLoopbackAddress) {
                    serverList.add(serverAddress)
                    Gdx.app.error(TAG, serverAddress.canonicalHostName)
                }
            }
        }
        return serverList
    }

    internal fun connect(address : InetAddress) {
        try {
            clientNet?.connect(1000, address, Network.TCP_PORT, Network.UDP_PORT)
            val message = Network.Login()
            message.clientName = GamePreferences.instance.player1Name
            clientNet?.sendTCP(message)
            // TODO Open waiting window

        } catch (e: IOException) {
            e.printStackTrace()
            Gdx.app.error(TAG, "IOException")
  //          dispose()
        }
    }

}