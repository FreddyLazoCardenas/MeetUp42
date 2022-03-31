package com.androiddev.meetup42

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.androiddev.meetup42.logging.manager.LogManager
import com.androiddev.meetup42.networking.getSecureOkHttpClient
import com.androiddev.meetup42.ui.theme.MeetUp42Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeetUp42Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Greeting("Community!")
                        Button(onClick = { makeRequest(OkHttpClient.Builder().build()) }) {
                            Text(
                                text = "Normal http Request"
                            )
                        }
                        Title("Secure SSL requests")
                        Button(onClick = {
                            makeRequest(
                                getSecureOkHttpClient(
                                    this@MainActivity.openRawResource(
                                        R.raw.github
                                    )
                                ).build()
                            )
                        }) { Text(text = "Secure http Request") }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Button(onClick = {
                            makeRequest(
                                getSecureOkHttpClient(
                                    this@MainActivity.openRawResource(
                                        R.raw.wrongcertificate
                                    )
                                ).build()
                            )
                        }) { Text(text = "Secure http Request wrong certificate") }
                    }
                }
            }
        }
    }

    private fun makeRequest(client: OkHttpClient) {
        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val request = Request.Builder().apply {
                        get()
                        url("https://api.github.com/orgs/octokit/repos")
                    }.build()
                    client.newCall(request).execute()
                }
                val response = result.body?.string() ?: "empty"
                Toast.makeText(baseContext, response, Toast.LENGTH_SHORT).show()
            } catch (exception: Exception) {
                Toast.makeText(baseContext, "${exception.message}", Toast.LENGTH_SHORT).show()
                LogManager.getInstance().sendLogToDashboard(exception.message ?: "empty error")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogManager.destroy()
    }
}


fun Context.openRawResource(id: Int): InputStream {
    try {
        return this.resources.openRawResource(id)
    } catch (exception: Exception) {
        error("Cannot read certificate")
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        modifier = Modifier.padding(16.dp),
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun Title(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp),
        fontWeight = FontWeight.Bold
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MeetUp42Theme {
        Greeting("Android")
    }
}