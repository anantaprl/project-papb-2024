package ns.mobile.mtreportfrag;

public class Reports {
    public String category;
    public String date;
    public String amount;
    public int categoryLogo;
    public String transactionType;

    public Reports(String category, String date, String amount, int categoryLogo, String transactionType){
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.categoryLogo = categoryLogo;
        this.transactionType = transactionType;
    }
}
