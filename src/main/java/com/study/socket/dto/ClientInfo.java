package com.study.socket.dto;

import com.study.socket.server.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * packageName    : com.study.socket.dto  <br>
 * fileName       : Client  <br>
 * author         : Eom SeungHo  <br>
 * date           : 2024-06-09  <br>
 * description    : <br><br> 접속한 클라이언트 정보를 보관하는 객체
 */
public class ClientInfo extends Thread {

    private final Socket clientSocket;
    final private String clientName;

    private PrintWriter out;
    private BufferedReader in;

    public ClientInfo(Socket clientSocket, String clientName) {
        this.clientSocket = clientSocket;
        this.clientName = clientName;
    }

    @Override
    public void run() {
        try {
            // 클라이언트와 데이터를 주고받기 위한 스트림을 만듭니다.
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                MessageInfo messageInfo = new MessageInfo(message);
                String sender = messageInfo.getSender();
                SocketServer.registerClient(clientName, this);
                System.out.println("Client registered with ID: " + clientName);

                System.out.println("Received from " + clientName + ": " + messageInfo.getMessage());
                SocketServer.sendMessageToClient(messageInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 리소스를 닫습니다.
            SocketServer.removeClient(clientName);
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 클라이언트에게 메시지를 전송하는 메소드입니다.
    public void sendMessage(String message) {
        out.println(message);
    }
}
