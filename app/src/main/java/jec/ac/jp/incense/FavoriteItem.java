package jec.ac.jp.incense;

import java.util.Objects;

public class FavoriteItem {
    private final String name;
    private final String effect;
    private final String imageUrl;
    private final String description;
    private final String url; // 添加 url 字段

    // 更新构造函数，包含 url
    public FavoriteItem(String name, String effect, String imageUrl, String description, String url) {
        this.name = name;
        this.effect = effect;
        this.imageUrl = imageUrl;
        this.description = description;
        this.url = url;
    }

    // Getter 方法
    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url; // 添加 url 的 getter
    }

    // 更新 equals 方法，包含 url
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteItem that = (FavoriteItem) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(effect, that.effect) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(description, that.description) &&
                Objects.equals(url, that.url); // 比较 url
    }

    // 更新 hashCode 方法，包含 url
    @Override
    public int hashCode() {
        return Objects.hash(name, effect, imageUrl, description, url);
    }
}
