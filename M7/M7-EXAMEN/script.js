document.addEventListener("DOMContentLoaded", () => {
    const pendents = document.getElementById("pendents");
    const fetes = document.getElementById("fetes");

    // Afegim un event a cada tasca
    pendents.addEventListener("click", (e) => {
        if (e.target && e.target.nodeName === "LI") {
            fetes.appendChild(e.target); // Mou la tasca a la secció FETES
        }
    });

    fetes.addEventListener("click", (e) => {
        if (e.target && e.target.nodeName === "LI") {
            pendents.appendChild(e.target); // Torna la tasca a la secció PENDENTS
        }
    });
});