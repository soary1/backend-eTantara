// Script pour la page de connexion
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
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

    // Gestion du formulaire de connexion
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            
            // Validation côté client
            if (!username || !password) {
                showAlert('Veuillez remplir tous les champs', 'danger');
                return;
            }
            
            // Afficher le loader
            const submitBtn = this.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerHTML;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Connexion...';
            submitBtn.disabled = true;
            
            // Appel API
            const loginData = {
                username: username,
                password: password
            };
            
            fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message || 'Erreur de connexion');
                    });
                }
                return response.json();
            })
            .then(data => {
                console.log('Données de connexion reçues:', data);
                console.log('Type de data.token:', typeof data.token);
                console.log('Longueur du token:', data.token ? data.token.length : 'undefined');
                
                if (data && data.token && data.token.trim() !== '') {
                    // Nettoyer le token au cas où
                    const cleanToken = data.token.trim();
                    
                    // Stocker le token
                    localStorage.setItem('authToken', cleanToken);
                    localStorage.setItem('username', data.username);
                    
                    // Vérifier immédiatement le stockage
                    const storedToken = localStorage.getItem('authToken');
                    const storedUsername = localStorage.getItem('username');
                    
                    console.log('✅ Token original:', cleanToken.substring(0, 50) + '...');
                    console.log('✅ Token stocké:', storedToken ? storedToken.substring(0, 50) + '...' : 'NULL');
                    console.log('✅ Username stocké:', storedUsername);
                    
                    if (storedToken && storedToken === cleanToken) {
                        // Afficher le succès
                        showAlert('Connexion réussie! Redirection...', 'success');
                        
                        // Rediriger vers le dashboard
                        setTimeout(() => {
                            window.location.href = '/dashboard';
                        }, 1500);
                    } else {
                        throw new Error('Erreur de stockage du token');
                    }
                } else {
                    console.error('Token invalide dans la réponse:', data);
                    throw new Error(data.message || 'Erreur de connexion - token invalide');
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                showAlert(error.message || 'Erreur de connexion. Veuillez réessayer.', 'danger');
            })
            .finally(() => {
                // Restaurer le bouton
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            });
        });
    }
});

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
    const form = document.getElementById('loginForm');
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
