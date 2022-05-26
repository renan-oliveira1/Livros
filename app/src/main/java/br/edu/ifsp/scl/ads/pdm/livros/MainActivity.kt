package br.edu.ifsp.scl.ads.pdm.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import br.edu.ifsp.scl.ads.pdm.livros.adapter.BooksAdapter
import br.edu.ifsp.scl.ads.pdm.livros.controller.BookController
import br.edu.ifsp.scl.ads.pdm.livros.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.pdm.livros.model.Book

class MainActivity : AppCompatActivity() {

    companion object Extras {
        const val EXTRA_BOOK = "EXTRA_BOOK"
        const val EXTRA_POSITION = "EXTRA_POSITION"
    }

    private val activityMainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val booksList:MutableList<Book> = mutableListOf()

    private lateinit var booksAdapter: BooksAdapter

    private lateinit var bookActivityResultLaucher: ActivityResultLauncher<Intent>
    private lateinit var editBookActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var bookController: BookController



    //preencher lista de books
    private fun inicializarLivrosList(){
        for(indice in 1..10){
            booksList.add(
                Book(
                    "Titulo $indice",
                    "ISBN $indice",
                    "Autor $indice",
                    "Editora $indice",
                    indice,
                    indice
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        bookController = BookController(this)

        registerForContextMenu(activityMainBinding.listBook)

        bookController.findAllBooks().forEach{ book ->
            booksList.add(book)
        }

        booksAdapter = BooksAdapter(
            this,
            R.layout.layout_book,
            booksList
        )

        activityMainBinding.listBook.adapter = booksAdapter

        bookActivityResultLaucher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result ->
            if (result.resultCode == RESULT_OK) {
                val book = result.data?.getParcelableExtra<Book>(EXTRA_BOOK)
                if(book != null){
                    booksList.add(book)
                    booksAdapter.notifyDataSetChanged()
                    bookController.insertBook(book)
                }
            }}

        editBookActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == RESULT_OK) {
                val position = result.data?.getIntExtra(EXTRA_POSITION, -1)
                result.data?.getParcelableExtra<Book>(EXTRA_BOOK)?.apply {
                    if (position != null && position != -1) {
                        booksList[position] = this
                        booksAdapter.notifyDataSetChanged()
                        bookController.updateBook(this)
                    }
                }

            }
        }

        activityMainBinding.listBook.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, posicao: Int, p3: Long) {
                val book = booksList[posicao]
                val consultBookIntent = Intent(this@MainActivity, BookActivity::class.java)
                consultBookIntent.putExtra(EXTRA_BOOK, book)
                startActivity(consultBookIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
            R.id.menu_add_book ->{
                bookActivityResultLaucher.launch(Intent(this, BookActivity::class.java))
                true
            }else ->{
                false
            }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val menuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = menuInfo.position
        val book = booksList[position]

        return when(item.itemId){
            R.id.item_edit_book -> {

                val editBookIntent = Intent(this, BookActivity::class.java)
                editBookIntent.putExtra(EXTRA_BOOK, book)
                editBookIntent.putExtra(EXTRA_POSITION, position)
                editBookActivityResultLauncher.launch(editBookIntent)

                Toast.makeText(this, "Clicou em editar", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item_remove_book-> {

                with(AlertDialog.Builder(this)){
                    setTitle("Remoção book")
                    setMessage("Deseja realmente remover?")
                    setPositiveButton("Sim"){_,_ ->
                        booksList.removeAt(position)
                        booksAdapter.notifyDataSetChanged()
                        bookController.removeBook(book.title)
                        Toast.makeText(this@MainActivity, "Livro removido", Toast.LENGTH_LONG).show()
                    }
                    setNegativeButton("Não"){ _, _ ->
                        Toast.makeText(this@MainActivity, "Remoção cancelada", Toast.LENGTH_LONG).show()
                    }
                    create()
                }.show()
                true
            }
            else -> {false}
        }
    }
}