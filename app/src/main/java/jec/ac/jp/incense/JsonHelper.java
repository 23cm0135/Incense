package jec.ac.jp.incense;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    public static List<Post> loadPostsData(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("posts.json"); // 確保 `posts.json` 在 `assets` 資料夾內
            InputStreamReader reader = new InputStreamReader(inputStream);
            return new Gson().fromJson(reader, new TypeToken<List<Post>>(){}.getType());
        } catch (Exception e) {
            Log.e("JsonHelper", "讀取 posts.json 失敗", e);
            return new ArrayList<>(); // 如果讀取失敗，返回空列表
        }
    }
}
