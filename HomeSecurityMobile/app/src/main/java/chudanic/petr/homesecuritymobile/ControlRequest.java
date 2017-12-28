package chudanic.petr.homesecuritymobile;

/**
 * Created by Petr on 10.12.2017.
 */

public class ControlRequest {
    String action;
    boolean processed;

    public String getAction() {
        return action;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
