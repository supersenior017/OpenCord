package com.xinto.opencord.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R

@Composable
fun LoginLandingScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                Text(
                    text = stringResource(R.string.login_landing_title),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                )
                CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.7f)) {
                    Text(
                        text = stringResource(R.string.login_landing_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLoginClick,
                ) {
                    Text(stringResource(R.string.login_action_login))
                }
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRegisterClick,
                ) {
                    Text(stringResource(R.string.login_action_register))
                }
            }
        }
    }
}
