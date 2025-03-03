package jec.ac.jp.incense;

import com.google.firebase.Timestamp;
import java.util.Objects;

public class FavoriteItem {
    private String name;         // 香的名稱
    private String effect;       // 功效
    private String imageUrl;     // 圖片 URL
    private String description;  // 描述
    private String url;          // 購買連結
    private String userId;       // 收藏該香的用戶 UID
    private Timestamp timestamp; // Firestore 的時間戳記

    // 空的構造函數（Firestore 反射需要）
    public FavoriteItem() {}

    // 構造函數：注意不要把 userId 傳入 name 裡
    public FavoriteItem(String name, String effect, String imageUrl, String description, String url) {
        this.name = name;
        this.effect = effect;
        this.imageUrl = imageUrl;
        this.description = description;
        this.url = url;
    }

    // Getter & Setter
    public String getName() { return name; }
    public String getEffect() { return effect; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public String getUserId() { return userId; }
    public Timestamp getTimestamp() { return timestamp; }

    public void setName(String name) { this.name = name; }
    public void setEffect(String effect) { this.effect = effect; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setUrl(String url) { this.url = url; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteItem)) return false;
        FavoriteItem that = (FavoriteItem) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(effect, that.effect) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(description, that.description) &&
                Objects.equals(url, that.url) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, effect, imageUrl, description, url, userId);
    }
}
