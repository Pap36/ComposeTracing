package com.example.composetracing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.tracing.TracingInitializer
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                val items by mainViewModel.uiItemsList.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items,
                            key = { it.value }
                        ) {
                            
                            
                            val rememberedShuffle by rememberUpdatedState(mainViewModel::shuffleInt)
                            
                            val rememberItemState by remember {
                                derivedStateOf { it }
                            }

                            val rememberCallBack by remember {
                                derivedStateOf { mainViewModel::shuffle }
                            }

                            val rememberIntCallBack by remember {
                                derivedStateOf { mainViewModel::shuffleInt }
                            }
                            
                            val rememberThis by remember {
                                derivedStateOf { mainViewModel.shuffle() }
                            }

                            ItemView(
                                item = rememberItemState,
                                clickCallback = rememberedShuffle
                            )

                            /*ItemView(
                                item = rememberItemState,
                                clickCallback = rememberIntCallBack,
                            )*/

                            /*ItemView(
                                item = { rememberItemState },
                                clickCallback = { mainViewModel.shuffle() }
                            )*/

                            /*ItemView(
                                item = { it },
                                // clickCallback = { mainViewModel.shuffle() }
                            )*/

                            /*Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = it.value.toString())
                                TextButton(
                                    onClick = { mainViewModel.shuffle() }
                                ) {
                                    Text(text = "Shuffle")
                                }
                            }*/
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ItemView(
    item: UIItem,
    clickCallback: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.value.toString())
        TextButton(
            onClick = { clickCallback() }
        ) {
            Text(text = "Shuffle")
        }
    }
}

@Composable
fun ItemView(
    item: UIItem,
    clickCallback: (Int) -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.value.toString())
        TextButton(
            onClick = { clickCallback(item.value) }
        ) {
            Text(text = "Shuffle")
        }
    }
}

@Composable
fun ItemView(
    item: () -> UIItem,
    clickCallback: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item().value.toString())
        TextButton(
            onClick = clickCallback
        ) {
            Text(text = "Shuffle")
        }
    }
}
