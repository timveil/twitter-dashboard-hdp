package dashboard.view.model;

import java.util.Date;

public class IngestStatus {

    private boolean running;

    private Date started;

    public IngestStatus(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }
}
