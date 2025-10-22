// The base URL for your Spring Boot backend.
// It's good practice to read this from an environment variable.
const API_BASE_URL = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080';

// A helper function to handle fetch requests and JSON parsing.
async function apiFetch(path: string, options?: RequestInit) {
    const response = await fetch(`${API_BASE_URL}/api${path}`, options);
    if (!response.ok) {
        throw new Error(`API Error: ${response.statusText}`);
    }
    // Handle responses that have no content (like a successful DELETE)
    if (response.status === 204) {
        return;
    }
    return response.json();
}

// --- Inventory Endpoints ---
export const getInventory = () => apiFetch('/inventory');
export const addOrIncrementItem = (name: string, amount: number) =>
    apiFetch('/inventory/addOrIncrement', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, amount }),
    });
export const deleteItem = (name: string) =>
    apiFetch(`/inventory/${encodeURIComponent(name)}`, { method: 'DELETE' });

// --- Queue Endpoints ---
export const getQueue = () => apiFetch('/queue');
export const enqueueCustomer = (name: string, note?: string) =>
    apiFetch('/queue/enqueue', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, note }),
    });
export const dequeueCustomer = () => apiFetch('/queue/dequeue', { method: 'POST' });
