package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import protocol.Hello;
import protocol.KryoFactory;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new Server();

        KryoFactory.register(server.getKryo());
        server.start();
        server.bind(6969, 6970);

        server.addListener(new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Hello){
                    Hello hello = (Hello) object;

                    hello.test();
                    connection.sendTCP(hello);
                }
            }
        });


    }
}
