package app.aoyagi.makkan.spajam2020testapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.SparseIntArray
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var uri: Uri
    private val pose: Pose? = null
    private val showInFrameLikelihood = false
    private val leftPaint: Paint? = null
    private val rightPaint: Paint? = null
    private val whitePaint: Paint? = null

    lateinit var bitmap: Bitmap
    val REQUEST_VIDEO_CAPTURE = 1
    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }

    fun PoseGraphic(overlay: GraphicOverlay?, pose: Pose, showInFrameLikelihood: Boolean) {
        super(overlay)
        pose = pose
        showInFrameLikelihood = showInFrameLikelihood
        whitePaint = Paint()
        whitePaint.setColor(Color.WHITE)
        whitePaint.setTextSize(com.google.mlkit.vision.demo.java.posedetector.PoseGraphic.IN_FRAME_LIKELIHOOD_TEXT_SIZE)
        leftPaint = Paint()
        leftPaint.setColor(Color.GREEN)
        rightPaint = Paint()
        rightPaint.setColor(Color.YELLOW)
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(
        cameraId: String,
        activity: Activity,
        isFrontFacing: Boolean
    ): Int {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIONS.get(deviceRotation)

        // Get the device's sensor orientation.
        val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360
        }
        return rotationCompensation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera.setOnClickListener {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                takeVideoIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                }
            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()
        val poseDetector = PoseDetection.getClient(options)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            if (intent != null) {
                uri = intent.data!!
            }
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageFromBitmap(bitmap, poseDetector)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun imageFromBitmap(bitmap: Bitmap, poseDetector: PoseDetector) {
        val rotationDegrees = 0
        // [START image_from_bitmap]
        val image = InputImage.fromBitmap(bitmap, 0)
        // [END image_from_bitmap]
        Task<Pose> = poseDetector.process(image)
            .addOnSuccessListener { results ->
                val allPoseLandmarks = pose.getAllPoseLandmarks()
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }

}