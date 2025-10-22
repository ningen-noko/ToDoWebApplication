package org.github.entity;

public class Record {
    private static int counterSequence = 0;
    private final int id;
    private final String title;
    private RecordStatus status;

    public Record(String title, RecordStatus status) {
        this.id = counterSequence++;
        this.title = title;
        this.status = status;
    }

    public Record(String title) {
        this.id = counterSequence++;
        this.title = title;
        status = RecordStatus.ACTIVE;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }
}
