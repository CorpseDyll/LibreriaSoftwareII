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
            <button class="details-button" data-isbn="${book.isbn}">
                Ver detalles
            </button>
        `;
       container.appendChild(card);
    });
}

document.addEventListener("click", (event) => {
    if (event.target.classList.contains("details-button")) {
        const isbn = event.target.dataset.isbn;
        console.log("ISBN seleccionado:", isbn);
        fetch(`/api/books/${isbn}`)
            .then(response => response.json())
            .then(book => {
                document.getElementById("modal-title").textContent = book.name;
                document.getElementById("modal-author").textContent = book.author;
                document.getElementById("modal-isbn").textContent = book.isbn;
                document.getElementById("modal-genre").textContent = book.genre;
                document.getElementById("modal-type").textContent = book.productType;
                document.getElementById("modal-price").textContent = book.price;
                document.getElementById("modal-rating").textContent = book.averageRating;
                document.getElementById("modal-rating-count").textContent = book.totalRatingsCount;
                document.getElementById("modal-description").textContent = book.description;
                document.getElementById("book-modal").style.display = "flex";
            });
    }
});

document.getElementById("close-modal").addEventListener("click", () => {
    document.getElementById("book-modal").style.display = "none";
});

document.getElementById("close-modal").addEventListener("click", (event) => {
    if (event.target.id === "book-modal"){
        document.getElementById("close-modal").style.display = "none";
    }
});

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