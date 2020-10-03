package app.aoyagi.makkan.spajam2020testapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import app.aoyagi.makkan.spajam2020testapp.MainActivity.Companion.REQUEST_CODE_PERMISSION
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // 定数
    private  val REQUEST_CODE_PERMISSIONS :Int = 101
    private val interpriter :PoseNetInterpriter =

    companion object {
        const val REQUEST_CODE_PERMISSION: Int = 200
        const val REQUEST_CODE_CAMERA: Int = 150
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera.setOnClickListener {


        }


    }

    fun startCamera() {

        val fileName = "${System.currentTimeMillis()}.jpg"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, fileName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "設定から許可しましょう", Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        bitmap = Bitmap.createBitmap(257, 257, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)

        drawable.draw(canvas)
        return bitmap
    }






}


//class Person {
//    var keyPoints: List<KeyPoint> = listOf<KeyPoint>()
//    var score: Float = 0.0f
//}
//
//class KeyPoint {
//    var bodyPart: BodyPart = BodyPart.NOSE
//    var position: Position = Position()
//    var score: Float = 0.0f
//}
//
//class Position {
//    var x: Int = 0
//    var y: Int = 0
//}
//
//enum class BodyPart {
//    NOSE,
//    LEFT_EYE,
//    RIGHT_EYE,
//    ...
//    RIGHT_ANKLE
//}
