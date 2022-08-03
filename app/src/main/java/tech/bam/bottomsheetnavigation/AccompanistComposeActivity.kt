package tech.bam.bottomsheetnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.bam.bottomsheetnavigation.accompanist.*
import tech.bam.bottomsheetnavigation.ui.bottomsheet.BottomSheetStructure
import tech.bam.bottomsheetnavigation.ui.theme.BottomSheetNavigationTheme

class AccompanistComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomSheetNavigationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DemonstrationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DemonstrationScreen() {
    val bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val navController = rememberNavController()
    val hideAndReset: () -> Unit =
        { navController.popBackStack(route = Destinations.Home, inclusive = false) }
    val bottomSheetNavigator: BottomSheetNavigator = rememberBottomSheetNavigator(
        sheetState = bottomSheetState,
        navController = navController,
        onSheetDismissed = hideAndReset
    )
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        )
    ) {
        NavHost(navController = navController, startDestination = Destinations.Home) {
            composable(route = Destinations.Home) {
                Column(Modifier.fillMaxSize().padding(48.dp)) {
                    Button(onClick = { navController.navigate(Destinations.Sheet_X_A) }) {
                        Text(Destinations.Sheet_X_A)
                    }
                    NavButton(Destinations.Sheet_X_B, navController)
                    NavButton(Destinations.Sheet_Y_A, navController)
                }
            }
            bottomSheet(route = Destinations.Sheet_X_A) {
                BottomSheetStructure(
                    onCrossClick = hideAndReset,
                    titleText = Destinations.Sheet_X_A
                ) {
                    NavButton(Destinations.Sheet_X_B, navController)
                    NavButton(Destinations.Sheet_Y_A, navController)
                }
            }

            bottomSheet(route = Destinations.Sheet_X_B) {
                BottomSheetStructure(
                    onCrossClick = hideAndReset,
                    titleText = Destinations.Sheet_X_B
                ) {
                    NavButton(Destinations.Sheet_X_C, navController)
                    NavButton(Destinations.Sheet_Y_A, navController)
                }
            }

            bottomSheet(route = Destinations.Sheet_X_C) {
                BottomSheetStructure(
                    onCrossClick = hideAndReset,
                    titleText = Destinations.Sheet_X_C
                ) {
                    NavButton(Destinations.Sheet_Y_A, navController)
                }
            }

            bottomSheet(route = Destinations.Sheet_Y_A) {
                BottomSheetStructure(
                    onCrossClick = hideAndReset,
                    titleText = Destinations.Sheet_Y_A
                ) {
                    NavButton(Destinations.Sheet_Y_B, navController)
                    NavButton(Destinations.Sheet_X_A, navController)
                }
            }
            bottomSheet(route = Destinations.Sheet_Y_B) {
                BottomSheetStructure(
                    onCrossClick = hideAndReset,
                    titleText = Destinations.Sheet_Y_B
                ) {
                    NavButton(Destinations.Sheet_Y_C, navController)
                    NavButton(Destinations.Sheet_X_A, navController)
                }
            }
            bottomSheet(route = Destinations.Sheet_Y_C) {
                BottomSheetStructure(
                    onCrossClick = hideAndReset,
                    titleText = Destinations.Sheet_Y_C
                ) {
                    NavButton(Destinations.Sheet_X_A, navController)
                }
            }
        }
    }
}

@Composable
private fun NavButton(route: String, navController: NavController) {
    Button(onClick = { navController.navigate(route) }) {
        Text(route)
    }
}

private object Destinations {
    const val Home = "HOME"
    const val Sheet_X_A = "SHEET_X_A"
    const val Sheet_X_B = "SHEET_X_B"
    const val Sheet_X_C = "SHEET_X_C"
    const val Sheet_Y_A = "SHEET_Y_A"
    const val Sheet_Y_B = "SHEET_Y_B"
    const val Sheet_Y_C = "SHEET_Y_C"
}
