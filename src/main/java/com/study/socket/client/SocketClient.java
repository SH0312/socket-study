package com.study.socket.client;

import com.study.socket.AbstractSocket;
import com.study.socket.dto.MessageInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * packageName    : com.study.socket.client  <br>
 * fileName       : SocketClient  <br>
 * author         : Eom SeungHo  <br>
 * date           : 2024-06-09  <br>
 * description    : <br><br> Socket client
 */
public class SocketClient extends AbstractSocket {

    private static boolean closeClient = false;

    public static void main(String[] args) {

    }
    public static void connectClient() {
        try (Socket socket = new Socket(socket_host, socket_port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader chatInput = new BufferedReader(new InputStreamReader(System.in));
        ) {

            System.out.println("Connected to Server...");

            // 전송
            //메세지 전송 포멧 : [client name 또는 ALL 또는 Server] : 전송 내용
            Thread messageSender = new Thread(() -> {
                try {
                    String userMessage;
                    while (true) {
                        userMessage = chatInput.readLine();
                        if("quit".equalsIgnoreCase(userMessage)) {
                            closeClient = true;
                            break;
                        }
                        out.println(userMessage);
                        System.out.println("me >> " + userMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error Occured Sending messages");
                }
            });

            // 수신
            Thread messageReceiver = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        MessageInfo messageInfo = new MessageInfo(serverMessage);
                        System.out.println(messageInfo.getReceiver()+" : " + messageInfo.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error Occured Sending messages");
                }
            });

            messageSender.start();
            messageReceiver.start();

            if (closeClient) {
                messageSender.interrupt();
                messageReceiver.interrupt();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}