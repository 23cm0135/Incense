package jec.ac.jp.incense;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {
    private String username;
    private String content;
    private String incenseName;
    private long timestamp; // 新增時間戳記

    public Post(String username, String content, String incenseName, long timestamp) {
        this.username = username;
        this.content = content;
        this.incenseName = incenseName;
        this.timestamp = timestamp;
    }

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

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN);
        return sdf.format(new Date(timestamp));
    }
}
