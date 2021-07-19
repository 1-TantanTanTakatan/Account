package me.ttt.takatan.account.Bluetooth.message;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class MessageReader implements Closeable {
    private final static String TAG = MessageReader.class.getSimpleName();
    private final JsonReader reader;

    public MessageReader(JsonReader reader) {
        this.reader = reader;
    }

    @Override
    public void close() throws IOException {
        Log.d(TAG, "close");
        reader.close();
    }

    public boolean hasNext() throws IOException {
        Log.d(TAG, "hasNext");
        return reader.hasNext();
    }

    public void beginArray() throws IOException {
        Log.d(TAG, "beginArray");
        reader.beginArray();
    }

    public void endArray() throws IOException {
        Log.d(TAG, "endArray");
        reader.endArray();
    }

    public TradeMessage read() throws IOException {
        Log.d(TAG, "read");
        int seq = -1;
        long time = -1;
        String date = null;
        String method = null;
        int price = 0;
        String sender = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case TradeMessage.FIELD_TIME:
                    time = reader.nextLong();
                    break;
                case TradeMessage.FIELD_DATE:
                    if (reader.peek() == JsonToken.NULL) {
                        reader.skipValue();
                        date = null;
                    } else {
                        date = reader.nextString();
                    }
                    break;
                case TradeMessage.FIELD_PRICE:
                    if (reader.peek() == JsonToken.NULL) {
                        reader.skipValue();
                        price = 0;
                    } else {
                        price = reader.nextInt();
                    }
                    break;
                case TradeMessage.FIELD_SENDER:
                    if (reader.peek() == JsonToken.NULL) {
                        reader.skipValue();
                        sender = null;
                    } else {
                        sender = reader.nextString();
                    }
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new TradeMessage(time, date, price, sender);
    }
}