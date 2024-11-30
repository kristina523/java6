package com.example.javaa6;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button addButton, updateButton, deleteButton, chooseImageButton;
    private ListView listView;
    private EditText titleText, descriptionText;
    private ArrayAdapter<String> adapter;
    private String selectedProductsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //библиотека Paper
        Paper.init(this);
        //Привязка элементов, такие как EditText,Button
        addButton = findViewById(R.id.addButton);
        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        listView = findViewById(R.id.listView);

        // Адаптер для отображения списка товров
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getProductsTitles());
        //Установка адаптера для lastlist
        listView.setAdapter(adapter);

        // Обработчик выбора элемента из списка товаров
        listView.setOnItemClickListener((parent, view , position, id) -> {
            selectedProductsTitle = adapter.getItem(position);

            // Получаем здесь данные выбранного нами продукта
            Products products = Paper.book().read(selectedProductsTitle, null);

            if (products != null) {
                titleText.setText(products.getTitle());
                descriptionText.setText(products.getDescription());
            }
        });

        // Обработчик кнопки добавления товара
        addButton.setOnClickListener(v -> {
            String title = titleText.getText().toString();
            String description = descriptionText.getText().toString();

            if (!title.isEmpty() && description != null) {
                Products products = new Products(title, description);
                Paper.book().write(title, products);
                updateProductsList();
                clearInputs();
            } else {
                Toast.makeText(MainActivity.this, "Заполните все здесь поля и выберите картинку", Toast.LENGTH_SHORT).show();
            }
        });
        // Обработчик кнопки обновления товара
        updateButton.setOnClickListener(v -> {
            if (selectedProductsTitle == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, выберите сначала товар", Toast.LENGTH_SHORT).show();
                return;
            }
            // Получаем текущий продукт из корзины
            Products currentProducts = Paper.book().read(selectedProductsTitle);

            if (currentProducts == null) {
                Toast.makeText(MainActivity.this, "Ошибка: товар не найден в списке", Toast.LENGTH_SHORT).show();
                return;
            }
            // Используем текущие данные, если поля были не заполнены
            String newTitle = titleText.getText().toString().isEmpty() ? currentProducts.getTitle() : titleText.getText().toString();
            String newDescription = descriptionText.getText().toString().isEmpty() ? currentProducts.getDescription() : descriptionText.getText().toString();


            // Удаляем старую запись и создаем новую запись
            Paper.book().delete(selectedProductsTitle);
            Products updatedProducts = new Products(newTitle, newDescription );
            Paper.book().write(newTitle, updatedProducts);
            //Обновление списка товаров и очищение поля ввода
            updateProductsList();
            clearInputs();
            Toast.makeText(MainActivity.this, "Товар обновлен!", Toast.LENGTH_SHORT).show();
        });
        // Обработчик кнопки удаления продукта
        deleteButton.setOnClickListener(v -> {
            if (selectedProductsTitle == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, выберите продукт", Toast.LENGTH_SHORT).show();
                return;
            }

            Paper.book().delete(selectedProductsTitle);
            updateProductsList();
            clearInputs();
        });
    }

    // Метод обновления списка продуктов listView
    private void updateProductsList() {
        adapter.clear();
        adapter.addAll(getProductsTitles());
        adapter.notifyDataSetChanged();
    }

    // Получение списка названий товаров
    private List<String> getProductsTitles() {
        return new ArrayList<>(Paper.book().getAllKeys());
    }
    // Очистка полей ввода
    private void clearInputs() {
        titleText.setText("");
        descriptionText.setText("");
        selectedProductsTitle = null;
    }

    // Обработка результата выбора картинки
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1);
        }
    }
