package frena.id.manager.ui.drawer

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.*
import frena.id.manager.ui.navigation.Screen

@Composable
fun DrawerContent(
    navController: NavController,
    onCloseDrawer: () -> Unit = {}
) {
    val context = LocalContext.current

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                text = "FR.ina module",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(12.dp)
            )
//            DrawerItem(
//                icon = LineAwesomeIcons.InfoCircleSolid,
//                label = "About",
//                onClick = {
//                    navController.navigate(Screen.About.route)
//                    onCloseDrawer()
//               }
//            )
//            DrawerItem(
//                icon = LineAwesomeIcons.Telegram,
//                label = "Telegram",
//                onClick = { Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show() }
//            )
//            DrawerItem(
//                icon = LineAwesomeIcons.Discord,
//                label = "Discord",
//                onClick = { Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show() }
//            )
//            DrawerItem(
//                icon = LineAwesomeIcons.Github,
//                label = "Github",
//                onClick = {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fredslbn/frena"))
//                    context.startActivity(intent)
//                    onCloseDrawer()
//                }
//            )
            DrawerItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = {
                    navController.navigate(Screen.Settings.route)
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = LineAwesomeIcons.InfoCircleSolid,
                label = "Gojek",
                onClick = {
                    navController.navigate(Screen.Gojek.route)
                    onCloseDrawer()
                }
            )

        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
