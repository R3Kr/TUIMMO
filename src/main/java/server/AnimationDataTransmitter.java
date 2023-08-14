package server;

import protocol.AnimationData;
import protocol.StateData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AnimationDataTransmitter implements Runnable{

    private BlockingQueue<AnimationData> animationDataBlockingQueue;
    private Clients clients;

    private DatagramSocket socket;
    private DatagramPacket packet;

    public AnimationDataTransmitter(BlockingQueue<AnimationData> animationDataBlockingQueue, Clients clients, DatagramSocket socket) {
        this.animationDataBlockingQueue = animationDataBlockingQueue;
        this.clients = clients;
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024);
    }

    @Override
    public void run() {
        while (true){
            try {
                send(animationDataBlockingQueue.take());
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    private void send(AnimationData data){
        try {
            packet.setData(data.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clients.forEach(c -> {
            packet.setSocketAddress(c);
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.err.println(e);
            }
        });
    }
}
