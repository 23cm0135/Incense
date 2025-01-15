package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Question extends AppCompatActivity {

    private Button submitButton;
    private RadioGroup effectGroup;
    private RadioGroup materialGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // 获取控件
        submitButton = findViewById(R.id.submit_button);
        effectGroup = findViewById(R.id.recommendation_grid);
        materialGroup = findViewById(R.id.radio_group_gender);

        // 设置按钮点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户选择的效果和材料
                String selectedEffect = getSelectedEffect();
                String selectedMaterial = getSelectedMaterial();

                // 从 assets 读取并解析 JSON 数据
                List<Incense> incenseList = loadIncenseData();

                if (incenseList != null) {
                    // 筛选符合条件的香
                    List<Incense> filteredIncenseList = filterIncenses(incenseList, selectedEffect, selectedMaterial);

                    // 判断筛选结果是否为空
                    if (filteredIncenseList.isEmpty()) {
                        // 显示提示信息
                        if (filteredIncenseList.isEmpty()) {
                            new AlertDialog.Builder(Question.this)
                                    .setTitle("お知らせ")
                                    .setMessage("該当する製品はありません")
                                    .setPositiveButton("OK", null)
                                    .show();
                            return;
                        }

                        return; // 保持当前界面
                    }

                    // 启动新的 Activity 并传递筛选后的数据
                    Intent intent = new Intent(Question.this, IncenseListActivity.class);
                    intent.putParcelableArrayListExtra("incenseList", new ArrayList<>(filteredIncenseList));
                    startActivity(intent);
                } else {
                    Log.e("Question", "No incense data available.");
                }
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
    }

    // 获取用户选择的效果
    private String getSelectedEffect() {
        int selectedId = effectGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";
    }

    // 获取用户选择的材料
    private String getSelectedMaterial() {
        int selectedId = materialGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";
    }

    // 读取并解析 JSON 数据
    // 读取并解析 JSON 数据
    private List<Incense> loadIncenseData() {
        List<Incense> incenseList = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("products.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();

            // 解析根 JSON 对象
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // 获取 "items" 数组
            JsonArray itemsArray = jsonObject.getAsJsonArray("items");

            // 将每个 item 解析成 Incense 对象并添加到 list 中
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incenseList;
    }

    // 筛选符合条件的香
    public List<Incense> filterIncenses(List<Incense> incenses, String effect, String material) {
        List<Incense> filteredIncenseList = new ArrayList<>();

        if (incenses != null) {
            Iterator<Incense> iterator = incenses.iterator();
            while (iterator.hasNext()) {
                Incense incense = iterator.next();
                // 根据效果和材料进行筛选
                if ((effect.isEmpty() || incense.getEffect().equals(effect)) &&
                        (material.isEmpty() || incense.getMaterial().equals(material))) {
                    filteredIncenseList.add(incense);
                }
            }
        } else {
            Log.e("FilterIncenses", "Incenses list is null");
        }
        return filteredIncenseList;
    }
}
