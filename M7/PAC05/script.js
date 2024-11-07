document.addEventListener("DOMContentLoaded", () => {
    fetch('https://raw.githubusercontent.com/OscarQuiRid/2DAM/main/M7/PAC05XMLlink/data.xml')
    
      .then(response => response.text())
      .then(str => (new window.DOMParser()).parseFromString(str, "application/xml"))
      .then(data => {
        const menu = document.getElementById("menu");
  
        // Itera pels elements GRUP dins de CARTA
        const grups = data.querySelectorAll("CARTA > GRUP");
        grups.forEach(grup => {
          // Crea un bloc GRUP
          const grupDiv = document.createElement("div");
          grupDiv.classList.add("GRUP");
  
          // Afegim el nom del grup (Entrants, Principals, etc.)
          const nomGrup = document.createElement("div");
          nomGrup.classList.add("NOM");
          nomGrup.textContent = grup.querySelector("NOM").textContent;
          grupDiv.appendChild(nomGrup);
  
          // Itera pels elements PLAT dins del GRUP
          const plats = grup.querySelectorAll("PLAT");
          plats.forEach(plat => {
            const platDiv = document.createElement("div");
            platDiv.classList.add("PLAT");
  
            // Afegim el nom del plat
            const nomPlat = document.createElement("div");
            nomPlat.classList.add("NOM");
            nomPlat.textContent = plat.querySelector("NOM").textContent;
            platDiv.appendChild(nomPlat);
  
            // Afegim la descripció del plat
            const descripcio = document.createElement("div");
            descripcio.classList.add("DESCRIPCIO");
            descripcio.textContent = plat.querySelector("DESCRIPCIO").textContent;
            platDiv.appendChild(descripcio);
  
            // Afegim el preu del plat
            const preu = document.createElement("div");
            preu.classList.add("PREU");
            preu.textContent = `${plat.querySelector("PREU").textContent} €`;
            platDiv.appendChild(preu);
  
            // Afegim el plat al grup
            grupDiv.appendChild(platDiv);
          });
  
          // Afegim el grup al menú
          menu.appendChild(grupDiv);
        });
      })
      .catch(error => console.error("Error carregant el menú:", error));
  });
  