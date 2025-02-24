package com.kurtnettle.harukaze.presentation.about.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.data.models.DeveloperProfile
import com.kurtnettle.harukaze.presentation.home.openUrl
import com.kurtnettle.harukaze.ui.theme.HarukazeTheme
import kotlinx.coroutines.launch

@Composable
fun ProfileSection(
    profile: DeveloperProfile, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var nameDialogState by rememberSaveable { mutableStateOf(false) }
    var sloganDialogState by rememberSaveable { mutableStateOf(false) }
    var descDialogState by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileDialogue(
            text = stringResource(R.string.dialogue_username_text),
            closeBtnTxt = stringResource(R.string.meh),
            onDismiss = { nameDialogState = false },
            showDialog = nameDialogState,
            lottieIconName = "seal"
        )

        ProfileDialogue(
            text = stringResource(R.string.dialogue_intake_text),
            closeBtnTxt = stringResource(R.string.okay),
            onDismiss = { descDialogState = false },
            showDialog = descDialogState,
            lottieIconName = "wave"
        )

        ProfileDialogue(
            text = stringResource(R.string.dialogue_automation_text),
            closeBtnTxt = stringResource(R.string.dumb),
            onDismiss = { sloganDialogState = false },
            showDialog = sloganDialogState,
            lottieIconName = "sunglasses"
        )

        Image(
            painter = painterResource(id = R.drawable.dev_github_avatar),
            contentDescription = "Github profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    shape = CircleShape
                )
                .clickable {
                    coroutineScope.launch {
                        openUrl(context, profile.githubUrl)
                    }
                }
        )

        Text(
            text = profile.username,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable { nameDialogState = true }
                .padding(horizontal = 12.dp)
        )

        Text(
            text = profile.description,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable { descDialogState = true }
                .padding(horizontal = 8.dp)
        )

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable { sloganDialogState = true }
        ) {
            Text(
                text = profile.slogan,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileSection() {
    HarukazeTheme {
        ProfileSection(
            profile = DeveloperProfile(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
