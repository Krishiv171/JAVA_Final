import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
public class PortfolioFrame extends JFrame {
    private final Portfolio portfolio;
    private DefaultTableModel investmentModel, transactionModel;
    private JTable investmentTable;
    private final JLabel totalInvestment = new JLabel(), currentValue = new JLabel(), totalReturn = new JLabel(), returnPct = new JLabel();
    private final JTextField search = new JTextField();
    private final JComboBox<String> filter=new JComboBox<>(new String[]{
        "All","Stock","Mutual Fund","Bond"
    });
    private static final NumberFormat INR = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    public PortfolioFrame(Portfolio p){
        portfolio=p;
        setTitle("Personal Investment Portfolio Tracker");
        setSize(1150,720);
        setMinimumSize(new Dimension(950,620));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(new Color(245,247,250));
        root.add(sidebar(),BorderLayout.WEST);
        JTabbedPane tabs=new JTabbedPane();
        tabs.addTab("Dashboard",dashboard());
        tabs.addTab("Investments",investments());
        tabs.addTab("Transactions",transactions());
        tabs.addTab("Allocation",allocation());
        tabs.addTab("Reports",reports());
        root.add(tabs,BorderLayout.CENTER);
        setContentPane(root);
        refreshAll();
    }
    private JPanel sidebar(){
        JPanel p=new JPanel();
        p.setPreferredSize(new Dimension(205,0));
        p.setBackground(new Color(30,34,43));
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(25,18,25,18));
        JLabel t=new JLabel("<html><b>Portfolio<br>Tracker</b></html>");
        t.setForeground(Color.WHITE);
        t.setFont(new Font("SansSerif",Font.BOLD,24));
        p.add(t);
        p.add(Box.createVerticalStrut(15));
        JLabel s=new JLabel("<html>Java Programming<br>Case Study 76</html>");
        s.setForeground(new Color(190,196,208));
        p.add(s);
        p.add(Box.createVerticalStrut(35));
        JLabel i=new JLabel("<html><b>Investor</b><br>"+portfolio.getInvestor().getName()+"</html>");
        i.setForeground(Color.WHITE);
        p.add(i);
        return p;
    }
    private JPanel dashboard(){
        JPanel p=base();
        JPanel cards=new JPanel(new GridLayout(1,4,12,0));
        cards.setOpaque(false);
        cards.add(card("Total Investment",totalInvestment));
        cards.add(card("Current Value",currentValue));
        cards.add(card("Total Return",totalReturn));
        cards.add(card("Return %",returnPct));
        p.add(cards,BorderLayout.NORTH);
        JTextArea info=new JTextArea("Personal Investment Portfolio Tracker\n\nTracks Stocks, Mutual Funds and Bonds.\n\n"
        +"Required Java concepts implemented:\n• Abstraction and inheritance\n• Method overriding\n• ArrayList, LinkedList, HashMap and TreeMap\n"
        +"• CRUD, search and sorting\n• Return and allocation analysis\n• Validation and exception handling\n• Swing GUI");
        info.setEditable(false);
        info.setFont(new Font("SansSerif",Font.PLAIN,15));
        info.setBorder(new EmptyBorder(25,25,25,25));
        p.add(info,BorderLayout.CENTER);
        return p;
    }
    private JPanel card(String title,JLabel value){
        JPanel p=new JPanel(new BorderLayout(5,8));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(18,18,18,18));
        JLabel h=new JLabel(title);
        h.setForeground(new Color(100,106,118));
        h.setFont(new Font("SansSerif",Font.BOLD,12));
        value.setFont(new Font("SansSerif",Font.BOLD,20));
        p.add(h,BorderLayout.NORTH);
        p.add(value,BorderLayout.CENTER);
        return p;
    }
    private JPanel base(){
        JPanel p=new JPanel(new BorderLayout(12,12));
        p.setBackground(new Color(245,247,250));
        p.setBorder(new EmptyBorder(12,12,12,12));
        return p;
    }
    private JPanel investments(){
        JPanel p = base();
        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);
        search.setToolTipText("Search by Asset ID, Name or Type");
        top.add(search, BorderLayout.CENTER);
        top.add(filter, BorderLayout.EAST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        JButton find = new JButton("Search");
        JButton clear = new JButton("Clear");
        JButton sv = new JButton("Sort by Value");
        JButton sr = new JButton("Sort by Return");
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        for (JButton b : new JButton[]{
            find, clear, sv, sr, add, edit, del
        }) {
            actions.add(b);
        }
        investmentModel = new DefaultTableModel(
        new Object[]{
            "ID",
            "Name",
            "Type",
            "Qty",
            "Purchase",
            "Current",
            "Return",
            "Return %",
            "Date"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        investmentTable = new JTable(investmentModel);
        investmentTable.setAutoCreateRowSorter(true);
        investmentTable.setRowHeight(27);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(investmentTable), BorderLayout.CENTER);
        p.add(actions, BorderLayout.SOUTH);
        find.addActionListener(e -> applyFilter());
        clear.addActionListener(e -> {
            search.setText("");
            filter.setSelectedItem("All");
            showAssets(portfolio.getHoldings());
        });
        filter.addActionListener(e -> applyFilter());
        search.addActionListener(e -> applyFilter());
        search.getDocument().addDocumentListener(
        new javax.swing.event.DocumentListener() {
            private void update() {
                SwingUtilities.invokeLater(() -> applyFilter());
            }
            @Override
            public void insertUpdate(
            javax.swing.event.DocumentEvent e) {
                update();
            }
            @Override
            public void removeUpdate(
            javax.swing.event.DocumentEvent e) {
                update();
            }
            @Override
            public void changedUpdate(
            javax.swing.event.DocumentEvent e) {
                update();
            }
        }
        );
        sv.addActionListener(e ->
        showAssets(portfolio.sortByValue())
        );
        sr.addActionListener(e ->
        showAssets(portfolio.sortByReturnPercentage())
        );
        add.addActionListener(e -> addAsset());
        edit.addActionListener(e -> editAsset());
        del.addActionListener(e -> deleteAsset());
        return p;
    }
    private JPanel transactions(){
        JPanel p=base();
        transactionModel=new DefaultTableModel(new Object[]{
            "Transaction ID","Asset ID","Type","Qty","Price","Amount","Date"
        },0){
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };
        JTable t=new JTable(transactionModel);
        t.setRowHeight(27);
        p.add(new JScrollPane(t),BorderLayout.CENTER);
        return p;
    }
    private JPanel allocation(){
        JPanel p=base();
        JTextArea area=new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced",Font.PLAIN,16));
        p.add(area,BorderLayout.CENTER);
        p.putClientProperty("allocationArea",area);
        return p;
    }
    private JPanel reports(){
        JPanel p=base();
        JTextArea a=new JTextArea();
        a.setEditable(false);
        a.setFont(new Font("Monospaced",Font.PLAIN,14));
        p.add(a,BorderLayout.CENTER);
        p.putClientProperty("reportArea",a);
        return p;
    }
    private void refreshAll(){
        totalInvestment.setText(INR.format(portfolio.getTotalInvestment()));
        currentValue.setText(INR.format(portfolio.getCurrentPortfolioValue()));
        totalReturn.setText(INR.format(portfolio.getTotalReturn()));
        returnPct.setText(String.format("%.2f%%",portfolio.getTotalReturnPercentage()));
        showAssets(portfolio.getHoldings());
        transactionModel.setRowCount(0);
        for (Transaction t:portfolio.getTransactionHistory())transactionModel.addRow(new Object[]{
            t.getTransactionId(),t.getAssetId(),t.getType(),t.getQuantity(),INR.format(t.getPrice()),INR.format(t.getAmount()),t.getDate()
        });
        refreshTextTabs();
    }
    private void showAssets(List<Asset> list){
        investmentModel.setRowCount(0);
        for (Asset a:list)investmentModel.addRow(new Object[]{
            a.getAssetId(),a.getName(),a.getAssetType(),a.getQuantity(),INR.format(a.getPurchasePrice()),
            INR.format(a.getCurrentValue()),INR.format(a.calculateReturn()),String.format("%.2f%%",a.getReturnPercentage()),a.getPurchaseDate()
        });
    }
    private void applyFilter() {
        String query = search.getText().trim().toLowerCase();
        String selectedType =
        String.valueOf(filter.getSelectedItem());
        List<Asset> list = portfolio.getHoldings().stream().filter(asset -> {
            if (query.isEmpty()) {
                return true;
            }
            String id = asset.getAssetId() == null
            ? ""
            : asset.getAssetId().toLowerCase();
            String name = asset.getName() == null
            ? ""
            : asset.getName().toLowerCase();
            String type = asset.getAssetType() == null
            ? ""
            : asset.getAssetType().toLowerCase();
            return id.contains(query)
            || name.contains(query)
            || type.contains(query);
        }).filter(asset -> {
            if ("All".equalsIgnoreCase(selectedType)) {
                return true;
            }
            return asset.getAssetType().equalsIgnoreCase(selectedType);
        }).toList();
        showAssets(list);
    }
    private void addAsset(){
        AssetDialog d=new AssetDialog(this,null);
        d.setVisible(true);
        if (d.result!=null) {

            portfolio.addAsset(d.result);
            portfolio.addTransaction(new Transaction("TXN-"+System.currentTimeMillis(),d.result.getAssetId(),
            Transaction.Type.BUY,d.result.getQuantity(),d.result.getPurchasePrice(),d.result.getPurchaseDate()));
            refreshAll();
        } catch (Exception e){
            error(e.getMessage());
        }
    }
    private void editAsset(){
        int r=investmentTable.getSelectedRow();
        if (r<0){
            error("Select an investment.");
            return;
        }
        String id=investmentTable.getValueAt(investmentTable.convertRowIndexToModel(r),0).toString();
        try{
            AssetDialog d=new AssetDialog(this,portfolio.findAssetById(id));
            d.setVisible(true);
            if (d.result!=null){
                portfolio.updateAsset(d.result);
                refreshAll();
            }
        } catch (Exception e){
            error(e.getMessage());
        }
    }
    private void deleteAsset(){
        int r=investmentTable.getSelectedRow();
        if (r<0){
            error("Select an investment.");
            return;
        }
        String id=investmentTable.getValueAt(investmentTable.convertRowIndexToModel(r),0).toString();
        if (JOptionPane.showConfirmDialog(this,"Delete "+id+"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

            portfolio.deleteAsset(id);
            refreshAll();
        } catch (Exception e){
            error(e.getMessage());
        }
    }
    private void error(String m){
        JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);
    }
    private void refreshTextTabs(){
        JTabbedPane tabs=(JTabbedPane)getContentPane().getComponent(1);
        JPanel alloc=(JPanel)tabs.getComponentAt(3);
        JTextArea aa=(JTextArea)alloc.getClientProperty("allocationArea");
        StringBuilder ab=new StringBuilder("PORTFOLIO ALLOCATION\n\n");
        portfolio.getAllocationByType().forEach((k,v)->ab.append(String.format("%-15s %6.2f%%%n",k,v)));
        aa.setText(ab.toString());
        JPanel rep=(JPanel)tabs.getComponentAt(4);
        JTextArea ra=(JTextArea)rep.getClientProperty("reportArea");
        ra.setText("PORTFOLIO REPORT\n\nInvestor: "+portfolio.getInvestor().getName()+"\n"
        +"Total Investment : "+INR.format(portfolio.getTotalInvestment())+"\nCurrent Value    : "+INR.format(portfolio.getCurrentPortfolioValue())
        +"\nTotal Return     : "+INR.format(portfolio.getTotalReturn())+"\nReturn Percentage: "+String.format("%.2f%%",portfolio.getTotalReturnPercentage())
        +"\nHoldings         : "+portfolio.getHoldings().size()+"\nTransactions     : "+portfolio.getTransactionHistory().size());
    }
    private class AssetDialog extends JDialog{
        Asset result;
        Asset existing;
        JTextField id=new JTextField(),name=new JTextField(),pp=new JTextField(),cv=new JTextField(),qty=new JTextField(),date=new JTextField(),extra=new JTextField();
        JComboBox<String> type=new JComboBox<>(new String[]{
            "Stock","Mutual Fund","Bond"
        });
        JLabel extraLabel=new JLabel("Dividend / unit");
        AssetDialog(Frame owner,Asset e){
            super(owner,e==null?"Add Investment":"Edit Investment",true);
            existing=e;
            setSize(460,420);
            setLocationRelativeTo(owner);
            JPanel f=new JPanel(new GridLayout(0,2,7,7));
            f.setBorder(new EmptyBorder(15,15,15,15));
            f.add(new JLabel("Asset ID"));
            f.add(id);
            f.add(new JLabel("Name"));
            f.add(name);
            f.add(new JLabel("Type"));
            f.add(type);
            f.add(new JLabel("Purchase Price"));
            f.add(pp);
            f.add(new JLabel("Current Value"));
            f.add(cv);
            f.add(new JLabel("Quantity"));
            f.add(qty);
            f.add(new JLabel("Purchase Date"));
            f.add(date);
            f.add(extraLabel);
            f.add(extra);
            JPanel b=new JPanel();
            JButton save=new JButton("Save"),cancel=new JButton("Cancel");
            b.add(save);
            b.add(cancel);
            add(f);
            add(b,BorderLayout.SOUTH);
            type.addActionListener(x->label());
            if (e!=null){
                id.setText(e.getAssetId());
                id.setEnabled(false);
                name.setText(e.getName());
                type.setSelectedItem(e.getAssetType());
                pp.setText(""+e.getPurchasePrice());
                cv.setText(""+e.getCurrentValue());
                qty.setText(""+e.getQuantity());
                date.setText(""+e.getPurchaseDate());
                if (e instanceof Stock s)extra.setText(""+s.getDividendPerUnit());
                else if (e instanceof MutualFund m)extra.setText(""+m.getDistributionPerUnit());
                else if (e instanceof Bond bnd)extra.setText(""+bnd.getCouponRate());
            } else date.setText(LocalDate.now().toString());
            label();
            save.addActionListener(x->save());
            cancel.addActionListener(x->dispose());
        }
        void label(){
            String t=(String)type.getSelectedItem();
            extraLabel.setText(t.equals("Stock")?"Dividend / unit":t.equals("Mutual Fund")?"Distribution / unit":"Coupon rate %");
        }
        void save(){
            try{
                String i=id.getText().trim(),n=name.getText().trim();
                double p=Double.parseDouble(pp.getText()),c=Double.parseDouble(cv.getText()),q=Double.parseDouble(qty.getText());
                LocalDate d=ValidationUtils.parseDate(date.getText().trim());
                ValidationUtils.validateAssetInput(i,n,p,c,q,d);
                double x=Double.parseDouble(extra.getText());
                String t=(String)type.getSelectedItem();
                result=t.equals("Stock")?new Stock(i,n,p,c,q,d,x):t.equals("Mutual Fund")?new MutualFund(i,n,p,c,q,d,x):new Bond(i,n,p,c,q,d,x);
                dispose();
            } catch (NumberFormatException e){
                error("Enter valid numeric values.");
            } catch (Exception e){
                error(e.getMessage());
            }
        }
    }
}
