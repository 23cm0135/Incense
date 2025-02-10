package jec.ac.jp.incense;

public class Post {
    private String username;
    private String content;
    private String incenseName;

    public Post(String username, String content, String incenseName) {
        this.username = username;
        this.content = content;
        this.incenseName = incenseName;
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
}
