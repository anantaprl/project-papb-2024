package ns.mobile.mtreportsfire;

public class Reports {
    private String category;
    private String date;
    private String amount;
    private String categoryLogo;
    private String transactionType;

    public Reports() {
    }

    public Reports(String category, String date, String amount, String categoryLogo, String transactionType) {
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.categoryLogo = categoryLogo;
        this.transactionType = transactionType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(String categoryLogo) {
        this.categoryLogo = categoryLogo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}

