package com.swpe.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.swpe.WebSocket.WebSocketServer;
import com.swpe.proto.proto_msgs;
import org.hibernate.criterion.Order;
import org.yaml.snakeyaml.Yaml;

import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Utility {

    private final String IMAGE = "image";
    private final String RESOLUTION = "resolution";
    private final String ORIGIN = "origin";
    private final String ORIGIN_GPS = "origin gps";
    private final String WIDTH = "width";
    private final String LENGTH = "length";

    public com.swpe.entity.Map ReadYamlAndMap(com.swpe.entity.Map map) throws FileNotFoundException {

        String[] arr=map.getFilepath().split("\\\\");

        arr[arr.length-1]=arr[arr.length-1].replace(".pgm",".yaml");


        String yamlFilePath=String.join("\\", arr);

        File file=new File(yamlFilePath);

        if(!file.exists())
            return null;

        InputStream inputStream = new FileInputStream(file);

        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);

        if(data.containsKey(RESOLUTION))
            map.setResolution((double) data.get(RESOLUTION));

        if(data.containsKey(ORIGIN))
            map.setOrigin(data.get(ORIGIN).toString());

        if(data.containsKey(ORIGIN_GPS))
            map.setOrigingps(data.get(ORIGIN_GPS).toString());

        if(data.containsKey(WIDTH))
            map.setWidth((Integer) data.get(WIDTH));

        if(data.containsKey(LENGTH))
            map.setLength((Integer) data.get(LENGTH));

        return map;
    }

    public static byte[] byteConvert(byte[] orgByte,byte[] recvByte){
        byte[] returnByte=new byte[orgByte.length+recvByte.length];

        System.arraycopy(orgByte,0,returnByte,0,orgByte.length);
        System.arraycopy(recvByte,0,returnByte,orgByte.length,recvByte.length);

        return returnByte;
    }
    // in order to get message from car
    public static JSONObject ByteToProtobuf(byte[] decodedBytes,int ugvid){
        proto_msgs.UGVMessage receiveMessage = null;
        try {
            receiveMessage = proto_msgs.UGVMessage.parseFrom(decodedBytes);
            // we will recieve two types of messages , 1- Navigation Message , 2- Car Status (including everything like post etc)
        } catch (InvalidProtocolBufferException e) {
            System.out.println("protobuff decode wrong");
            return new JSONObject();
//           e.printStackTrace();
        }
        Integer type = receiveMessage.getMessageType();
        String Path = "D:\\Course\\CCUpdated\\ImageFolderForUGV\\car" + ugvid;
        JSONObject response=new JSONObject();

        switch (type)
        {
            case 0:

                JSONObject carinformation=new JSONObject();
                proto_msgs.statusInfo statusinfo =receiveMessage.getCarInfo();


                carinformation.put("id", ugvid);
                carinformation.put("mapStatus", statusinfo.getMapStatus());
                carinformation.put("carStatus", statusinfo.getCarStatus());
                carinformation.put("lidarStatus", statusinfo.getLidarStatus());
                carinformation.put("cameraStatus", statusinfo.getCameraStatus());
                carinformation.put("gpsStatus", statusinfo.getGpsStatus());
                carinformation.put("chassisStatus", statusinfo.getChassisStatus());
                carinformation.put("localizerStatus", statusinfo.getLocalizerStatus());
                carinformation.put("battery", statusinfo.getBattery());
                carinformation.put("speed", statusinfo.getSpeed());
                carinformation.put("stopFlag", statusinfo.getStopFlag());
                carinformation.put("imagesPath", Path);

                // if gps is on get gps information
                if(statusinfo.getGpsStatus()==1) {
                    proto_msgs.gpsInfo gpsInfo = statusinfo.getGpsInfo();
                    carinformation.put("lat", gpsInfo.getLat());
                    carinformation.put("lon", gpsInfo.getLon());
                }else{
                    carinformation.put("lat", "");
                    carinformation.put("lon", "");
                }

                // if camera is on get camera images data and convert into images
                if(statusinfo.getCameraStatus()==1) {

                    proto_msgs.image obsImg = statusinfo.getObsImg();
                    List<Integer> imgData = obsImg.getDataList();

                    //Change List<int> into byte[]
                    if (imgData.size() > 0) {
                        byte[] saveImgByte = new byte[imgData.size()];
                        for (int i = 0; i < imgData.size(); i++) {
                            int single = imgData.get(i);
                            byte a = (byte) single;
                            saveImgByte[i] = a;
                        }
                        //change byte[] into a file
                        try {
                            // read these values from config json file , later
                            File carFileObj = new File(Path);

                            if (!carFileObj.isDirectory())
                                Files.createDirectories(Paths.get(Path));

                            File file = new File(Path + "\\"+ugvid+""+new SimpleDateFormat("yyyy-MM-dd-HH-mm'.png'").format(new Date()));
                            FileOutputStream fos = new FileOutputStream(file);
                            BufferedOutputStream buf = new BufferedOutputStream(fos);
                            buf.write(saveImgByte);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }

                JSONArray obstacleInfo = new JSONArray();

                List<proto_msgs.obstacle> obstacle=statusinfo.getObsInfoList();
                if(obstacle.size()>0){
                    for (int index = 0; index < obstacle.size(); index++) {

                        JSONObject SingleObstacleInfo=new JSONObject();

                        proto_msgs.obstacle ObstacleItem = obstacle.get(index);
                        proto_msgs.pose ObstaclePos = ObstacleItem.getPose();

                        SingleObstacleInfo.put("x",ObstaclePos.getX());
                        SingleObstacleInfo.put("y",ObstaclePos.getY());
                        SingleObstacleInfo.put("z",ObstaclePos.getZ());
                        SingleObstacleInfo.put("roll",ObstaclePos.getRoll());
                        SingleObstacleInfo.put("pitch",ObstaclePos.getPitch());
                        SingleObstacleInfo.put("yaw",ObstaclePos.getYaw());
                        SingleObstacleInfo.put("type",ObstacleItem.getType());

                        obstacleInfo.add(SingleObstacleInfo);
                    }
                    carinformation.put("obstacle",obstacleInfo);
                }
                response.put("type",0);
                response.put("info",carinformation);

                break;

            case 1:

                JSONObject navigationObject = new JSONObject();
                proto_msgs.path carNavigation = receiveMessage.getCarNav();

                List<proto_msgs.point> points = carNavigation.getPointList();
                JSONArray nestedPoints=new JSONArray();

                    for (int i = 0; i < points.size(); i++) {

                        proto_msgs.point sinlgePoint = points.get(i);
                        JSONObject singleP=new JSONObject();

                        singleP.put("x",sinlgePoint.getX());
                        singleP.put("y",sinlgePoint.getY());

                        nestedPoints.add(singleP);
                    }

                navigationObject.put("id", ugvid);
                navigationObject.put("path", nestedPoints);

                response.put("type", 1);
                response.put("info", navigationObject);


                break;
        }

        try {
            WebSocketServer.onMessage(response.toJSONString());
        } catch (Exception ex) {
            System.out.println(ex);
        }
            return new JSONObject();
    }

    public static boolean SelectMap(int ugvid,String mapName){
        try {
            proto_msgs.CCMessage.Builder builder = proto_msgs.CCMessage.newBuilder();

            builder.setMessageType(Enumerators.UGVMessageTypes.CHOOSEMAP.ordinal());
            builder.setMapName(mapName);

            ProtobufToByte(ugvid, builder.build().toByteArray());

            return true;
        }catch(Exception ex){
            System.out.println("Exception in  select Map in Utility "+ex);
            return false;
        }
    }

    public static boolean ManualControl(int SwitchToManual,int ugvid){
        try {
            proto_msgs.CCMessage.Builder builder = proto_msgs.CCMessage.newBuilder();

            // set CCMessage Builder
            switch(SwitchToManual){
                case 0:
                    builder.setMessageType(Enumerators.UGVMessageTypes.MANUALCONTROL.ordinal());
                    ProtobufToByte(ugvid, builder.build().toByteArray());
                    return true;
                case 1:
                    // what to do in order to convert from manual to automatic control
                    return true;
            }

            return false;
        }catch (Exception ex){
            System.out.println("Exception in  Manual Control in Utility "+ex);
            return false;
        }
    }

    public static boolean SendOrderForManualControl(int OrderType,int ugvid){
        try {
            //0-straight, 1-left-s, 2-right-s, 3-back, 4-l-b, 5-r-b, 6-stop
            proto_msgs.CCMessage.Builder CCBuilder = proto_msgs.CCMessage.newBuilder();
            CCBuilder.setMessageType(Enumerators.UGVMessageTypes.SENDORDER.ordinal());

            proto_msgs.controlOrder.Builder controlBuilder=proto_msgs.controlOrder.newBuilder();
            controlBuilder.setMoveType(OrderType);

            CCBuilder.setSendOrder(controlBuilder);

            ProtobufToByte(ugvid, CCBuilder.build().toByteArray());

            return true;
        }catch (Exception ex){
            System.out.println("Exception in  Manual Control in Utility "+ex);
            return false;
        }
    }

    public static boolean SetGlobalPath(int x,int y,int ugvid){
        try {

            proto_msgs.CCMessage.Builder CCBuilder = proto_msgs.CCMessage.newBuilder();
            CCBuilder.setMessageType(Enumerators.UGVMessageTypes.GETPATH.ordinal());


            proto_msgs.point.Builder pointBuilder=proto_msgs.point.newBuilder();

            pointBuilder.setX(x);
            pointBuilder.setY(y);

            CCBuilder.setTargetPoint(pointBuilder);

            ProtobufToByte(ugvid, CCBuilder.build().toByteArray());

            return true;
        }catch (Exception ex){
            System.out.println("Exception in  Manual Control in Utility "+ex);
            return false;
        }
    }

    public static boolean StartMoving(int ugvid){
        try {

            proto_msgs.CCMessage.Builder CCBuilder = proto_msgs.CCMessage.newBuilder();
            CCBuilder.setMessageType(Enumerators.UGVMessageTypes.STARTMOVE.ordinal());

            ProtobufToByte(ugvid, CCBuilder.build().toByteArray());

            return true;
        }catch (Exception ex){
            System.out.println("Exception in  Manual Control in Utility "+ex);
            return false;
        }
    }

    public static boolean StopMoving(int ugvid){
        try {

            proto_msgs.CCMessage.Builder CCBuilder = proto_msgs.CCMessage.newBuilder();
            CCBuilder.setMessageType(Enumerators.UGVMessageTypes.STOPMOVE.ordinal());

            ProtobufToByte(ugvid, CCBuilder.build().toByteArray());

            return true;
        }catch (Exception ex){
            System.out.println("Exception in  Manual Control in Utility "+ex);
            return false;
        }
    }

    // in order to send message to car
    public static boolean ProtobufToByte(int carid,byte[] protomessage){
        try {
            ThreadedSocket.FindSocketAndSendMessage(carid, CreatePacket(protomessage));
            return true;
        }catch (Exception ex){
            System.out.println("Exception in  protobuf to byte in Utility "+ex);
            return false;
        }
    }

    public static byte[] CreatePacket(byte[] packet){

        byte[] packet_length=new byte[8];

        String header="$START";
        byte[] header_byte=header.getBytes();

        String end="$END";
        byte[] end_byte=end.getBytes();

        int totalSize=header_byte.length+packet_length.length+packet.length+end_byte.length;
        byte[] packet_length_new=String.valueOf(totalSize).getBytes();
        System.arraycopy(packet_length_new,0,packet_length,0,packet_length_new.length);

        byte[] packetArray=new byte[totalSize];  //null


        packetArray=byteConvert(header_byte,packet_length);// =$START+000000020
        packetArray=byteConvert(packetArray,packet);//  = packetArray  +XXXXXX
        packetArray=byteConvert(packetArray,end_byte);//= packetArray  +$END




        return packetArray;
    }

}
