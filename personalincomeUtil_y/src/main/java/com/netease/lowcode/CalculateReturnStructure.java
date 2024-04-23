package com.netease.lowcode;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 *
 */
@NaslStructure
public class CalculateReturnStructure {
    public Integer month;//月份(期数)


    public String principal;//本金

    public String interest;//利息

    public String totalPayment;//总计

    public String loanAmount;//剩余贷款

    @Override
    public String toString() {
        return "CalculateReturnStructure{" +
                "month=" + month +
                ", principal='" + principal + '\'' +
                ", interest='" + interest + '\'' +
                ", totalPayment='" + totalPayment + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                '}';
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Integer getmonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

}
