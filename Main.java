import javax.swing.SwingUtilities;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Investor investor=new Investor("INV001","Demo Investor","investor@example.com");
        Portfolio portfolio=new Portfolio(investor);
        try {
            portfolio.addAsset(new Stock("STK001","Reliance Industries",2400,2750,10,LocalDate.of(2026,1,15),20));
            portfolio.addAsset(new MutualFund("MF001","SBI Bluechip Fund",850,930,25,LocalDate.of(2026,2,10),8));
            portfolio.addAsset(new Bond("BND001","Government Bond",1000,1040,20,LocalDate.of(2026,3,5),7.5));
            portfolio.addTransaction(new Transaction("TXN001","STK001",Transaction.Type.BUY,10,2400,LocalDate.of(2026,1,15)));
            portfolio.addTransaction(new Transaction("TXN002","MF001",Transaction.Type.BUY,25,850,LocalDate.of(2026,2,10)));
            portfolio.addTransaction(new Transaction("TXN003","BND001",Transaction.Type.BUY,20,1000,LocalDate.of(2026,3,5)));
        } catch(Exception e){System.err.println("Demo data error: "+e.getMessage());}
        SwingUtilities.invokeLater(()->new PortfolioFrame(portfolio).setVisible(true));
    }
}