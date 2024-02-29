package com.anas.aiassistant.presentaion.bottomCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.domain.viewModel.BaseViewModel
import com.anas.aiassistant.shared.BaseViewmodelEvents
import com.anas.aiassistant.ui.theme.SendIconClickableColor
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor
import com.anas.aiassistant.ui.theme.TextPrimaryColor
import com.anas.aiassistant.ui.theme.TextSecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(viewModel: BaseViewModel?) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current


    TextField(
        value = viewModel?.messageTextInput!!,
        onValueChange = { viewModel.onEvent(BaseViewmodelEvents.SetMessageInput(it.text)) },
        placeholder = {
            Text(text = viewModel.messageTextInputHint, fontSize = 23.sp, color = TextSecondaryColor)
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.68f)
            .background(Color.White)
            .focusRequester(focusRequester)
            ,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor =  Color.Black
        ),
        textStyle = TextStyle(fontSize = 23.sp, color = TextPrimaryColor),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )

    LaunchedEffect(viewModel.messageTextFieldFocus, viewModel.launchedEffectTFFocusTrigger) {
        if (viewModel.messageTextFieldFocus){
            focusRequester.requestFocus()
            keyboardController?.show()
        }else{
            focusRequester.freeFocus()
            keyboardController?.hide()
        }

    }
}

fun onValueChange(text:String, viewModel: BaseViewModel) {
    viewModel.cursorPosition = text.length
    viewModel.messageTextInput = TextFieldValue(text, TextRange(viewModel.cursorPosition))
    if (text.trim().isBlank()) {
        viewModel.sendIconColor = SendIconNotClickableColor
    } else {
        viewModel.sendIconColor = SendIconClickableColor
    }
}

@Preview
@Composable
fun CustomTextFieldPrev() {
//    CustomTextField(viewModel = MainViewModel())
}