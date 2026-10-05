import java.time.LocalDate;

public abstract class Asset {
    private final String assetId;
    private String name;
    private double purchasePrice, currentValue, quantity;
    private LocalDate purchaseDate;

    public Asset(String assetId, String name, double purchasePrice, double currentValue,
                 double quantity, LocalDate purchaseDate) {
        if (assetId == null || assetId.isBlank()) throw new IllegalArgumentException("Asset ID is required.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Asset name is required.");
        if (purchasePrice <= 0) throw new IllegalArgumentException("Purchase price must be greater than 0.");
        if (currentValue < 0) throw new IllegalArgumentException("Current value cannot be negative.");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0.");
        if (purchaseDate == null) throw new IllegalArgumentException("Purchase date is required.");
        this.assetId=assetId; this.name=name; this.purchasePrice=purchasePrice;
        this.currentValue=currentValue; this.quantity=quantity; this.purchaseDate=purchaseDate;
    }

    public abstract double calculateReturn();
    public abstract String getAssetType();

    public double getInvestmentCost() { return purchasePrice * quantity; }
    public double getReturnPercentage() {
        return getInvestmentCost() == 0 ? 0 : calculateReturn() / getInvestmentCost() * 100.0;
    }

    public String getAssetId(){return assetId;}
    public String getName(){return name;}
    public double getPurchasePrice(){return purchasePrice;}

    public double getCurrentValue() {
        return currentValue * quantity;
    }

    public double getCurrentPrice() {
        return currentValue;
    }

    public double getQuantity(){return quantity;}
    public LocalDate getPurchaseDate(){return purchaseDate;}


    public void setName(String v){if(v==null||v.isBlank())throw new IllegalArgumentException("Name is required.");name=v;}
    public void setPurchasePrice(double v){if(v<=0)throw new IllegalArgumentException("Purchase price must be greater than 0.");purchasePrice=v;}
    public void setCurrentValue(double v){if(v<0)throw new IllegalArgumentException("Current value cannot be negative.");currentValue=v;}
    public void setQuantity(double v){if(v<=0)throw new IllegalArgumentException("Quantity must be greater than 0.");quantity=v;}
    public void setPurchaseDate(LocalDate v){if(v==null)throw new IllegalArgumentException("Purchase date is required.");purchaseDate=v;}
}
