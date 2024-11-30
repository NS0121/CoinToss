package com.example.coin

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import com.example.coin.ui.theme.CoinTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.height

const val BUTTON_DELAY = 1500

class MainActivity : ComponentActivity() {
    // coin.cppの関数を宣言
    external fun coinToss(result: IntArray)
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // C++のライブラリを読み込む
        System.loadLibrary("coin")
        enableEdgeToEdge()
        mediaPlayer = MediaPlayer.create(this, R.raw.secoin)

        setContent {

            CoinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val result = remember { mutableStateOf(0) }

                    TossCoin(
                        // 乱数の結果
                        result = result.value,
                        // ボタンを押した際の処理
                        onButtonClick = {
                            val resultArray = IntArray(1)
                            coinToss(resultArray)
                            result.value = resultArray[0]
                            mediaPlayer.start()

                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // 音に割いたメモリを解放する
        mediaPlayer.release()
    }
}



// 表示する部分
@Composable
fun TossCoin(result: Int, onButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    // ボタンの表示・非表示状態を管理
    val isButtonVisible = remember { mutableStateOf(true) }
    // コルーチンスコープの取得
    val coroutineScope = rememberCoroutineScope()

    // 縦並びに表示
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // コインを表示
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (result == 0) {
                Image(
                    painter = painterResource(id = R.drawable.coin),  // 表面
                    contentDescription = "Coin Image"
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.coin_002),  // 裏面
                    contentDescription = "Coin Image"
                )
            }
        }

        // ボタンを表示
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(48.dp), // ボタンの高さを固定
            contentAlignment = Alignment.Center
        ) {
            if (isButtonVisible.value) {
                Button(onClick = {
                    // ボタンを非表示にする
                    isButtonVisible.value = false

                    // ボタンを押した時の処理
                    onButtonClick()

                    // 非同期で数秒後にボタンを再表示
                    coroutineScope.launch {
                        // long型に変換する必要あり
                        kotlinx.coroutines.delay(BUTTON_DELAY.toLong())  // 数秒待機
                        isButtonVisible.value = true  // 再表示。
                    }
                }) {
                    Text(text = "コイントス")
                }
            }
        }
    }
}