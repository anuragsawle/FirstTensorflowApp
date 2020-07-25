package com.example.firsttensorflowapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class MainActivity : AppCompatActivity() {

    lateinit var interpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            interpreter = Interpreter(loadModelFile()!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        predict.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val f = doInference(input.getText().toString())
                result.setText(f.toString() + "")
            }
        })
    }

    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer? {
        val assetFileDescriptor = this.assets.openFd("linear.tflite")
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = fileInputStream.getChannel()
        val startOffset = assetFileDescriptor.startOffset
        val length = assetFileDescriptor.length
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length)
    }
    fun doInference(`val`: String): Float {
        val input = FloatArray(1)
        input[0] = `val`.toFloat()
        val output = Array(1) { FloatArray(1) }
        interpreter.run(input, output)
        return output[0][0]
    }
}