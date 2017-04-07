package com.by_syk.schttableserver.vo;

import java.io.Serializable;

public class TermVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 学期
    private int term;
    
    // 距开学天数（以在学期内为参考点）
    private int daysFromStart = -1;
    
    // 距放假天数（以在学期内为参考点）
    private int daysBeforeEnd = -1;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getDaysFromStart() {
        return daysFromStart;
    }

    public void setDaysFromStart(int daysFromStart) {
        this.daysFromStart = daysFromStart;
    }

    public int getDaysBeforeEnd() {
        return daysBeforeEnd;
    }

    public void setDaysBeforeEnd(int daysBeforeEnd) {
        this.daysBeforeEnd = daysBeforeEnd;
    }
}
