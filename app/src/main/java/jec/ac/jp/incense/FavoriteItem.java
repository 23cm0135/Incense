package jec.ac.jp.incense;

public class FavoriteItem {
    private final String name;
    private final String effect;
    private final String imageUrl;

    public FavoriteItem(String name, String effect, String imageUrl) {
        this.name = name;
        this.effect = effect;
        this.imageUrl = imageUrl;
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
}
