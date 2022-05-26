package br.edu.ifsp.scl.ads.pdm.livros.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.scl.ads.pdm.livros.R
import java.sql.SQLException

class BookSqlite(contexto: Context): BookDAO {

    companion object {
        private val BD_BOOKS = "livros"
        private val TABLE_BOOK = "livro"
        private val COLUMN_TITLE = "titulo"
        private val COLUMN_ISBN = "isbn"
        private val COLUMN_FIRST_AUTHOR = "primeiro_autor"
        private val COLUMN_PUBLISHER = "editora"
        private val COLUMN_EDITION = "edicao"
        private val COLUMN_PAGES = "paginas"

        /* Statement que será usado na primeira vez para criar a tabela. Em uma única li
        nha executada uma única vez a concatenação de String não fará diferença no desempe
        nho, além de ser mais didático */
        val CRIAR_TABLE_BOOK_STMT = "CREATE TABLE IF NOT EXISTS ${TABLE_BOOK} (" +
                "${COLUMN_TITLE} TEXT NOT NULL PRIMARY KEY, " +
                "${COLUMN_ISBN} TEXT NOT NULL, " +
                "${COLUMN_FIRST_AUTHOR} TEXT NOT NULL, " +
                "${COLUMN_PUBLISHER} TEXT NOT NULL, " +
                "${COLUMN_EDITION} INTEGER NOT NULL, " +
                "${COLUMN_PAGES} INTEGER NOT NULL );"
    }

    // Referencia para o banco de dados
    private val booksBd: SQLiteDatabase
    init {
        // Criando ou abrindo o BD e conectando com o BD
        booksBd = contexto.openOrCreateDatabase(BD_BOOKS, MODE_PRIVATE, null)
        // Criando a tabela
        try{
            booksBd.execSQL(CRIAR_TABLE_BOOK_STMT)
        } catch(se: SQLException){
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    override fun createBook(book: Book): Long {

        val bookCv = ContentValues()

        bookCv.put(COLUMN_TITLE, book.title)
        bookCv.put(COLUMN_ISBN, book.isbn)
        bookCv.put(COLUMN_FIRST_AUTHOR, book.firstAuthor)
        bookCv.put(COLUMN_PUBLISHER, book.publisher)
        bookCv.put(COLUMN_EDITION, book.edition)
        bookCv.put(COLUMN_PAGES, book.pages)

        return booksBd.insert(TABLE_BOOK, null, bookCv)
    }

    override fun findBook(title: String): Book {
        val bookCursor = booksBd.query(
            true,
            TABLE_BOOK,
            null, //todas colunas
            "${COLUMN_TITLE} = ?",
            arrayOf(title),
            null, //group by
            null, //having
            null, //orderBy
            null, //limit
        )

        return if(bookCursor.moveToFirst()){
            with(bookCursor){
                Book(
                    getString(getColumnIndexOrThrow(COLUMN_TITLE)),
                    getString(getColumnIndexOrThrow(COLUMN_ISBN)),
                    getString(getColumnIndexOrThrow(COLUMN_FIRST_AUTHOR)),
                    getString(getColumnIndexOrThrow(COLUMN_PUBLISHER)),
                    getInt(getColumnIndexOrThrow(COLUMN_EDITION)),
                    getInt(getColumnIndexOrThrow(COLUMN_PAGES))

                )
            }
        }
        else{
            Book()
        }
    }

    override fun findAllBooks(): MutableList<Book> {
        val booksList = mutableListOf<Book>()

        val consultaQuery = "SELECT * FROM ${TABLE_BOOK};"
        val booksCursor = booksBd.rawQuery(consultaQuery, null)

        while(booksCursor.moveToNext()){
            with(booksCursor) {
                booksList.add(
                    Book(
                        getString(getColumnIndexOrThrow(COLUMN_TITLE)),
                        getString(getColumnIndexOrThrow(COLUMN_ISBN)),
                        getString(getColumnIndexOrThrow(COLUMN_FIRST_AUTHOR)),
                        getString(getColumnIndexOrThrow(COLUMN_PUBLISHER)),
                        getInt(getColumnIndexOrThrow(COLUMN_EDITION)),
                        getInt(getColumnIndexOrThrow(COLUMN_PAGES))

                    )
                )
            }
        }

        return booksList
    }

    override fun updateBook(book: Book): Int {
        val bookCv = ContentValues()

        bookCv.put(COLUMN_TITLE, book.title)
        bookCv.put(COLUMN_ISBN, book.isbn)
        bookCv.put(COLUMN_FIRST_AUTHOR, book.firstAuthor)
        bookCv.put(COLUMN_PUBLISHER, book.publisher)
        bookCv.put(COLUMN_EDITION, book.edition)
        bookCv.put(COLUMN_PAGES, book.pages)

        return booksBd.update(
            TABLE_BOOK, bookCv,
            "${COLUMN_TITLE} = ?", arrayOf(book.title)
        )
    }

    override fun removeBook(title: String): Int =
        booksBd.delete(TABLE_BOOK, "${COLUMN_TITLE} = ?", arrayOf(title))


}