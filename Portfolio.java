import java.util.*;
import java.util.stream.Collectors;

public class Portfolio {
    private final Investor investor;
    private final ArrayList<Asset> holdings = new ArrayList<>();
    private final LinkedList<Transaction> transactionHistory = new LinkedList<>();
    private final HashMap<String,Asset> assetMap = new HashMap<>();
    private final TreeMap<Double,List<Asset>> returnSortedHoldings = new TreeMap<>();

    public Portfolio(Investor investor){if(investor==null)throw new IllegalArgumentException("Investor is required.");this.investor=investor;}
    public Investor getInvestor(){return investor;}

    public void addAsset(Asset a)throws ValidationException{
        if(a==null)throw new ValidationException("Asset cannot be null.");
        if(assetMap.containsKey(a.getAssetId()))throw new ValidationException("Asset ID already exists: "+a.getAssetId());
        holdings.add(a);assetMap.put(a.getAssetId(),a);rebuildReturnIndex();
    }
    public void updateAsset(Asset a)throws AssetNotFoundException{
        Asset old=assetMap.get(a.getAssetId());
        if(old==null)throw new AssetNotFoundException("Asset not found: "+a.getAssetId());
        holdings.set(holdings.indexOf(old),a);assetMap.put(a.getAssetId(),a);rebuildReturnIndex();
    }
    public void deleteAsset(String id)throws AssetNotFoundException{
        Asset a=assetMap.remove(id);
        if(a==null)throw new AssetNotFoundException("Asset not found: "+id);
        holdings.remove(a);rebuildReturnIndex();
    }
    public Asset findAssetById(String id)throws AssetNotFoundException{
        Asset a=assetMap.get(id);if(a==null)throw new AssetNotFoundException("Asset not found: "+id);return a;
    }
    public List<Asset> search(String q){
        String s=q==null?"":q.trim().toLowerCase();
        return holdings.stream().filter(a->a.getAssetId().toLowerCase().contains(s)
                ||a.getName().toLowerCase().contains(s)||a.getAssetType().toLowerCase().contains(s)).collect(Collectors.toList());
    }
    public List<Asset> searchByType(String type){
        if(type==null||type.equalsIgnoreCase("All"))return new ArrayList<>(holdings);
        return holdings.stream().filter(a->a.getAssetType().equalsIgnoreCase(type)).collect(Collectors.toList());
    }
    public List<Asset> sortByValue(){return holdings.stream().sorted(Comparator.comparingDouble(Asset::getCurrentValue).reversed()).collect(Collectors.toList());}
    public List<Asset> sortByReturnPercentage(){return holdings.stream().sorted(Comparator.comparingDouble(Asset::getReturnPercentage).reversed()).collect(Collectors.toList());}
    public void addTransaction(Transaction t)throws ValidationException{
        if(t==null)throw new ValidationException("Transaction cannot be null.");
        if(!assetMap.containsKey(t.getAssetId()))throw new ValidationException("Unknown asset: "+t.getAssetId());
        transactionHistory.add(t);
    }
    private void rebuildReturnIndex(){
        returnSortedHoldings.clear();
        for(Asset a:holdings)returnSortedHoldings.computeIfAbsent(a.getReturnPercentage(),k->new ArrayList<>()).add(a);
    }
    public double getTotalInvestment(){return holdings.stream().mapToDouble(Asset::getInvestmentCost).sum();}
    public double getCurrentPortfolioValue(){return holdings.stream().mapToDouble(Asset::getCurrentValue).sum();}
    public double getTotalReturn(){return holdings.stream().mapToDouble(Asset::calculateReturn).sum();}
    public double getTotalReturnPercentage(){return getTotalInvestment()==0?0:getTotalReturn()/getTotalInvestment()*100;}
    public Map<String,Double> getAllocationByType(){
        Map<String,Double> m=new HashMap<>();double total=getCurrentPortfolioValue();if(total==0)return m;
        for(Asset a:holdings)m.merge(a.getAssetType(),a.getCurrentValue()/total*100,Double::sum);return m;
    }
    public ArrayList<Asset> getHoldings(){return new ArrayList<>(holdings);}
    public LinkedList<Transaction> getTransactionHistory(){return new LinkedList<>(transactionHistory);}
    public HashMap<String,Asset> getAssetMap(){return new HashMap<>(assetMap);}
    public TreeMap<Double,List<Asset>> getReturnSortedHoldings(){return new TreeMap<>(returnSortedHoldings);}
}