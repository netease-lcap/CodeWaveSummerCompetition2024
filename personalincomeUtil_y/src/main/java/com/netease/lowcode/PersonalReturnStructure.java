package com.netease.lowcode;

import com.netease.lowcode.core.annotation.NaslStructure;
@NaslStructure
public class PersonalReturnStructure {
    public Double incomeTaxPayable;//应纳所得税额
    public Double paidTaxes;//已缴税款
    public Double accumulatedTaxPayable;//累计应缴税款
    public Double taxPayableMonth;//本月应缴税款
    public Double afterTaxIncome;//本月税后收入

    public Double getIncomeTaxPayable() {
        return incomeTaxPayable;
    }

    public void setIncomeTaxPayable(Double incomeTaxPayable) {
        this.incomeTaxPayable = incomeTaxPayable;
    }

    public Double getPaidTaxes() {
        return paidTaxes;
    }

    public void setPaidTaxes(Double paidTaxes) {
        this.paidTaxes = paidTaxes;
    }

    public Double getAccumulatedTaxPayable() {
        return accumulatedTaxPayable;
    }

    public void setAccumulatedTaxPayable(Double accumulatedTaxPayable) {
        this.accumulatedTaxPayable = accumulatedTaxPayable;
    }

    public Double getTaxPayableMonth() {
        return taxPayableMonth;
    }

    public void setTaxPayableMonth(Double taxPayableMonth) {
        this.taxPayableMonth = taxPayableMonth;
    }

    public Double getAfterTaxIncome() {
        return afterTaxIncome;
    }

    public void setAfterTaxIncome(Double afterTaxIncome) {
        this.afterTaxIncome = afterTaxIncome;
    }

    @Override
    public String toString() {
        return "PersonalReturnStructure{" +
                "IncomeTaxPayable=" + incomeTaxPayable +
                ", PaidTaxes=" + paidTaxes +
                ", AccumulatedTaxPayable=" + accumulatedTaxPayable +
                ", TaxPayableMonth=" + taxPayableMonth +
                ", AfterTaxIncome=" + afterTaxIncome +
                '}';
    }
}
