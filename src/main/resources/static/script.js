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
                    $${Number(book.price).toFixed(2)}
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

let selectedReviewScore = 0;
const reviewStars = document.querySelectorAll("#review-stars button");

reviewStars.forEach(star => {
    star.addEventListener("mouseenter", () => {
        const score = Number(star.dataset.score);

        reviewStars.forEach(s => {
            const starScore = Number(s.dataset.score);
            s.textContent = starScore <= score ? "★" : "☆";
        });
    });

    star.addEventListener("click", () => {
        selectedReviewScore = Number(star.dataset.score);

        reviewStars.forEach(s => {
            const starScore = Number(s.dataset.score);
            s.textContent =
                starScore <= selectedReviewScore ? "★" : "☆";
        });
    });
});

document.getElementById("review-stars").addEventListener("mouseleave", () => {
    reviewStars.forEach(star => {
        const starScore = Number(star.dataset.score);

        star.textContent =
            starScore <= selectedReviewScore ? "★" : "☆";
    });
});

const previewReviewButton = document.getElementById("preview-review-button");
const reviewPreviewModal = document.getElementById("review-preview-modal");
const closeReviewPreview = document.getElementById("close-review-preview");
const backToReview = document.getElementById("back-to-review");

const previewReviewTitle = document.getElementById("preview-review-title");
const previewReviewerName = document.getElementById("preview-reviewer-name");
const previewReviewRating = document.getElementById("preview-review-rating");
const previewReviewComment = document.getElementById("preview-review-comment");

previewReviewButton.addEventListener("click", () => {

    const reviewerName = document.getElementById("reviewer-name").value.trim();
    const reviewTitle = document.getElementById("review-title").value.trim();
    const comment = document.getElementById("review-comment").value.trim();
    const isbn = document.getElementById("modal-isbn").textContent;

    if (!reviewerName || !reviewTitle || !comment || selectedReviewScore === 0) {
        alert("Completa todos los campos y selecciona una calificación.");
        return;
    }

    fetch(`/api/books/${isbn}/reviews/preview`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            reviewerName: reviewerName,
            reviewTitle: reviewTitle,
            comment: comment,
            score: selectedReviewScore
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(
                        error.error || "No se pudo generar la vista previa."
                    );
                });
            }

            return response.json();
        })
        .then(data => {
            console.log("Vista previa recibida:", data);

            previewReviewTitle.textContent = reviewTitle;
            previewReviewerName.textContent = reviewerName;
            previewReviewComment.textContent = comment;

            previewReviewRating.textContent =
                "★".repeat(selectedReviewScore) +
                "☆".repeat(5 - selectedReviewScore);

            reviewModal.style.display = "none";
            reviewPreviewModal.style.display = "flex";
        })
        .catch(error => {
            console.error("Error al generar la vista previa:", error);
            alert(error.message);
        });
});

backToReview.addEventListener("click", () => {
    reviewPreviewModal.style.display = "none";
    reviewModal.style.display = "flex";
});

closeReviewPreview.addEventListener("click", () => {
    reviewPreviewModal.style.display = "none";
});

reviewPreviewModal.addEventListener("click", (event) => {
    if (event.target.id === "review-preview-modal") {
        reviewPreviewModal.style.display = "none";
    }
});

const publishReviewButton = document.getElementById("publish-review");

publishReviewButton.addEventListener("click", () => {
    const reviewerName =
        document.getElementById("reviewer-name").value.trim();

    const reviewTitle =
        document.getElementById("review-title").value.trim();

    const comment =
        document.getElementById("review-comment").value.trim();

    const isbn =
        document.getElementById("modal-isbn").textContent;

    fetch(`/api/books/${isbn}/reviews`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            reviewerName: reviewerName,
            reviewTitle: reviewTitle,
            comment: comment,
            score: selectedReviewScore
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(
                        error.error || "No se pudo publicar la reseña."
                    );
                });
            }

            return response.json();
        })
        .then(data => {
            console.log("Reseña publicada:", data);

            alert("¡Reseña publicada con éxito!");

            reviewPreviewModal.style.display = "none";
            reviewModal.style.display = "none";
        })
        .catch(error => {
            console.error("Error al publicar la reseña:", error);
            alert(error.message);
        });
});

document.getElementById("close-modal").addEventListener("click", () => {
    document.getElementById("book-modal").style.display = "none";
});

document.getElementById("close-modal").addEventListener("click", (event) => {
    if (event.target.id === "book-modal"){
        document.getElementById("close-modal").style.display = "none";
    }
});

const writeReviewButton = document.getElementById("write-review-button");
const reviewModal = document.getElementById("review-modal");
const closeReviewModal = document.getElementById("close-review-modal");

writeReviewButton.addEventListener("click", () => {
    reviewModal.style.display = "flex";
});

closeReviewModal.addEventListener("click", () => {
    reviewModal.style.display = "none";
});

reviewModal.addEventListener("click", (event) => {
    if (event.target.id === "review-modal") {
        reviewModal.style.display = "none";
    }
});

const advancedSearchLink = document.getElementById("advance-search-link");
const advancedSearchModal = document.getElementById("advanced-search-modal");
const closedAdvancedSearch = document.getElementById("close-advanced-search");

advancedSearchLink.addEventListener("click", () => {
    advancedSearchModal.style.display = "flex";
});

closedAdvancedSearch.addEventListener("click", () => {
    advancedSearchModal.style.display = "none";
});

advancedSearchModal.addEventListener("click", (event) => {
    if (event.target.id === "advanced-search-modal") {
        advancedSearchModal.style.display = "none";
    }
});

const advancedSearchButton = document.getElementById("advanced-search-button");

const advancedAuthor = document.getElementById("advanced-author");
const advancedTitle = document.getElementById("advanced-title");
const advancedIsbn = document.getElementById("advanced-isbn");

advancedSearchButton.addEventListener("click", () => {
    const author = advancedAuthor.value;
    const title = advancedTitle.value;
    const isbn = advancedIsbn.value;
    fetch("/api/books/search/advanced", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            author: author,
            title: title,
            isbn: isbn
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al realizar la búsqueda avanzada.");
            }
            return response.json();
        })
        .then(data => {
            renderBooks(data);
            advancedSearchModal.style.display = "none";
        })
        .catch(error => {
            console.error("Error en búsqueda avanzada:", error);
        });
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