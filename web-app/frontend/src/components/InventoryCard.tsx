import React, { useState, useMemo } from 'react';
import { Plus, Trash2, Search, Package } from 'lucide-react';
import { Card, SectionTitle, TextInput, NumberInput, Button } from './uiPrimitives';
import { DataTable } from './DataTable';
import * as apiClient from '../apiClient';

// Types
type Item = { name: string; qty: number };

interface InventoryCardProps {
  initialItems: Item[];
}

export const InventoryCard: React.FC<InventoryCardProps> = ({ initialItems }) => {
  const [items, setItems] = useState<Item[]>(initialItems);
  const [itemName, setItemName] = useState('');
  const [itemQty, setItemQty] = useState(1);
  const [itemSearch, setItemSearch] = useState('');

  const refreshInventory = async () => {
    try {
      const freshItems = await apiClient.getInventory();
      setItems(freshItems);
    } catch (error) {
      console.error("Failed to refresh inventory:", error);
      // Optionally show an error message to the user
    }
  };

  const handleAddOrIncrement = async () => {
    const n = itemName.trim();
    const q = Math.max(1, Number(itemQty) || 1);
    if (!n) return;
    await apiClient.addOrIncrementItem(n, q);
    setItemName('');
    setItemQty(1);
    await refreshInventory();
  };

  const handleRemoveItem = async (name: string) => {
    await apiClient.deleteItem(name);
    await refreshInventory();
  };

  const filteredItems = useMemo(() => {
    const q = itemSearch.trim().toLowerCase();
    const list = q ? items.filter((it) => it.name.toLowerCase().includes(q)) : items;
    return [...list].sort((a, b) => a.name.localeCompare(b.name));
  }, [items, itemSearch]);

  // Effect to update internal state if the initial prop changes
  React.useEffect(() => {
    setItems(initialItems);
  }, [initialItems]);

  return (
    <Card className="p-6">
      <SectionTitle icon={<Package className="w-5 h-5" />} title="Inventory" subtitle="Track item quantities and quick search." />

      <div className="grid md:grid-cols-3 gap-3 mb-4">
        <TextInput placeholder="Item name" value={itemName} onChange={(e) => setItemName(e.target.value)} />
        <NumberInput placeholder="Qty" value={itemQty} onChange={(e) => setItemQty(Number(e.target.value))} />
        <div className="flex gap-2">
          <Button onClick={handleAddOrIncrement} title="Add or increment"><Plus className="w-4 h-4" />Add/Inc</Button>
        </div>
      </div>

      <div className="flex items-center gap-2 mb-3">
        <Search className="w-4 h-4" />
        <TextInput placeholder="Search items" value={itemSearch} onChange={(e) => setItemSearch(e.target.value)} />
      </div>

      <DataTable
        columns={[
          { key: 'name', header: 'Item' },
          { key: 'qty', header: 'Qty' },
          { key: 'actions', header: '' },
        ]}
        data={filteredItems}
        emptyStateMessage="No items yet."
        renderRow={(it) => (
          <tr key={it.name} className="border-t border-gray-100 dark:border-zinc-800">
            <td className="px-4 py-2 font-medium">{it.name}</td>
            <td className="px-4 py-2 tabular-nums">{it.qty}</td>
            <td className="px-4 py-2">
              <Button className="text-red-600" onClick={() => handleRemoveItem(it.name)} title="Delete">
                <Trash2 className="w-4 h-4" />
              </Button>
            </td>
          </tr>
        )}
      />
    </Card>
  );
};

