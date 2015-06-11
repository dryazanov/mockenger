package org.mockenger.dev.model.dto;

import java.util.List;

/**
 * Created by x079089 on 6/9/2015.
 */
public class BootGridDTO {

    private int current;

    private int rowCount;

    private int total;

    private List<BootGridRowDTO> rows;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BootGridRowDTO> getRows() {
        return rows;
    }

    public void setRows(List<BootGridRowDTO> rows) {
        this.rows = rows;
    }
}
