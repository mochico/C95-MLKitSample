package mochico.example.com.sample.mlkit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.io.IOException
import java.io.InputStream
import java.util.Arrays

class MainActivity : AppCompatActivity() {

    var targetImageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        targetImageBitmap = getTargetBitmapFromAsset()

        findViewById<View>(R.id.detectButton).setOnClickListener {
            runTextRecognition()
            // runTextRecognitionForJa()
        }

        findViewById<ImageView>(R.id.imageView).setImageBitmap(targetImageBitmap)
    }

    private fun runTextRecognition() {
        val image = FirebaseVisionImage.fromBitmap(targetImageBitmap!!)
        val recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer

        val onSuccessListener = OnSuccessListener<FirebaseVisionText> { firebaseVisionText ->
            val firstLineText = firebaseVisionText.textBlocks[0].lines[0].text
            Toast.makeText(
                this@MainActivity,
                "success : $firstLineText",
                Toast.LENGTH_LONG).show()
        }
        val onFailureListener = OnFailureListener {
            Log.e("ERROR", it.message)
        }
        recognizer.processImage(image)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }

    private fun runTextRecognitionForJa() {
        val image = FirebaseVisionImage.fromBitmap(targetImageBitmap!!)
        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setLanguageHints(Arrays.asList("ja"))
            .build()
        val recognizer = FirebaseVision.getInstance().getCloudTextRecognizer(options)

        val onSuccessListener = OnSuccessListener<FirebaseVisionText> { firebaseVisionText ->
            val firstLineText = firebaseVisionText.textBlocks[0].lines[0].text
            Toast.makeText(
                this@MainActivity,
                "success : $firstLineText",
                Toast.LENGTH_LONG).show()
        }
        val onFailureListener = OnFailureListener {
            Log.e("ERROR", it.message)
        }
        recognizer.processImage(image)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }

    private fun getTargetBitmapFromAsset(): Bitmap? {
        val image = "hello_world.jpg"
//        val image = "beautiful_form.jpg"

        val inputStream: InputStream
        var bitmap: Bitmap? = null
        try {
            inputStream = this@MainActivity.assets.open(image)
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }
}
