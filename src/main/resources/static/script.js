fetch("/api/books")
    .then(response => response.json())
    .then(books => {
        const container = document.getElementById("books-container");
        books.forEach(book => {
            const card = document.createElement("div");
            card.className = "book-card";
            card.innerHTML = `
                <h3>${book.title}</h3>   
                <p><strong>Autor:</strong> ${book.author}</p>
                <p><strong>Género:</strong> ${book.genre}</p>
                <p><strong>Precio:</strong> ${book.price}</p>
                `;
                container.appendChild(card);
        });
    });

const searchInput = document.getElementById("search-input");
const searchButton = document.getElementById("search-button");

searchButton.addEventListener("click", () => {
    const query = searchInput.value;
    fetch(`/api/books/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(books => {
            console.log("Resultados:", books);
        });
});