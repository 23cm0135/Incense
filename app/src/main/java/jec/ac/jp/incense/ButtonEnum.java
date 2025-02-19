package jec.ac.jp.incense;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public enum ButtonEnum {

    BUTTON_1( "id_1","大江戸香 伽羅姫","◆大江戸香　ブランドコンセプト◆\n「雅」と「俗」の文化交流が作りあげた、遊びと洒落の江戸文化。\n四季を楽しみ、えにしを尊び、人々がいきいきと暮らしていた江戸の町。\n江戸の匂い立つような暮らしぶりを、ここ江戸は東京の香司が香りで表現。\nひとすじのけむりが、粋でモダンな江戸の世界へとご招待。\nさあさあ、江戸の香りの世界へようこそ。\n\n◆大江戸香　伽羅姫　香りの特徴◆\n江戸の憧れ。粋な女性の嗜み。深く味わいのある伽羅をイメージした香り。",
            R.drawable.senkou1, "https://www.nipponkodo.co.jp/shop/products/detail/38347?srsltid=AfmBOopMvHnlj3cjy1WhZqyen2IVS3sJ1MWXRpunJ8BS-ESnxIdQnpie"),

    BUTTON_2("id_2","チエ","古来より、各地には『神の木』といわれてきた香木があります。\n" +
            "それらは、祈りや心身や空間を浄化するために使われ、\n" +
            "古人の智慧として受け継がれてきました。\n" +
            "Chie(チエ)は、合成香料を使わず、自然の香原料と粉体のみを使い、\n" +
            "今を生きる香りとして開発されたブランドです。\n" +
            "自然とつながり、心を解放してください。",
            R.drawable.senkou2, "https://www.nipponkodo.co.jp/shop/products/detail/38918?srsltid=AfmBOoqur34kLkUG1N6WBJVdCVsaI7brk2pWLEJq9VChgyweueeQ93_1"),

    BUTTON_3("id_3","伽羅桃山","豪華、絢爛な安土桃山文化。\n" +
            "この時代には、茶の湯、連歌、能楽など、人の和を意図したさまざまな寄り合いが行われました。\n" +
            "香道もその一つで香木がおおいに用いられ、人の心を和ませるものとして貴重なものでした。",
            R.drawable.senkou3, "https://www.nipponkodo.co.jp/shop/products/detail/38642"),

    BUTTON_4("id_4","伽羅桃山","伽羅桃山は入手の難しい甘みのあるベトナム産の沈香を豊富に用いた、華やかな桃山の時代にふさわしい香りです。",
            R.drawable.senkou4, "https://www.nipponkodo.co.jp/shop/products/detail/38246"),

    BUTTON_5("id_5","沈香寿山","貴重な香木・沈香(じんこう)を用いた「沈香寿山」です。さっぱりとした味わいの沈香をはじめ、白檀・生薬系香料を豊富に使用しています。",
            R.drawable.senkou5, "https://www.nipponkodo.co.jp/shop/products/detail/36591"),

    BUTTON_6("id_6","沈香寿山","貴重な香木・沈香(じんこう)を用いた「沈香寿山」です。天然素材の香りの活きたきりりと深い香りです。",
            R.drawable.senkou6, "https://www.nipponkodo.co.jp/shop/products/detail/36590"),

    BUTTON_7("id_7","伽羅金剛","甘みのある華やかな味わいの沈香・伽羅を豊富に用い、白檀・生薬系香料を調合しております。",
            R.drawable.senkou7, "https://www.nipponkodo.co.jp/shop/products/detail/36585"),

    BUTTON_8("id_8","愛と誠実の白玉蘭","日本の小京都ともいわれる兵庫県出石にある福住。幸福が宿る香りを創香したいという想いを込めたブランドです。",
            R.drawable.senkou8, "https://www.nipponkodo.co.jp/shop/products/detail/38881"),

    BUTTON_9("id_9","癒しのラベンダー","日本の小京都ともいわれる兵庫県出石にある福住。癒しに良いと言われるラベンダーの香りのお香です。",
            R.drawable.senkou9, "https://www.nipponkodo.co.jp/shop/products/detail/38878"),

    BUTTON_10("id_10","伽羅静玄 棒状","大自然に育まれた香木は、古来より人の心をとらえ、高貴で貴重なものとして、大切に扱われてきました。",
             R.drawable.senkou10, "https://www.koju.co.jp/shop/products/detail/1668"),
    BUTTON_11("id_11","高井十右衛門","清和源氏の末裔、安田又右衛門源光弘を初代とする香十は、天正年間の初め、京で創業し御所御用も務めていました。",
            R.drawable.senkou11, "https://www.koju.co.jp/shop/products/detail/1357"),
    BUTTON_12("id_12","いろは 玄関先のおとめ椿","千重咲きの「乙女椿」グリーンフローラルの美しくエレガントな香りで表現しました",
            R.drawable.senkou12, "https://www.koju.co.jp/shop/products/detail/1536"),
    BUTTON_13("id_13","福住　招福の柚子","縁起が良く⾦運を招くと言われる柚子の香りのお香です。",
              R.drawable.senkou13, "https://www.nipponkodo.co.jp/shop/products/detail/38882"),
    BUTTON_14("id_14","ｲﾝｾﾝｽ ﾌﾟﾚﾐｱﾑ 紫野","最高品質の沈香や白檀を合わせた、平安時代から伝わる奥深い香りです。",
            R.drawable.senkou14, "https://www.kyukyodo-shop.co.jp/?pid=182693179"),
    BUTTON_15("id_15","ｲﾝｾﾝｽ ﾌﾟﾚﾐｱﾑ 武蔵野","最高級の伽羅に沈香や白檀など天然の香料を合わせた、凛とした幽玄な香りです。",
            R.drawable.senkou15, "https://www.kyukyodo-shop.co.jp/?pid=182693710"),
    BUTTON_16("id_16","ｲﾝｾﾝｽ ﾌﾟﾚﾐｱﾑ 冬の夜","老山白檀を主体に沈香など天然香料を合わせた、甘く爽やかな香りです。",
            R.drawable.senkou16, "https://www.kyukyodo-shop.co.jp/?pid=182693811"),
    BUTTON_17("id_17","お香 六種の薫物 黒方","平安時代から伝わる煉香の香りを、手軽に楽しめるスティックタイプのお香に仕立てました。\n" +
            "黒方は慶事の他、四季を通じて用いられ、玄妙な香りが特長のお香です。",
            R.drawable.senkou17, "https://www.kyukyodo-shop.co.jp/?pid=136583045"),
    BUTTON_18("id_18","お香 六種の薫物 梅花","平安時代から伝わる煉香の香りを、手軽に楽しめるスティックタイプのお香に仕立てました。\n" +
            "梅花は梅の花を想わせる、春の香りです。",
            R.drawable.senkou18, "https://www.kyukyodo-shop.co.jp/?pid=136583047"),
    BUTTON_19("id_19","お香セット 香木の香り","伽羅・沈香・白檀 のセットです。",
            R.drawable.senkou19, "https://www.kyukyodo-shop.co.jp/?pid=136583089"),
    BUTTON_20("id_20","百楽香(ひゃくらくこう)木蓮","厳選された天然白檀と原料を調合した上質なお香です。\n" +
            "お部屋焚き用はもちろん御仏前用としてもお使い頂けます。",
            R.drawable.senkou20, "https://kuyukou.com/products/%E7%99%BE%E6%A5%BD%E9%A6%99-%E3%81%B2%E3%82%83%E3%81%8F%E3%82%89%E3%81%8F%E3%81%93%E3%81%86-%E6%9C%A8%E8%93%AE-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF40%E6%9C%AC"),
    BUTTON_21("id_21","かゆらぎ 白檀","ここちよい香りもその 『 ゆらぎ 』 のひとつかもしれません。\n" +
            "「かゆらぎ」は、そんな自然の恵みを想起させる和風の香りをお香にしました。" +
            "お部屋焚き用はもちろん御仏前用としてもお使い頂けます。",
            R.drawable.senkou21, "https://kuyukou.com/products/kayuragi-byakudan"),
    BUTTON_22("id_22","以花伝心　金木犀","秋の始まりを告げる、金木犀の香り",
            R.drawable.senkou22, "https://kuyukou.com/products/%E4%BB%A5%E8%8A%B1%E4%BC%9D%E5%BF%83-%E9%87%91%E6%9C%A8%E7%8A%80-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF20%E6%9C%AC%E5%85%A5"),
    BUTTON_23("id_23","百楽香（ひゃくらくこう）楠","森の香気を浴びるような楠の香り",
            R.drawable.senkou23, "https://kuyukou.com/products/%E7%99%BE%E6%A5%BD%E9%A6%99-%E3%81%B2%E3%82%83%E3%81%8F%E3%82%89%E3%81%8F%E3%81%93%E3%81%86-%E6%A5%A0-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF40%E6%9C%AC"),
    BUTTON_24("id_24","ESTEBAN（エステバン） ピュアリネン","エステバンのお香は400年以上にわたって育まれた日本伝統の薫香技術を用いて、日本で丁寧に作り上げられています。\n" +
            "洗練された芳香で、落ち着きのある雰囲気をお楽しみいただけます。",
            R.drawable.senkou24, "https://kuyukou.com/products/esteban-%E3%82%A8%E3%82%B9%E3%83%86%E3%83%90%E3%83%B3-%E3%83%94%E3%83%A5%E3%82%A2%E3%83%AA%E3%83%8D%E3%83%B3-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF40%E6%9C%AC%E5%85%A5"),

    BUTTON_25("id_25","山紫水明 嵯峨の朝霧　","『山紫水明』は、四季毎に色合いを変える西陣織のような山々に囲まれ、友禅流しの名残をとどめる澄んだ川を抱く京都。そんな自然の香りをお香に托したシリーズです。「嵯峨の朝霧」浅緑の竹林をしっとり濡らす嵯峨の朝霧をイメージした、緑色のお香です。シンプルでクリーンな、草原の香り。保存に適した桐箱入りです。",
              R.drawable.senkou25, "https://kuyukou.com/products/sanshisuimei_saga"),
    BUTTON_26("id_26","山紫水明　嵐山の月影","「嵐山の月影」梔子(くちなし)色にひんやりと川面に映る、嵐山の月影をイメージした灰色のお香です。エキゾチックで甘さを抑えた、大人っぽい香りです。保存に適した桐箱入りです。",
              R.drawable.senkou26, "https://kuyukou.com/products/%E5%B1%B1%E7%B4%AB%E6%B0%B4%E6%98%8E-%E5%B5%90%E5%B1%B1%E3%81%AE%E6%9C%88%E5%BD%B1-%E6%A1%90%E7%AE%B1%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF30%E6%9C%AC%E5%85%A5"),
    BUTTON_27("id_27","山紫水明　祇園の華","「祇園の華」今様色にはんなりとすれ違い香る、祇園の華をイメージした桃色のお香です。数種の花たちが醸し出す、情熱的な香り。保存に適した桐箱入りです",
            R.drawable.senkou27, "https://kuyukou.com/products/%E5%B1%B1%E7%B4%AB%E6%B0%B4%E6%98%8E-%E7%A5%87%E5%9C%92%E3%81%AE%E8%8F%AF-%E6%A1%90%E7%AE%B1%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF30%E6%9C%AC%E5%85%A5"),
    BUTTON_28("id_28","山紫水明　加茂のせせらぎ","「加茂のせせらぎ」は、浅縹(あさはなだ)色に水澄む、加茂のせせらぎをイメージした、濃紺色のお香です。みずみずしい爽やかな香りに、フローラル系のやさしい香りをプラスしたユニセックスな香り。保存に適した桐箱入りです。",
              R.drawable.senkou28, "https://kuyukou.com/products/%E5%B1%B1%E7%B4%AB%E6%B0%B4%E6%98%8E-%E5%8A%A0%E8%8C%82%E3%81%AE%E3%81%9B%E3%81%9B%E3%82%89%E3%81%8E-%E6%A1%90%E7%AE%B1%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF30%E6%9C%AC%E5%85%A5"),
    BUTTON_29("id_29","香源のお香 日本の香り しゃむ","『日本の香り しゃむ』は２種類のベトナム産沈香を使った、甘みのある香りです。\n" +
            "香木の香りは、時の権力者たちに古くから愛されてきました。そんな香りの歴史に想いを馳せつつ、芳醇な香りをどうぞお楽しみください。",
            R.drawable.senkou29, "https://www.kohgen.com/i/kc-okoh-M0002");





    private final String id;
    private final String text;
    private final int imageResId;
    private final String url;
    private final String incenseName;


    // 构造函数
    ButtonEnum(String id,String incenseName,String text, int imageResId, String url) {
        this.id = id;
        this.text = text;
        this.imageResId = imageResId;
        this.url = url;
        this.incenseName = incenseName;

    }
    public String getId() { return id; }
    public String getIncenseName() { return incenseName; }

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
    public static List<ButtonEnum> getRandomButtons() {
        List<ButtonEnum> buttonList = Arrays.asList(ButtonEnum.values());
        Collections.shuffle(buttonList);
        return buttonList.subList(0, Math.min(9, buttonList.size())); // 取前9個
    }
}
