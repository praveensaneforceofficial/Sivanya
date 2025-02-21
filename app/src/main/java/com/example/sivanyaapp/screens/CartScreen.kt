package com.example.sivanyaapp.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.sivanyaapp.api.ApiInterface
import com.example.sivanyaapp.api.RetrofitClient
import com.example.sivanyaapp.api.CloudinaryResponse
import com.example.sivanyaapp.api.ProductRequest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CartScreen(navController: NavHostController) {
    val context = LocalContext.current
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    MainScreen(navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Upload Product", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Image Preview
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Selected Image",
                        modifier = Modifier.size(150.dp)
                    )
                } ?: Text(text = "Tap to Select Image", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product Details
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = productPrice,
                onValueChange = { productPrice = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = productQuantity,
                onValueChange = { productQuantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    imageUri?.let { uri ->
                        uploadProduct(uri, productName, productDescription, productPrice, productQuantity, context)
                    } ?: Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Upload Product")
            }
        }
    }
}

// Upload function
fun uploadProduct(imageUri: Uri, name: String, description: String, price: String, quantity: String, context: android.content.Context) {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(imageUri)

    // Generate a unique filename using timestamp
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "IMG_$timeStamp.jpg"

    val file = File(context.cacheDir, fileName)

    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    val imagePart = MultipartBody.Part.createFormData("file", file.name, requestBody)

    val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
    val uploadPreset = RequestBody.create("text/plain".toMediaTypeOrNull(), "sivanya")

    // Upload Image to Cloudinary
    apiService.uploadImageToCloudinary(imagePart,uploadPreset).enqueue(object : Callback<CloudinaryResponse> {
        override fun onResponse(call: Call<CloudinaryResponse>, response: Response<CloudinaryResponse>) {
            if (response.isSuccessful && response.body() != null) {
                val cloudinaryUrl = response.body()?.secure_url ?: ""

                if (cloudinaryUrl.isNotEmpty()) {
                    // Now send product details to the backend
                    val productData = ProductRequest(
                        imageUrl = cloudinaryUrl,
                        name = name,
                        description = description,
                        price = price,
                        quantity = quantity
                    )

                    apiService.uploadProductDetails(productData).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Product uploaded successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to upload product details", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(context, "Cloudinary Upload Failed: No URL returned", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Cloudinary Upload Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<CloudinaryResponse>, t: Throwable) {
            Toast.makeText(context, "Cloudinary Upload Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

