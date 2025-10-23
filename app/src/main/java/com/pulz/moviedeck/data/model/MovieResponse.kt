package com.pulz.moviedeck.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search") val search: List<MovieItem>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String?
)

data class MovieItem(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Type") val type: String?,
    @SerializedName("Poster") val poster: String?
)