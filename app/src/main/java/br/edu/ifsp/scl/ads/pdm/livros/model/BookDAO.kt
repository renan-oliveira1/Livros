package br.edu.ifsp.scl.ads.pdm.livros.model

interface BookDAO {

    fun createBook(book: Book): Long
    fun findBook(title: String): Book
    fun findAllBooks(): MutableList<Book>
    fun updateBook(book: Book): Int
    fun removeBook(title: String): Int

}