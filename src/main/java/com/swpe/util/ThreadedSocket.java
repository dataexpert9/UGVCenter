package com.swpe.util;

import com.swpe.WebSocket.WebSocketServer;
import org.hibernate.result.Output;

import java.io.*;
import java.net.*;
import java.util.*;


public class ThreadedSocket {
    public static HashMap<Integer, Socket> socketConnections = new HashMap<Integer, Socket>();


    public boolean CreateConnection(int ugvid,String Name,String ugvIp,String ugvPort){
        try {
            // close if already exist else create new
            if (socketConnections.containsKey(ugvid)) {
                Socket existingSocket = socketConnections.get(ugvid);
                try {
                    existingSocket.close();
                    socketConnections.remove(ugvid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SocketNode node = new SocketNode(ugvid, Name, ugvIp, ugvPort);
            node.start();
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public static boolean CloseConnection(int ugvid){

        try {
            if(socketConnections.containsKey(ugvid)){
                Socket socket=socketConnections.get(ugvid);
                socket.close();
                if(socket.isClosed())
                    return true;
                else
                    return false;
            }else
                return false;

        }catch(Exception ex){
                System.out.println("Exception in closing socket connection "+ex);
                return false;
        }
    }

    public static boolean FindSocketAndSendMessage(int ugvid,byte[] messageToSend){
            if(socketConnections.containsKey(ugvid)){
                Socket socket=socketConnections.get(ugvid);

                try {
                    OutputStream os =socket.getOutputStream();
                    os.write(messageToSend);
                    os.flush();
//                    os.close();
                    return true;
                }catch(Exception ex){
                    System.out.println("Exception in FindSocket and send message occured"+ex);
                    return false;
                }
            }
            return false;
    }


    class SocketNode extends Thread{

        public int ugvid;
        public String Name;
        public boolean Connection;
        public String ugvIp;
        public String ugvPort;


        public SocketNode(int ugvid,String Name,String ugvIp,String ugvPort) {
            this.ugvid=ugvid;
            this.Name=Name;
            this.ugvIp=ugvIp;
            this.ugvPort=ugvPort;
            this.Connection=true;
        }

        public void run() {
            System.out.println("Socket Thread Started : Car Id "+ this.ugvid);
            try {
                Socket socket2 = new Socket(this.ugvIp, Integer.parseInt(this.ugvPort));

                socketConnections.put(this.ugvid,socket2);

                InputStream stream = socket2.getInputStream();
                byte[] saveByte = new byte[0];


                while (socket2.isConnected()) {
                    byte[] data = new byte[stream.available()];

                    if (stream.read(data) > 0) {

                        if (data.length < 4)
                            continue;

                        String startMessage = new String(data, 0, 6);

                        if (startMessage.equals("$START")) {
                            saveByte = new byte[0]; // to start reading package from start (in case of error , restart , or reading new package)
                        }


                        saveByte = Utility.byteConvert(saveByte, data);

                        // Format of package recieved
                        /*
                            Start of package we will have $START to check the packet is started
                            Length (len:8) of packet of size 00000020
                            Data : which need to be mapped on protobuf
                            $END : Signal that packet has been ended
                         */


                        String endMessage = new String(data, data.length - 4, 4);

                        if (endMessage.equals("$END")) {
                            // we need to decode the package into proto format
                            byte[] decodedBytes = new byte[saveByte.length - 18]; // because STart package have $START of 6 length + $END of 4 length and length of packet is 8 = 18
                            System.arraycopy(saveByte, 14, decodedBytes, 0, saveByte.length - 18);
                            Utility.ByteToProtobuf(decodedBytes, ugvid);
                        }
                    }
                }

            }catch(Exception ex){

                return;
            }
        }
    }
}
