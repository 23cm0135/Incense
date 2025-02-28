package jec.ac.jp.incense; // 确保包名与你的项目包名匹配

public class ChatMessage {
    private String message;
    private boolean isAi;

    public ChatMessage(String message, boolean isAi) {
        this.message = message;
        this.isAi = isAi;
    }

    public String getMessage() {
        return message;
    }

    public boolean getIsAi() {
        return isAi;
    }
}
