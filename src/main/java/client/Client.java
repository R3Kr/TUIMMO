package client;

import client.states.LoginState;
import client.states.PlayingState;
import com.googlecode.lanterna.TerminalSize;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;


import java.io.IOException;

import java.rmi.NotBoundException;


/**
 * The Client class represents a game client using Lanterna library for terminal-based UI.
 */
public class Client {
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
    public Client(String playerName, String address, boolean emulated) throws IOException, NotBoundException {
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
        Client client;

        if (args.length == 2) {

            client = new Client(args[0].substring(0, 2), args[1], true);
        } else if (args.length == 3) {
            client = new Client(args[0].substring(0, 2), args[1], false);
        } else {
            throw new IllegalArgumentException("Needs 2 arguments, name and ipaddress");
        }

        //client.run();   //temporärt
        client.context.run();
        client.context.shutdown();
        client.screen.stopScreen();
    }
}
