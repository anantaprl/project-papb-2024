package sf.mobile.wantsscreen.model;

import com.google.gson.annotations.SerializedName;

public class DataItem {
    private String name;
    private String date;
    private String amount;

    @SerializedName("imageResource") // Cocokkan dengan JSON
    private String imageResource;

    // Getters
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getImageResource() { // Gunakan imageResource di sini
        return imageResource;
    }
}
