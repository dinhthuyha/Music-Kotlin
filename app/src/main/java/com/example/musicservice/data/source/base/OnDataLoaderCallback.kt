package com.example.musicservice.data.source.base



interface OnDataLoaderCallback <T>{
    fun onSuccess(data: T)
    fun onFailure( exception: Exception= Exception())
}