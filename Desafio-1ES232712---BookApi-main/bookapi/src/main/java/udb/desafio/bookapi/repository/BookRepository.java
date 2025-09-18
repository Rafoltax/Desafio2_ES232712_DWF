package udb.desafio.bookapi.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import udb.desafio.bookapi.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Buscar por título con paginación
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // 🔹 Nuevo método: buscar libros entre dos años
    Page<Book> findByPublicationYearBetween(int startYear, int endYear, Pageable pageable);
}