package com.elamed.almag.net;

import com.elamed.almag.data.Request;
import com.elamed.almag.data.Responce;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public Client() {
    }

    public static Responce loadDecisions(Request request, String ip, int port) throws Exception {
        Socket socket = new Socket(ip, port);
        ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
        DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
        ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
        //Request request = new Request();
//        request.setDiseases(diseases);
//        request.setTypeRequest(type);
        request.setVersionMobileApp("1.0.0.0");
        objOutStream.writeObject(request);
        objOutStream.flush();
        Responce responce = (Responce)objInStream.readObject();
        System.out.println(Arrays.toString(responce.getDiseases().toArray()));
        System.out.println(responce.getType());
        outStream.close();
        objOutStream.close();
        objInStream.close();
        socket.close();
        return responce;
    }
}
