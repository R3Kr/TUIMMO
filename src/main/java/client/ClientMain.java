package client;

import client.states.LoginState;
import client.states.PlayingState;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.googlecode.lanterna.TerminalSize;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import protocol.Hello;
import protocol.KryoFactory;


import java.io.IOException;

import java.rmi.NotBoundException;


/**
 * The Client class represents a game client using Lanterna library for terminal-based UI.
 */
public class ClientMain {
    private Terminal terminal;
    private Screen screen;

    public ClientContext context;

    /**
     * Constructs a new Client instance.
     *
     * @param playerName The name of the player.
     * @param address    The IP address of the server.
     * @throws IOException       If an I/O error occurs.
     * @throws NotBoundException If a binding-related error occurs.
     */
    public ClientMain(String playerName, String address, boolean emulated) throws IOException, NotBoundException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(new TerminalSize(80, 27));
        terminal = terminalFactory.setPreferTerminalEmulator(emulated).createTerminal();
        screen = new TerminalScreen(terminal);

        this.context = new ClientContext(new LoginState(screen), new PlayingState(screen, playerName, address));
    }


    /**
     * The main method to start the game client.
     *
     * @param args Command-line arguments containing player name and server IP address.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     * @throws NotBoundException    If a binding-related error occurs.
     */
    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {
//        Client client;
//
//        if (args.length == 2) {
//
//            client = new Client(args[0].substring(0, 2), args[1], true);
//        } else if (args.length == 3) {
//            client = new Client(args[0].substring(0, 2), args[1], false);
//        } else {
//            throw new IllegalArgumentException("Needs 2 arguments, name and ipaddress");
//        }
//
//        //client.run();   //temporärt
//        client.context.run();
//        client.context.shutdown();
//        client.screen.stopScreen();

        Client client = new Client();
        KryoFactory.register(client.getKryo());

        client.start();
        client.connect(5000, "127.0.0.1", 6969, 6970);

        client.addListener(new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Hello){
                    Hello hello = (Hello) object;

                    hello.test();
                }
            }
        });

        client.sendTCP(new Hello());

        while (true){

        }




    }

}
