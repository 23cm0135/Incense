package jec.ac.jp.incense;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public enum ButtonEnum {

    BUTTON_1( "id_1","numuon 【 最高級白檀 × 淡路島の伝統製法 】 お香 線香 白檀 サンダルウッド","◆お香の生産量日本一“香りの島”淡路島。その淡路島で創業して130年以上の歴史を持つ会社にご協力いただいて作ったお香です。淡路島の中で数人しかいない「香司」が素材を吟味し、調合したオリジナルの香りとなっています。香司が作成したレシピをもとに熟練職人が130年以上受け継がれてきた伝統製法を用いて製作。強すぎない自然な香りをお楽しみいただけます。",
            R.drawable.senkou30, "https://www.amazon.co.jp/numuon-%E6%9C%80%E9%AB%98%E7%B4%9A%E7%99%BD%E6%AA%80-%E6%B7%A1%E8%B7%AF%E5%B3%B6%E3%81%AE%E4%BC%9D%E7%B5%B1%E8%A3%BD%E6%B3%95-%E3%82%B5%E3%83%B3%E3%83%80%E3%83%AB%E3%82%A6%E3%83%83%E3%83%89-%E9%A6%99%E7%AB%8B%E3%81%AA%E3%81%97/dp/B0DVSZMSLL/ref=sr_1_1_sspa?adgrpid=60019607251&dib=eyJ2IjoiMSJ9.tnCEtG5Dp6NScMQ2Z-dA-cbxnquL_3lhcB1Q1SAyNSxKvGpo-czyILT2P3-Q-rJg8l4jzdZDyRzlSPTujYM6Ji_bk7FMogPYBef0A7Pd7HtTh9I5kf3DBgQr9XzEg_8YVAbYBq_zIz67GUfLlkweEP_QGg_6ktyDjrhxLIv_aZJrnZ-lMP2eawO_uSaVk3JnQBQbMgO7CQ6L2hciVt-QhP6GkPOF6NPyXoxiwvS3c8rEc5JUhWSCMnhCLG4oGgM49lpF-SvzkVD9bPLBwxTfbgj4GY-gJY3sKzldmgbQJu0.nKstL2NNgsQ5joK42JqAVJEbUdYcP9I_KkQ-Ow76Zyk&dib_tag=se&hvadid=651368982896&hvdev=c&hvlocphy=1009310&hvnetw=g&hvqmt=b&hvrand=11885213549811437436&hvtargid=kwd-590424412778&hydadcr=10270_13607493&jp-ad-ap=0&keywords=amazon+%E3%81%8A%E9%A6%99&mcid=a2303e9bef2030bf8ce65fad6feed139&qid=1740012095&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1"),

    BUTTON_2("id_2","hono 線香 白檀 天然由来香料 香木白檀サンダルウッド 浄化","白檀は香木系のお線香やフレグランス系の香のベースなど、最も香原料に使われている香木です。 ”古き良きお寺”を連想させる、名前を知らなくても一番知られている、お香の中でも親しみのある香りかもしれません。白檀の木で香るのは木の中心部分（心材）と根の部分だけ。伐採してから香りが立って熟成するまでに20年以上の長い年月がかかります。",
            R.drawable.senkou31, "https://www.amazon.co.jp/hono-%E5%A4%A9%E7%84%B6%E7%94%B1%E6%9D%A5%E9%A6%99%E6%96%99-%E9%A6%99%E6%9C%A8%E7%99%BD%E6%AA%80%E3%82%B5%E3%83%B3%E3%83%80%E3%83%AB%E3%82%A6%E3%83%83%E3%83%89-%E5%8F%A4%E3%81%8D%E8%89%AF%E3%81%8D%E6%97%A5%E6%9C%AC%E3%81%AE%E3%81%8A%E9%A6%99-%E3%82%A4%E3%83%B3%E3%82%BB%E3%83%B3%E3%82%B9%E3%81%8A%E7%B7%9A%E9%A6%99/dp/B0DDX21P26/ref=sr_1_2_sspa?adgrpid=60019607251&dib=eyJ2IjoiMSJ9.tnCEtG5Dp6NScMQ2Z-dA-cbxnquL_3lhcB1Q1SAyNSxKvGpo-czyILT2P3-Q-rJg8l4jzdZDyRzlSPTujYM6Ji_bk7FMogPYBef0A7Pd7HtTh9I5kf3DBgQr9XzEg_8YVAbYBq_zIz67GUfLlkweEP_QGg_6ktyDjrhxLIv_aZJrnZ-lMP2eawO_uSaVk3JnQBQbMgO7CQ6L2hciVt-QhP6GkPOF6NPyXoxiwvS3c8rEc5JUhWSCMnhCLG4oGgM49lpF-SvzkVD9bPLBwxTfbgj4GY-gJY3sKzldmgbQJu0.nKstL2NNgsQ5joK42JqAVJEbUdYcP9I_KkQ-Ow76Zyk&dib_tag=se&hvadid=651368982896&hvdev=c&hvlocphy=1009310&hvnetw=g&hvqmt=b&hvrand=11885213549811437436&hvtargid=kwd-590424412778&hydadcr=10270_13607493&jp-ad-ap=0&keywords=amazon%2B%E3%81%8A%E9%A6%99&mcid=a2303e9bef2030bf8ce65fad6feed139&qid=1740012095&sr=8-2-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&th=1"),

    BUTTON_3("id_3","【 金木犀の香りを追求 】お香 キンモクセイ 線香 煙の少ない 白檀 アロマ","キンモクセイの香りに合う最高級のインド産老山白檀など様々な成分をブレンドしています。花と香木、それぞれの個性を引き出しつつ、全体の調和を保つように配合しています。シーンで変化する「 奥行きのある金木犀の香り」があなたを心地よい空間へと誘います",
            R.drawable.senkou32, "https://www.amazon.co.jp/%E9%87%91%E6%9C%A8%E7%8A%80%E3%81%AE%E9%A6%99%E3%82%8A%E3%82%92%E8%BF%BD%E6%B1%82-%E3%82%AD%E3%83%B3%E3%83%A2%E3%82%AF%E3%82%BB%E3%82%A4-%E7%85%99%E3%81%AE%E5%B0%91%E3%81%AA%E3%81%84-%E7%B4%8460%E6%9C%AC%E5%85%A5-ENSEN/dp/B0BX2C4WYX/ref=sr_1_3_sspa?adgrpid=60019607251&dib=eyJ2IjoiMSJ9.tnCEtG5Dp6NScMQ2Z-dA-cbxnquL_3lhcB1Q1SAyNSxKvGpo-czyILT2P3-Q-rJg8l4jzdZDyRzlSPTujYM6Ji_bk7FMogPYBef0A7Pd7HtTh9I5kf3DBgQr9XzEg_8YVAbYBq_zIz67GUfLlkweEP_QGg_6ktyDjrhxLIv_aZJrnZ-lMP2eawO_uSaVk3JnQBQbMgO7CQ6L2hciVt-QhP6GkPOF6NPyXoxiwvS3c8rEc5JUhWSCMnhCLG4oGgM49lpF-SvzkVD9bPLBwxTfbgj4GY-gJY3sKzldmgbQJu0.nKstL2NNgsQ5joK42JqAVJEbUdYcP9I_KkQ-Ow76Zyk&dib_tag=se&hvadid=651368982896&hvdev=c&hvlocphy=1009310&hvnetw=g&hvqmt=b&hvrand=11885213549811437436&hvtargid=kwd-590424412778&hydadcr=10270_13607493&jp-ad-ap=0&keywords=amazon+%E3%81%8A%E9%A6%99&mcid=a2303e9bef2030bf8ce65fad6feed139&qid=1740012095&sr=8-3-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1"),

    BUTTON_4("id_4","少し贅沢な香りの癒し】お香 白檀 線香 約60本入 日本製 上品でやさしい香り 天然由来 淡路島産 サンダルウッド","国内に限られた人数しか存在しない香りの匠「香司」によってこだわり抜かれた原料を調合してできたお香です。香りの産地である淡路島で伝統製法を用いてひとつひとつ丁寧に手作業で生産されています。",
            R.drawable.senkou33, "https://www.amazon.co.jp/%E3%80%90%E5%B0%91%E3%81%97%E8%B4%85%E6%B2%A2%E3%81%AA%E9%A6%99%E3%82%8A%E3%81%AE%E7%99%92%E3%81%97%E3%80%91%E3%81%8A%E9%A6%99-%E4%B8%8A%E5%93%81%E3%81%A7%E3%82%84%E3%81%95%E3%81%97%E3%81%84%E9%A6%99%E3%82%8A-%E3%82%B5%E3%83%B3%E3%83%80%E3%83%AB%E3%82%A6%E3%83%83%E3%83%89-%E3%80%90%E9%95%B7%E3%81%95%EF%BC%9A%E7%B4%8413-5cm-%E7%87%83%E7%84%BC%E6%99%82%E9%96%93%EF%BC%9A%E7%B4%8430%E5%88%86%E3%80%91%E7%81%AF/dp/B0D72J63DZ/ref=sr_1_4_sspa?adgrpid=60019607251&dib=eyJ2IjoiMSJ9.tnCEtG5Dp6NScMQ2Z-dA-cbxnquL_3lhcB1Q1SAyNSxKvGpo-czyILT2P3-Q-rJg8l4jzdZDyRzlSPTujYM6Ji_bk7FMogPYBef0A7Pd7HtTh9I5kf3DBgQr9XzEg_8YVAbYBq_zIz67GUfLlkweEP_QGg_6ktyDjrhxLIv_aZJrnZ-lMP2eawO_uSaVk3JnQBQbMgO7CQ6L2hciVt-QhP6GkPOF6NPyXoxiwvS3c8rEc5JUhWSCMnhCLG4oGgM49lpF-SvzkVD9bPLBwxTfbgj4GY-gJY3sKzldmgbQJu0.nKstL2NNgsQ5joK42JqAVJEbUdYcP9I_KkQ-Ow76Zyk&dib_tag=se&hvadid=651368982896&hvdev=c&hvlocphy=1009310&hvnetw=g&hvqmt=b&hvrand=11885213549811437436&hvtargid=kwd-590424412778&hydadcr=10270_13607493&jp-ad-ap=0&keywords=amazon+%E3%81%8A%E9%A6%99&mcid=a2303e9bef2030bf8ce65fad6feed139&qid=1740012095&sr=8-4-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1"),

    BUTTON_5("id_5","日本香堂(Nippon Kodo) かゆらぎ 白檀","植物が成長するさま、波音、せせらぎ、色や光の変化など、自然の恵みから発せられる「ゆらぎ」はわたしたちに快適な感覚を与えてくれます。ここちよい香りもその「ゆらぎ」のひとつかもしれません。「かゆらぎ」は、そんな自然の恵みを想起させる和風の香りをお香にしました。",
            R.drawable.senkou34, "https://www.amazon.co.jp/%E6%97%A5%E6%9C%AC%E9%A6%99%E5%A0%82-Nippon-Kodo-38405-%E7%99%BD%E6%AA%8040%E6%9C%AC%E5%85%A5/dp/B000FQO01Y/ref=sr_1_5?adgrpid=60019607251&dib=eyJ2IjoiMSJ9.tnCEtG5Dp6NScMQ2Z-dA-cbxnquL_3lhcB1Q1SAyNSxKvGpo-czyILT2P3-Q-rJg8l4jzdZDyRzlSPTujYM6Ji_bk7FMogPYBef0A7Pd7HtTh9I5kf3DBgQr9XzEg_8YVAbYBq_zIz67GUfLlkweEP_QGg_6ktyDjrhxLIv_aZJrnZ-lMP2eawO_uSaVk3JnQBQbMgO7CQ6L2hciVt-QhP6GkPOF6NPyXoxiwvS3c8rEc5JUhWSCMnhCLG4oGgM49lpF-SvzkVD9bPLBwxTfbkpAzZ9QwnG5RVZAyB6Sz34YlI1myRY5ERhFAaWr1h0o809NO5tyZV2C71cyZSIoDKGoLwc-nD956QGx4GhOjjCweUJaOy524-6b8LQ0o94SLQRYOUxtWizSF5UvdlGLzTAnouHIj3rQ6p0FBteF9eX89_xLBB2E5PJ_v7G0pqR1.E-KtQbSDl8rLNCXnXfAfmVLP8_LvMgLzJpIDMH0iKlw&dib_tag=se&hvadid=651368982896&hvdev=c&hvlocphy=1009310&hvnetw=g&hvqmt=b&hvrand=11885213549811437436&hvtargid=kwd-590424412778&hydadcr=10270_13607493&jp-ad-ap=0&keywords=amazon+%E3%81%8A%E9%A6%99&mcid=a2303e9bef2030bf8ce65fad6feed139&qid=1740012095&sr=8-5"),

    BUTTON_6("id_6","【少し贅沢なかぐわしい金木犀の香り】お香 金木犀 線香","お香の焚きはじめは燃焼に伴う煙の香りが立ちますので、くゆりとともに煙の香りでお部屋や心を浄化しましょう。その後、品で少し甘みのあるの金木犀を想わせるような華やかな香りへの移ろいをお楽しみください。",
            R.drawable.senkou35, "https://www.amazon.co.jp/%E3%80%90%E5%B0%91%E3%81%97%E8%B4%85%E6%B2%A2%E3%81%AA%E3%81%8B%E3%81%90%E3%82%8F%E3%81%97%E3%81%84%E9%87%91%E6%9C%A8%E7%8A%80%E3%81%AE%E9%A6%99%E3%82%8A%E3%80%91%E3%81%8A%E9%A6%99-%E3%82%84%E3%81%95%E3%81%97%E3%81%84%E9%A6%99%E3%82%8A-%E3%82%AD%E3%83%B3%E3%83%A2%E3%82%AF%E3%82%BB%E3%82%A4-%E3%80%90%E9%95%B7%E3%81%95%EF%BC%9A%E7%B4%8413-5cm-%E7%87%83%E7%84%BC%E6%99%82%E9%96%93%EF%BC%9A%E7%B4%8430%E5%88%86%E3%80%91%E7%81%AF/dp/B0D62CNBSW/ref=sr_1_6?adgrpid=60019607251&dib=eyJ2IjoiMSJ9.tnCEtG5Dp6NScMQ2Z-dA-cbxnquL_3lhcB1Q1SAyNSxKvGpo-czyILT2P3-Q-rJg8l4jzdZDyRzlSPTujYM6Ji_bk7FMogPYBef0A7Pd7HtTh9I5kf3DBgQr9XzEg_8YVAbYBq_zIz67GUfLlkweEP_QGg_6ktyDjrhxLIv_aZJrnZ-lMP2eawO_uSaVk3JnQBQbMgO7CQ6L2hciVt-QhP6GkPOF6NPyXoxiwvS3c8rEc5JUhWSCMnhCLG4oGgM49lpF-SvzkVD9bPLBwxTfbkpAzZ9QwnG5RVZAyB6Sz34YlI1myRY5ERhFAaWr1h0o809NO5tyZV2C71cyZSIoDKGoLwc-nD956QGx4GhOjjCweUJaOy524-6b8LQ0o94SLQRYOUxtWizSF5UvdlGLzTAnouHIj3rQ6p0FBteF9eX89_xLBB2E5PJ_v7G0pqR1.E-KtQbSDl8rLNCXnXfAfmVLP8_LvMgLzJpIDMH0iKlw&dib_tag=se&hvadid=651368982896&hvdev=c&hvlocphy=1009310&hvnetw=g&hvqmt=b&hvrand=11885213549811437436&hvtargid=kwd-590424412778&hydadcr=10270_13607493&jp-ad-ap=0&keywords=amazon+%E3%81%8A%E9%A6%99&mcid=a2303e9bef2030bf8ce65fad6feed139&qid=1740012095&sr=8-6"),

    BUTTON_7("id_7","【 天然香料100％使用 】お香 線香 最高級の老山白檀","天然香料100％の原材料にこだわりつつも、過剰な包装を控えることで、低コストを実現しました。お気軽に「本物の和のかおり」をお試しください。",
            R.drawable.senkou36, "https://www.amazon.co.jp/%E5%A4%A9%E7%84%B6%E9%A6%99%E6%96%99100%EF%BC%85%E4%BD%BF%E7%94%A8-%E6%9C%80%E9%AB%98%E7%B4%9A%E3%81%AE%E8%80%81%E5%B1%B1%E7%99%BD%E6%AA%80-%E5%8F%A4%E3%81%8D%E3%82%88%E3%81%8D%E3%81%8A%E5%AF%BA%E3%81%AE%E9%A6%99%E3%82%8A-%E3%82%B5%E3%83%B3%E3%83%80%E3%83%AB%E3%82%A6%E3%83%83%E3%83%89-ENSEN/dp/B0B53HN6T4/ref=sr_1_1_sspa?__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&crid=1T6RFLBXL60X1&dib=eyJ2IjoiMSJ9.jpSKBplDguie_S8bE_hBuaBxTU_jhfO8KX0EDLMOKlldVy_fUhsqF3hw7kfaecImpAZ57yi496cAgrh_oadsyG44r6VxGyj6CdVasqQaUhdrjEHy5WGN38HssiFnqs_ykklz8CWTBtykugz7T45umo1nMChXZ-SJ3nKB5bYUv9D5_opSRXumaHBH-I62_9bKFUDdK7R7sKXpsHp9Dt5vcqSNs_qLO1L8XhH3YuPbtyw2hi6iRklV0FWTzpy-5XR_GO2yR2sbPjy_eH4Ej70AtIXV4LYC157OH3xBH-wLIbs.oEVrGH2yv2xprhWpSBK7EFBaAypjdL1CPkKvnMqSDso&dib_tag=se&keywords=%E3%81%8A%E9%A6%99&qid=1740012713&sprefix=%E3%81%8A%E9%A6%99%2Caps%2C180&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1"),

    BUTTON_8("id_8","ENSEN お香 沈香 線香【 天然香料100％使用 】最高級のタニ沈香 ","沈香(じんこう)は別名アガーウッドと呼ばれ、日本最古の香りです。 名前の由来は普通の木よりも重く、「水に沈む」特徴から沈香と呼ばれています。 香木として利用できるまで、少なくとも50年はかかる大変貴重な香りです",
            R.drawable.senkou37, "https://www.amazon.co.jp/ENSEN-%E5%A4%A9%E7%84%B6%E9%A6%99%E6%96%99100%EF%BC%85%E4%BD%BF%E7%94%A8-%E3%80%91%E6%9C%80%E9%AB%98%E7%B4%9A%E3%81%AE%E3%82%BF%E3%83%8B%E6%B2%88%E9%A6%99-%E5%8F%A4%E3%81%8D%E3%82%88%E3%81%8D%E3%81%8A%E5%AF%BA%E3%81%AE%E9%A6%99%E3%82%8A-%E7%B4%8460%E6%9C%AC%E5%85%A5/dp/B0D6NCJH83/ref=sr_1_4_sspa?__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&crid=1T6RFLBXL60X1&dib=eyJ2IjoiMSJ9.jpSKBplDguie_S8bE_hBuaBxTU_jhfO8KX0EDLMOKlldVy_fUhsqF3hw7kfaecImpAZ57yi496cAgrh_oadsyG44r6VxGyj6CdVasqQaUhdrjEHy5WGN38HssiFnqs_ykklz8CWTBtykugz7T45umo1nMChXZ-SJ3nKB5bYUv9D5_opSRXumaHBH-I62_9bKFUDdK7R7sKXpsHp9Dt5vcqSNs_qLO1L8XhH3YuPbtyw2hi6iRklV0FWTzpy-5XR_GO2yR2sbPjy_eH4Ej70AtIXV4LYC157OH3xBH-wLIbs.oEVrGH2yv2xprhWpSBK7EFBaAypjdL1CPkKvnMqSDso&dib_tag=se&keywords=%E3%81%8A%E9%A6%99&qid=1740012713&sprefix=%E3%81%8A%E9%A6%99%2Caps%2C180&sr=8-4-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1"),

    BUTTON_9("id_9","ホワイトムスク お香","スモーキーなニュアンスと香りの個性を両立させたJohn’s Blendのインセンススティック",
            R.drawable.senkou38, "https://www.amazon.co.jp/Johns-Blend-%E3%82%B8%E3%83%A7%E3%83%B3%E3%82%BA%E3%83%96%E3%83%AC%E3%83%B3%E3%83%89-%E3%82%A4%E3%83%B3%E3%82%BB%E3%83%B3%E3%82%B9%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF-OX-JOW-5-1/dp/B0CTQ1YKT2/ref=sr_1_7?__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&crid=1T6RFLBXL60X1&dib=eyJ2IjoiMSJ9.jpSKBplDguie_S8bE_hBuaBxTU_jhfO8KX0EDLMOKlldVy_fUhsqF3hw7kfaecImpAZ57yi496cAgrh_oadsyG44r6VxGyj6CdVasqQaUhdrjEHy5WGN38HssiFnqs_ykklz8CWTBtykugz7T45umo1nMChXZ-SJ3nKB5bYUv9D5_opSRXumaHBH-I62_9bKFUDdK7R7sKXpsHp9Dt5vcqSNs_qLO1L8XhH3YuPbtyw2hi6iRklV0FWTzpy-5XR_GO2yR2sbPjy_eH4Ej70AtHHXZManfqoBWi6znfNWjSq0V47SodmrOwbJOwukX4Qb-P6MeInDAxJtu-9WWAK2YzbXBEMxDiNzQSePQW9OcyPuzLI7yLqO-N63HDRztd9yMmRscpb40RqA0KAJMq9iTz4tVGDmP0JZoCnSPe4sL5tzLbJF2YuIeNB0VD1uN76z.IXbwQbB7RdcyDxXvcLrBXGaYtS9wT9j3F7nzdeJ3zCQ&dib_tag=se&keywords=%E3%81%8A%E9%A6%99&qid=1740012713&sprefix=%E3%81%8A%E9%A6%99%2Caps%2C180&sr=8-7&th=1"),

    BUTTON_10("id_10","グランセンス インセンススティック ホワイトムスク","【 grancense グランセンス 】 記憶と感性を刺激する絶妙なブレンドアロマ\n" +
            "【 香りのインテリア 】 クラシックでありながらアバンギャルド、個性的でありながらどこか懐かしく親しみやすい深みのあるブレンドアロマ",
             R.drawable.senkou39, "https://www.amazon.co.jp/%E3%82%B0%E3%83%A9%E3%83%B3%E3%82%BB%E3%83%B3%E3%82%B9-%E3%82%A4%E3%83%B3%E3%82%BB%E3%83%B3%E3%82%B9%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF-%E3%83%9B%E3%83%AF%E3%82%A4%E3%83%88%E3%83%A0%E3%82%B9%E3%82%AF-%E7%B4%8430%E6%9C%AC-%E3%83%99%E3%83%AB%E3%82%AC%E3%83%A2%E3%83%83%E3%83%88%E3%81%A8%E3%83%9F%E3%83%B3%E3%83%88%E3%81%AE%E9%80%8F%E6%98%8E%E6%84%9F%E3%81%82%E3%82%8B%E9%A6%99%E3%82%8A/dp/B00NHJHI7O/ref=sr_1_8?__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&crid=1T6RFLBXL60X1&dib=eyJ2IjoiMSJ9.jpSKBplDguie_S8bE_hBuaBxTU_jhfO8KX0EDLMOKlldVy_fUhsqF3hw7kfaecImpAZ57yi496cAgrh_oadsyG44r6VxGyj6CdVasqQaUhdrjEHy5WGN38HssiFnqs_ykklz8CWTBtykugz7T45umo1nMChXZ-SJ3nKB5bYUv9D5_opSRXumaHBH-I62_9bKFUDdK7R7sKXpsHp9Dt5vcqSNs_qLO1L8XhH3YuPbtyw2hi6iRklV0FWTzpy-5XR_GO2yR2sbPjy_eH4Ej70AtHHXZManfqoBWi6znfNWjSq0V47SodmrOwbJOwukX4Qb-P6MeInDAxJtu-9WWAK2YzbXBEMxDiNzQSePQW9OcyPuzLI7yLqO-N63HDRztd9yMmRscpb40RqA0KAJMq9iTz4tVGDmP0JZoCnSPe4sL5tzLbJF2YuIeNB0VD1uN76z.IXbwQbB7RdcyDxXvcLrBXGaYtS9wT9j3F7nzdeJ3zCQ&dib_tag=se&keywords=%E3%81%8A%E9%A6%99&qid=1740012713&sprefix=%E3%81%8A%E9%A6%99%2Caps%2C180&sr=8-8");
//    BUTTON_11("id_11","高井十右衛門","清和源氏の末裔、安田又右衛門源光弘を初代とする香十は、天正年間の初め、京で創業し御所御用も務めていました。",
//            R.drawable.senkou11, "https://www.koju.co.jp/shop/products/detail/1357"),
//    BUTTON_12("id_12","いろは 玄関先のおとめ椿","千重咲きの「乙女椿」グリーンフローラルの美しくエレガントな香りで表現しました",
//            R.drawable.senkou12, "https://www.koju.co.jp/shop/products/detail/1536"),
//    BUTTON_13("id_13","福住　招福の柚子","縁起が良く⾦運を招くと言われる柚子の香りのお香です。",
//              R.drawable.senkou13, "https://www.nipponkodo.co.jp/shop/products/detail/38882"),
//    BUTTON_14("id_14","ｲﾝｾﾝｽ ﾌﾟﾚﾐｱﾑ 紫野","最高品質の沈香や白檀を合わせた、平安時代から伝わる奥深い香りです。",
//            R.drawable.senkou14, "https://www.kyukyodo-shop.co.jp/?pid=182693179"),
//    BUTTON_15("id_15","ｲﾝｾﾝｽ ﾌﾟﾚﾐｱﾑ 武蔵野","最高級の伽羅に沈香や白檀など天然の香料を合わせた、凛とした幽玄な香りです。",
//            R.drawable.senkou15, "https://www.kyukyodo-shop.co.jp/?pid=182693710"),
//    BUTTON_16("id_16","ｲﾝｾﾝｽ ﾌﾟﾚﾐｱﾑ 冬の夜","老山白檀を主体に沈香など天然香料を合わせた、甘く爽やかな香りです。",
//            R.drawable.senkou16, "https://www.kyukyodo-shop.co.jp/?pid=182693811"),
//    BUTTON_17("id_17","お香 六種の薫物 黒方","平安時代から伝わる煉香の香りを、手軽に楽しめるスティックタイプのお香に仕立てました。\n" +
//            "黒方は慶事の他、四季を通じて用いられ、玄妙な香りが特長のお香です。",
//            R.drawable.senkou17, "https://www.kyukyodo-shop.co.jp/?pid=136583045"),
//    BUTTON_18("id_18","お香 六種の薫物 梅花","平安時代から伝わる煉香の香りを、手軽に楽しめるスティックタイプのお香に仕立てました。\n" +
//            "梅花は梅の花を想わせる、春の香りです。",
//            R.drawable.senkou18, "https://www.kyukyodo-shop.co.jp/?pid=136583047"),
//    BUTTON_19("id_19","お香セット 香木の香り","伽羅・沈香・白檀 のセットです。",
//            R.drawable.senkou19, "https://www.kyukyodo-shop.co.jp/?pid=136583089"),
//    BUTTON_20("id_20","百楽香(ひゃくらくこう)木蓮","厳選された天然白檀と原料を調合した上質なお香です。\n" +
//            "お部屋焚き用はもちろん御仏前用としてもお使い頂けます。",
//            R.drawable.senkou20, "https://kuyukou.com/products/%E7%99%BE%E6%A5%BD%E9%A6%99-%E3%81%B2%E3%82%83%E3%81%8F%E3%82%89%E3%81%8F%E3%81%93%E3%81%86-%E6%9C%A8%E8%93%AE-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF40%E6%9C%AC"),
//    BUTTON_21("id_21","かゆらぎ 白檀","ここちよい香りもその 『 ゆらぎ 』 のひとつかもしれません。\n" +
//            "「かゆらぎ」は、そんな自然の恵みを想起させる和風の香りをお香にしました。" +
//            "お部屋焚き用はもちろん御仏前用としてもお使い頂けます。",
//            R.drawable.senkou21, "https://kuyukou.com/products/kayuragi-byakudan"),
//    BUTTON_22("id_22","以花伝心　金木犀","秋の始まりを告げる、金木犀の香り",
//            R.drawable.senkou22, "https://kuyukou.com/products/%E4%BB%A5%E8%8A%B1%E4%BC%9D%E5%BF%83-%E9%87%91%E6%9C%A8%E7%8A%80-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF20%E6%9C%AC%E5%85%A5"),
//    BUTTON_23("id_23","百楽香（ひゃくらくこう）楠","森の香気を浴びるような楠の香り",
//            R.drawable.senkou23, "https://kuyukou.com/products/%E7%99%BE%E6%A5%BD%E9%A6%99-%E3%81%B2%E3%82%83%E3%81%8F%E3%82%89%E3%81%8F%E3%81%93%E3%81%86-%E6%A5%A0-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF40%E6%9C%AC"),
//    BUTTON_24("id_24","ESTEBAN（エステバン） ピュアリネン","エステバンのお香は400年以上にわたって育まれた日本伝統の薫香技術を用いて、日本で丁寧に作り上げられています。\n" +
//            "洗練された芳香で、落ち着きのある雰囲気をお楽しみいただけます。",
//            R.drawable.senkou24, "https://kuyukou.com/products/esteban-%E3%82%A8%E3%82%B9%E3%83%86%E3%83%90%E3%83%B3-%E3%83%94%E3%83%A5%E3%82%A2%E3%83%AA%E3%83%8D%E3%83%B3-%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF40%E6%9C%AC%E5%85%A5"),
//
//    BUTTON_25("id_25","山紫水明 嵯峨の朝霧　","『山紫水明』は、四季毎に色合いを変える西陣織のような山々に囲まれ、友禅流しの名残をとどめる澄んだ川を抱く京都。そんな自然の香りをお香に托したシリーズです。「嵯峨の朝霧」浅緑の竹林をしっとり濡らす嵯峨の朝霧をイメージした、緑色のお香です。シンプルでクリーンな、草原の香り。保存に適した桐箱入りです。",
//              R.drawable.senkou25, "https://kuyukou.com/products/sanshisuimei_saga"),
//    BUTTON_26("id_26","山紫水明　嵐山の月影","「嵐山の月影」梔子(くちなし)色にひんやりと川面に映る、嵐山の月影をイメージした灰色のお香です。エキゾチックで甘さを抑えた、大人っぽい香りです。保存に適した桐箱入りです。",
//              R.drawable.senkou26, "https://kuyukou.com/products/%E5%B1%B1%E7%B4%AB%E6%B0%B4%E6%98%8E-%E5%B5%90%E5%B1%B1%E3%81%AE%E6%9C%88%E5%BD%B1-%E6%A1%90%E7%AE%B1%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF30%E6%9C%AC%E5%85%A5"),
//    BUTTON_27("id_27","山紫水明　祇園の華","「祇園の華」今様色にはんなりとすれ違い香る、祇園の華をイメージした桃色のお香です。数種の花たちが醸し出す、情熱的な香り。保存に適した桐箱入りです",
//            R.drawable.senkou27, "https://kuyukou.com/products/%E5%B1%B1%E7%B4%AB%E6%B0%B4%E6%98%8E-%E7%A5%87%E5%9C%92%E3%81%AE%E8%8F%AF-%E6%A1%90%E7%AE%B1%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF30%E6%9C%AC%E5%85%A5"),
//    BUTTON_28("id_28","山紫水明　加茂のせせらぎ","「加茂のせせらぎ」は、浅縹(あさはなだ)色に水澄む、加茂のせせらぎをイメージした、濃紺色のお香です。みずみずしい爽やかな香りに、フローラル系のやさしい香りをプラスしたユニセックスな香り。保存に適した桐箱入りです。",
//              R.drawable.senkou28, "https://kuyukou.com/products/%E5%B1%B1%E7%B4%AB%E6%B0%B4%E6%98%8E-%E5%8A%A0%E8%8C%82%E3%81%AE%E3%81%9B%E3%81%9B%E3%82%89%E3%81%8E-%E6%A1%90%E7%AE%B1%E3%82%B9%E3%83%86%E3%82%A3%E3%83%83%E3%82%AF30%E6%9C%AC%E5%85%A5"),
//    BUTTON_29("id_29","香源のお香 日本の香り しゃむ","『日本の香り しゃむ』は２種類のベトナム産沈香を使った、甘みのある香りです。\n" +
//            "香木の香りは、時の権力者たちに古くから愛されてきました。そんな香りの歴史に想いを馳せつつ、芳醇な香りをどうぞお楽しみください。",
//            R.drawable.senkou29, "https://www.kohgen.com/i/kc-okoh-M0002");





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
