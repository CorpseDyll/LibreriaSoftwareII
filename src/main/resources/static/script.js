function renderBooks(books){
    const container = document.getElementById("books-container");
    container.innerHTML = "";
    if (books.length === 0){
        container.innerHTML = `
            <p class="no-results">
                No se encontraron libros.
            </p>
        `;
        return;
    }
    books.forEach(book => {
       const card = document.createElement("div");
       card.className = "book-card";
       card.innerHTML = `
            <div class="book-info">
                <h3>${book.title}</h3>
                <p class="book-author">
                    ${book.author}
                </p>
                <p class="book-genre">
                    ${book.genre}
                </p>
                <p class="book-price">
                    ${book.price}
                </p>
            </div>
            <button class="details-button">
                Ver detalles
            </button>
        `;
       container.appendChild(card);
    });
}

fetch("/api/books")
    .then(response => response.json())
    .then(books => {
        renderBooks(books);
    });

const searchInput = document.getElementById("search-input");
const searchButton = document.getElementById("search-button");

searchButton.addEventListener("click", () => {
    const query = searchInput.value;
    fetch(`/api/books/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(books => {
            renderBooks(books);
        });
});