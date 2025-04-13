document.addEventListener("DOMContentLoaded", function () {
    const otpInputs = document.querySelectorAll(".otp-input");

    otpInputs.forEach((input, index) => {
        input.addEventListener("input", function () {
            if (this.value.length === this.maxLength) {
                const nextInput = otpInputs[index + 1];
                if (nextInput) {
                    nextInput.focus();
                }
            }
        });

        // Optionally, allow backspace to focus the previous input if empty
        input.addEventListener("keydown", function (e) {
            if (e.key === "Backspace" && this.value.length === 0 && index > 0) {
                otpInputs[index - 1].focus();
            }
        });
    });


    // logo home link
    document.querySelector(".logo").addEventListener("click", () => {
        window.location.href = "./index.jsp";
    });
    // remove active on message box
    const closeBtn = document.querySelector(".close-btn");
    const messageBox = document.querySelector(".message-wrapper");

    closeBtn.addEventListener("click", () => {
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

    // show the verification overlay
    const adminLink = document.querySelector(".admin-link");
    const verificationOverlay = document.querySelector(".verification-overlay");
    const emailTitle = document.getElementById("email-title");
    const emailBox = document.querySelector(".recipient-email");
    const codeBtn = document.querySelector(".get-code-btn");
    const passkeyBox = document.querySelector(".modal-fields");

    adminLink.addEventListener("click", (e) => {
        e.preventDefault();
        verificationOverlay.classList.add("active");
    });

    //
    codeBtn.addEventListener("click", () => {
        setTime(() => {
            emailTitle.style.display = "none";
            emailBox.style.display = "none";
            passkeyBox.classList.remove("hidden");
        }, 1000);
    });
    // hide the verification overlay
    const closeModal = document.querySelector(".close-modal");
    closeModal.addEventListener("click", () => {
        verificationOverlay.classList.remove("active");
        emailTitle.style.display = "block";
        emailBox.style.display = "block";
        passkeyBox.classList.add("hidden");
    });
});

