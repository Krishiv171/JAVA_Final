import java.time.LocalDate;

public class Bond extends Asset {
    private double couponRate;

    public Bond(String id,String name,double purchasePrice,double currentValue,double quantity,
                LocalDate date,double couponRate) {
        super(id,name,purchasePrice,currentValue,quantity,date);
        if(couponRate<0) throw new IllegalArgumentException("Coupon rate cannot be negative.");
        this.couponRate=couponRate;
    }

    @Override public double calculateReturn() {
        return (getCurrentValue()-getInvestmentCost()) + getInvestmentCost()*couponRate/100.0;
    }
    @Override public String getAssetType(){return "Bond";}
    public double getCouponRate(){return couponRate;}
    public void setCouponRate(double v){if(v<0)throw new IllegalArgumentException("Coupon rate cannot be negative.");couponRate=v;}
}
