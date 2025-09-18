package udb.desafio.bookapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import udb.desafio.bookapi.DTO.BookDTO;
import udb.desafio.bookapi.model.Book;
import udb.desafio.bookapi.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // CRUD
    public BookDTO create(BookDTO dto) {
        Book book = bookRepository.save(toEntity(dto));
        return toDto(book);
    }

    public Page<BookDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::toDto);
    }

    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        return toDto(book);
    }

    public Page<BookDTO> searchByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable).map(this::toDto);
    }

    public BookDTO update(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublicationYear(dto.getPublicationYear());

        Book updated = bookRepository.save(book);
        return toDto(updated);
    }

    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    // 🔹 Nuevo método: filtro por rango de años
    public Page<BookDTO> filterByYearRange(int start, int end, Pageable pageable) {
        return bookRepository.findByPublicationYearBetween(start, end, pageable).map(this::toDto);
    }

    // Helpers de mapeo
    private Book toEntity(BookDTO dto) {
        Book b = new Book();
        b.setId(dto.getId());
        b.setTitle(dto.getTitle());
        b.setAuthor(dto.getAuthor());
        b.setPublicationYear(dto.getPublicationYear());
        return b;
    }

    private BookDTO toDto(Book b) {
        BookDTO dto = new BookDTO();
        dto.setId(b.getId());
        dto.setTitle(b.getTitle());
        dto.setAuthor(b.getAuthor());
        dto.setPublicationYear(b.getPublicationYear());
        return dto;
    }
}