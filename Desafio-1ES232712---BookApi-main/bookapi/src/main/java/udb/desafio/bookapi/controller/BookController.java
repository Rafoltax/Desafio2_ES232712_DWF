package udb.desafio.bookapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import udb.desafio.bookapi.DTO.BookDTO;
import udb.desafio.bookapi.service.BookService;

import java.net.URI;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Create a book")
    @ApiResponse(responseCode = "201", description = "Book created")
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO dto) {
        BookDTO created = bookService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get all books (paged)")
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @Operation(summary = "Get book by id")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @Operation(summary = "Search books by title (paged)")
    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchBooksByTitle(
            @RequestParam(required = false, defaultValue = "") String title,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.searchByTitle(title, pageable));
    }

    @Operation(summary = "Update a book")
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @Operation(summary = "Delete a book")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Nuevo endpoint: filtro por año
    @Operation(summary = "Filter books by publication year range")
    @GetMapping("/filter/year")
    public ResponseEntity<Page<BookDTO>> filterByYear(
            @RequestParam int start,
            @RequestParam int end,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.filterByYearRange(start, end, pageable));
    }
}
