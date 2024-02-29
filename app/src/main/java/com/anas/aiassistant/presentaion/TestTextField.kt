package com.anas.aiassistant.presentaion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.ui.theme.TextPrimaryColor
import com.anas.aiassistant.ui.theme.TextSecondaryColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTextField() {
    val value = remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = value.value,
        onValueChange = {value.value=it},

        placeholder = {
            Text(text = "hello", fontSize = 23.sp, color = TextSecondaryColor)
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.68f)
            .background(Color.White)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->

            },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White
        ),
        textStyle = TextStyle(fontSize = 23.sp, color = TextPrimaryColor),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = RectangleShape,

    )


}
@Preview
@Composable
fun Prev1 () {
    TestTextField()
}