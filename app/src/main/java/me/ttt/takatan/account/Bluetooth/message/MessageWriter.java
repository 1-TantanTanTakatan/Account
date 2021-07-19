package me.ttt.takatan.account.Bluetooth.message;

import android.util.JsonWriter;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class MessageWriter implements Closeable {
    private final static String TAG = MessageWriter.class.getSimpleName();
    private final JsonWriter writer;

    public MessageWriter(JsonWriter writer) {
        this.writer = writer;
    }

    @Override
    public void close() throws IOException {
        Log.d(TAG, "close");
        writer.close();
    }

    public void flush() throws IOException {
        Log.d(TAG, "flush");
        writer.flush();
    }

    public void beginArray() throws IOException {
        Log.d(TAG, "beginArray");
        writer.beginArray();
    }

    public void endArray() throws IOException {
        Log.d(TAG, "endArray");
        writer.endArray();
    }

    public void write(TradeMessage message) throws IOException {
        Log.d(TAG, "write");
        writer.beginObject();
        writer.name(TradeMessage.FIELD_TIME).value(message.time);
        writer.name(TradeMessage.FIELD_DATE).value(message.date);
        writer.name(TradeMessage.FIELD_PRICE).value(message.price);
        writer.name(TradeMessage.FIELD_SENDER);
        if (message.sender == null) {
            writer.nullValue();
        } else {
            writer.value(message.sender);
        }
        writer.endObject();
    }
}
