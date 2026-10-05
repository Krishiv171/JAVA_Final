public class Investor {
    private final String investorId;
    private String name, email;

    public Investor(String investorId,String name,String email) {
        if(investorId==null||investorId.isBlank())throw new IllegalArgumentException("Investor ID is required.");
        if(name==null||name.isBlank())throw new IllegalArgumentException("Investor name is required.");
        if(email==null||email.isBlank())throw new IllegalArgumentException("Email is required.");
        this.investorId=investorId;this.name=name;this.email=email;
    }
    public String getInvestorId(){return investorId;}
    public String getName(){return name;}
    public String getEmail(){return email;}
    public void setName(String v){if(v==null||v.isBlank())throw new IllegalArgumentException("Name is required.");name=v;}
    public void setEmail(String v){if(v==null||v.isBlank())throw new IllegalArgumentException("Email is required.");email=v;}
}
