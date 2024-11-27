package sf.mobile.wantsscreen;

public class WantsItem {
    private String name;
    private String date;
    private String amount;
    private int imageResId; // Untuk menyimpan ID drawable gambar

    // Constructor
    public WantsItem(String name, String date, String amount, int imageResId) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.imageResId = imageResId;
    }

    // Getter methods
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getAmount() { return amount; }
    public int getImageResId() { return imageResId; }
}