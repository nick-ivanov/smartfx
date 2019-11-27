import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date.*;

public class OHLC {
    private int granularity; // In seconds, e.g., 1M=60, 1H=3600, etc.
    private LocalDateTime dateTime;
    private float o;
    private float h;
    private float l;
    private float c;
    private long timestamp;

    public OHLC() {
    }

    public OHLC(int granularity, LocalDateTime dateTime, float o, float h, float l, float c) {
        this.granularity = granularity;
        this.dateTime = dateTime;
        timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        this.o = o;
        this.h = h;
        this.l = l;
        this.c = c;
    }

    public int getGranularity() {
        return granularity;
    }

    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
    }

    public float getO() {
        return o;
    }

    public void setO(float o) {
        this.o = o;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "OHLC{" +
                "granularity=" + granularity +
                ", dateTime=" + dateTime +
                ", o=" + o +
                ", h=" + h +
                ", l=" + l +
                ", c=" + c +
                ", timestamp=" + timestamp +
                '}';
    }
}
