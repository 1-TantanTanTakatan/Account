package me.ttt.takatan.account.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import me.ttt.takatan.account.Bluetooth.message.TradeMessage;
import me.ttt.takatan.account.Bluetooth.message.MessageReader;
import me.ttt.takatan.account.Bluetooth.message.MessageWriter;

abstract class Agent {
    private final static String TAG = Agent.class.getSimpleName();

    final static int MSG_STARTED = 1111;
    final static int MSG_RECEIVED = 2222;
    final static int MSG_FINISHED = 3333;

    private CommThread thread;

    final ConnectActivity activity;

    private final Handler handler;

    Agent(ConnectActivity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
    }

    void runCommThread(BluetoothSocket socket) {
        try {
            thread = new CommThread(socket, handler);
            thread.start();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    void send(TradeMessage message) {
        if (thread != null) {
            thread.send(message);
        }
    }

    void close() {
        if (thread != null) {
            thread.close();
            thread = null;
        }
    }

    abstract void onActivityResult(int reqCode, int resCode, Intent data);

    private static class CommThread extends Thread {

        private final BluetoothSocket sock;
        private final MessageReader reader;
        private final MessageWriter writer;
        private final Handler handler;
        private boolean writerClosed = false;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        CommThread(BluetoothSocket sock, Handler handler) throws IOException {
            if (!sock.isConnected()) {
                throw new IOException("Socket is not connected");
            }
            this.sock = sock;
            this.handler = handler;
            Reader rawReader = new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8);
            this.reader = new MessageReader(new JsonReader(rawReader));
            Writer rawWriter = new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8);
            this.writer = new MessageWriter(new JsonWriter(rawWriter));
        }

        @Override
        public void run() {
            Log.d(TAG, "run");
            try (BluetoothSocket s = sock) {
                handler.sendMessage(handler.obtainMessage(MSG_STARTED, s.getRemoteDevice()));
                writer.beginArray();
                reader.beginArray();
                while (reader.hasNext()) {
                    handler.sendMessage(handler.obtainMessage(MSG_RECEIVED, reader.read()));
                }
                reader.endArray();
                if (!writerClosed) {
                    writer.endArray();
                    writer.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.sendMessage(handler.obtainMessage(MSG_FINISHED));
        }

        void send(TradeMessage message) {
            Log.d(TAG, "send");
            try {
                writer.write(message);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void close() {
            Log.d(TAG, "close");
            try {
                writer.endArray();
                writer.flush();
                writerClosed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
