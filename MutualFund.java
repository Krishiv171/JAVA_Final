import java.time.LocalDate;

public class MutualFund extends Asset {
    private double distributionPerUnit;

    public MutualFund(String id,String name,double purchasePrice,double currentValue,double quantity,
                      LocalDate date,double distributionPerUnit) {
        super(id,name,purchasePrice,currentValue,quantity,date);
        if(distributionPerUnit<0) throw new IllegalArgumentException("Distribution cannot be negative.");
        this.distributionPerUnit=distributionPerUnit;
    }

    @Override public double calculateReturn() {
        return (getCurrentValue()-getInvestmentCost()) + distributionPerUnit*getQuantity();
    }
    @Override public String getAssetType(){return "Mutual Fund";}
    public double getDistributionPerUnit(){return distributionPerUnit;}
    public void setDistributionPerUnit(double v){if(v<0)throw new IllegalArgumentException("Distribution cannot be negative.");distributionPerUnit=v;}
}
