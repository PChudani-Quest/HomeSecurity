package chudanic.petr.homesecuritymobile;

import java.util.Date;

/**
 * Created by Petr on 10.12.2017.
 */

public class Status {
    long date = new Date().getTime();
    boolean processed;
    String status = "PENDING";

    public long getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
