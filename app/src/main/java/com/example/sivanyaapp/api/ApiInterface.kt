package com.example.sivanyaapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Multipart
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Define the API Interface for registration
interface ApiInterface {
    @POST("register")
    fun registerUser(@Body user: User): Call<ResponseBody>

    @POST("login")  // Adjust the endpoint as per your backend
    fun loginUser(@Body credentials: LoginCredentials): Call<ResponseBody>

    @POST("getUser") // Ensure this matches your backend API
    fun getUserProfile(@Body request: UserEmailRequest): Call<UserProfileResponse>

    @POST("updateUser")  // Adjust endpoint based on your backend API
    fun updateUserProfile(@Body request: UserProfileUpdateRequest): Call<ResponseBody>

    @POST("getProducts") // Adjust this endpoint as per your backend
    fun getProducts(): Call<List<Product>>

    @Multipart
    @POST("https://api.cloudinary.com/v1_1/dfqnfl0pi/image/upload")
    fun uploadImageToCloudinary(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): Call<CloudinaryResponse>

    @POST("addProduct") // Backend API to store product data
    fun uploadProductDetails(@Body product: ProductRequest): Call<ResponseBody>

    @POST("getFavorites")
    fun getFavoriteProducts(@Body request: UserEmailRequest): Call<List<Product>>

    @POST("getProductById")
    fun getProductById(@Body request: ProductIdRequest): Call<Product>

    @POST("toggleFavorite") // Adjust endpoint according to your backend
    fun toggleFavorite(@Body request: FavoriteRequest): Call<ResponseBody>

    @POST("checkFavorite")
    fun checkFavoriteStatus(@Body request: FavoriteRequest): Call<ResponseBody>

}



