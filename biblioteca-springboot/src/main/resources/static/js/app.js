// Aplicar tema inmediatamente para evitar flash
(function() {
    var t = localStorage.getItem('biblioteca-theme') || 'light';
    document.documentElement.setAttribute('data-theme', t);
})();

function syncToggle(t) {
    var thumb = document.getElementById('toggle-thumb');
    var toggle = document.getElementById('theme-toggle');
    var icon = document.getElementById('theme-icon');
    if (thumb) thumb.style.left = t === 'dark' ? '23px' : '3px';
    if (toggle) toggle.style.background = t === 'dark' ? '#1f6feb' : '#30363d';
    if (icon) icon.textContent = t === 'dark' ? '☀️' : '🌙';
}

document.addEventListener('DOMContentLoaded', function() {

    // Tema
    var t = localStorage.getItem('biblioteca-theme') || 'light';
    document.documentElement.setAttribute('data-theme', t);
    syncToggle(t);

    var btn = document.getElementById('theme-toggle');
    if (btn) {
        btn.addEventListener('click', function() {
            var current = document.documentElement.getAttribute('data-theme') || 'light';
            var next = current === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', next);
            localStorage.setItem('biblioteca-theme', next);
            syncToggle(next);
        });
    }

    // Alertas
    document.querySelectorAll('.alert').forEach(function(alert) {
        var closeBtn = alert.querySelector('.alert-close');
        if (closeBtn) closeBtn.addEventListener('click', function() { cerrarAlerta(alert); });
        setTimeout(function() { cerrarAlerta(alert); }, 4000);
    });

    // Búsqueda
    var searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            var query = this.value.toLowerCase();
            var rows = document.querySelectorAll('tbody tr');
            var count = 0;
            rows.forEach(function(row) {
                var text = row.textContent.toLowerCase();
                var show = !query || text.includes(query);
                row.style.display = show ? '' : 'none';
                if (show && row.cells.length > 1) count++;
            });
            var counter = document.getElementById('search-counter');
            if (counter) counter.textContent = query ? count + ' resultado(s)' : '';
        });
    }

    // Confirmaciones
    document.querySelectorAll('[data-confirm]').forEach(function(el) {
        el.addEventListener('click', function(e) {
            e.preventDefault();
            var msg = this.getAttribute('data-confirm');
            var action = this.getAttribute('href');
            mostrarModal(msg, action);
        });
    });

    // Contadores animados
    document.querySelectorAll('.stat-value[data-count]').forEach(function(el) {
        var target = parseInt(el.getAttribute('data-count'), 10);
        animarContador(el, target, 900);
    });

    // Spinner exportar
    document.querySelectorAll('.btn-export').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var orig = this.innerHTML;
            this.innerHTML = '⏳ Generando...';
            this.style.pointerEvents = 'none';
            var self = this;
            setTimeout(function() { self.innerHTML = orig; self.style.pointerEvents = ''; }, 3500);
        });
    });
});

function cerrarAlerta(el) {
    el.style.transition = 'opacity 0.4s, transform 0.4s';
    el.style.opacity = '0';
    el.style.transform = 'translateY(-8px)';
    setTimeout(function() { if (el.parentNode) el.parentNode.removeChild(el); }, 400);
}

function mostrarModal(mensaje, action) {
    var existing = document.getElementById('confirm-modal');
    if (existing) existing.remove();
    var overlay = document.createElement('div');
    overlay.id = 'confirm-modal';
    overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;z-index:9999;';
    overlay.innerHTML = '<div style="background:var(--bg-card);border-radius:12px;padding:28px;max-width:360px;width:90%;border:1px solid var(--border-color);box-shadow:0 20px 60px rgba(0,0,0,0.4);">'
        + '<div style="font-size:40px;text-align:center;margin-bottom:12px;">⚠️</div>'
        + '<h3 style="font-size:15px;font-weight:600;color:var(--text-primary);text-align:center;margin-bottom:8px;">¿Confirmar acción?</h3>'
        + '<p style="font-size:13px;color:var(--text-muted);text-align:center;margin-bottom:22px;">' + mensaje + '</p>'
        + '<div style="display:flex;gap:10px;justify-content:center;">'
        + '<button onclick="document.getElementById(\'confirm-modal\').remove()" style="padding:8px 18px;border-radius:6px;border:1px solid var(--border-color);background:var(--bg-primary);color:var(--text-primary);cursor:pointer;font-size:13px;">Cancelar</button>'
        + '<a href="' + action + '" style="padding:8px 18px;border-radius:6px;background:#dc3545;color:white;text-decoration:none;font-size:13px;font-weight:500;">Confirmar</a>'
        + '</div></div>';
    document.body.appendChild(overlay);
    overlay.addEventListener('click', function(e) { if (e.target === overlay) overlay.remove(); });
}

function animarContador(el, target, duracion) {
    var inicio = performance.now();
    function step(ahora) {
        var p = Math.min((ahora - inicio) / duracion, 1);
        el.textContent = Math.round(target * (1 - Math.pow(1 - p, 3)));
        if (p < 1) requestAnimationFrame(step);
    }
    requestAnimationFrame(step);
}
