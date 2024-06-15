package com.study.socket.server;

import com.study.socket.AbstractSocket;
import com.study.socket.dto.ClientInfo;
import com.study.socket.dto.MessageInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * packageName    : com.study.socket.server  <br>
 * fileName       : Socket_Server  <br>
 * author         : Eom SeungHo  <br>
 * date           : 2024-06-09  <br>
 * description    : <br><br> Socket Server
 */
public class SocketServer extends AbstractSocket {

    private final static Map<String, ClientInfo> clientMap = new ConcurrentHashMap<>();

    private final static AtomicInteger clientNumber = new AtomicInteger(1);

    public static void main(String[] args) {
        OnServer();
    }


    public static void OnServer(){

        try(ServerSocket socketServer = new ServerSocket(socket_port);
        ){

            while(true) {
                ClientInfo clientInfo = new ClientInfo(socketServer.accept(), makeClientName());

                clientMap.put(makeClientName(), clientInfo);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void registerClient(String clientId, ClientInfo clientInfo) {

    }

    public static void sendMessageToClient(MessageInfo messageInfo) {
        ClientInfo clientInfo = clientMap.get(messageInfo.getReceiver());
        clientInfo.sendMessage(messageInfo.getMessage());

    }

    public static void removeClient(String clientId) {

    }

    /**
     * 클라이언트 이름 생성기
     * @return
     */
    private static String makeClientName() {
        return "client-" + clientNumber.getAndIncrement();
    }

    private static void closeClient(String clientName){
        clientNumber.decrementAndGet();


        ClientInfo clientInfo = clientMap.get(clientName);

        clientInfo.interrupt();
        try {
            clientInfo.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(clientName + " 접속 종료....");
    }
}
