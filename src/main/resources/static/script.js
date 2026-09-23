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

let selectedScore = 0;
const ratingStars = document.querySelectorAll("#rating-stars button");
const selectedRating = document.getElementById("selected-rating");

const submitRating = document.getElementById("submit-rating");

ratingStars.forEach(star => {
    star.addEventListener("mouseenter", () => {
        const score = Number(star.dataset.score);
        ratingStars.forEach(s => {
            const starScore = Number(s.dataset.score);
            s.textContent =starScore <= score ? "★" : "☆";
        });
    });
});

ratingStars.forEach(star => {
    star.addEventListener("click", () => {
        selectedScore = Number(star.dataset.score);
        selectedRating.textContent =
            `Tu valoración: ${selectedScore}/5`;
    });
});

document.getElementById("rating-stars").addEventListener("mouseleave", () => {
    ratingStars.forEach(star => {
        const starScore = Number(star.dataset.score);
        star.textContent =
            starScore <= selectedScore ? "★" : "☆";
    });
});

submitRating.addEventListener("click", () => {
    if (selectedScore === 0) {
        selectedRating.textContent =
            "Selecciona una calificación antes de enviar.";
        return;
    }

    const isbn = document.getElementById("modal-isbn").textContent;
    fetch(`/api/books/${isbn}/ratings`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userId: "Anónimo",
            score: selectedScore
        })
    })

        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(error.error || "Error al enviar la valoración.");
                });
            }
            return response.json();
        })

        .then(data => {
            console.log("Valoración registrada:", data);
            document.getElementById("modal-rating").textContent =
                data.calificacionPromedio;
            document.getElementById("modal-rating-count").textContent =
                data.totalValoraciones;
            selectedRating.textContent =
                `¡Gracias por tu valoración de ${selectedScore}/5!`;
            submitRating.textContent = "Valoración enviada ✓";
            submitRating.disabled = true;
        })
        .catch(error => {
            console.error("Error al enviar valoración:", error);
            selectedRating.textContent =
                "No se pudo registrar la valoración.";
        });
})

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
                selectedScore = 0;
                selectedRating.textContent =
                    "Selecciona una calificación de 1 a 5.";
                submitRating.textContent = "Enviar valoración";
                submitRating.disabled = false;
                ratingStars.forEach(star => {
                    star.textContent = "☆";
                });
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