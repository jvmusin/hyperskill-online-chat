package chat.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Client {
    public static void main(String[] args) throws Exception {
        String address = "127.0.0.1";
        int port = 23456;
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Client started!");

            CountDownLatch latch = new CountDownLatch(1);

            new Thread(() -> {
                while (true) {
                    try {
                        String message = br.readLine();
                        if (message == null || message.equals("/exit")) {
                            latch.countDown();
                            break;
                        } else {
                            output.writeUTF(message);
                        }
                    } catch (IOException e) {
                        latch.countDown();
                        break;
                    }
                }
            }).start();
            new Thread(() -> {
                while (true) {
                    try {
                        System.out.println(input.readUTF());
                    } catch (IOException e) {
                        latch.countDown();
                        break;
                    }
                }
            }).start();

            latch.await();
        }
    }
}
