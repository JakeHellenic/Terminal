
package com.example.terminal.socket;

import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.terminal.Backend;
import com.example.terminal.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.AndroidEntryPoint;


@Singleton
public class Socket implements Runnable {

    private String IP, PORT, lastPacket;
    private int packetNumber;
    public AsynchronousSocketChannel client;
    public ByteBuffer buffer;
    public MutableLiveData<Boolean> hasReplied;
    public MutableLiveData<Integer> hasConnected;
    int emptyCounter;
    /*
        HasConnected
        0 :: No connection
        1 :: Attempting Connection
        2 :: Connected
    */

    private Executor handleOut, handleIn;

    @Inject
    Utils utils;

    @Inject
    public Socket() {
        IP = "";
        PORT = "";
        lastPacket = "";
        emptyCounter = 0;
        packetNumber = 0;
        hasReplied = new MutableLiveData<>(false);
        hasConnected = new MutableLiveData<>(0);
    }

    public void print() {
        System.out.println("ACCESSED");
    }

    @Override
    public void run() {

        Log.i("Socket", "Running...");

        openSocket();

        if (client.isOpen()) {

            Log.i("Socket", "Client open ");
            hasConnected.postValue(2);

            handleOut = Executors.newSingleThreadExecutor();
            handleIn = Executors.newSingleThreadExecutor();
            while (!Thread.interrupted()  && client.isOpen()/*&& ClientService.isRunning*/) {
                //TODO UNLIMTED LOOP WHY IS THIS
                try {
                    buffer = ByteBuffer.allocate(1024);
                    Future<Integer> read = client.read(buffer);
                    read.get();
                    new onReceive(buffer);
                    buffer.clear();
                    buffer.compact();
                } catch (Exception e) {
                    Log.i("Socket ", e.toString());
                    closeSocket();
                }
            }
            // closeSocket();
        }

        Log.i("Socket", "Client closed ");
        closeSocket();
    }

    /**********SOCKET CONTROL****************/

    public void openSocket() {

        IP = "192.168.0.23";
        PORT = "5005";

        try {
            client = AsynchronousSocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(IP, Integer.parseInt(PORT));
            Future<Void> result = client.connect(socketAddress);
            result.get(10000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e("Socket ", e.toString());
            closeSocket();
        }
    }

    public void maintainSocket() {
        Log.i("Socket", "Connected..");
        handleOut = Executors.newSingleThreadExecutor();
        handleIn = Executors.newSingleThreadExecutor();
        while (!Thread.interrupted() /*&& ClientService.isRunning*/) {
            try {
                buffer = ByteBuffer.allocate(1024);
                Future<Integer> input = client.read(buffer);
                input.get();
                new onReceive(buffer);
                buffer.clear();
                buffer.compact();
            } catch (Exception e) {
                Log.i("Socket ", e.toString());
                closeSocket();
            }
        }
    }

    public void closeSocket() {

        hasConnected.postValue(0);
        Log.i("Socket", "Closing..");

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().join();
        } catch (Exception e) {
            Log.e("Socket", e.toString());
        }
    }


    /**********SOCK COMMUNICATION****************/

    //PARENT SEND
    public class parentSend {

        private String packet;

        public parentSend(String packet) {
            this.packet = packet;

            if (client.isOpen() && handleOut != null) {
                handleOut.execute(new ParentSendThread());
            } else
            {
                Log.i("ParentSend", " Can't send packet, attempting to reconnect..");
            }

        }

        public class ParentSendThread implements Runnable {

            public ParentSendThread() {
            }

            @Override
            public void run() {
                try {
                    ByteBuffer buffer = ByteBuffer.wrap(utils.padByte(packet));
                    Future<Integer> write = client.write(buffer);
                    write.get();
                    buffer.compact();
                    buffer.clear();
                    Log.i("Socket", "ParentSend sent :" + packet);
                } catch (Exception e) {
                    Log.i("Socket", "ParentSend :" + e);
                }
            }
        }
    }


    //SEND
    public class send {
        private String packet;
        private String type;

        public send(String packet, String type) {
            this.packet = packet;
            this.type = type;

            if (client != null && client.isOpen() && handleOut != null)
                handleOut.execute(new SendThread());
            else
                Log.i("ParentSend", " Can't send packet, client is closed.");
        }

        public class SendThread implements Runnable {

            public SendThread() {}

            @Override
            public void run() {
                StringBuilder str = new StringBuilder();
                //if(!str.toString().equals(ConnectionProperties.lastPacket))ConnectionProperties.m_packetNumber++;
                if (++packetNumber > 65535)
                    packetNumber = 1;

                str.append("HSL,0," + packetNumber + "," + type + ",").append(utils.STX).append(packet).append(utils.calcCheckSum(packet)).append(utils.ETX);

                try {
                    ByteBuffer buffer = ByteBuffer.wrap(utils.padByte(str.toString()));
                    Future<Integer> write = client.write(buffer);
                    write.get();
                    buffer.compact();
                    buffer.clear();
                    lastPacket = str.toString();
                    Log.i("SocketAdapter", "Sending packet : " + str.toString());
                    Thread.sleep(250);
                } catch (Exception e) {
                    Log.i("Socket", "SEND :" + e);
                }
            }
        }
    }


    /**********SOCKET DECODING ****************/


    public class onReceive {

        private ByteBuffer byteBuffer;

        public onReceive(ByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
            handleIn.execute(new onReceiveThread());
        }

        public class onReceiveThread implements Runnable {

            @Override
            public void run() {

                String data = new String(buffer.array()).trim();

                if(TextUtils.isEmpty(data)){
                    if(++emptyCounter == 20) closeSocket();
                    return;
                }



                Log.i("Socket", " Received : " + data);

                if(data.contains("DISCONNECT")){
                    closeSocket();
                }

            }
        }
    }
}


