const projectDialog = document.getElementById('project-dialog');
const projectForm = document.getElementById('project-form');
const projectList = document.getElementById('project-list');
const projectCount = document.getElementById('project-count');
const formError = document.getElementById('form-error');
const endpointForm = document.getElementById('endpoint-form');
const endpointProject = document.getElementById('endpoint-project');
const endpointMessage = document.getElementById('endpoint-message');
const resultList = document.getElementById('result-list');

function openProjectDialog() {
    formError.textContent = '';
    projectForm.reset();
    projectDialog.showModal();
}

function closeProjectDialog() {
    projectDialog.close();
}

function showToast(message) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.classList.add('show');
    window.setTimeout(() => toast.classList.remove('show'), 2600);
}

async function loadProjects() {
    const response = await fetch('/api/projects');
    const projects = await response.json();
    projectCount.textContent = projects.length;
    if (projects.length === 0) return;
    endpointProject.innerHTML = projects.map(project => `<option value="${project.id}">${escapeHtml(project.name)}</option>`).join('');
    projectList.innerHTML = projects.map(project => `<article class="project-row"><div><strong>${escapeHtml(project.name)}</strong><small>${escapeHtml(project.description || 'No description')}</small></div><code>${escapeHtml(project.baseUrl)}</code><button class="secondary-button" onclick="runTests(${project.id})">Run checks</button></article>`).join('');
}

projectForm.addEventListener('submit', async event => {
    event.preventDefault();
    formError.textContent = '';
    const data = Object.fromEntries(new FormData(projectForm));
    try {
        const response = await fetch('/api/projects', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) });
        const result = await response.json();
        if (!response.ok) throw new Error(result.message || 'Could not register project');
        closeProjectDialog();
        await loadProjects();
        showToast('Project registered successfully.');
    } catch (error) {
        formError.textContent = error.message;
    }
});

endpointForm.addEventListener('submit', async event => {
    event.preventDefault();
    endpointMessage.textContent = '';
    const data = Object.fromEntries(new FormData(endpointForm));
    data.projectId = Number(data.projectId);
    data.name = `${data.method} ${data.path}`;
    data.description = 'Registered endpoint';
    data.requestBody = '';
    try {
        const response = await fetch(`/api/projects/${data.projectId}/endpoints`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) });
        const result = await response.json();
        if (!response.ok) throw new Error(result.message || 'Could not save endpoint');
        endpointForm.reset();
        endpointMessage.textContent = 'Endpoint registered. You can run checks from the project row.';
    } catch (error) {
        endpointMessage.textContent = error.message;
    }
});

async function runTests(projectId) {
    try {
        const response = await fetch(`/api/test-runs/${projectId}`, { method: 'POST' });
        const result = await response.json();
        if (!response.ok) throw new Error(result.message || 'Could not start checks');
        const results = await fetch(`/api/test-runs/${result.id}/results`).then(response => response.json());
        resultList.innerHTML = results.map(item => `<article class="result-row"><div><strong>${escapeHtml(item.testName)}</strong><small>${escapeHtml(item.evidence || item.description)}</small></div><span class="result-badge ${item.status.toLowerCase()}">${item.status}</span></article>`).join('');
        document.getElementById('results').scrollIntoView({ behavior: 'smooth' });
        showToast(`Run ${result.id} completed: ${result.status}`);
    } catch (error) {
        showToast(error.message);
    }
}

function escapeHtml(value) {
    return String(value).replace(/[&<>'"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[character]));
}

loadProjects().catch(() => showToast('Backend is unavailable. Start Spring Boot first.'));
