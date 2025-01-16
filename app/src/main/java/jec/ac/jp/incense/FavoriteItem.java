package jec.ac.jp.incense;

import java.util.Objects;

public class FavoriteItem {
    private final String name;
    private final String effect;
    private final String imageUrl;
    private final String description;

    public FavoriteItem(String name, String effect, String imageUrl, String description) {
        this.name = name;
        this.effect = effect;
        this.imageUrl = imageUrl;
        this.description = description; // 正确赋值
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteItem that = (FavoriteItem) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(effect, that.effect) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, effect, imageUrl, description);
    }
}
