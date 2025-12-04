package com.example.drawingtool

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.drawingtool.Blue
import com.example.drawingtool.LightGray
import com.example.drawingtool.Red
import com.example.drawingtool.ui.theme.DrawingToolTheme


private val Unit.Red: Any
private val Unit.LightGray: Any

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawingToolTheme {

            }
        }
    }
}

@Composable
fun ToolPanel(){

}
@Composable
fun DrawingCanvas(){

}


@Composable
fun ColorPicker(onColorSelected: (Color) -> Unit) {
    val context = LocalContext.current

    val colorMap = mapOf(
        Color.Red to "Red",
        Color.Blue to "Blue",
        Color.Yellow to "Yellow"
    )

    Row {
        colorMap.forEach { (color, name) ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, CircleShape)
                    .clickable {
                        onColorSelected(color)
                        Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
                    }
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun BrushSizeSelector(
    currentSize: Float,
    onSizeSelected: (Float) -> Unit,
    isEraser: Boolean,
    keepMode: (Boolean) -> Unit
) {
    var sizeText by remember { mutableStateOf(currentSize.toString()) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = sizeText,
            onValueChange = { input ->
                sizeText = input
                val newSize = input.toFloatOrNull() ?: currentSize
                onSizeSelected(newSize)
                keepMode(isEraser)
            },
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .width(60.dp)
                .background(Color.LightGray, CircleShape)
                .padding(8.dp)
        )

        Text(
            text = "px",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

data class Line(val start: Offset,
    val end:Offset,
    val color: Color,
    val strokeWidth: Float=10f)


@Preview(showBackground = true)
@Composable
fun DrawingPreview() {
    DrawingToolTheme {
        DrawingCanvas()
        ToolPanel()
    }
}