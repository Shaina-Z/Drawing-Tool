package com.example.drawingtool

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.drawingtool.ui.theme.DrawingToolTheme





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawingToolTheme {
                DrawingCanvas()
            }
        }
    }
}

@Composable
fun DrawingCanvas() {

    var currentColor by remember { mutableStateOf(Color.Red) }
    var brushSize by remember { mutableFloatStateOf(10f) }
    var isEraser by remember { mutableStateOf(false) }

    val lines = remember { mutableStateListOf<Line>() }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorPicker { selectedColor ->
                currentColor = selectedColor
                isEraser = false
            }

            BrushSizeSelector(
                currentSize = brushSize,
                onSizeSelected = { newSize -> brushSize = newSize },
                isEraser = isEraser,
                keepMode = { keep -> isEraser = keep }
            )

            Button(onClick = { isEraser = true }) { Text("Eraser") }

            Button(onClick = { lines.clear() }) { Text("Reset") }
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(true) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val line = Line(
                        start = change.position - dragAmount,
                        end = change.position,
                        color = if (isEraser) Color.White else currentColor,
                        strokeWidth = brushSize

                    )
                    lines.add(line)
                }
            }
        ){
                lines.forEach{line->
                    drawLine(
                        color=line.color,
                        start=line.start,
                        end=line.end,
                        strokeWidth=line.strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
    }

        }
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
    }
}