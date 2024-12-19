package jec.ac.jp.incense;

import android.os.Parcel;
import android.os.Parcelable;

public class Incense implements Parcelable {

    private String name;
    private String effect;
    private String material;
    private String imageUrl;
    private String description;  // 只保留描述字段

    // 构造函数
    public Incense(String name, String effect, String material, String imageUrl, String description) {
        this.name = name;
        this.effect = effect;
        this.material = material;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Parcelable 构造函数
    protected Incense(Parcel in) {
        name = in.readString();
        effect = in.readString();
        material = in.readString();
        imageUrl = in.readString();
        description = in.readString();  // 读取描述
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(effect);
        dest.writeString(material);
        dest.writeString(imageUrl);
        dest.writeString(description);  // 写入描述
    }

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

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
