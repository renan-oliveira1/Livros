package br.edu.ifsp.scl.ads.pdm.livros.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.ads.pdm.livros.databinding.LayoutBookBinding
import br.edu.ifsp.scl.ads.pdm.livros.model.Book

class BooksAdapter(
    val contexto: Context,
    val layout: Int,
    val booksList: MutableList<Book>
): ArrayAdapter<Book>(contexto, layout, booksList) {

    private data class BookLayoutHolder(
        val titleTv: TextView,
        val firstAuthorTv: TextView,
        val publisherTv: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val bookLayoutView: View
        if (convertView != null) {
            // celula que ja existe e precia ter o valor trocado(reciclado)
            bookLayoutView = convertView
        } else {
            // celula precisa ser inflada
            val layoutBookBinding: LayoutBookBinding = LayoutBookBinding.inflate(
                contexto.getSystemService(
                    LAYOUT_INFLATER_SERVICE
                ) as LayoutInflater, parent, false
            )

            bookLayoutView = layoutBookBinding.root

            with(layoutBookBinding) {
                val holder = BookLayoutHolder(textTitle, textFirstAuthor, textPublisher)
                bookLayoutView.tag = holder
            }

        }

        // Preenchendo e atualizando a view
        val book = booksList[position]
        with(bookLayoutView.tag as BookLayoutHolder) {
            titleTv.text = book.title
            firstAuthorTv.text = book.firstAuthor
            publisherTv.text = book.publisher
        }

        return bookLayoutView
    }

}