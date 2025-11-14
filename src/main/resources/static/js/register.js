// Script pour la page d'inscription
document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    // Toggle password visibility
    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    }

    // Validation en temps réel du mot de passe
    const confirmPasswordInput = document.getElementById('confirmPassword');
    if (confirmPasswordInput && passwordInput) {
        confirmPasswordInput.addEventListener('input', function() {
            if (this.value !== passwordInput.value) {
                this.setCustomValidity('Les mots de passe ne correspondent pas');
                this.classList.add('is-invalid');
            } else {
                this.setCustomValidity('');
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            }
        });
    }

    // Gestion du formulaire d'inscription
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const formData = new FormData(this);
            const registerData = {
                nom: formData.get('nom'),
                prenom: formData.get('prenom'),
                username: formData.get('username'),
                email: formData.get('email'),
                password: formData.get('password')
            };
            
            // Validation côté client
            if (!validateForm(registerData)) {
                return;
            }
            
            // Vérifier que les mots de passe correspondent
            if (registerData.password !== formData.get('confirmPassword')) {
                showAlert('Les mots de passe ne correspondent pas', 'danger');
                return;
            }
            
            // Vérifier les conditions d'utilisation
            if (!document.getElementById('acceptTerms').checked) {
                showAlert('Vous devez accepter les conditions d\'utilisation', 'danger');
                return;
            }
            
            // Afficher le loader
            const submitBtn = this.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerHTML;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Inscription...';
            submitBtn.disabled = true;
            
            // Debug: afficher les données avant l'envoi
            console.log('Données d\'inscription:', registerData);
            
            // Appel API
            fetch('/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(registerData)
            })
            .then(response => {
                console.log('Statut de la réponse:', response.status);
                if (!response.ok) {
                    return response.text().then(text => {
                        console.error('Erreur du serveur:', text);
                        throw new Error(`Erreur ${response.status}: ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                console.log('Réponse du serveur:', data);
                if (data.token) {
                    showAlert('Inscription réussie! Redirection vers la connexion...', 'success');
                    
                    // Rediriger vers la page de connexion
                    setTimeout(() => {
                        window.location.href = '/login?registered=true';
                    }, 2000);
                } else {
                    throw new Error(data.message || 'Erreur lors de l\'inscription');
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                showAlert(error.message || 'Erreur lors de l\'inscription. Veuillez réessayer.', 'danger');
            })
            .finally(() => {
                // Restaurer le bouton
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            });
        });
    }
});

// Validation du formulaire
function validateForm(data) {
    let isValid = true;
    
    // Valider le nom
    if (!data.nom || data.nom.trim().length < 2) {
        setFieldError('nom', 'Le nom doit contenir au moins 2 caractères');
        isValid = false;
    } else {
        clearFieldError('nom');
    }
    
    // Valider le prénom
    if (!data.prenom || data.prenom.trim().length < 2) {
        setFieldError('prenom', 'Le prénom doit contenir au moins 2 caractères');
        isValid = false;
    } else {
        clearFieldError('prenom');
    }
    
    // Valider le nom d'utilisateur
    if (!data.username || data.username.trim().length < 3) {
        setFieldError('username', 'Le nom d\'utilisateur doit contenir au moins 3 caractères');
        isValid = false;
    } else {
        clearFieldError('username');
    }
    
    // Valider l'email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!data.email || !emailRegex.test(data.email)) {
        setFieldError('email', 'Veuillez saisir un email valide');
        isValid = false;
    } else {
        clearFieldError('email');
    }
    
    // Valider le mot de passe
    if (!data.password || data.password.length < 6) {
        setFieldError('password', 'Le mot de passe doit contenir au moins 6 caractères');
        isValid = false;
    } else {
        clearFieldError('password');
    }
    
    return isValid;
}

// Fonctions utilitaires pour les erreurs de champs
function setFieldError(fieldName, message) {
    const field = document.getElementById(fieldName);
    const feedback = field.nextElementSibling;
    
    field.classList.add('is-invalid');
    field.classList.remove('is-valid');
    
    if (feedback && feedback.classList.contains('invalid-feedback')) {
        feedback.textContent = message;
    }
}

function clearFieldError(fieldName) {
    const field = document.getElementById(fieldName);
    const feedback = field.nextElementSibling;
    
    field.classList.remove('is-invalid');
    field.classList.add('is-valid');
    
    if (feedback && feedback.classList.contains('invalid-feedback')) {
        feedback.textContent = '';
    }
}

// Fonction pour afficher les alertes
function showAlert(message, type) {
    // Supprimer les alertes existantes
    const existingAlerts = document.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-exclamation-triangle'} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Insérer l'alerte avant le formulaire
    const form = document.getElementById('registerForm');
    form.parentNode.insertBefore(alertDiv, form);
    
    // Auto-supprimer après 5 secondes
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// Animation d'entrée
document.addEventListener('DOMContentLoaded', function() {
    const card = document.querySelector('.card');
    if (card) {
        card.classList.add('fade-in');
    }
});
