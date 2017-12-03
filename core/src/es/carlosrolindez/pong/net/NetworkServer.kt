package es.carlosrolindez.pong.net

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import java.io.IOException

public class NetworkServer  {
	var serverNet : Server? = null
    var started : Boolean = false
    var connected : Boolean = false
    var serverName : String = "es.carlosrolindez.pong.server"
    var clientName : String? = null

    internal fun stop()  {
        started = false
        serverNet?.stop()
        serverNet?.dispose()
        serverNet = null
        connected = false
    }

	internal fun start()  {
        if (started) return

        started = true
		val server = Server()
        serverNet = server

        Network.register(server);

		server.addListener(object :  Listener() {
            override fun received(connection: Connection?, genObject: Any?) {
                if (connection==null) {
                    //TODO warning message
                    return
                }


				if (genObject is Network.Login) {
                    if (connected /* already connected */ || genObject.clientName==null /*invalid name*/) {
                        connection.sendTCP(Network.RegisterNok())
                        return
                    }
					// Ignore if already logged in.

					clientName = genObject.clientName
                    val message = Network.RegisterOk()
                    message.serverName = serverName
                    connection.sendTCP(message)
                    connected = true
                    return
				}

			}

            override fun disconnected(c: Connection?) {
                stop()
            }


		})

		server.start()
        try {
            server.bind(Network.TCP_PORT, Network.UDP_PORT)
        } catch (ex : IOException) {
            this.stop()
        }
	}
}
