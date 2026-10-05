import java.time.LocalDate;

public class Stock extends Asset {
    private double dividendPerUnit;

    public Stock(String id,String name,double purchasePrice,double currentValue,double quantity,
                 LocalDate date,double dividendPerUnit) {
        super(id,name,purchasePrice,currentValue,quantity,date);
        if(dividendPerUnit<0) throw new IllegalArgumentException("Dividend cannot be negative.");
        this.dividendPerUnit=dividendPerUnit;
    }

    @Override public double calculateReturn() {
        return (getCurrentValue()-getInvestmentCost()) + dividendPerUnit*getQuantity();
    }
    @Override public String getAssetType(){return "Stock";}
    public double getDividendPerUnit(){return dividendPerUnit;}
    public void setDividendPerUnit(double v){if(v<0)throw new IllegalArgumentException("Dividend cannot be negative.");dividendPerUnit=v;}
}
