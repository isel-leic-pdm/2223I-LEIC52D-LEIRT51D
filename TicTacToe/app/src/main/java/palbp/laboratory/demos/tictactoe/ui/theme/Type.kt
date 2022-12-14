package palbp.laboratory.demos.tictactoe.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import palbp.laboratory.demos.tictactoe.R

// https://www.fontspace.com/rusty-hooks-font-f87577
private val TicTacToe = FontFamily(
    Font(R.font.rusty_hooks)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 40.sp
    ),

    h4 = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 24.sp
    ),

    h5 = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 18.sp
    ),

    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),

    subtitle2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    button = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 24.sp
    ),
)