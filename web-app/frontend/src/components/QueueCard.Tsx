import React, { useState, useMemo } from 'react';
import { UserPlus, Play, Search, Users, RefreshCw } from 'lucide-react';
import { Card, SectionTitle, TextInput, Button } from './uiPrimitives'; // Assuming UI primitives are extracted
import { DataTable } from './DataTable';
import * as apiClient from '../apiClient';

// Types
type Customer = { name: string; note?: string };

interface QueueCardProps {
  initialQueue: Customer[];
}

export const QueueCard: React.FC<QueueCardProps> = ({ initialQueue }) => {
  const [queue, setQueue] = useState<Customer[]>(initialQueue);
  const [custName, setCustName] = useState('');
  const [custNote, setCustNote] = useState('');
  const [custSearch, setCustSearch] = useState('');

  const refreshQueue = async () => {
    const freshQueue = await apiClient.getQueue();
    setQueue(freshQueue);
  };

  const handleEnqueue = async () => {
    const n = custName.trim();
    if (!n) return;
    await apiClient.enqueueCustomer(n, custNote.trim() || undefined);
    setCustName('');
    setCustNote('');
    await refreshQueue();
  };

  const handleDequeue = async () => {
    await apiClient.dequeueCustomer();
    await refreshQueue();
  };

  const handleClearQueue = async () => {
    // You would add a clearQueue function to your apiClient.ts
    // await apiClient.clearQueue();
    console.log("Clearing queue..."); // Placeholder
    await refreshQueue();
  };

  const filteredQueue = useMemo(() => {
    const q = custSearch.trim().toLowerCase();
    return q ? queue.filter((c) => c.name.toLowerCase().includes(q)) : queue;
  }, [queue, custSearch]);

  return (
    <Card className="p-6">
      <SectionTitle icon={<Users className="w-5 h-5" />} title="Customer Queue" subtitle="FIFO queue with enqueue/dequeue and search." />

      <div className="grid md:grid-cols-3 gap-3 mb-4">
        <TextInput placeholder="Customer name" value={custName} onChange={(e) => setCustName(e.target.value)} />
        <TextInput placeholder="Note (optional)" value={custNote} onChange={(e) => setCustNote(e.target.value)} />
        <div className="flex gap-2">
          <Button onClick={handleEnqueue} title="Enqueue"><UserPlus className="w-4 h-4" />Enqueue</Button>
          <Button onClick={handleDequeue} disabled={queue.length === 0} title="Serve next"><Play className="w-4 h-4" />Serve</Button>
        </div>
      </div>

      <div className="flex items-center gap-2 mb-3">
        <Search className="w-4 h-4" />
        <TextInput placeholder="Search by name" value={custSearch} onChange={(e) => setCustSearch(e.target.value)} />
        <Button onClick={handleClearQueue} className="ml-auto" title="Clear queue"><RefreshCw className="w-4 h-4" />Clear</Button>
      </div>

      <DataTable
        columns={[
          { key: 'name', header: 'Name' },
          { key: 'note', header: 'Note' },
        ]}
        data={filteredQueue}
        emptyStateMessage="Queue is empty."
        renderRow={(c, i) => (
          <tr key={`${c.name}-${i}`} className="border-t border-gray-100 dark:border-zinc-800">
            <td className="px-4 py-2 font-medium">{c.name}</td>
            <td className="px-4 py-2 text-gray-600 dark:text-gray-300">{c.note || '—'}</td>
          </tr>
        )}
      />
    </Card>
  );
};
