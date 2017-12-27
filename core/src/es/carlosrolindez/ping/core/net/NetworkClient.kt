package es.carlosrolindez.ping.core.net

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Listener.ThreadedListener
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.net.Network.receivedPlayingMessage
import es.carlosrolindez.ping.core.utils.GamePreferences
import java.io.IOException
import java.net.InetAddress
import java.nio.channels.ClosedSelectorException


class NetworkClient(private val pingScreen: PingScreen) {
    companion object {
        val TAG : String = NetworkClient::class.java.name
    }

    private var clientNet : Client? = null
//    internal var serverAddress: InetAddress? = null


    init {
        clientNet = Client()
        Network.register(clientNet, pingScreen)
    }


    internal fun dispose() {
       try {
            clientNet?.stop()
            clientNet = null
            Network.connection = null

        } catch (e: IOException) {
        }
        catch (e: ClosedSelectorException) {
        }

    }




	internal fun start() {
        clientNet?.start()



        clientNet?.addListener(ThreadedListener(object : Listener() {

            override fun connected(connection: Connection?) {
            }

            override fun received(connection: Connection?, genObject: Any?) {

                if (connection == null || genObject == null) {
                    return
                }

                //  Connection classes
                when (genObject) {
                    is Network.LoginRejected -> {           // Login Rejected
 //                       Gdx.app.error(TAG,"${genObject::class.java.name}: ${genObject.stampTime - TimeUtils.nanoTime()}")
                        pingScreen.connectionMessageDialog.closeDialog()
                        connection.close()
                        Network.connection = null
                        return
                    }
                    is Network.LoginAccepted -> {    // Login Accepted
 //                       Gdx.app.error(TAG,"${genObject::class.java.name}: ${genObject.stampTime - TimeUtils.nanoTime()}")
                        pingScreen.connectionMessageDialog.closeDialog()
                        pingScreen.opponentName = genObject.serverName
                        Network.connection = connection
                        Network.play()

                        // Playing classes
                    }
                    else -> receivedPlayingMessage(genObject)
                }

            }

            override fun disconnected(connection: Connection?) {
                Network.connection = null
                pingScreen.level.initBall()
                pingScreen.paused = true
            }

        }))
    }


    internal fun getServerList() : kotlin.collections.List<InetAddress> {
        val serverList = mutableListOf<InetAddress>()
        val inetAddresses = clientNet?.discoverHosts(Network.UDP_PORT, 5000)

        if (inetAddresses == null || inetAddresses.isEmpty()) {
        } else {
            for (serverAddress : InetAddress in inetAddresses) {
                if (!serverList.contains(serverAddress) &&  !serverAddress.isLoopbackAddress) {
                    serverList.add(serverAddress)
                }
            }
        }
        return serverList
    }

    internal fun connect(address : InetAddress) {
        try {
            clientNet?.connect(1000, address, Network.TCP_PORT, Network.UDP_PORT)
            val message = Network.Login()
            message.address = address.toString()
            message.clientName = GamePreferences.instance.player1Name
            clientNet?.sendTCP(message)
        } catch (e: IOException) {
            e.printStackTrace()
  //          dispose()
        }
    }

}