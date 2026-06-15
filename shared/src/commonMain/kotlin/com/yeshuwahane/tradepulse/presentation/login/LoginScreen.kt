package com.yeshuwahane.tradepulse.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.presentation.admin.AdminOverviewScreen
import com.yeshuwahane.tradepulse.presentation.marketplace.CustomerMarketplaceScreen
import com.yeshuwahane.tradepulse.presentation.supplier.SupplierDashboardScreen

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginViewModel>()
        val state by viewModel.state.collectAsState()
        val effect by viewModel.effect.collectAsState()

        var isSignUpMode by remember { mutableStateOf(false) }

        val pagerState = rememberPagerState(
            initialPage = state.selectedRole.ordinal,
            pageCount = { UserRole.values().size }
        )
        val scope = rememberCoroutineScope()

        LaunchedEffect(pagerState.currentPage) {
            val role = UserRole.values()[pagerState.currentPage]
            if (state.selectedRole != role) {
                viewModel.onIntent(LoginIntent.SelectRole(role))
                isSignUpMode = false
            }
        }

        LaunchedEffect(state.selectedRole) {
            if (pagerState.currentPage != state.selectedRole.ordinal) {
                pagerState.animateScrollToPage(state.selectedRole.ordinal)
            }
        }

        LaunchedEffect(effect) {
            effect?.let {
                when (it) {
                    LoginEffect.NavigateToCustomerMarketplace -> navigator.replaceAll(
                        CustomerMarketplaceScreen()
                    )
                    LoginEffect.NavigateToSupplierDashboard -> navigator.replaceAll(
                        SupplierDashboardScreen()
                    )
                    LoginEffect.NavigateToAdminDashboard -> navigator.replaceAll(AdminOverviewScreen())
                }
                viewModel.resetEffect()
            }
        }

        val errorMessage = state.errorMessage
        if (errorMessage.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { viewModel.onIntent(LoginIntent.DismissDialog) },
                title = {
                    Text(
                        text = "Authentication Failed",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                text = {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onIntent(LoginIntent.DismissDialog) }
                    ) {
                        Text(
                            text = "Dismiss",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            )
        }

        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                FullScreenPulseAnimation(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TRADEPULSE",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 3.sp
                            )
                            Text(
                                text = "B2B Trading & Auction Terminal",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                Spacer(modifier = Modifier.height(28.dp))

                TabRow(
                    selectedTabIndex = state.selectedRole.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    UserRole.values().forEach { role ->
                        Tab(
                            selected = state.selectedRole == role,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(role.ordinal)
                                }
                            },
                            text = {
                                Text(
                                    text = role.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val pageRole = UserRole.values()[page]
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isSignUpMode) "Supplier/Client Signup" else "${pageRole.name} Login",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                AnimatedVisibility(visible = isSignUpMode) {
                                    Column {
                                        OutlinedTextField(
                                            value = state.name,
                                            onValueChange = { viewModel.onIntent(LoginIntent.UpdateName(it)) },
                                            label = { Text("Display / Company Name") },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Person,
                                                    contentDescription = "Name"
                                                )
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }

                                OutlinedTextField(
                                    value = state.email,
                                    onValueChange = { viewModel.onIntent(LoginIntent.UpdateEmail(it)) },
                                    label = { Text("Email Address") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Email,
                                            contentDescription = "Email"
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = state.password,
                                    onValueChange = { viewModel.onIntent(LoginIntent.UpdatePassword(it)) },
                                    label = { Text("Password") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "Password"
                                        )
                                    },
                                    visualTransformation = PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (isSignUpMode) {
                                            viewModel.onIntent(LoginIntent.SubmitRegister)
                                        } else {
                                            viewModel.onIntent(LoginIntent.SubmitLogin)
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = when (pageRole) {
                                            UserRole.CUSTOMER -> MaterialTheme.colorScheme.primary
                                            UserRole.SUPPLIER -> MaterialTheme.colorScheme.secondary
                                            UserRole.ADMIN -> MaterialTheme.colorScheme.tertiary
                                        }
                                    ),
                                    enabled = !state.isLoading
                                ) {
                                    if (state.isLoading) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = if (isSignUpMode) "Register & Authenticate" else "Secure Login",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (pageRole != UserRole.ADMIN) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = if (isSignUpMode) "Already have an account? " else "Need a new account? ",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (isSignUpMode) "Log In" else "Sign Up",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.clickable {
                                                isSignUpMode = !isSignUpMode
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Auto-Fill Simulator Accounts",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (pageRole) {
                                UserRole.CUSTOMER -> {
                                    AutoFillButton(
                                        label = "Alice Smith (Customer)",
                                        email = "alice@customer.com",
                                        psw = "alice123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("alice@customer.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("alice123"))
                                        }
                                    )
                                    AutoFillButton(
                                        label = "Bob Jones (Customer)",
                                        email = "bob@customer.com",
                                        psw = "bob123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("bob@customer.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("bob123"))
                                        }
                                    )
                                }
                                UserRole.SUPPLIER -> {
                                    AutoFillButton(
                                        label = "Global Tech (Supplier)",
                                        email = "info@globaltech.com",
                                        psw = "global123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("info@globaltech.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("global123"))
                                        }
                                    )
                                    AutoFillButton(
                                        label = "Apex Electronics (Supplier)",
                                        email = "sales@apexelectronics.com",
                                        psw = "apex123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("sales@apexelectronics.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("apex123"))
                                        }
                                    )
                                }
                                UserRole.ADMIN -> {
                                    AutoFillButton(
                                        label = "Chief Admin (Admin)",
                                        email = "admin@zeerostock.com",
                                        psw = "admin123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("admin@zeerostock.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("admin123"))
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    AutoFillButton(
                                        label = "Operations Manager (Admin)",
                                        email = "manager@zeerostock.com",
                                        psw = "manager123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("manager@zeerostock.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("manager123"))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

    @Composable
    private fun AutoFillButton(
        label: String,
        email: String,
        psw: String,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.5f
                )
            )
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Autofill",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Email: $email | Psw: $psw",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun FullScreenPulseAnimation(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val transition = rememberInfiniteTransition()

    // Progress of wave 1
    val progress1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Progress of wave 2 (staggered by 1250ms)
    val progress2 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, delayMillis = 1250, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Progress of wave 3 (staggered by 2500ms)
    val progress3 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, delayMillis = 2500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Progress of wave 4 (staggered by 3750ms)
    val progress4 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, delayMillis = 3750, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Center horizontally and align vertically around the logo area (roughly 90.dp from the top)
        val centerX = width / 2f
        val centerY = 90.dp.toPx()

        // Max radius expanded to 1.5x of the screen bounds to feel massive
        val maxRadius = maxOf(width, height) * 1.5f

        // Draw the 4 staggered volumetric waves
        drawPulseWave(centerX, centerY, progress1, maxRadius, color)
        drawPulseWave(centerX, centerY, progress2, maxRadius, color)
        drawPulseWave(centerX, centerY, progress3, maxRadius, color)
        drawPulseWave(centerX, centerY, progress4, maxRadius, color)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPulseWave(
    centerX: Float,
    centerY: Float,
    progress: Float,
    maxRadius: Float,
    baseColor: Color
) {
    // Fade out as it expands: starts at 0.22 max alpha and goes to 0f
    val alpha = (1f - progress) * 0.22f
    if (alpha <= 0.01f) return

    val radius = progress * maxRadius

    // Draw volumetric water displacement glow (radial gradient)
    val gradientBrush = Brush.radialGradient(
        colors = listOf(
            baseColor.copy(alpha = alpha * 0.35f),
            baseColor.copy(alpha = alpha * 0.08f),
            Color.Transparent
        ),
        center = Offset(centerX, centerY),
        radius = radius.coerceAtLeast(1f)
    )
    drawCircle(
        brush = gradientBrush,
        radius = radius
    )
}

