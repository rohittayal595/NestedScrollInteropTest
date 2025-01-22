package com.example.testcomposeinterop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.testcomposeinterop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.composeView.apply {
      setContent {
        var fabOffsetPx by remember { mutableIntStateOf(0) }
        val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
        viewTreeObserver.addOnPreDrawListener {
          val bottomOccludedHeight = binding.composeView.bottom - screenHeight
          fabOffsetPx = bottomOccludedHeight
          return@addOnPreDrawListener true
        }
        Scaffold(
          floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {}, text = {
              Text(text = "Create New")
            }, icon = {
              Icon(
                painter = painterResource(id = R.drawable.ic_home_black_24dp),
                contentDescription = null
              )
            }, modifier = Modifier.offset { IntOffset(x = 0, y = -fabOffsetPx) })
          }) { padding ->
          val myDataList = List<String>(1000) { i -> "Element at index $i" }
          val nestedScrollInterop = rememberNestedScrollInteropConnection()
          LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.nestedScroll(nestedScrollInterop)
          ) {
            items(myDataList) { it ->
              Text(text = it, modifier = Modifier.padding(8.dp))
            }
          }

        }
      }
    }
  }
}