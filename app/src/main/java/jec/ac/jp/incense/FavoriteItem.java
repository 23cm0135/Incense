package jec.ac.jp.incense;

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
}
