document.addEventListener('DOMContentLoaded', function () {
    // Check if a saved theme exists in localStorage
    const savedTheme = localStorage.getItem("saved-theme");
    if (savedTheme) {
        document.body.classList[savedTheme === "dark" ? "add" : "remove"]("dark-theme");
    }

    const navItems = document.querySelectorAll(".nav__item");
    const sidebarSections = document.querySelectorAll(".section");

    // Helper function to hide all section
    function hideAllSections() {
        sidebarSections.forEach(section => {
            section.classList.add("hidden");
        });
    }

    navItems.forEach((navItem) => {
        navItem.addEventListener("click", function () {
            navItems.forEach(item => item.classList.remove("active"));
            this.classList.add("active");

            // Hide all sections
            hideAllSections();
            let targetSectionId = this.id + "Section";

            // Display the corresponding section by removing the hidden class
            const targetSection = document.getElementById(targetSectionId);
            if (targetSection) {
                targetSection.classList.remove("hidden");
            }
        });
    });

    const sidebarContainer = document.getElementById('sidebarContainer');
    const toggleBtn = document.querySelector('.sidebar__operations .icon');
    const toggleIcon = document.querySelector('.sidebar__operations .icon i');

    if (toggleBtn) { // Make sure toggleBtn is not null
        toggleBtn.addEventListener('click', () => {
            sidebarContainer.classList.toggle('collapsed');
            // Optionally toggle an 'open' class for mobile
            sidebarContainer.classList.toggle('open');

            if (sidebarContainer.classList.contains('collapsed')) {
                // when collapsed, show the bar icon
                toggleIcon.classList.remove('fa-xmark');
                toggleIcon.classList.add('fa-bars');
            } else {
                // when expanded, show the xmark icon
                toggleIcon.classList.remove('fa-bars');
                toggleIcon.classList.add('fa-xmark');
            }
        });
    } else {
        console.error("toggleBtn is null. Check that the element with the selector '.sidebar__operations .icon' exists in your HTML.");
    }

    // remove active on message box
    const closeMsgBtn = document.querySelector(".close__msg__btn");
    const messageBox = document.querySelector(".message__wrapper");

    closeMsgBtn.addEventListener("click", () => {
        messageBox.classList.remove("active");
        setTimeout(() => {
            messageBox.style.display = "none";
        }, 300);
    });

    // Automatically remove "active" class after 10 seconds (10,000 milliseconds)
    if (messageBox) {
        setTimeout(() => {
            messageBox.classList.remove("active");
            messageBox.style.display = "none";
        }, 10000);
    }


    // hide the modal overlay
    const modal = document.querySelector(".modal__overlay");
    const closeModalBtn = document.querySelector(".close__modal__btn");
    closeModalBtn.addEventListener("click", () => {
        modal.classList.remove("active");
    });
});