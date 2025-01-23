
package jec.ac.jp.incense;

public enum ButtonEnum {

    BUTTON_1("◆大江戸香　ブランドコンセプト◆\n「雅」と「俗」の文化交流が作りあげた、遊びと洒落の江戸文化。\n四季を楽しみ、えにしを尊び、人々がいきいきと暮らしていた江戸の町。\n江戸の匂い立つような暮らしぶりを、ここ江戸は東京の香司が香りで表現。\nひとすじのけむりが、粋でモダンな江戸の世界へとご招待。\nさあさあ、江戸の香りの世界へようこそ。\n\n◆大江戸香　伽羅姫　香りの特徴◆\n江戸の憧れ。粋な女性の嗜み。深く味わいのある伽羅をイメージした香り。",
            R.drawable.senkou1, "https://example.com/button1"),
    BUTTON_2("234567.",
            R.drawable.senkou2, "https://example.com/button2"),
    BUTTON_3("豪華、絢爛な安土桃山文化。...",
            R.drawable.senkou3, "https://example.com/button3"),
    BUTTON_4("豪華、絢爛な安土桃山文化。...",
            R.drawable.senkou4, "https://example.com/button4"),
    BUTTON_5("貴重な香木・沈香(じんこう)を用いた「沈香寿山」です...",
            R.drawable.senkou5, "https://example.com/button5"),
    BUTTON_6("貴重な香木・沈香(じんこう)を用いた「沈香寿山」です...",
            R.drawable.senkou6, "https://example.com/button6"),
    BUTTON_7("甘みのある華やかな味わいの沈香・伽羅を豊富に用い、白檀...",
            R.drawable.senkou7, "https://example.com/button7"),
    BUTTON_8("日本の小京都ともいわれる兵庫県出石にある福住。...",
            R.drawable.senkou8, "https://example.com/button8"),
    BUTTON_9("日本の小京都ともいわれる兵庫県出石にある福住。...",
            R.drawable.senkou9, "https://example.com/button9");

    private final String text;
    private final int imageResId;
    private final String url;

    // 构造函数
    ButtonEnum(String text, int imageResId, String url) {
        this.text = text;
        this.imageResId = imageResId;
        this.url = url;
    }

    // 获取文本内容
    public String getText() {
        return text;
    }

    // 获取图片资源 ID
    public int getImageResId() {
        return imageResId;
    }

    // 获取链接
    public String getUrl() {
        return url;
    }
}
