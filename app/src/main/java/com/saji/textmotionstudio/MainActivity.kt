package com.saji.textmotionstudio

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import androidx.media3.ui.PlayerView
import java.io.File
import java.io.FileInputStream

private val EditorBg = Color(0xFF202020)
private val EditorPanel = Color(0xFF292929)
private val EditorAccent = Color(0xFFFF315F)
private val EditorText = Color(0xFFF5F5F5)
private val EditorMuted = Color(0xFF9A9A9A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = darkColorScheme(primary = EditorAccent, background = EditorBg, surface = EditorPanel)) { SajiVideoEditor() } }
    }
}

@Composable
private fun SajiVideoEditor() {
    val context = LocalContext.current
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var overlayText by remember { mutableStateOf("Your text") }
    var showTextEditor by remember { mutableStateOf(false) }
    var activeTool by remember { mutableStateOf("Canvas") }
    var trimStart by remember { mutableFloatStateOf(0f) }
    var trimEnd by remember { mutableFloatStateOf(100f) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var exporting by remember { mutableStateOf(false) }
    var playerReady by remember { mutableStateOf(false) }
    var splitMarkers by remember { mutableStateOf(listOf<Float>()) }
    var exportProgress by remember { mutableFloatStateOf(0f) }

    val player = remember { ExoPlayer.Builder(context).build() }
    DisposableEffect(player) { onDispose { player.release() } }

    val videoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            videoUri = uri
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            player.playWhenReady = false
            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        durationMs = player.duration.coerceAtLeast(0L)
                        playerReady = true
                    }
                }
            })
        }
    }
    val audioPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            audioUri = uri
            Toast.makeText(context, "Audio track added to project", Toast.LENGTH_SHORT).show()
        }
    }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) Toast.makeText(context, "Image selected: overlay import is next in the editor workflow", Toast.LENGTH_SHORT).show()
    }

    Column(Modifier.fillMaxSize().background(EditorBg)) {
        Row(Modifier.fillMaxWidth().height(58.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ArrowBack, null, tint = EditorText, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(22.dp))
            Icon(Icons.Default.HelpOutline, "Help", tint = EditorText, modifier = Modifier.size(25.dp))
            Spacer(Modifier.weight(1f))
            Text("Export", color = EditorText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(enabled = videoUri != null && !exporting) {
                    exportVideo(context, videoUri!!, (durationMs * trimStart / 100f).toLong(), (durationMs * trimEnd / 100f).toLong(),
                        onStart = { exporting = true; exportProgress = 0f },
                        onDone = { file -> exporting = false; Toast.makeText(context, "Export saved: ${file.name}", Toast.LENGTH_LONG).show() },
                        onError = { message -> exporting = false; Toast.makeText(context, message, Toast.LENGTH_LONG).show() })
                })
        }

        Box(Modifier.fillMaxWidth().weight(1f).background(Color.Black), contentAlignment = Alignment.Center) {
            if (videoUri != null) {
                AndroidView(factory = { ctx -> PlayerView(ctx).apply { this.player = player; useController = false; resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT } },
                    modifier = Modifier.fillMaxSize())
                if (overlayText.isNotBlank()) {
                    Text(overlayText, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.Center).padding(horizontal = 20.dp))
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.VideoLibrary, null, tint = Color(0xFF777777), modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(10.dp))
                    Text("Import a video to start editing", color = Color.White, fontSize = 16.sp)
                    Text("Your preview will appear here", color = EditorMuted, fontSize = 12.sp)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { videoPicker.launch(arrayOf("video/*")) }, colors = ButtonDefaults.buttonColors(containerColor = EditorAccent)) {
                        Icon(Icons.Default.Add, null); Spacer(Modifier.width(6.dp)); Text("Add video")
                    }
                }
            }
        }

        Row(Modifier.fillMaxWidth().height(54.dp).background(EditorBg).padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Undo, null, tint = EditorMuted, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(20.dp))
            Icon(Icons.Default.Redo, null, tint = EditorMuted, modifier = Modifier.size(24.dp))
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { if (player.isPlaying) player.pause() else player.play() }, enabled = videoUri != null) {
                Icon(if (player.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Spacer(Modifier.weight(1f))
            Text("00:${(player.currentPosition / 1000).toInt().toString().padStart(2,'0')}", color = EditorMuted, fontSize = 11.sp)
            Spacer(Modifier.width(12.dp))
            Icon(Icons.Default.Fullscreen, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }

        Row(Modifier.fillMaxWidth().height(78.dp).background(EditorPanel).horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            EditorTool("Canvas", Icons.Default.Crop, activeTool) { activeTool = it }
            EditorTool("Audio", Icons.Default.MusicNote, activeTool) { activeTool = it; audioPicker.launch(arrayOf("audio/*")) }
            EditorTool("Sticker", Icons.Default.EmojiEmotions, activeTool) { activeTool = it; imagePicker.launch(arrayOf("image/*")) }
            EditorTool("Text", Icons.Default.TextFields, activeTool) { activeTool = it; showTextEditor = true }
            EditorTool("Effect", Icons.Default.AutoAwesome, activeTool) { activeTool = it; showTextEditor = true }
            EditorTool("Filter", Icons.Default.Tune, activeTool) { activeTool = it }
            EditorTool("PIP", Icons.Default.Layers, activeTool) { activeTool = it; imagePicker.launch(arrayOf("image/*")) }
        }

        Column(Modifier.fillMaxWidth().height(222.dp).background(Color(0xFF242424)).padding(horizontal = 12.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Timeline", color = EditorText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(if (durationMs > 0) String.format("%.1fs", durationMs / 1000f) else "00:00", color = EditorMuted, fontSize = 11.sp)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { videoPicker.launch(arrayOf("video/*")) }, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EditorAccent)) { Icon(Icons.Default.Add, null); Text("Add") }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(if (videoUri != null) "VIDEO 1" else "VIDEO TRACK", color = EditorMuted, fontSize = 9.sp)
                    Box(Modifier.fillMaxWidth().height(44.dp).background(Color(0xFF3A3A3A), RoundedCornerShape(5.dp)), contentAlignment = Alignment.Center) {
                        Text(if (videoUri != null) "Video clip • drag handles with Trim below" else "No video imported", color = EditorText, fontSize = 10.sp)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("AUDIO", color = EditorMuted, fontSize = 9.sp, modifier = Modifier.width(55.dp))
                Box(Modifier.weight(1f).height(28.dp).background(if (audioUri != null) Color(0xFF386B57) else Color(0xFF383838), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center) {
                    Text(if (audioUri != null) "Music track added" else "Tap Audio to add song", color = EditorText, fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("TEXT", color = EditorMuted, fontSize = 9.sp, modifier = Modifier.width(55.dp))
                Box(Modifier.weight(1f).height(26.dp).background(if (overlayText.isNotBlank()) Color(0xFF69518D) else Color(0xFF383838), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center) {
                    Text(if (overlayText.isNotBlank()) overlayText else "Tap Text to add title", color = EditorText, fontSize = 10.sp, maxLines = 1)
                }
            }
            if (videoUri != null && durationMs > 0) {
                Spacer(Modifier.height(7.dp))
                Text("Trim range", color = EditorMuted, fontSize = 10.sp)
                RangeSlider(value = trimStart..trimEnd, onValueChange = { trimStart = it.start; trimEnd = it.endInclusive },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth().height(28.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Start ${(durationMs * trimStart / 100000f).toInt()}s", color = EditorMuted, fontSize = 9.sp)
                    Text("End ${(durationMs * trimEnd / 100000f).toInt()}s", color = EditorMuted, fontSize = 9.sp)
                    TextButton(onClick = { splitMarkers = splitMarkers + (player.currentPosition.toFloat() / durationMs.coerceAtLeast(1) * 100f) }) {
                        Text("Split marker", fontSize = 10.sp)
                    }
                }
            }
        }
    }

    if (showTextEditor) {
        AlertDialog(onDismissRequest = { showTextEditor = false }, title = { Text("Edit text layer") },
            text = { OutlinedTextField(value = overlayText, onValueChange = { overlayText = it }, label = { Text("Your text") }, singleLine = false) },
            confirmButton = { TextButton(onClick = { showTextEditor = false }) { Text("Apply") } },
            dismissButton = { TextButton(onClick = { overlayText = ""; showTextEditor = false }) { Text("Remove") } })
    }
    if (exporting) {
        AlertDialog(onDismissRequest = {}, title = { Text("Exporting video") }, text = { Column { LinearProgressIndicator(progress = { exportProgress }); Text("Rendering MP4…") } },
            confirmButton = {})
    }
}

@Composable
private fun RowScope.EditorTool(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, active: String, onClick: (String) -> Unit) {
    Column(Modifier.width(68.dp).fillMaxHeight().clickable { onClick(label) }.padding(vertical = 9.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(icon, null, tint = if (active == label) Color.White else Color(0xFFBDBDBD), modifier = Modifier.size(27.dp))
        Spacer(Modifier.height(5.dp))
        Text(label, color = if (active == label) Color.White else Color(0xFF929292), fontSize = 10.sp, fontWeight = if (active == label) FontWeight.Bold else FontWeight.Normal)
    }
}

private fun exportVideo(context: Context, uri: Uri, startMs: Long, endMs: Long, onStart: () -> Unit, onDone: (File) -> Unit, onError: (String) -> Unit) {
    try {
        val input = MediaItem.Builder().setUri(uri).setClippingConfiguration(
            MediaItem.ClippingConfiguration.Builder().setStartPositionMs(startMs.coerceAtLeast(0L))
                .setEndPositionMs(endMs.coerceAtLeast(startMs + 1L)).build()).build()
        val edited = EditedMediaItem.Builder(input).build()
        val output = File(context.cacheDir, "SAJI_export_${System.currentTimeMillis()}.mp4")
        val transformer = Transformer.Builder(context).addListener(object : Transformer.Listener {
            override fun onCompleted(composition: Composition, exportResult: ExportResult) {
                onDone(output)
            }
            override fun onError(composition: Composition, exportResult: ExportResult, exportException: ExportException) {
                onError(exportException.localizedMessage ?: "Export failed")
            }
        }).build()
        onStart()
        transformer.start(edited, output.absolutePath)
    } catch (e: Exception) { onError(e.localizedMessage ?: "Unable to start export") }
}
