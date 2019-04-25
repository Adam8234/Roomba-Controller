package daddyroast.io.tcp;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import daddyroast.io.IRobotConnection;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Communicates with the robot via TCP WiFi
 * @author adamcorp
 */
public class TCPRobotConnection implements IRobotConnection {
    private Socket socket;

    @Override
    public boolean init() {
        while (socket == null || !socket.isConnected()) {
            System.out.println("Connecting");
            try {
                socket = new Socket("192.168.1.1", 288);
            } catch (Exception e) {
                System.out.println("Failed to connect");
            }
        }
        System.out.println("Connected!");
        return true;
    }

    @Override
    public void sendString(String string) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] bytes = string.getBytes(StandardCharsets.US_ASCII);
            //send one byte at a time slowly
            for (int i = 0; i < bytes.length; i++) {
                outputStream.write(bytes, i, 1);
                outputStream.flush();
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readString() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
            char[] bytes = new char[256];
            int size = reader.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Synchronous reading from the robot. Blocks thread until it sees a D (Done) response
     * @return The strings received from the response
     */
    public List<String> readResponse() {
        List<String> response = Lists.newArrayList();
        while (true) {
            for (String s : Splitter.on(';').split(readString())) {
                if (s.charAt(0) != 0) {
                    response.add(s);
                }
            }
            if (response.get(response.size() - 1).equals("D")) {
                break;
            }
        }
        response.remove(response.size() - 1);
        return response;
    }
}
