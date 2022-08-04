package tech.bam.bottomsheetnavigation.accompanist

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.Navigator

@Navigator.Name("CustomBottomSheetNavigator")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class)
class CustomBottomSheetNavigator(
    sheetState: ModalBottomSheetState,
    val customOnSheetDismissed: (() -> Unit)? = null
) : BottomSheetNavigator(sheetState) {
    override val sheetContent: @Composable ColumnScope.() -> Unit
        get() = @Composable {
            val columnScope = this
            val saveableStateHolder = rememberSaveableStateHolder()
            val backStackEntries by backStack.collectAsState()
            val transitionsInProgressEntries by transitionsInProgress.collectAsState()

            // We always replace the sheet's content instead of overlaying and nesting floating
            // window destinations. That means that only *one* concurrent destination is supported by
            // this navigator.
            val latestEntry = backStackEntries.lastOrNull { entry ->
                // We might have entries in the back stack that aren't started currently, so filter
                // these
                entry.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
            }

            // Mark all of the entries' transitions as complete, except for the entry we are
            // currently displaying because it will have its transition completed when the sheet's
            // animation has completed
            DisposableEffect(backStackEntries) {
                transitionsInProgressEntries.forEach {
                    if (it != latestEntry) state.markTransitionComplete(it)
                }
                onDispose { }
            }

            SheetContentHost(
                columnHost = columnScope,
                backStackEntry = latestEntry,
                sheetState = sheetState,
                saveableStateHolder = saveableStateHolder,
                onSheetShown = { backStackEntry ->
                    state.markTransitionComplete(backStackEntry)
                },
                onSheetDismissed = { backStackEntry ->
                    if (customOnSheetDismissed != null) {
                        customOnSheetDismissed!!()
                    } else {
                        // Sheet dismissal can be started through popBackStack in which case we have a
                        // transition that we'll want to complete
                        if (transitionsInProgressEntries.contains(backStackEntry)) {
                            state.markTransitionComplete(backStackEntry)
                        } else {
                            state.pop(popUpTo = backStackEntry, saveState = false)
                        }
                    }
                }
            )
        }
}

/**
 * Create and remember a [BottomSheetNavigator]
 */
@ExperimentalMaterialNavigationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec
    ),
    onSheetDismissed: (() -> Unit)? = null,
    navController: NavHostController
): CustomBottomSheetNavigator {
    return CustomBottomSheetNavigator(
        sheetState = sheetState,
        customOnSheetDismissed = onSheetDismissed
    ).apply {
        navController.navigatorProvider.addNavigator(this)
    }
}
