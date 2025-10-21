import React, { useEffect, useMemo, useState } from "react";
import { Plus, Trash2, UserPlus, Play, Search, Package, Users, RefreshCw } from "lucide-react";

// Types
type Item = { name: string; qty: number };
type Customer = { name: string; note?: string };

// Reusable UI primitives
const Card: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({ className = "", ...props }) => (
  <div className={`rounded-2xl shadow-lg border border-gray-200 dark:border-gray-800 bg-white dark:bg-zinc-900 ${className}`} {...props} />
);
const SectionTitle: React.FC<{ icon: React.ReactNode; title: string; subtitle?: string }> = ({ icon, title, subtitle }) => (
  <div className="flex items-center gap-3 mb-6">
    <div className="p-2 rounded-xl bg-gray-100 dark:bg-zinc-800">{icon}</div>
    <div>
      <h2 className="text-xl font-semibold tracking-tight">{title}</h2>
      {subtitle && <p className="text-sm text-gray-500 dark:text-gray-400">{subtitle}</p>}
    </div>
  </div>
);
const TextInput: React.FC<React.InputHTMLAttributes<HTMLInputElement>> = ({ className = "", ...props }) => (
  <input
    className={`w-full rounded-xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-zinc-900 px-3 py-2 outline-none focus:ring-2 focus:ring-indigo-500 ${className}`}
    {...props}
  />
);
const NumberInput: React.FC<React.InputHTMLAttributes<HTMLInputElement>> = (props) => (
  <TextInput type="number" inputMode="numeric" {...props} />
);
const Button: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>> = ({ className = "", ...props }) => (
  <button
    className={`inline-flex items-center gap-2 rounded-xl px-3 py-2 shadow-sm border border-gray-200 dark:border-gray-800 hover:shadow transition active:scale-[.99] disabled:opacity-50 disabled:cursor-not-allowed ${className}`}
    {...props}
  />
);

export default function CustomerQueueApp() {
  const API = (path: string) => `${(import.meta as any).env?.VITE_API_BASE ?? 'http://localhost:8080'}/api${path}`;

  // Inventory state
  const [items, setItems] = useState<Item[]>([]);
  const [itemName, setItemName] = useState("");
  const [itemQty, setItemQty] = useState(1);
  const [itemSearch, setItemSearch] = useState("");

  // Queue state
  const [queue, setQueue] = useState<Customer[]>([]);
  const [custName, setCustName] = useState("");
  const [custNote, setCustNote] = useState("");
  const [custSearch, setCustSearch] = useState("");

  // Initial fetch
  useEffect(() => {
    Promise.all([
      fetch(API('/inventory')).then(r => r.json()),
      fetch(API('/queue')).then(r => r.json())
    ]).then(([inv, q]) => { setItems(inv); setQueue(q); }).catch(() => {});
  }, []);

  // Inventory actions -> API
  const addOrIncrement = async () => {
    const n = itemName.trim();
    const q = Math.max(1, Number(itemQty) || 1);
    if (!n) return;
    await fetch(API('/inventory/addOrIncrement'), { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ name: n, amount: q }) });
    setItemName(""); setItemQty(1);
    const next = await fetch(API('/inventory')).then(r => r.json());
    setItems(next);
  };
  const setQuantity = async () => {
    const n = itemName.trim();
    const q = Math.max(0, Number(itemQty) || 0);
    if (!n) return;
    await fetch(API('/inventory/setQuantity'), { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ name: n, qty: q }) });
    const next = await fetch(API('/inventory')).then(r => r.json());
    setItems(next);
  };
  const removeItem = async (name: string) => {
    await fetch(API(`/inventory/${encodeURIComponent(name)}`), { method: 'DELETE' });
    const next = await fetch(API('/inventory')).then(r => r.json());
    setItems(next);
  };

  const filteredItems = useMemo(() => {
    const q = itemSearch.trim().toLowerCase();
    const list = q ? items.filter((it) => it.name.toLowerCase().includes(q)) : items;
    return [...list].sort((a, b) => a.name.localeCompare(b.name));
  }, [items, itemSearch]);

  // Queue actions -> API
  const enqueue = async () => {
    const n = custName.trim();
    if (!n) return;
    await fetch(API('/queue/enqueue'), { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ name: n, note: custNote.trim() || undefined }) });
    setCustName(""); setCustNote("");
    const next = await fetch(API('/queue')).then(r => r.json());
    setQueue(next);
  };
  const dequeue = async () => {
    await fetch(API('/queue/dequeue'), { method: 'POST' });
    const next = await fetch(API('/queue')).then(r => r.json());
    setQueue(next);
  };
  const clearQueue = async () => {
    await fetch(API('/queue'), { method: 'DELETE' });
    setQueue([]);
  };

  const filteredQueue = useMemo(() => {
    const q = custSearch.trim().toLowerCase();
    return q ? queue.filter((c) => c.name.toLowerCase().includes(q)) : queue;
  }, [queue, custSearch]);

  return (
    <div className="min-h-screen w-full p-6 md:p-10 bg-gradient-to-b from-gray-50 to-white dark:from-black dark:to-zinc-950 text-gray-900 dark:text-gray-100">
      <div className="max-w-6xl mx-auto grid gap-8">
        <header className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl md:text-3xl font-semibold tracking-tight">Customer Queue & Inventory</h1>
            <p className="text-sm text-gray-500 dark:text-gray-400">Connected to a Spring Boot REST API.</p>
          </div>
        </header>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Inventory */}
          <Card className="p-6">
            <SectionTitle icon={<Package className="w-5 h-5" />} title="Inventory" subtitle="Track item quantities and quick search." />

            <div className="grid md:grid-cols-3 gap-3 mb-4">
              <TextInput placeholder="Item name" value={itemName} onChange={(e) => setItemName(e.target.value)} />
              <NumberInput placeholder="Qty" value={itemQty} onChange={(e) => setItemQty(Number(e.target.value))} />
              <div className="flex gap-2">
                <Button onClick={addOrIncrement} title="Add or increment"><Plus className="w-4 h-4" />Add/Inc</Button>
                <Button onClick={setQuantity} title="Set quantity">Set</Button>
              </div>
            </div>

            <div className="flex items-center gap-2 mb-3">
              <Search className="w-4 h-4" />
              <TextInput placeholder="Search items" value={itemSearch} onChange={(e) => setItemSearch(e.target.value)} />
            </div>

            <div className="max-h-80 overflow-auto rounded-xl border border-gray-200 dark:border-gray-800">
              <table className="w-full text-sm">
                <thead className="sticky top-0 bg-gray-50 dark:bg-zinc-900">
                  <tr className="text-left">
                    <th className="px-4 py-2">Item</th>
                    <th className="px-4 py-2">Qty</th>
                    <th className="px-4 py-2 w-10"></th>
                  </tr>
                </thead>
                <tbody>
                  {filteredItems.length === 0 ? (
                    <tr>
                      <td className="px-4 py-6 text-gray-500" colSpan={3}>No items yet.</td>
                    </tr>
                  ) : (
                    filteredItems.map((it) => (
                      <tr key={it.name} className="border-t border-gray-100 dark:border-zinc-800">
                        <td className="px-4 py-2 font-medium">{it.name}</td>
                        <td className="px-4 py-2 tabular-nums">{it.qty}</td>
                        <td className="px-4 py-2">
                          <Button className="text-red-600" onClick={() => removeItem(it.name)} title="Delete"><Trash2 className="w-4 h-4" /></Button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </Card>

          {/* Queue */}
          <Card className="p-6">
            <SectionTitle icon={<Users className="w-5 h-5" />} title="Customer Queue" subtitle="FIFO queue with enqueue/dequeue and search." />

            <div className="grid md:grid-cols-3 gap-3 mb-4">
              <TextInput placeholder="Customer name" value={custName} onChange={(e) => setCustName(e.target.value)} />
              <TextInput placeholder="Note (optional)" value={custNote} onChange={(e) => setCustNote(e.target.value)} />
              <div className="flex gap-2">
                <Button onClick={enqueue} title="Enqueue"><UserPlus className="w-4 h-4" />Enqueue</Button>
                <Button onClick={dequeue} disabled={queue.length === 0} title="Serve next"><Play className="w-4 h-4" />Serve</Button>
              </div>
            </div>

            <div className="flex items-center gap-2 mb-3">
              <Search className="w-4 h-4" />
              <TextInput placeholder="Search by name" value={custSearch} onChange={(e) => setCustSearch(e.target.value)} />
              <Button onClick={clearQueue} className="ml-auto" title="Clear queue"><RefreshCw className="w-4 h-4" />Clear</Button>
            </div>

            <div className="max-h-80 overflow-auto rounded-xl border border-gray-200 dark:border-gray-800">
              <table className="w-full text-sm">
                <thead className="sticky top-0 bg-gray-50 dark:bg-zinc-900">
                  <tr className="text-left">
                    <th className="px-4 py-2">Name</th>
                    <th className="px-4 py-2">Note</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredQueue.length === 0 ? (
                    <tr>
                      <td className="px-4 py-6 text-gray-500" colSpan={2}>Queue is empty.</td>
                    </tr>
                  ) : (
                    filteredQueue.map((c, i) => (
                      <tr key={`${c.name}-${i}`} className="border-t border-gray-100 dark:border-zinc-800">
                        <td className="px-4 py-2 font-medium">{c.name}</td>
                        <td className="px-4 py-2 text-gray-600 dark:text-gray-300">{c.note || "—"}</td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </Card>
        </div>

        <footer className="text-xs text-gray-500 dark:text-gray-400">
          <p>Base URL reads from <code>VITE_API_BASE</code>; defaults to <code>http://localhost:8080</code>.</p>
        </footer>
      </div>
    </div>
  );
}
