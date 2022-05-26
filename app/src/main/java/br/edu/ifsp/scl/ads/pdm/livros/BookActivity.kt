package br.edu.ifsp.scl.ads.pdm.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.ads.pdm.livros.MainActivity.Extras.EXTRA_BOOK
import br.edu.ifsp.scl.ads.pdm.livros.MainActivity.Extras.EXTRA_POSITION

import br.edu.ifsp.scl.ads.pdm.livros.databinding.ActivityBookBinding
import br.edu.ifsp.scl.ads.pdm.livros.model.Book

class BookActivity : AppCompatActivity() {

    private val activityBookBinding
    : ActivityBookBinding by lazy{
        ActivityBookBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityBookBinding
            .root)

        // Verificando se recebi um livro e a posicao
        val book: Book? = intent.getParcelableExtra(EXTRA_BOOK)
        val position: Int? = intent.getIntExtra(EXTRA_POSITION, -1)
        if(book != null){

            with(activityBookBinding
            ){
                editTitle.isEnabled = false
                editTitle.setText(book.title)
                editIsbn.setText(book.isbn)
                editFirstAuthor.setText(book.firstAuthor)
                editPublisher.setText(book.publisher)
                editEdition.setText(book.edition.toString())
                editPages.setText(book.pages.toString())
            }
            if(position == -1){

                with(activityBookBinding
                ){
                    editIsbn.isEnabled = false
                    editFirstAuthor.isEnabled = false
                    editPublisher.isEnabled = false
                    editEdition.isEnabled = false
                    editPages.isEnabled = false
                    buttonSave.setText("Voltar")
                }
            }
        }

        with(activityBookBinding
        ){
            buttonSave.setOnClickListener {
                val book: Book = Book(
                    editTitle.text.toString(),
                    editIsbn.text.toString(),
                    editFirstAuthor.text.toString(),
                    editPublisher.text.toString(),
                    editEdition.text.toString().toInt(),
                    editPages.text.toString().toInt()
              )

                val resultIntent: Intent = Intent()
                resultIntent.putExtra(EXTRA_BOOK, book)
                resultIntent.putExtra(EXTRA_POSITION, position)

                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

    }
}