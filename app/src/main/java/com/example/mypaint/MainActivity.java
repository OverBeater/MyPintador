package com.example.mypaint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class MainActivity extends AppCompatActivity {
    private SimplePaint simplePaint;
    private ImageView ivColorPicker;
    private final int[] buttonIds = {
            R.id.linha, R.id.quadrado, R.id.circulo,
            R.id.limpar, R.id.voltar, R.id.free_draw, R.id.ivColorPicker
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simplePaint = findViewById(R.id.simplePaint);
        ivColorPicker = findViewById(R.id.ivColorPicker);

        // Set Click Listeners
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(onClickListener);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.linha:
                    simplePaint.setStyleType(StyleType.linha);
                    break;
                case R.id.quadrado:
                    simplePaint.setStyleType(StyleType.quadrado);
                    break;
                case R.id.circulo:
                    simplePaint.setStyleType(StyleType.circulo);
                    break;
                case R.id.limpar:
                    simplePaint.removeDraw();
                    break;
                case R.id.voltar:
                    simplePaint.backDraw();
                    break;
                case R.id.ivColorPicker:
                    colorPickerSelectColor();
                    break;
                case R.id.free_draw:
                    simplePaint.setStyleType(StyleType.desenhoLivre);
                    break;
            }
        }
    };

    private void colorPickerSelectColor() {
        new ColorPickerDialog.Builder(this)
                .setTitle("Select a Color")
                .setPreferenceName("ColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm), new ColorEnvelopeListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                        simplePaint.setColor(envelope.getColor());
                        ivColorPicker.setColorFilter(envelope.getColor());
                    }
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show();
    }
}
