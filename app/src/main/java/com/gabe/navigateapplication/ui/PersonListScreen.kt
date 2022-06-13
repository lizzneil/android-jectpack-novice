package com.gabe.navigateapplication.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel

import com.gabe.navigateapplication.PersonEntity

@Preview
@Composable
fun PersonListScreen(
    viewModel: PersonListViewModel = hiltViewModel()
) {
    val persons = viewModel.persons
        .collectAsState(initial = emptyList())
        .value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp,top=40.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(persons) { person ->
                    PersonItem(
                        person = person,
                        onItemClick = { viewModel.getPersonById(person.id) },
                        onDeleteClick = { viewModel.onDeleteClick(person.id) },
                        modifier =  Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = viewModel.firstNameText,
                    onValueChange = viewModel::onFirstNameChange,
                    placeholder = { Text(text = "First name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = viewModel.lastNameText,
                    onValueChange = viewModel::onLastNameChange,
                    placeholder = {
                        Text(text = "Last name")
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = viewModel::onInsertPersonClick) {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = "Insert person",
                        tint = Color.Black
                    )
                }
            }

        }
        viewModel.personDetails?.let { detail ->
            Dialog(onDismissRequest = viewModel::onPersonDetailsDialogDismiss) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(text = "${detail.firstName}  ${detail.lastName}")
                }
            }
        }
    }
}

@Composable
fun PersonItem(
    person: PersonEntity,
    onItemClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.clickable { onItemClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = person.firstName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete person",
                tint = Color.Gray
            )
        }
    }
}