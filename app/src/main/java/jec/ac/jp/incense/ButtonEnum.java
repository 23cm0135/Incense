package jec.ac.jp.incense;

public enum ButtonEnum {

    BUTTON_1("◆大江戸香　ブランドコンセプト◆\n「雅」と「俗」の文化交流が作りあげた、遊びと洒落の江戸文化。\n四季を楽しみ、えにしを尊び、人々がいきいきと暮らしていた江戸の町。\n江戸の匂い立つような暮らしぶりを、ここ江戸は東京の香司が香りで表現。\nひとすじのけむりが、粋でモダンな江戸の世界へとご招待。\nさあさあ、江戸の香りの世界へようこそ。\n\n◆大江戸香　伽羅姫　香りの特徴◆\n江戸の憧れ。粋な女性の嗜み。深く味わいのある伽羅をイメージした香り。",
            R.drawable.senkou1, "https://www.nipponkodo.co.jp/shop/products/detail/38347?srsltid=AfmBOopMvHnlj3cjy1WhZqyen2IVS3sJ1MWXRpunJ8BS-ESnxIdQnpie"),

    BUTTON_2("古来より、各地には『神の木』といわれてきた香木があります。\n" +
            "それらは、祈りや心身や空間を浄化するために使われ、\n" +
            "古人の智慧として受け継がれてきました。\n" +
            "Chie(チエ)は、合成香料を使わず、自然の香原料と粉体のみを使い、\n" +
            "今を生きる香りとして開発されたブランドです。\n" +
            "自然とつながり、心を解放してください。",
            R.drawable.senkou2, "https://www.nipponkodo.co.jp/shop/products/detail/38918?srsltid=AfmBOoqur34kLkUG1N6WBJVdCVsaI7brk2pWLEJq9VChgyweueeQ93_1"),

    BUTTON_3("豪華、絢爛な安土桃山文化。\n" +
            "この時代には、茶の湯、連歌、能楽など、人の和を意図したさまざまな寄り合いが行われました。\n" +
            "香道もその一つで香木がおおいに用いられ、人の心を和ませるものとして貴重なものでした。",
            R.drawable.senkou3, "https://www.nipponkodo.co.jp/shop/products/detail/38642"),

    BUTTON_4("伽羅桃山は入手の難しい甘みのあるベトナム産の沈香を豊富に用いた、華やかな桃山の時代にふさわしい香りです。",
            R.drawable.senkou4, "https://www.nipponkodo.co.jp/shop/products/detail/38246"),

    BUTTON_5("貴重な香木・沈香(じんこう)を用いた「沈香寿山」です。さっぱりとした味わいの沈香をはじめ、白檀・生薬系香料を豊富に使用しています。",
            R.drawable.senkou5, "https://www.nipponkodo.co.jp/shop/products/detail/36591"),

    BUTTON_6("貴重な香木・沈香(じんこう)を用いた「沈香寿山」です。天然素材の香りの活きたきりりと深い香りです。",
            R.drawable.senkou6, "https://www.nipponkodo.co.jp/shop/products/detail/36590"),

    BUTTON_7("甘みのある華やかな味わいの沈香・伽羅を豊富に用い、白檀・生薬系香料を調合しております。",
            R.drawable.senkou7, "https://www.nipponkodo.co.jp/shop/products/detail/36585"),

    BUTTON_8("日本の小京都ともいわれる兵庫県出石にある福住。幸福が宿る香りを創香したいという想いを込めたブランドです。",
            R.drawable.senkou8, "https://www.nipponkodo.co.jp/shop/products/detail/38881"),

    BUTTON_9("日本の小京都ともいわれる兵庫県出石にある福住。癒しに良いと言われるラベンダーの香りのお香です。",
            R.drawable.senkou9, "https://www.nipponkodo.co.jp/shop/products/detail/38878");

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
