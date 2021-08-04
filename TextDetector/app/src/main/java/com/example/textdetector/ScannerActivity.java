package com.example.textdetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ScannerActivity extends AppCompatActivity {
    private Image captureIv;
    private TextView resultTv;
    private Button snapBtn,detectBtn;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        captureIv=findViewById(R.id.idIVCaptureImage);
        resultTv=findViewById(R.id.idTVDetectText);
        snapBtn=findViewById(R.od.idBtnSnap);
        detectBtn=findViewById(R.id.idBtnDetect);

        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectText();
            }
        });
        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void Onclick(View v) {
                if(checkPermission()){
                    captureImage();
                }else{
                    requestPermission();
                }

            }
        });
    }
}

private boolean checkPermission() {
    int camerpermission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
    return camerpermission == PackageManager.PERMISSION_GRANTED;
}

private void requestPermission() {
    int PERMISSION_CODE = 200;
    ActivityCompat.requestPermissions(activity:this, new String[] {CAMERA}, PERmISSION_CODE );
}

private void captureImage(){
    Intent takePicture= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if(takePicture.resolveActivity(getPackageManager())!=null){
        startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);
    }
}
@Override
public void onRequestPermissionsResult(int requestCode, string[] permissions, @NonNull int[] grantResults){
    super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    if(grantResults.length>0){
        boolean cameraPermission = grantResults[0]==PackageManager.PERMISSION_GRANTED;
        if(cameraPermission){
            Toast.makeText(context: this, text "Permissions Granted..", Toast.LENGTH_SHORT).show();
            captureImage();;
        }else{
            Toast.makeText(context this, text:"Permissions denied..",Toast.LENGTH_SHORT).show();
        }
    }
}


@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data){
    super.onActivityResult(requestCode,resultCode,data);
    if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
      Bundle extras = data.getExtras();
      imageBitmap=(Bitmap) extras.get("data");
      captureIv.setImageBitmap(imageBitmap);

    }
}

private void detectText(){
    InputImage image = InputImage.fromBitmap(imageBitmap);
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>(){
        @Override
        public void onSuccess(@NonNull Text text){
            StringBuilder result = new StringBuilder();
            for(Text.TextBlock block:text.getTextBlocks()){
                String blockText = block.getText();
                Point[] blockCornerPoint = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox9
;
            for(Text.Line line : block.getlines()){
                String lineText = line.getText();
                Point[] lineCornerPoint = line.getCornerPoints();
                Rect linRect = line.getBoundingBox();
                for(Text.Element element; line.getElements()){
                    String elementText = element.getText();
                    result.append(elementText);
                }
                resultTv.setText(blockText);
            }
            }
        }.addOnFailureListener(new OnFailureListener){
        @Override
        public void onFailure(@NonNull @org.jetbrains.annotations.Notnull Exception e){
            Toast.makeText(context: ScannerActivity.this, text:"Fail to detect text from image.", Toast.LENGTH_SHORT.show();

            }
        })
    }
}