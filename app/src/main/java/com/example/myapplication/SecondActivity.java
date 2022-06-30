package com.example.myapplication;
import static com.example.myapplication.MainActivity.ITEM;
import static com.example.myapplication.MainActivity.Send_Text;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class SecondActivity extends AppCompatActivity {

    private Todo todo;
    private EditText editText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.editText = findViewById(R.id.et_text);
        this.todo = getIntent().getParcelableExtra(ITEM);
        editText.setText(todo.getTodoText());

    }

    private void sendTextItem() {
        Intent data = new Intent(this, MainActivity.class);
        data.putExtra(Send_Text, todo);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_button) {
            String textEntered = editText.getText().toString();
            todo.setTodoText(textEntered);
            sendTextItem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}