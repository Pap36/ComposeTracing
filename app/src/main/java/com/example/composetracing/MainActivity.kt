package com.example.composetracing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composetracing.ui.theme.ComposeTracingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTracingTheme {
                // A surface container using the 'background' color from the theme

                val mainViewModel = hiltViewModel<MainViewModel>()
                val uiItems by mainViewModel.uiItemsList.collectAsStateWithLifecycle()
                val currentItem by mainViewModel.currentItem.collectAsStateWithLifecycle()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(
                            uiItems,
                            key = { it.value }
                        ) {
                            UIItemView(
                                uiItem = it,
                                isCurrent = { it == currentItem },
                                onClick = { mainViewModel.setCurrentItemAndShuffle(it) },
                                shuffle = mainViewModel::shuffle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ABad(
    item: UIItem,
    itemsSize: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        UIItemView(uiItem = item)
        BBad(itemsSize)
    }
}

@Composable
fun BBad(itemsSize: Int) {
    if (itemsSize % 2 == 0) Text("Even")
    else Text("Odd")
}

@Composable
fun AGood(
    item: UIItem,
    itemsSize: () -> Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        UIItemView(uiItem = item)
        BGood(itemsSize)
    }
}

@Composable
fun BGood(itemsSize: () -> Int) {
    if (itemsSize() % 2 == 0) Text("Even")
    else Text("Odd")
}

@Composable
fun UIItemView(
    uiItem: UIItem,
    isCurrent: () -> Boolean = { false },
    onClick: () -> Unit = {},
    shuffle: () -> Unit = {},
) {
    val text by remember {
        derivedStateOf {
            if (isCurrent()) "Current"
            else "Not current"
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                shuffle()
                onClick()
            }
    ) {
        Text(uiItem.value.toString())
        Text(text)
    }
    Divider(modifier = Modifier.fillMaxWidth())
}

@Composable
fun LazyItemScope.ItemViewWithModifier(
    modifier: Modifier = Modifier,
    item: UIItem,
    clickCallback: () -> Unit = {},
    onItemClick: () -> Unit = {},
    currentItem: UIItem? = null,
    isThisCurrentNoLambda: Boolean = false,
    isThisCurrent: () -> Boolean = { false },
) {
    ItemComposable(
        modifier = modifier,
        item = item,
        clickCallback = clickCallback,
        onItemClick = onItemClick,
        currentItem = currentItem,
        isThisCurrent = isThisCurrent,
        isThisCurrentNoLambda = isThisCurrentNoLambda,
    )
    Divider(modifier = modifier.fillMaxWidth())
}

@Composable
fun ItemComposable(
    modifier: Modifier = Modifier,
    item: UIItem,
    clickCallback: () -> Unit = {},
    uselessVariable: Int = -1,
    onItemClick: () -> Unit = {},
    currentItem: UIItem? = null,
    isThisCurrentNoLambda: Boolean = false,
    isThisCurrent: () -> Boolean = { false },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .alpha(
                if (isThisCurrent()) 1f
                else 0.5f
            )
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.value.toString())
        TextButton(
            onClick = clickCallback
        ) {
            Text(text = "Shuffle $uselessVariable")
        }
        Icon(
            imageVector =
            if (item.value % 2 == 0) Icons.Filled.Favorite
            else Icons.Filled.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier
                .scale(if (isThisCurrent()) 1.5f else 1f)
        )
    }
}
