package br.edu.ifsp.scl.ads.pdm.livros.controller

import br.edu.ifsp.scl.ads.pdm.livros.MainActivity
import br.edu.ifsp.scl.ads.pdm.livros.model.Book
import br.edu.ifsp.scl.ads.pdm.livros.model.BookDAO
import br.edu.ifsp.scl.ads.pdm.livros.model.BookSqlite

class BookController(mainActivity: MainActivity) {
    private val bookDao: BookDAO = BookSqlite(mainActivity)

    fun insertBook(book: Book): Long = bookDao.createBook(book)

    fun findBook(title: String): Book = bookDao.findBook(title)

    fun findAllBooks(): MutableList<Book> = bookDao.findAllBooks()

    fun updateBook(book: Book): Int = bookDao.updateBook(book)

    fun removeBook(title: String): Int = bookDao.removeBook(title)

}