package me.ttt.takatan.account.Bluetooth.message;

import android.os.Parcel;
import android.os.Parcelable;

public class TradeMessage implements Parcelable {
    final static String FIELD_TIME = "time";
    final static String FIELD_DATE = "date";
    final static String FIELD_PRICE = "price";
    final static String FIELD_SENDER = "sender";

    public final long time;
    public final String date;
    public final int price;
    public final String sender;

    public TradeMessage(long time, String date, int price, String sender) {
        this.time = time;
        this.date = date;
        this.price  = price;
        this.sender = sender;
    }

    private TradeMessage(Parcel in) {
        time = in.readLong();
        date = in.readString();
        price = in.readInt();
        sender = in.readString();
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(date);
        dest.writeInt(price);
        dest.writeString(sender);
    }

    public static final Parcelable.Creator<TradeMessage> CREATOR = new Parcelable.Creator<TradeMessage>() {
        @Override
        public TradeMessage createFromParcel(Parcel src) {
            return new TradeMessage(src);
        }

        @Override
        public TradeMessage[] newArray(int size) {
            return new TradeMessage[size];
        }
    };
}
