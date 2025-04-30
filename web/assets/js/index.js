document.addEventListener("DOMContentLoaded", function () {
    // Navigation bar effects on scroll
    window.addEventListener("scroll", () => {
        const header = document.querySelector("header");
        header.classList.toggle("sticky", window.scrollY > 0);
    });

    
    // Toggle forms
    const searchBtns = document.querySelectorAll(".search-btn");
    const internshipForm = document.getElementById("internship-form");
    const jobForm = document.getElementById("job-form");

    searchBtns.forEach((btn) => {
        btn.addEventListener("click", function () {
            // Remove 'active' from all buttons
            searchBtns.forEach((btn) => btn.classList.remove("active"));
            // Add 'active' on the clicked button
            this.classList.add("active");

            // toggle forms based on the button text
            if (this.textContent.trim() === "Internships") {
                internshipForm.classList.remove("hidden");
                jobForm.classList.add("hidden");
            } else if (this.textContent.trim() === "Jobs") {
                jobForm.classList.remove("hidden");
                internshipForm.classList.add("hidden");
            }
            btn.classList.add("active");
        });
    });

    // Dark | light theme
    const themeBtn = document.querySelector(".themeBtn");
    if (themeBtn) {

        themeBtn.addEventListener("click", () => {
            themeBtn.classList.toggle("sun");
            localStorage.setItem("saved-icon", getCurrentIcon());
        });

        const getCurrentIcon = () => themeBtn.classList.contains("sun") ? "sun" : "moon";
        const savedIcon = localStorage.getItem("saved-icon");

        if (savedIcon) {
            themeBtn.classList[savedIcon === "sun" ? "add" : "remove"]("sun");
        }
    }



    const dropdownBtn = document.querySelector(".dropdown-btn");
    const dropdown = document.querySelector(".dropdown");

    if (dropdownBtn) {
        // Show dropdown when button is clicked
        dropdownBtn.addEventListener("click", (event) => {
            dropdown.classList.remove("hidden");
            // Prevent the click event from bubbling up to the document
            event.stopPropagation();
        });

        // Hide dropdown when clicking outside
        document.addEventListener("click", (event) => {
            if (!dropdown.contains(event.target) && !dropdownBtn.contains(event.target)) {
                dropdown.classList.add("hidden");
            }
        });
    }


});