package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.ai.client.generativeai.type.Content;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Question extends AppCompatActivity {

    private GenerativeAiService generativeAiService;
    private static final String TAG = "Question";
    private GenerativeModelFutures model; // 在类级别声明 model 变量
    private AlertDialog dialog; // メンバー変数として宣言
    private boolean isAiAnalyzing = false; // 添加一个标志来跟踪AI分析状态
    //private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        GenerativeModel gm = new GenerativeModel(
                "gemini-1.5-flash",
                BuildConfig.API_KEY
        );
        model = GenerativeModelFutures.from(gm);

        Button submitButton = findViewById(R.id.submit_button);

        // 每次用户选择选项时，检查是否启用提交按钮
        RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> {
            if (isAllQuestionsAnswered()) {
                submitButton.setEnabled(true);  // 如果所有问题都已回答，启用按钮
            }
        };

        // 为每个 RadioGroup 设置监听器
        RadioGroup recommendationGrid = findViewById(R.id.recommendation_grid);
        RadioGroup weatherGroup = findViewById(R.id.radio_group_weather);
        RadioGroup ageGroup = findViewById(R.id.radio_group_age);
        RadioGroup materialGroup = findViewById(R.id.radio_group_gender);

        recommendationGrid.setOnCheckedChangeListener(listener);
        weatherGroup.setOnCheckedChangeListener(listener);
        ageGroup.setOnCheckedChangeListener(listener);
        materialGroup.setOnCheckedChangeListener(listener);

        submitButton.setOnClickListener(v -> {
            if (!isNetworkAvailable()) {
                new AlertDialog.Builder(Question.this)
                        .setTitle("ネットワークエラー")
                        .setMessage("ネットワークに接続されていません。\nGPT にリクエストを送信できません。")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
            if (!isAllQuestionsAnswered()) {
                // 如果没有回答所有问题，弹出提示框
                showMissingAnswersDialog();
            } else {
               handleButtonClick();
            }
        });

        ImageButton homeButton = findViewById(R.id.btn_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Question.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        ImageButton user = findViewById(R.id.btn_user);
        user.setOnClickListener(v -> {
            Intent intent = new Intent(Question.this, User.class);
            startActivity(intent);
        });

        ImageButton alarm = findViewById(R.id.btn_alarm);
        alarm.setOnClickListener(v -> {
            Intent intent = new Intent(Question.this, TimerActivity.class);
            startActivity(intent);
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    // 弹出提示框，提示用户未回答所有问题
    private void showMissingAnswersDialog() {
        new AlertDialog.Builder(this)
                .setTitle("すべての質問に回答してください")
                .setMessage("すべての質問に回答してから送信してください。")
                .setPositiveButton("はい", null)
                .show();
    }

    private void startAiAnalysis() {
        // 检查每个问题是否已被选择
        if (!isAllQuestionsAnswered()) {
            Toast.makeText(this, "请回答所有问题！", Toast.LENGTH_SHORT).show();
            return;  // 如果未回答所有问题，则返回
        }
        isAiAnalyzing = true; // 设置AI分析标志为true

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("AIが応答を生成しています...");
        builder.setCancelable(false); // キャンセル不可

        if (dialog == null) {
            dialog = builder.create();
        } else {
            dialog.setMessage("AIが応答を生成しています...");
        }

        dialog.show();
        // ... 其他 AI 分析相关代码 ...
    }

    private void handleButtonClick() {
        // 检查每个问题是否已被选择
        if (!isAllQuestionsAnswered()) {
            Toast.makeText(this, "请回答所有问题！", Toast.LENGTH_SHORT).show();
            return;  // 如果未回答所有问题，则返回
        }
        isAiAnalyzing = true; // 设置AI分析标志为true

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("AIが応答を生成しています...");
        builder.setCancelable(false); // キャンセル不可

        if (dialog == null) {
            dialog = builder.create();
        } else {
            dialog.setMessage("AIが応答を生成しています...");
        }

        dialog.show();
        String userPreferences = getUserSelection();
        List<Incense> incenseList = loadIncenseData();

//        Publisher<GenerateContentResponse> streamingResponse = model.generateContentStream(content); // 现在应该可以识别 model 了

        if (incenseList.isEmpty()) {
            Log.e(TAG, "产品数据为空，无法进行推荐");
            Toast.makeText(this, "条件に合う線香はございません", Toast.LENGTH_SHORT).show();
            return;
        }

        String prompt = buildAiPrompt(userPreferences, incenseList);
        Log.d(TAG, "发送到AI的提示: " + prompt);

        Content content = new Content.Builder()
                .addText(prompt) // Part.newBuilder().setText() の代わりに addText() を使用
                .build();

        Publisher<GenerateContentResponse> streamingResponse = model.generateContentStream(content);
        StringBuilder outputContent = new StringBuilder();

       // Toast.makeText(this, "分析しています...", Toast.LENGTH_SHORT).show();

        streamingResponse.subscribe(
                new Subscriber<GenerateContentResponse>() {
                    @Override
                    public void onNext(GenerateContentResponse generateContentResponse) {
                        String chunk = generateContentResponse.getText();
                        outputContent.append(chunk);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "AI请求完成");
                        isAiAnalyzing = false; // 设置AI分析标志为false
                        handleAiResponse(outputContent.toString());

                        // 在AI分析完成后关闭弹窗
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            Log.d(TAG, "AI分析完成，关闭弹窗");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "AI请求出错", t);
                        Toast.makeText(Question.this, "AI 请求出错，请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }
                });
    }
    private boolean isAllQuestionsAnswered() {
        // 检查每个问题是否都已经回答
        RadioGroup recommendationGrid = findViewById(R.id.recommendation_grid);
        RadioGroup weatherGroup = findViewById(R.id.radio_group_weather);
        RadioGroup ageGroup = findViewById(R.id.radio_group_age);
        RadioGroup materialGroup = findViewById(R.id.radio_group_gender);

        return recommendationGrid.getCheckedRadioButtonId() != -1 &&
                weatherGroup.getCheckedRadioButtonId() != -1 &&
                ageGroup.getCheckedRadioButtonId() != -1 &&
                materialGroup.getCheckedRadioButtonId() != -1;
    }


    @Override
    public void onBackPressed() {
        // 如果弹窗正在显示，则关闭弹窗
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            Log.d(TAG, "关闭了正在显示的弹窗");
        }
        super.onBackPressed();  // 调用父类方法，返回到上一个活动
    }

    private void handleAiResponse(String aiResponse) {
        try {
            Log.d(TAG, "AI原始响应: " + aiResponse);

            List<Incense> aiSelectedIncenses = parseAiResponse(aiResponse);
            Log.d(TAG, "解析后的香列表大小: " +
                    (aiSelectedIncenses != null ? aiSelectedIncenses.size() : "null"));

            if (aiSelectedIncenses != null && !aiSelectedIncenses.isEmpty()) {
                Log.d(TAG, "准备跳转到列表页面，列表项数: " + aiSelectedIncenses.size());
                passDataToIncenseListActivity(aiSelectedIncenses);
            } else {
                Log.e(TAG, "未找到符合条件的香");
                Toast.makeText(Question.this, "没有找到符合条件的香，请尝试其他选项", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "处理AI响应时出错", e);
            Toast.makeText(Question.this, "处理响应时出错，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserSelection() {
        RadioGroup recommendationGrid = findViewById(R.id.recommendation_grid);
        RadioGroup weatherGroup = findViewById(R.id.radio_group_weather);
        RadioGroup ageGroup = findViewById(R.id.radio_group_age);
        RadioGroup materialGroup = findViewById(R.id.radio_group_gender);

        String recommendation = getSelectedText(recommendationGrid);
        String weather = getSelectedText(weatherGroup);
        String age = getSelectedText(ageGroup);
        String material = getSelectedText(materialGroup);

        return "User's preferences:\n" +
                "Effect: " + recommendation + "\n" +
                "Weather: " + weather + "\n" +
                "Age group: " + age + "\n" +
                "Material: " + material;
    }

    private String getSelectedText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        return radioButton != null ? radioButton.getText().toString() : "未选择";
    }

    private List<Incense> loadIncenseData() {
        List<Incense> incenseList = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("products.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray itemsArray = jsonObject.getAsJsonArray("items");

            for (JsonElement itemElement : itemsArray) {
                JsonObject itemObject = itemElement.getAsJsonObject();
                String name = itemObject.get("name").getAsString();
                String effect = itemObject.get("effect").getAsString();
                String material = itemObject.get("material").getAsString();
                String imageUrl = itemObject.get("imageUrl").getAsString();
                String description = itemObject.get("description").getAsString();
                String url = itemObject.get("url").getAsString();

                Incense incense = new Incense(name, effect, material, imageUrl, description, url);
                incenseList.add(incense);
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载产品数据出错", Toast.LENGTH_SHORT).show();
        }
        return incenseList;
    }

    private String buildAiPrompt(String userPreferences, List<Incense> incenseList) {
        Gson gson = new Gson();
        String incenseJson = gson.toJson(incenseList);
        return "以下是用户的需求：" + userPreferences + "\n" +
                "以下是香列表：" + incenseJson + "\n" +
                "请根据用户的需求，从香列表中选择符合条件的香（最多5个），返回仅包含JSON格式的香列表，不要添加任何额外的文本解释。" +
                "你必须确保返回可被Gson直接解析的有效JSON数组，格式应该是 [{},{},{}] 这样的。";
    }

    private List<Incense> parseAiResponse(String aiResponse) {
        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            Log.e("Question", "AI响应为空");
            return new ArrayList<>();
        }

        Log.d("Question", "开始解析AI响应");
        List<Incense> result = new ArrayList<>();
        Gson gson = new Gson();

        try {
            // 首先尝试直接解析完整响应
            Type incenseListType = new TypeToken<List<Incense>>() {}.getType();
            result = gson.fromJson(aiResponse, incenseListType);
            Log.d("Question", "直接解析完整响应成功，得到 " + result.size() + " 项");
            return result;
        } catch (Exception e1) {
            Log.d("Question", "直接解析完整响应失败，尝试提取JSON部分", e1);

            try {
                // 尝试找到JSON数组部分
                int startIndex = aiResponse.indexOf('[');
                int endIndex = aiResponse.lastIndexOf(']') + 1;

                if (startIndex >= 0 && endIndex > startIndex) {
                    String jsonPart = aiResponse.substring(startIndex, endIndex);
                    Log.d("Question", "提取的JSON数组: " + jsonPart);

                    Type incenseListType = new TypeToken<List<Incense>>() {}.getType();
                    result = gson.fromJson(jsonPart, incenseListType);
                    Log.d("Question", "解析JSON数组成功，得到 " + result.size() + " 项");
                    return result;
                } else {
                    Log.e("Question", "未找到JSON数组格式");
                }
            } catch (Exception e2) {
                Log.e("Question", "解析JSON数组失败", e2);
            }

            try {
                // 可能是单个对象而不是数组
                int startIndex = aiResponse.indexOf('{');
                int endIndex = aiResponse.lastIndexOf('}') + 1;

                if (startIndex >= 0 && endIndex > startIndex) {
                    String jsonPart = aiResponse.substring(startIndex, endIndex);
                    Log.d("Question", "提取的单个JSON对象: " + jsonPart);

                    Incense singleIncense = gson.fromJson(jsonPart, Incense.class);
                    if (singleIncense != null && singleIncense.getName() != null) {
                        result.add(singleIncense);
                        Log.d("Question", "解析单个对象成功");
                        return result;
                    }
                }
            } catch (Exception e3) {
                Log.e("Question", "解析单个JSON对象失败", e3);
            }
        }

        Log.e("Question", "所有解析方法都失败，返回空列表");
        return result; // 可能为空
    }

    // 辅助方法，从 AI 响应中提取 JSON 部分
    private String extractJsonFromResponse(String response) {
        // 尝试找到响应中的 JSON 数组开始和结束位置
        int startIndex = response.indexOf('[');
        int endIndex = response.lastIndexOf(']') + 1;

        if (startIndex >= 0 && endIndex > startIndex) {
            return response.substring(startIndex, endIndex);
        }

        // 如果没有找到数组格式，尝试查找单个对象格式
        startIndex = response.indexOf('{');
        endIndex = response.lastIndexOf('}') + 1;

        if (startIndex >= 0 && endIndex > startIndex) {
            return "[" + response.substring(startIndex, endIndex) + "]"; // 将单个对象包装为数组
        }

        return response; // 如果无法识别 JSON 格式，返回原始响应
    }

    private void processAiResponse(List<Incense> aiSelectedIncenses) {
        if (aiSelectedIncenses != null && !aiSelectedIncenses.isEmpty()) {
            displaySelectedIncense(aiSelectedIncenses);
        } else {
            displayNoIncenseFound();
        }
    }

    private void displaySelectedIncense(List<Incense> selectedIncenses) {
        Log.d("AI Response", "Found " + selectedIncenses.size() + " incense(s):");
        for (Incense incense : selectedIncenses) {
            Log.d("AI Response", "Name: " + incense.getName());
        }
    }

    private void displayNoIncenseFound() {
        Log.d("AI Response", "No incense found.");
        Toast.makeText(this, "条件に合う線香はございません", Toast.LENGTH_SHORT).show();
    }
    private void passDataToIncenseListActivity(List<Incense> incenseList) {
        // 检查已经在 handleButtonClick 中完成，这里可以直接使用
        ArrayList<Incense> parcelableIncenseList = new ArrayList<>(incenseList);
        ArrayList<String> urlList = new ArrayList<>();

        for (Incense incense : incenseList) {
            urlList.add(incense.getUrl());
        }

        Intent intent = new Intent(Question.this, IncenseListActivity.class);
        intent.putParcelableArrayListExtra("incenseList", parcelableIncenseList);
        intent.putStringArrayListExtra("urlList", urlList);
        startActivity(intent);
    }
}
