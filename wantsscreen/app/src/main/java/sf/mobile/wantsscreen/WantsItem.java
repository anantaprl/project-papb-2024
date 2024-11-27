package sf.mobile.wantsscreen;

public class WantsItem {
    private String name;
    private String date;
    private String amount;
    private int imageResId;  // Ubah ke tipe int

    public WantsItem(String name, String date, String amount, int imageResId) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.imageResId = imageResId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getImageResId() {  // Ini sekarang sesuai dengan tipe int
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
