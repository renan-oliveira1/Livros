package br.edu.ifsp.scl.ads.pdm.livros.model

import android.os.Parcelable
//import kotlinx.android.parcel.Parcelize Antigo pacote do kotlin-extensions
import kotlinx.parcelize.Parcelize // Novo kotlin-parcelize

@Parcelize
data class Book(
    val title: String = "",
    val isbn: String = "",
    val firstAuthor: String = "",
    val publisher: String = "",
    val edition: Int? = 0,
    val pages: Int? = 0
) : Parcelable
