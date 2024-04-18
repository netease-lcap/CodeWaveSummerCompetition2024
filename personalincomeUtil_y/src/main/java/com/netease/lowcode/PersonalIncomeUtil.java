package com.netease.lowcode;

import com.netease.lowcode.core.annotation.NaslLogic;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PersonalIncomeUtil {
    // 税率常量
    private static final double[] INCOME_THRESHOLDS = {0, 36000, 144000, 300000, 420000, 660000, 960000};
    //税率
    private static final double[] INCOME_RATES = {0.03, 0.1, 0.2, 0.25, 0.3, 0.35, 0.45};
    //速算扣除
    private static final double[] INCOME_QUICK_DEDUCTIONS = {0, 2520, 16920, 31920, 52920, 85920, 181920};
    ; // 从一月开始累积收入
    ; // 社会保险费（每月）



    /**
     * 个税计算器
     * @param Month 缴税期数
     * @param income 税前月收入
     * @param totalSal 五险一金(每月)
     * @param socInsur 专享扣除费
     * @return
     */
    @NaslLogic
    public static PersonalReturnStructure calculateIncomeTax(Integer Month,Double income,Double totalSal,Double socInsur) {
        if (Objects.isNull(Month)|| Objects.isNull(income) || Objects.isNull(totalSal) || Objects.isNull(socInsur)) {
            throw new IllegalArgumentException("入参不能为null");
        }


        if (Month < 0 || income < 0 || totalSal < 0 || socInsur < 0) {
            throw new IllegalArgumentException("输入参数必须为正数");
        }
        //期数为1-12
        if (Month < 1 || Month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        PersonalReturnStructure personalReturnStructure = new PersonalReturnStructure();


        double tax = 0;//当期总税
        double tax1 = 0;//上月总税

        //每月应纳所得税额
        Double taxableIncome ;
        //应纳所得税额
        Double taxableIncomeMonth ;
        //(月税前工资 -5000 -五险一金-专项扣除)
        taxableIncome=(income - 5000-totalSal - socInsur);
        taxableIncomeMonth=(income - 5000-totalSal - socInsur) * Month;

        //无需缴税的情况
        if(taxableIncome<=0){
            personalReturnStructure.setIncomeTaxPayable(0D);
            personalReturnStructure.setAccumulatedTaxPayable(tax1);
            personalReturnStructure.setPaidTaxes(tax);
            personalReturnStructure.setTaxPayableMonth(tax-tax1);
            personalReturnStructure.setAfterTaxIncome(income);
            return personalReturnStructure;
        }

        //已缴纳税额
        for (int i = INCOME_THRESHOLDS.length - 1; i >= 0; i--) {
            if ((taxableIncome*(Month-1)) > INCOME_THRESHOLDS[i]) {
                tax1 = taxableIncome*(Month-1)  * INCOME_RATES[i] - INCOME_QUICK_DEDUCTIONS[i];
                break;
            }
        }

        //当期总税额
        for (int i = INCOME_THRESHOLDS.length - 1; i >= 0; i--) {
            if (taxableIncomeMonth > INCOME_THRESHOLDS[i]) {
                tax = taxableIncomeMonth  * INCOME_RATES[i] - INCOME_QUICK_DEDUCTIONS[i];
                System.out.println("当月应缴=="+(tax-tax1));
                break;
            }
        }
        //组装数据
        personalReturnStructure.setIncomeTaxPayable(taxableIncomeMonth);
        personalReturnStructure.setAccumulatedTaxPayable(tax1);
        personalReturnStructure.setPaidTaxes(tax);
        personalReturnStructure.setTaxPayableMonth(tax-tax1);
        personalReturnStructure.setAfterTaxIncome(income-totalSal-(tax-tax1));

        return personalReturnStructure ; // 个税不能为负数
    }
    public static void main(String[] args) {
        PersonalReturnStructure personalReturnStructure = calculateIncomeTax(5, 15000D, 2000d, 1000d);
//        PersonalReturnStructure personalReturnStructure = calculateIncomeTax(4, 7000D, 2000d, 1000d);
        System.out.println("金额=="+personalReturnStructure);
        //calculateEqualPrincipalAndInterest(1500000d,4.45,30);

        //等额本息
//        List<CalculateReturnStructure> calculateReturnStructures = calculateEqualPrincipal(1500000d, 4.45, 30);
        //等额本金
        CalculateAndSumReturnStructure calculateAndSumReturnStructure = calculateEqualPrincipalAndInterest(1500000d, 4.45, 30);

        System.out.println(calculateAndSumReturnStructure.toString());
    }


    /**
     * 房贷等额本息
     * @param loanAmount 贷款总额
     * @param annualInterestRate 贷款利率
     * @param loanTermInYears 贷款年数
     */
    @NaslLogic
    public static CalculateAndSumReturnStructure calculateEqualPrincipalAndInterest(Double loanAmount, Double annualInterestRate, Integer loanTermInYears) {
        if (Objects.isNull(loanAmount)|| Objects.isNull(annualInterestRate) || Objects.isNull(loanTermInYears) ) {
            throw new IllegalArgumentException("入参不能为null");
        }

        if (loanAmount < 0 || annualInterestRate < 0 || loanTermInYears < 0 ) {
            throw new IllegalArgumentException("输入参数必须为正数");
        }

        if(loanTermInYears<1 || loanTermInYears>30){
            throw new IllegalArgumentException("loanTermInYears must be between 1 and 30");
        }

            double monthlyInterestRate = annualInterestRate / 12 / 100;
            int loanTermInMonths = loanTermInYears * 12;
            double monthlyPayment = (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -loanTermInMonths));

            ArrayList<CalculateReturnStructure> resultList = new ArrayList<>();
            DecimalFormat df = new DecimalFormat("#.##");

    System.out.println("等额本息每月还款金额： " + df.format(monthlyPayment));
        double sumInterest=0;
    for (int i = 1; i <= loanTermInMonths; i++) {
        double interest = loanAmount * monthlyInterestRate;
        double principal = monthlyPayment - interest;
        loanAmount -= principal;

        sumInterest+=interest;
        CalculateReturnStructure calculateReturnStructure = new CalculateReturnStructure();
        String format = df.format(principal);

        //组装
        calculateReturnStructure.setMonth(i);
        calculateReturnStructure.setPrincipal(df.format(principal));
        calculateReturnStructure.setInterest(df.format(interest));
        calculateReturnStructure.setTotalPayment(df.format(monthlyPayment));
        calculateReturnStructure.setLoanAmount(df.format(loanAmount));
        resultList.add(calculateReturnStructure);
        System.out.println("第" + i + "月还款： 本金：" + df.format(principal) + " 利息：" + df.format(interest) + " 剩余贷款：" + df.format(loanAmount));
    }

        System.out.println("总利息=="+df.format(sumInterest));

        //返回实体
        CalculateAndSumReturnStructure calculateAndSumReturnStructure = new CalculateAndSumReturnStructure();
        calculateAndSumReturnStructure.setListCalculateReturnStructure(resultList);
        calculateAndSumReturnStructure.setSumInterest(df.format(sumInterest));
        return calculateAndSumReturnStructure;
}

    /**
     * 房贷等额本金
     * @param loanAmount 贷款总额
     * @param annualInterestRate 贷款利率
     * @param loanTermInYears 贷款年数
     */
    @NaslLogic
    public static CalculateAndSumReturnStructure calculateEqualPrincipal(Double loanAmount, Double annualInterestRate, Integer loanTermInYears) {
        if (Objects.isNull(loanAmount)|| Objects.isNull(annualInterestRate) || Objects.isNull(loanTermInYears) ) {
            throw new IllegalArgumentException("入参不能为null");
        }


        if (loanAmount < 0 || annualInterestRate < 0 || loanTermInYears < 0 ) {
            throw new IllegalArgumentException("输入参数必须为正数");
        }

        if(loanTermInYears<1 || loanTermInYears>30){
            throw new IllegalArgumentException("loanTermInYears must be between 1 and 30");
        }

        double monthlyInterestRate = annualInterestRate / 12 / 100;
        int loanTermInMonths = loanTermInYears * 12;
        double monthlyPrincipal = loanAmount / loanTermInMonths;

        ArrayList<CalculateReturnStructure> resultList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("等额本金每月还款金额： " + df.format(monthlyPrincipal));

        double remainingLoan = loanAmount;
        double sumInterest=0;
        for (int i = 1; i <= loanTermInMonths; i++) {
            CalculateReturnStructure calculateReturnStructure = new CalculateReturnStructure();

            double interest = remainingLoan * monthlyInterestRate;
            double totalPayment = monthlyPrincipal + interest;
            sumInterest+=interest;
            remainingLoan -= monthlyPrincipal;
            //组装
            calculateReturnStructure.setMonth(i);
            calculateReturnStructure.setPrincipal(df.format(monthlyPrincipal));
            calculateReturnStructure.setInterest(df.format(interest));
            calculateReturnStructure.setTotalPayment(df.format(totalPayment));
            calculateReturnStructure.setLoanAmount(df.format(remainingLoan));
            resultList.add(calculateReturnStructure);

            System.out.println("第" + i + "月还款： 本金：" + df.format(monthlyPrincipal) + " 利息：" + df.format(interest) + " 总计：" + df.format(totalPayment) + " 剩余贷款："+ df.format(remainingLoan));
        }

        System.out.println("总利息=="+df.format(sumInterest));


        //返回实体
        CalculateAndSumReturnStructure calculateAndSumReturnStructure = new CalculateAndSumReturnStructure();
        calculateAndSumReturnStructure.setListCalculateReturnStructure(resultList);
        calculateAndSumReturnStructure.setSumInterest(df.format(sumInterest));
        return calculateAndSumReturnStructure;
    }


}
