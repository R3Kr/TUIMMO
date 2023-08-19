package client;

import client.states.LoginState;
import client.states.PlayingState;
import com.esotericsoftware.kryonet.Client;
import com.googlecode.lanterna.TerminalSize;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import protocol.KryoFactory;


import java.io.IOException;

import java.rmi.NotBoundException;


/**
 * The Client class represents a game client using Lanterna library for terminal-based UI.
 */
public class ClientMain {
    private Terminal terminal;
    private Screen screen;

    private Client client;

    public ClientContext context;

    /**
     * Constructs a new Client instance.
     *
     * @param playerName The name of the player.
     * @param address    The IP address of the server.
     * @throws IOException       If an I/O error occurs.
     * @throws NotBoundException If a binding-related error occurs.
     */
    public ClientMain(String playerName, Client client, String address, boolean emulated) throws IOException, NotBoundException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(new TerminalSize(80, 27));
        terminal = terminalFactory.setPreferTerminalEmulator(emulated).createTerminal();
        screen = new TerminalScreen(terminal);
        this.client = client;

        LoginState loginState = new LoginState(screen, client::sendTCP, playerName);
        this.context = new ClientContext(loginState, new PlayingState(screen, client, loginState::getPlayerName, address));
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
        ClientMain clientMain;
        Client client = new Client();
        KryoFactory.init(client.getKryo());

        client.start();
        client.connect(5000, "127.0.0.1", 6969, 6970);

        if (args.length == 2) {

            clientMain = new ClientMain(args[0].substring(0, 2), client, args[1], true);
        } else if (args.length == 3) {
            clientMain = new ClientMain(args[0].substring(0, 2), client, args[1], false);
        } else {
            throw new IllegalArgumentException("Needs 2 arguments, name and ipaddress");
        }


        clientMain.context.run();
        clientMain.context.shutdown();
        clientMain.screen.stopScreen();
        client.stop();





//        clientMain.addListener(new Listener(){
//            @Override
//            public void received(Connection connection, Object object) {
//                if (object instanceof Hello){
//                    Hello hello = (Hello) object;
//
//                    hello.run();
//
//                }
//            }
//        });
//
//        clientMain.sendTCP(new Hello());
//
//        while (true){
//
//        }




    }

}
