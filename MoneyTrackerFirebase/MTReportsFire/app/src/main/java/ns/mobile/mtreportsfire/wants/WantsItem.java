package ns.mobile.mtreportsfire.wants;

public class WantsItem {
    private String uid;
    private String name;
    private String date;
    private String amount;
    private int imageResId; // Untuk menyimpan ID drawable gambar

    // Constructor
    public WantsItem(String uid, String name, String date, String amount, int imageResId) {
        this.uid = uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}