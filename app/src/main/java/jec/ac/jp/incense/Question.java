package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Question extends AppCompatActivity {

    private Button submitButton;
    private RadioGroup effectGroup;
    private RadioGroup materialGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        submitButton = findViewById(R.id.submit_button);
        effectGroup = findViewById(R.id.recommendation_grid);
        materialGroup = findViewById(R.id.radio_group_gender);

        submitButton.setOnClickListener(v -> {
            if (!isNetworkAvailable()) {
                new AlertDialog.Builder(Question.this)
                        .setTitle("ネットワークエラー")
                        .setMessage("ネットワークに接続されていません。\nGPT にリクエストを送信できません。")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            String selectedEffect = getSelectedEffect();
            String selectedMaterial = getSelectedMaterial();

            if (selectedEffect.isEmpty() || selectedMaterial.isEmpty()) {
                Toast.makeText(this, "すべての選択肢を選択してください", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog loadingDialog = new AlertDialog.Builder(Question.this)
                    .setTitle("AI による分析中...")
                    .setMessage("GPT があなたの好みに合うお香を選定中です。しばらくお待ちください...")
                    .setCancelable(false)
                    .show();

            new Handler().postDelayed(() -> {
                List<Incense> incenseList = loadIncenseData();
                if (incenseList == null) {
                    Log.e("Question", "No incense data available.");
                    loadingDialog.dismiss();
                    return;
                }

                List<Incense> filteredIncenseList = getFilteredIncenseFromGPT(selectedEffect, selectedMaterial, incenseList);
                loadingDialog.dismiss();

                if (filteredIncenseList == null) {
                    new AlertDialog.Builder(Question.this)
                            .setTitle("GPT の応答エラー")
                            .setMessage("GPT からの応答がありません。\nネットワークを確認してください。")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                if (filteredIncenseList.isEmpty()) {
                    new AlertDialog.Builder(Question.this)
                            .setTitle("AI の分析結果")
                            .setMessage("申し訳ありませんが、GPT は適切なお香を見つけることができませんでした。")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                Intent intent = new Intent(Question.this, IncenseListActivity.class);
                intent.putParcelableArrayListExtra("incenseList", new ArrayList<>(filteredIncenseList));

                Log.d("Question", "filteredIncenseList size: " + filteredIncenseList.size());
                startActivity(intent);
            }, 2000);
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


    private String getSelectedEffect() {
        int selectedId = effectGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";
    }
    // 读取并解析 JSON 数据
    private List<Incense> loadIncenseData() {
        List<Incense> incenseList = new ArrayList<>();
        try {
            // 从 assets 目录中读取 JSON 文件
            InputStream inputStream = getAssets().open("products.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();

            // 解析 JSON 数据
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
        }
        return incenseList;
    }


    private String getSelectedMaterial() {
        int selectedId = materialGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    private List<Incense> getFilteredIncenseFromGPT(String effect, String material, List<Incense> incenses) {
        if (!isNetworkAvailable()) {
            return null;
        }

        Gson gson = new Gson();
        JsonArray incenseArray = new JsonArray();

        for (Incense incense : incenses) {
            JsonObject incenseObject = new JsonObject();
            incenseObject.addProperty("name", incense.getName());
            incenseObject.addProperty("effect", incense.getEffect());
            incenseObject.addProperty("material", incense.getMaterial());
            incenseObject.addProperty("imageUrl", incense.getImageUrl());
            incenseObject.addProperty("description", incense.getDescription());
            incenseObject.addProperty("url", incense.getUrl());
            incenseArray.add(incenseObject);
        }

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("effect", effect);
        requestJson.addProperty("material", material);
        requestJson.add("incenseList", incenseArray);

        String requestJsonString = gson.toJson(requestJson);
        Log.d("GPT_Request", "GPT API に送信されたリクエスト: " + requestJsonString);

        String gptResponse = simulateGPTResponse(requestJsonString);
        if (gptResponse == null) return null;

        Log.d("GPT_Response", "GPT API からの応答: " + gptResponse);

        JsonObject responseJson = gson.fromJson(gptResponse, JsonObject.class);
        JsonArray filteredArray = responseJson.getAsJsonArray("filteredIncenseList");

        List<Incense> filteredIncenseList = new ArrayList<>();
        for (JsonElement element : filteredArray) {
            JsonObject obj = element.getAsJsonObject();
            Incense incense = new Incense(
                    obj.get("name").getAsString(),
                    obj.get("effect").getAsString(),
                    obj.get("material").getAsString(),
                    obj.get("imageUrl").getAsString(),
                    obj.get("description").getAsString(),
                    obj.get("url").getAsString()
            );
            filteredIncenseList.add(incense);
        }

        return filteredIncenseList;
    }

    private String simulateGPTResponse(String requestJsonString) {
        if (!isNetworkAvailable()) return null;

        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(requestJsonString, JsonObject.class);
        String effect = requestJson.get("effect").getAsString();
        String material = requestJson.get("material").getAsString();
        JsonArray incenseArray = requestJson.getAsJsonArray("incenseList");

        JsonArray filteredArray = new JsonArray();
        for (JsonElement element : incenseArray) {
            JsonObject incense = element.getAsJsonObject();
            if ((effect.isEmpty() || incense.get("effect").getAsString().equals(effect)) &&
                    (material.isEmpty() || incense.get("material").getAsString().equals(material))) {
                filteredArray.add(incense);
            }
        }

        JsonObject responseJson = new JsonObject();
        responseJson.add("filteredIncenseList", filteredArray);

        return gson.toJson(responseJson);
    }

}
