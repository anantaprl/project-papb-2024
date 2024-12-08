package ns.mobile.mtreportsfire.wants.model;

public class DataItem {
    String uid;
    String name;
    String date;
    String amount;
    String imageResource;

    public DataItem() {
        // Konstruktor kosong untuk memanggil snapshot
    }

    public DataItem(String uid, String name, String date, String amount, String imageResource) {
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.imageResource = imageResource;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }


}
