package com.apollo9921.quizrise.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.BuildConfig
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.Red
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.core.getTypography
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoute(
    navHostController: NavHostController,
    viewModel: SettingsScreenViewModel = koinViewModel<SettingsScreenViewModel>()
) {
    val context = LocalContext.current
    val onBackClick = { navHostController.navigateUp() }
    val onPrivacyPolicyClick = { viewModel.showPrivacyPolicy(context) }
    val onLogoutClick = { viewModel.logout(navHostController) }
    val onDeleteAccountClick = { viewModel.deleteAccount(navHostController) }
    val onGdprConsentChange = { value: Boolean -> viewModel.changeGdprConsent(value) }
    val gdprConsent = viewModel.isGdprConsentGranted.collectAsStateWithLifecycle().value
    SettingsScreen(
        onBackClick = onBackClick,
        onPrivacyPolicyClick = onPrivacyPolicyClick,
        onLogoutClick = onLogoutClick,
        onDeleteAccountClick = onDeleteAccountClick,
        onGdprConsentChange = onGdprConsentChange,
        gdprConsent = gdprConsent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onBackClick: () -> Boolean,
    onPrivacyPolicyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onGdprConsentChange: (Boolean) -> Boolean,
    gdprConsent: Boolean
) {
    Scaffold(
        containerColor = PurpleGrey40,
        topBar = {
            TopBar(
                backgroundColor = PurpleGrey40,
                title = stringResource(id = R.string.settings),
                onClick = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.consent_ue),
                    style = MaterialTheme.typography.labelMedium,
                    color = White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
                Switch(
                    checked = gdprConsent,
                    onCheckedChange = {
                        onGdprConsentChange(it)
                    }
                )
            }

            HorizontalDivider(
                color = White.copy(alpha = 0.1f),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onPrivacyPolicyClick() }
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.terms_private_policy),
                    style = MaterialTheme.typography.labelMedium,
                    color = White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
            }

            HorizontalDivider(
                color = White.copy(alpha = 0.1f),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onLogoutClick() }
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.logout),
                    style = MaterialTheme.typography.labelMedium,
                    color = Red.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.delete_account_btn),
                style = MaterialTheme.typography.labelSmall,
                color = White.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onDeleteAccountClick() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.labelSmall,
                color = White.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme(typography = getTypography()) {
        SettingsScreen(
            onBackClick = { true },
            onPrivacyPolicyClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {},
            onGdprConsentChange = { false },
            gdprConsent = false
        )
    }
}