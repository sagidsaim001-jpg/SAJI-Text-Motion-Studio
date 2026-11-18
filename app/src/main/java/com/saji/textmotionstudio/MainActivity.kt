package com.saji.textmotionstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF111214)
private val Panel = Color(0xFF202124)
private val Muted = Color(0xFF92959B)
private val Accent = Color(0xFFFF3568)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = darkColorScheme(primary = Accent, background = Bg, surface = Panel)) { EditorScreen() } }
    }
}

@Composable
private fun EditorScreen() {
    var text by remember { mutableStateOf("YOUR TEXT") }
    var selected by remember { mutableStateOf("Text") }
    var playing by remember { mutableStateOf(false) }
    val tools = listOf("Canvas", "Audio", "Sticker", "Text", "Motion", "Filter", "Layer")
    val presets = listOf("Kinetic", "3D Depth", "Liquid", "Word Pop", "Mask Reveal", "Camera")
    Column(Modifier.fillMaxSize().background(Bg)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            Spacer(Modifier.width(22.dp))
            Text("SAJI", color = Color.White, fontWeight = FontWeight.Black, letterSpacing = 2.sp, fontSize = 18.sp)
            Text("  MOTION STUDIO", color = Muted, fontSize = 11.sp, letterSpacing = 1.sp)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = {}) { Text("Export", color = Color.White, fontWeight = FontWeight.Bold) }
        }
        Box(Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp).background(Color.Black, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
            Box(Modifier.fillMaxWidth().height(210.dp).background(Brush.linearGradient(listOf(Color(0xFF25262B), Color(0xFF09090B))), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Text(text, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text("PREVIEW", Modifier.align(Alignment.TopStart).padding(12.dp), color = Muted, fontSize = 9.sp, letterSpacing = 2.sp)
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) { Icon(Icons.Default.Undo, null, tint = Muted) }
            IconButton(onClick = {}) { Icon(Icons.Default.Redo, null, tint = Muted) }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { playing = !playing }) { Icon(if (playing) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(34.dp)) }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Fullscreen, null, tint = Color.White)
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).background(Color(0xFF191A1C)).padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(26.dp)) {
            tools.forEach { item ->
                Column(Modifier.width(58.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    val icon = when(item) { "Canvas" -> Icons.Default.Crop; "Audio" -> Icons.Default.MusicNote; "Sticker" -> Icons.Default.EmojiEmotions; "Text" -> Icons.Default.TextFields; "Motion" -> Icons.Default.AutoAwesome; "Filter" -> Icons.Default.Tune; else -> Icons.Default.Layers }
                    IconButton(onClick = { selected = item }) { Icon(icon, null, tint = if(selected == item) Accent else Color.White, modifier = Modifier.size(27.dp)) }
                    Text(item, color = if(selected == item) Color.White else Muted, fontSize = 11.sp)
                }
            }
        }
        Column(Modifier.fillMaxWidth().background(Color(0xFF1B1C1E)).padding(top = 12.dp, bottom = 18.dp)) {
            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("TEXT LAYER", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(Modifier.weight(1f))
                Text("00:00", color = Muted, fontSize = 11.sp)
                Text("  •  00:12", color = Muted, fontSize = 11.sp)
            }
            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Accent, shape = RoundedCornerShape(12.dp), onClick = {}) { Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) { Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(30.dp)) } }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    BasicTextField(value = text, onValueChange = { text = it }, singleLine = true, textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold), modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(8.dp)).padding(12.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Drag timeline clips • Tap text to edit", color = Muted, fontSize = 10.sp)
                }
            }
            Text("QUICK MOTION PRESETS", Modifier.padding(start = 16.dp, bottom = 8.dp), color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presets) { preset ->
                    Surface(color = if(selected == preset) Accent.copy(alpha = .2f) else Panel, shape = RoundedCornerShape(10.dp), modifier = Modifier.border(1.dp, if(selected == preset) Accent else Color(0xFF34363A), RoundedCornerShape(10.dp)), onClick = { selected = preset }) {
                        Text(preset, Modifier.padding(horizontal = 14.dp, vertical = 11.dp), color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
