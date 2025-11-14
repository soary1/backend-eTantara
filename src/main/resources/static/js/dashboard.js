// Script pour le tableau de bord
document.addEventListener('DOMContentLoaded', function() {
    // Vérifier l'authentification
    checkAuthentication();
    
    // Initialiser les animations
    initAnimations();
    
    // Charger les données du dashboard
    loadDashboardData();
});

// Vérifier si l'utilisateur est connecté (optionnel)
function checkAuthentication() {
    const token = localStorage.getItem('authToken');
    const username = localStorage.getItem('username');
    
    // Afficher le nom d'utilisateur si disponible, sinon afficher "Visiteur"
    const welcomeText = document.querySelector('h1');
    if (welcomeText) {
        if (username) {
            welcomeText.textContent = `Bienvenue ${username}`;
        } else {
            welcomeText.textContent = `Bienvenue Visiteur`;
        }
    }
}

// Fonction de déconnexion
function logout() {
    // Supprimer les données stockées
    localStorage.removeItem('authToken');
    localStorage.removeItem('username');
    
    // Afficher une confirmation
    if (confirm('Êtes-vous sûr de vouloir vous déconnecter ?')) {
        // Rediriger vers la page de connexion
        window.location.href = '/login?logout=true';
    }
}

// Initialiser les animations
function initAnimations() {
    // Animation des cartes au scroll
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
            }
        });
    });
    
    // Observer toutes les cartes
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        observer.observe(card);
    });
}

// Charger les données du dashboard
// Charger les données du dashboard depuis le backend
function loadDashboardData() {
    const token = localStorage.getItem('authToken');

    // Préparer les headers (avec ou sans token)
    const headers = {
        'Content-Type': 'application/json'
    };
    
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    // 🔹 Appel à ton API (accessible avec ou sans authentification)
    fetch('/api/dashboard', {
        method: 'GET',
        headers: headers
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erreur lors du chargement du tableau de bord.');
        }
        return response.json();
    })
    .then(data => {
        console.log('✅ Données du dashboard:', data);

        // 🔹 Ici tu peux afficher les infos reçues du backend
        // Exemple :
        if (data.username) {
            document.querySelector('h1').textContent = `Bienvenue ${data.username}`;
        }

        if (data.activities) {
            updateActivitiesList(data.activities);
        }

        // Mettre à jour les barres de progression
        updateProgressBars();
    })
    .catch(error => {
        console.error('Erreur:', error);
        showNotification('Erreur: ' + error.message, 'danger');
    });
}


// Mettre à jour les barres de progression avec animation
function updateProgressBars() {
    const progressBars = document.querySelectorAll('.progress-bar');
    
    progressBars.forEach(bar => {
        const width = bar.style.width;
        bar.style.width = '0%';
        
        setTimeout(() => {
            bar.style.width = width;
        }, 300);
    });
}

// Charger les activités récentes
function loadRecentActivities() {
    // Simuler des données d'activités récentes
    const activities = [
        {
            title: 'Nouvelle histoire ajoutée',
            description: 'Les contes de grand-mère Rasoa',
            time: 'Il y a 2 heures',
            badge: 'Nouveau',
            badgeClass: 'bg-primary'
        },
        {
            title: 'Quiz completé',
            description: 'Culture et traditions - Score: 85%',
            time: 'Hier',
            badge: 'Terminé',
            badgeClass: 'bg-success'
        },
        {
            title: 'Musique écoutée',
            description: 'Hira gasy traditionnel',
            time: 'Il y a 3 jours',
            badge: 'Écouté',
            badgeClass: 'bg-info'
        }
    ];
    
    // Mettre à jour l'interface avec les données
    updateActivitiesList(activities);
}

// Mettre à jour la liste des activités
function updateActivitiesList(activities) {
    const activitiesList = document.querySelector('.list-group');
    
    if (!activitiesList) return;
    
    // Ajouter une animation de chargement
    activitiesList.innerHTML = '<div class="loader"></div>';
    
    setTimeout(() => {
        activitiesList.innerHTML = '';
        
        activities.forEach((activity, index) => {
            const activityElement = document.createElement('div');
            activityElement.className = 'list-group-item d-flex justify-content-between align-items-center';
            activityElement.style.animationDelay = `${index * 0.1}s`;
            
            activityElement.innerHTML = `
                <div>
                    <h6 class="mb-1">${activity.title}</h6>
                    <p class="mb-1">${activity.description}</p>
                    <small>${activity.time}</small>
                </div>
                <span class="badge ${activity.badgeClass} rounded-pill">${activity.badge}</span>
            `;
            
            activitiesList.appendChild(activityElement);
        });
    }, 1000);
}

// Gestion des clics sur les cartes
document.addEventListener('click', function(e) {
    const card = e.target.closest('.card');
    if (card && card.querySelector('a')) {
        const link = card.querySelector('a');
        if (link && !e.target.closest('a')) {
            // Ajouter une animation de clic
            card.style.transform = 'scale(0.95)';
            setTimeout(() => {
                card.style.transform = '';
                // Simuler un clic sur le lien
                // link.click();
            }, 150);
        }
    }
});

// Fonctions utilitaires
function formatDate(date) {
    const options = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    };
    return new Date(date).toLocaleDateString('fr-FR', options);
}

function showNotification(message, type = 'info') {
    // Créer une notification toast
    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    
    // Ajouter le toast au conteneur
    let toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }
    
    toastContainer.appendChild(toast);
    
    // Afficher le toast
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
    
    // Supprimer le toast après fermeture
    toast.addEventListener('hidden.bs.toast', () => {
        toast.remove();
    });
}

// Exposer les fonctions globalement
window.logout = logout;
window.showNotification = showNotification;
