package com.mtg.randomcard.data

data class CardDto(
    val name: String,
    val oracle_text: String? = null,
    val type_line: String? = null,
    val power: String? = null,
    val toughness: String? = null,
    val image_uris: ImageUris? = null,
    val card_faces: List<CardFaceDto>? = null,
)

data class CardFaceDto(
    val name: String,
    val oracle_text: String? = null,
    val type_line: String? = null,
    val power: String? = null,
    val toughness: String? = null,
    val image_uris: ImageUris? = null,
)

data class ImageUris(val normal: String)