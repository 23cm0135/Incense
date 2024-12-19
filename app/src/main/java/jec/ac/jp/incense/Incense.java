package jec.ac.jp.incense;
import android.os.Parcel;
import android.os.Parcelable;

public class Incense implements Parcelable {

    private String name;
    private String effect;
    private String material;
    private String imageUrl;
    private String description;
    private String url; // 新增 URL 属性

    public Incense(String name, String effect, String material, String imageUrl, String description, String url) {
        this.name = name;
        this.effect = effect;
        this.material = material;
        this.imageUrl = imageUrl;
        this.description = description;
        this.url = url;
    }

    // Parcelable 构造方法
    protected Incense(Parcel in) {
        name = in.readString();
        effect = in.readString();
        material = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        url = in.readString();
    }

    // 必须实现的方法：写入到 Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(effect);
        dest.writeString(material);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(url);
    }

    // 必须实现的方法：内容描述，通常返回 0
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 的 Creator，用于反序列化
    public static final Creator<Incense> CREATOR = new Creator<Incense>() {
        @Override
        public Incense createFromParcel(Parcel in) {
            return new Incense(in);
        }

        @Override
        public Incense[] newArray(int size) {
            return new Incense[size];
        }
    };

    // Getter 方法
    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public String getMaterial() {
        return material;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
