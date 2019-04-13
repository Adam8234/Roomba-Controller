package daddyroast.io.tcp;

import daddyroast.io.IRobotConnection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPRobotConnection implements IRobotConnection {
    Socket socket;
    @Override
    public boolean init() {
        try {
            socket = new Socket("192.168.1.1", 288);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void sendString(String string) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.US_ASCII);
            byte[] bytes = string.getBytes(StandardCharsets.US_ASCII);
            for (int i = 0; i < bytes.length; i++) {
                outputStream.write(bytes, i, 1);
                outputStream.flush();
            }
            outputStream.write(13);
            outputStream.flush();
            outputStream.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readString() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
            String string = reader.readLine();
            reader.close();
            inputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
