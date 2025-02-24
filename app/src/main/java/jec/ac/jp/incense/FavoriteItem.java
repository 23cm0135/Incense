package jec.ac.jp.incense;

import java.util.Objects;

public class FavoriteItem {
    private String name;         // 香的名称
    private String effect;       // 功效
    private String imageUrl;     // 网络图片 URL
    private String description;  // 描述
    private String url;          // 购买链接

    // Firestore 反射用的默认构造函数
    public FavoriteItem() {}

    // 网络图片用构造函数
    public FavoriteItem(String name, String effect, String imageUrl, String description, String url) {
        this.name = name;
        this.effect = effect;
        this.imageUrl = imageUrl;
        this.description = description;
        this.url = url;
    }

    // Getter 方法
    public String getName() { return name; }
    public String getEffect() { return effect; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }

    // Setter 方法
    public void setName(String name) { this.name = name; }
    public void setEffect(String effect) { this.effect = effect; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setUrl(String url) { this.url = url; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteItem that = (FavoriteItem) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(effect, that.effect) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(description, that.description) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, effect, imageUrl, description, url);
    }
}
