package jec.ac.jp.incense;

import java.util.Objects;

public class FavoriteItem {
    private final String name;
    private final String effect;
    private final String imageUrl; // 可能为空
    private final int imageResId;  // 可能为 0
    private final String description;
    private final String url;

    // **构造函数 (本地图片)**
    public FavoriteItem(String name, String effect, int imageResId, String description, String url) {
        this.name = name;
        this.effect = effect;
        this.imageUrl = null;
        this.imageResId = imageResId;
        this.description = description;
        this.url = url;
    }

    // **构造函数 (网络图片)**
    public FavoriteItem(String name, String effect, String imageUrl, String description, String url) {
        this.name = name;
        this.effect = effect;
        this.imageUrl = imageUrl;
        this.imageResId = 0; // 默认为 0，表示没有本地图片
        this.description = description;
        this.url = url;
    }

    public String getName() { return name; }
    public String getEffect() { return effect; }
    public String getImageUrl() { return imageUrl; }
    public int getImageResId() { return imageResId; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteItem that = (FavoriteItem) o;
        return imageResId == that.imageResId &&
                Objects.equals(name, that.name) &&
                Objects.equals(effect, that.effect) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(description, that.description) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, effect, imageUrl, imageResId, description, url);
    }
}
