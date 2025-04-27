package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author kanan
 */
public class DayCount implements Serializable {

    private LocalDate date;
    private int count;

    public DayCount() {
    }

    public DayCount(LocalDate d, int c) {
        this.date = d;
        this.count = c;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
