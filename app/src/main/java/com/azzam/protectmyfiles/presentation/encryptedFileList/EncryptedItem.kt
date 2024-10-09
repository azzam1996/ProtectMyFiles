package com.azzam.protectmyfiles.presentation.encryptedFileList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.presentation.ui.theme.Biscay
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleLarge
import com.azzam.protectmyfiles.util.extensions.getValueOrNoData

@Composable
fun EncryptedItem(
    encryptedModel: EncryptedModel?,
    modifier: Modifier = Modifier,
    action: (EncryptedModel?) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                action.invoke(encryptedModel)
            }
    ) {
        val (image, details) = createRefs()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = Biscay,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(15.dp)
                .constrainAs(details) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, 37.5.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        ) {
            Text(
                text = getValueOrNoData(value = encryptedModel?.name),
                color = White,
                style = titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = encryptedModel?.fileType?.type ?: R.string.document),
                color = White,
                style = titleLarge,
                modifier = Modifier
                    .padding(start = 40.dp)
            )
        }

        Image(
            painter = painterResource(id = encryptedModel?.fileType?.image ?: R.drawable.ic_type_document),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(75.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )
    }
}