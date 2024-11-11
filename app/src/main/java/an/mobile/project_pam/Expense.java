package an.mobile.project_pam;

public class Expense {
    private String category;
    private String date;
    private String amount;

    public Expense(String category, String date, String amount) {
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}