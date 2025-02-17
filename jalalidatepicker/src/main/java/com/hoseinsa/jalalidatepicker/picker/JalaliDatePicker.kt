package com.hoseinsa.jalalidatepicker.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.hoseinsa.jalalidatepicker.components.WheelPicker
import com.hoseinsa.jalalidatepicker.utils.DateUtils
import com.hoseinsa.jalalidatepicker.utils.PersianNumberUtils.formatToHindiIfLanguageIsFa
import com.hoseinsa.jalalidatepicker.utils.PersianNumberUtils.toArabicDigits
import ir.huri.jcal.JalaliCalendar

@Composable
fun JalaliDatePicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    color: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    fontFamily: FontFamily = FontFamily.Default,
    onDateSelected: (jYear: Int, jMonth: Int, jDay: Int) -> Unit,
) {

    val isEnglish = Locale.current.language != "fa"

    val initialDate by remember {
        mutableStateOf(JalaliCalendar())
    }

    var selectedDate by remember {
        mutableStateOf(initialDate)
    }

    val years by remember {
        mutableStateOf(
            DateUtils.initYearList(initialDate.year).map { it.toString().formatToHindiIfLanguageIsFa() })
    }

    val months by remember {
        mutableStateOf(
            DateUtils.initMonthList(isEnglish)
        )
    }

    val days by remember {
        mutableStateOf(
            DateUtils.initDaysList(initialDate.monthLength).map { it })
    }

    LaunchedEffect(selectedDate) {
        onDateSelected(
            selectedDate.year,
            selectedDate.month,
            selectedDate.day,
        )
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WheelPicker(
                modifier = Modifier,
                options = days,
                initialValue = initialDate.day - 1,
                fontFamily = fontFamily,
                textStyle = textStyle,
                color = color,
                onValueSelected = { index, _ ->
                    selectedDate = JalaliCalendar().apply {
                        day = index + 1
                        month = selectedDate.month
                        year = selectedDate.year
                    }
                }
            )
            WheelPicker(
                options = months,
                initialValue = initialDate.month - 1,
                fontFamily = fontFamily,
                textStyle = textStyle,
                color = color,
                onValueSelected = { index, _ ->
                    selectedDate = JalaliCalendar().apply {
                        day = selectedDate.day
                        month = index + 1
                        year = selectedDate.year
                    }
                }
            )
            WheelPicker(
                options = years,
                initialValue = 1,
                fontFamily = fontFamily,
                textStyle = textStyle,
                color = color,
                onValueSelected = { index, _ ->
                    selectedDate = JalaliCalendar().apply {
                        day = selectedDate.day
                        month = selectedDate.month
                        year = years[index].toArabicDigits()
                    }
                }
            )
        }
    }
}