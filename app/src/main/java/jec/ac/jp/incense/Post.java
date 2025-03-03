package jec.ac.jp.incense;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {
    private String username;
    private String content;
    private String incenseName;
    private long timestamp;
    private String userId; // 貼文作者的 UID

    // 完整構造函數
    public Post(String username, String content, String incenseName, long timestamp, String userId) {
        this.username = username;
        this.content = content;
        this.incenseName = incenseName;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    // 無參構造函數（Firestore 反射用）
    public Post() {}

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getIncenseName() {
        return incenseName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN);
        return sdf.format(new Date(timestamp));
    }
}
