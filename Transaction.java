import java.time.LocalDate;

public class Transaction {
    public enum Type { BUY, SELL }
    private final String transactionId, assetId;
    private final Type type;
    private final double quantity, price;
    private final LocalDate date;

    public Transaction(String transactionId,String assetId,Type type,double quantity,double price,LocalDate date) {
        if(transactionId==null||transactionId.isBlank())throw new IllegalArgumentException("Transaction ID is required.");
        if(assetId==null||assetId.isBlank())throw new IllegalArgumentException("Asset ID is required.");
        if(type==null)throw new IllegalArgumentException("Transaction type is required.");
        if(quantity<=0)throw new IllegalArgumentException("Transaction quantity must be greater than 0.");
        if(price<=0)throw new IllegalArgumentException("Transaction price must be greater than 0.");
        if(date==null)throw new IllegalArgumentException("Transaction date is required.");
        this.transactionId=transactionId;this.assetId=assetId;this.type=type;
        this.quantity=quantity;this.price=price;this.date=date;
    }
    public String getTransactionId(){return transactionId;}
    public String getAssetId(){return assetId;}
    public Type getType(){return type;}
    public double getQuantity(){return quantity;}
    public double getPrice(){return price;}
    public LocalDate getDate(){return date;}
    public double getAmount(){return quantity*price;}
}
