// Scripts communs à toute l'application
document.addEventListener('DOMContentLoaded', function() {
    // Initialiser les tooltips Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialiser les popovers Bootstrap
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Smooth scrolling pour les liens d'ancrage
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });
});

// Fonctions utilitaires globales
window.AppUtils = {
    // Formater une date
    formatDate: function(date, locale = 'fr-FR') {
        const options = { 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric'
        };
        return new Date(date).toLocaleDateString(locale, options);
    },
    
    // Formater une date avec l'heure
    formatDateTime: function(date, locale = 'fr-FR') {
        const options = { 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        };
        return new Date(date).toLocaleDateString(locale, options);
    },
    
    // Debounce function
    debounce: function(func, wait, immediate) {
        let timeout;
        return function executedFunction() {
            const context = this;
            const args = arguments;
            const later = function() {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            const callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    },
    
    // Valider un email
    isValidEmail: function(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },
    
    // Générer un ID unique
    generateId: function() {
        return '_' + Math.random().toString(36).substr(2, 9);
    },
    
    // Copier du texte dans le presse-papier
    copyToClipboard: function(text) {
        if (navigator.clipboard) {
            return navigator.clipboard.writeText(text);
        } else {
            // Fallback pour les navigateurs plus anciens
            const textArea = document.createElement('textarea');
            textArea.value = text;
            document.body.appendChild(textArea);
            textArea.focus();
            textArea.select();
            try {
                document.execCommand('copy');
                document.body.removeChild(textArea);
                return Promise.resolve();
            } catch (err) {
                document.body.removeChild(textArea);
                return Promise.reject(err);
            }
        }
    }
};

// Gestionnaire d'API centralisé
window.ApiClient = {
    baseUrl: '/api',
    
    // Méthode GET
    get: async function(endpoint, options = {}) {
        return this.request('GET', endpoint, null, options);
    },
    
    // Méthode POST
    post: async function(endpoint, data, options = {}) {
        return this.request('POST', endpoint, data, options);
    },
    
    // Méthode PUT
    put: async function(endpoint, data, options = {}) {
        return this.request('PUT', endpoint, data, options);
    },
    
    // Méthode DELETE
    delete: async function(endpoint, options = {}) {
        return this.request('DELETE', endpoint, null, options);
    },
    
    // Méthode de requête générique
    request: async function(method, endpoint, data, options = {}) {
        const url = this.baseUrl + endpoint;
        const token = localStorage.getItem('authToken');
        
        const config = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };
        
        // Ajouter le token d'authentification si disponible
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        
        // Ajouter les données pour POST/PUT
        if (data && (method === 'POST' || method === 'PUT')) {
            config.body = JSON.stringify(data);
        }
        
        try {
            const response = await fetch(url, config);
            
            // Gérer les erreurs HTTP
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `Erreur HTTP: ${response.status}`);
            }
            
            // Retourner les données JSON
            return await response.json();
        } catch (error) {
            console.error('Erreur API:', error);
            throw error;
        }
    }
};

// Gestionnaire de notifications
window.NotificationManager = {
    show: function(message, type = 'info', duration = 5000) {
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        notification.style.cssText = `
            top: 20px;
            right: 20px;
            z-index: 9999;
            min-width: 300px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        `;
        
        const icon = this.getIcon(type);
        notification.innerHTML = `
            <i class="${icon} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(notification);
        
        // Auto-suppression
        if (duration > 0) {
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.remove();
                }
            }, duration);
        }
        
        return notification;
    },
    
    getIcon: function(type) {
        const icons = {
            'success': 'fas fa-check-circle',
            'danger': 'fas fa-exclamation-triangle',
            'warning': 'fas fa-exclamation-circle',
            'info': 'fas fa-info-circle'
        };
        return icons[type] || icons.info;
    },
    
    success: function(message, duration) {
        return this.show(message, 'success', duration);
    },
    
    error: function(message, duration) {
        return this.show(message, 'danger', duration);
    },
    
    warning: function(message, duration) {
        return this.show(message, 'warning', duration);
    },
    
    info: function(message, duration) {
        return this.show(message, 'info', duration);
    }
};
