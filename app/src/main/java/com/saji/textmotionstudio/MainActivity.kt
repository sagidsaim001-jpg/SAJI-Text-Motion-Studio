package com.saji.textmotionstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saji.textmotionstudio.data.MotionStyle
import com.saji.textmotionstudio.data.TemplateCatalog
import com.saji.textmotionstudio.data.TextTemplate
import kotlin.math.PI
import kotlin.math.sin

private val Ink = Color(0xFF090B12)
private val Panel = Color(0xFF151927)
private val PanelRaised = Color(0xFF1C2232)
private val Muted = Color(0xFF9299AD)
private val Violet = Color(0xFF9B82FF)
private val White = Color(0xFFF7F7FC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TextMotionApp() }
    }
}

@Composable
fun TextMotionApp() {
    var selectedTemplate by remember { mutableStateOf<TextTemplate?>(null) }
    var activeTemplate by remember { mutableStateOf<TextTemplate?>(null) }
    var customText by remember { mutableStateOf("MAKE IT MOVE") }
    var appliedMessage by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Violet,
            background = Ink,
            surface = Panel,
            onPrimary = Color.White,
            onBackground = White,
            onSurface = White
        )
    ) {
        if (selectedTemplate == null) {
            TemplateGalleryScreen(
                activeTemplate = activeTemplate,
                customText = customText,
                appliedMessage = appliedMessage,
                onTemplateClick = { selectedTemplate = it }
            )
        } else {
            val templateToPreview = selectedTemplate!!
            TemplatePreviewScreen(
                template = templateToPreview,
                text = customText,
                onTextChanged = { customText = it },
                onBack = { selectedTemplate = null },
                onApply = {
                    activeTemplate = templateToPreview
                    appliedMessage = "${templateToPreview.title} is ready to use"
                    selectedTemplate = null
                }
            )
        }
    }
}

@Composable
private fun TemplateGalleryScreen(
    activeTemplate: TextTemplate?,
    customText: String,
    appliedMessage: String,
    onTemplateClick: (TextTemplate) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var activeCategory by remember { mutableStateOf("All") }

    val visibleTemplates = TemplateCatalog.templates.filter { template ->
        (activeCategory == "All" || template.category == activeCategory) &&
            (searchQuery.isBlank() ||
                template.title.contains(searchQuery, ignoreCase = true) ||
                template.description.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 18.dp, top = 18.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(Violet, Color(0xFF56DCE7)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("SAJI", color = White, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("TEXT MOTION STUDIO", color = Muted, fontSize = 10.sp, letterSpacing = 1.4.sp)
            }
            Surface(shape = CircleShape, color = PanelRaised) {
                Text(
                    "36 PRESETS",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = Color(0xFFBFB1FF),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PanelRaised)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFF292448), Color(0xFF182D3A), Color(0xFF171C2B))))
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        if (activeTemplate == null) "MOTION THAT MAKES A STATEMENT" else "YOUR SELECTED MOTION",
                        color = Color(0xFFB8ACFF),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.3.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(74.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedTemplateText(
                            template = activeTemplate ?: TemplateCatalog.templates.first(),
                            text = customText,
                            fontSize = 29.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        activeTemplate?.title ?: "36 fresh ways to move your words",
                        color = White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        appliedMessage.ifBlank { "Tap any preset to edit text and preview its animation." },
                        color = Muted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            singleLine = true,
            placeholder = { Text("Search text styles...", color = Muted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Muted) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Panel,
                unfocusedContainerColor = Panel,
                focusedBorderColor = Violet,
                unfocusedBorderColor = Color(0xFF292F40),
                focusedTextColor = White,
                unfocusedTextColor = White,
                cursorColor = Violet
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TemplateCatalog.categories.forEach { category ->
                FilterChip(
                    selected = activeCategory == category,
                    onClick = { activeCategory = category },
                    label = {
                        Text(
                            category,
                            fontSize = 11.sp,
                            fontWeight = if (activeCategory == category) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Explore templates", color = White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text("${visibleTemplates.size} styles", color = Muted, fontSize = 11.sp)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(visibleTemplates, key = { it.id }) { template ->
                TemplateCard(template = template, onClick = { onTemplateClick(template) })
            }
        }
    }
}

@Composable
private fun TemplateCard(template: TextTemplate, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(158.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        border = BorderStroke(1.dp, Color(0xFF292F40))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF22263A), Color(0xFF111725), Color(0xFF202039)))),
                contentAlignment = Alignment.Center
            ) {
                AnimatedTemplateText(template = template, text = "YOUR TEXT", fontSize = 17.sp)
            }
            Spacer(Modifier.height(9.dp))
            Text(template.title, color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Spacer(Modifier.height(3.dp))
            Text(
                template.category.uppercase(),
                color = Color(template.accent),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun TemplatePreviewScreen(
    template: TextTemplate,
    text: String,
    onTextChanged: (String) -> Unit,
    onBack: () -> Unit,
    onApply: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .padding(horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Template preview", color = White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(template.category, color = Muted, fontSize = 11.sp)
            }
            Surface(shape = CircleShape, color = Color(0xFF20342F)) {
                Text(
                    "LIVE",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    color = Color(0xFF8FE2B8),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF292448), Color(0xFF172D3A), Color(0xFF171C2B))))
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedTemplateText(template = template, text = text.ifBlank { "YOUR TEXT" }, fontSize = 35.sp)
        }

        Spacer(Modifier.height(22.dp))
        Text("YOUR TEXT", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            placeholder = { Text("Type something...", color = Muted) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Panel,
                unfocusedContainerColor = Panel,
                focusedBorderColor = Violet,
                unfocusedBorderColor = Color(0xFF292F40),
                focusedTextColor = White,
                unfocusedTextColor = White,
                cursorColor = Violet
            )
        )
        Spacer(Modifier.height(16.dp))
        Text(template.title, color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        Text(template.description, color = Muted, fontSize = 12.sp)
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp)
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Violet)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(9.dp))
            Text("Use this template", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AnimatedTemplateText(
    template: TextTemplate,
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "motion-${template.id}")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(template.durationMs, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress-${template.id}"
    )

    var displayText = text
    var scaleX = 1f
    var scaleY = 1f
    var rotationX = 0f
    var rotationY = 0f
    var rotationZ = 0f
    var translationX = 0f
    var translationY = 0f
    var alpha = 1f
    var spacing = 0.02f.sp

    when (template.motion) {
        MotionStyle.POP -> {
            scaleX = 0.72f + 0.28f * progress
            scaleY = 0.72f + 0.28f * progress
        }
        MotionStyle.RISE -> {
            translationY = (1f - progress) * 34f
            alpha = 0.45f + 0.55f * progress
        }
        MotionStyle.FLIP -> {
            rotationX = (1f - progress) * 72f
            scaleY = 0.84f + 0.16f * progress
        }
        MotionStyle.TILT -> {
            rotationY = (0.5f - progress) * 42f
            rotationZ = (0.5f - progress) * 5f
        }
        MotionStyle.TRACKING -> {
            spacing = (0.26f * (1f - progress) + 0.02f).sp
            scaleX = 0.94f + 0.06f * progress
        }
        MotionStyle.TYPEWRITER -> {
            val count = (text.length * progress).toInt().coerceIn(0, text.length)
            displayText = text.take(count) + if (count < text.length) "▏" else ""
        }
        MotionStyle.GLITCH -> {
            translationX = sin(progress * 22f) * 3.5f
            translationY = sin(progress * 35f) * 1.8f
            alpha = if (progress > 0.93f) 0.72f else 1f
        }
        MotionStyle.WAVE -> {
            translationY = sin(progress * 2f * PI.toFloat()) * 8f
            rotationZ = sin(progress * 2f * PI.toFloat()) * 2.5f
        }
        MotionStyle.ROLL -> {
            rotationZ = (1f - progress) * -18f
            translationY = (1f - progress) * 16f
        }
        MotionStyle.PULSE -> {
            val pulse = sin(progress * PI.toFloat())
            scaleX = 0.94f + pulse * 0.08f
            scaleY = 0.94f + pulse * 0.08f
        }
        MotionStyle.BOUNCE -> {
            scaleX = 0.78f + progress * 0.22f
            scaleY = 0.78f + progress * 0.22f
            translationY = (1f - progress) * -22f
        }
        MotionStyle.FADE -> {
            alpha = progress
            scaleX = 0.96f + progress * 0.04f
            scaleY = 0.96f + progress * 0.04f
        }
        MotionStyle.DROP -> {
            translationY = (1f - progress) * -42f
            rotationZ = (1f - progress) * 4f
        }
        MotionStyle.FLOAT -> {
            translationY = sin(progress * 2f * PI.toFloat()) * 9f
            rotationZ = sin(progress * 2f * PI.toFloat()) * 1.5f
        }
        MotionStyle.STRETCH -> {
            scaleX = 0.48f + progress * 0.52f
            scaleY = 1.12f - progress * 0.12f
        }
        MotionStyle.ECHO -> {
            scaleX = 0.92f + progress * 0.08f
            scaleY = 0.92f + progress * 0.08f
            translationX = sin(progress * 2f * PI.toFloat()) * 4f
        }
        MotionStyle.SPIN -> {
            rotationY = (1f - progress) * 180f
            scaleX = 0.86f + progress * 0.14f
        }
        MotionStyle.SNAP -> {
            scaleX = if (progress < 0.72f) 0.82f + progress * 0.22f else 0.98f + (progress - 0.72f) * 0.07f
            scaleY = scaleX
        }
        MotionStyle.STAGGER -> {
            translationY = (1f - progress) * 22f
            alpha = 0.55f + progress * 0.45f
            spacing = (0.12f * (1f - progress) + 0.02f).sp
        }
        MotionStyle.SPLIT -> {
            scaleX = 0.12f + progress * 0.88f
            alpha = 0.5f + progress * 0.5f
        }
        MotionStyle.KARAOKE -> {
            scaleY = 0.96f + sin(progress * PI.toFloat()) * 0.06f
            spacing = (0.03f + 0.08f * progress).sp
        }
        MotionStyle.ZOOM -> {
            scaleX = 0.62f + progress * 0.38f
            scaleY = 0.62f + progress * 0.38f
            alpha = 0.4f + progress * 0.6f
        }

        MotionStyle.STACKED -> {
            scaleY = 0.72f + 0.28f * progress
            translationY = (1f - progress) * 12f
            spacing = (0.03f + 0.08f * (1f - progress)).sp
        }
        MotionStyle.WORD_STACK -> {
            scaleX = 0.82f + 0.18f * progress
            scaleY = 0.76f + 0.24f * progress
            translationY = sin(progress * PI.toFloat()) * -5f
        }
        MotionStyle.SLAM -> {
            scaleX = 1.28f - 0.28f * progress
            scaleY = 1.28f - 0.28f * progress
            translationY = (1f - progress) * -18f
        }
        MotionStyle.SHADOW -> {
            translationX = sin(progress * 2f * PI.toFloat()) * 2f
            translationY = sin(progress * 2f * PI.toFloat()) * 2f
            alpha = 0.82f + 0.18f * progress
        }
        MotionStyle.SCRIPT_ACCENT -> {
            rotationZ = (0.5f - progress) * 7f
            scaleX = 0.9f + 0.1f * progress
            alpha = 0.72f + 0.28f * progress
        }
        MotionStyle.FLIP_STACK -> {
            rotationX = (1f - progress) * 88f
            rotationY = (1f - progress) * -18f
            scaleY = 0.82f + 0.18f * progress
        }
        MotionStyle.SHIMMER -> {
            scaleX = 0.96f + sin(progress * 2f * PI.toFloat()) * 0.04f
            alpha = 0.72f + 0.28f * progress
        }
        MotionStyle.NEON_GLOW -> {
            scaleX = 0.94f + 0.06f * progress
            scaleY = 0.94f + 0.06f * progress
            alpha = 0.65f + 0.35f * progress
        }
        MotionStyle.VERTICAL_WIPE -> {
            scaleY = 0.08f + 0.92f * progress
            alpha = 0.45f + 0.55f * progress
        }
        MotionStyle.GLOW_PULSE -> {
            val pulse = sin(progress * PI.toFloat())
            scaleX = 0.96f + pulse * 0.07f
            scaleY = 0.96f + pulse * 0.07f
            alpha = 0.82f + pulse * 0.18f
        }
        MotionStyle.SWEEP -> {
            translationX = (1f - progress) * -46f
            rotationZ = (1f - progress) * -4f
            alpha = 0.45f + 0.55f * progress
        }
        MotionStyle.WAVE_3D -> {
            translationY = sin(progress * 2f * PI.toFloat()) * 8f
            rotationY = sin(progress * 2f * PI.toFloat()) * 12f
            rotationZ = sin(progress * 2f * PI.toFloat()) * 2f
        }
        MotionStyle.FLOAT_UP -> {
            translationY = (1f - progress) * 28f + sin(progress * 2f * PI.toFloat()) * 4f
            alpha = 0.45f + 0.55f * progress
        }
        MotionStyle.SPLIT_REVEAL -> {
            scaleX = 0.04f + 0.96f * progress
            rotationY = (1f - progress) * 24f
            alpha = 0.55f + 0.45f * progress
        }
        MotionStyle.RGB_SHIFT -> {
            translationX = sin(progress * 38f) * 4.5f
            translationY = sin(progress * 19f) * 1.5f
            rotationZ = sin(progress * 31f) * 1.2f
        }
        MotionStyle.DEPTH -> {
            rotationY = (0.5f - progress) * 30f
            rotationX = (1f - progress) * 18f
            scaleX = 0.88f + 0.12f * progress
            scaleY = 0.88f + 0.12f * progress
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (template.motion == MotionStyle.ECHO) {
            Text(
                text = displayText,
                modifier = Modifier.graphicsLayer {
                    translationX = 7f
                    translationY = 4f
                    alpha = 0.22f
                },
                color = Color(template.accent),
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
        Text(
            text = displayText,
            modifier = Modifier.graphicsLayer {
                this.scaleX = scaleX
                this.scaleY = scaleY
                this.rotationX = rotationX
                this.rotationY = rotationY
                this.rotationZ = rotationZ
                this.translationX = translationX
                this.translationY = translationY
                this.alpha = alpha
                transformOrigin = TransformOrigin.Center
                cameraDistance = 18f * density
            },
            color = Color(template.accent),
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            letterSpacing = spacing,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
