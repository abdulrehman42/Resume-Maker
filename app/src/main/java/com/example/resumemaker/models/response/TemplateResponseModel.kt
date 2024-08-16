package com.example.resumemaker.models.response

data class TemplateResponseModel(
    val `data`: List<Data>,
    val meta: Meta,
    val msg: String
)
{
    data class Data(
        val baseUrl: String,
        val createdAt: String,
        val id: Int,
        val path: String,
        val themeId: Int,
        val type: String,
        val updatedAt: String
    )
    data class Meta(
        val pagination: Pagination
    )
    data class Pagination(
        val page: Int,
        val size: Int
    )
}