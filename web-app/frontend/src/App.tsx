import React, { useEffect, useState } from 'react';
import { InventoryCard } from './components/InventoryCard';
import { QueueCard } from './components/QueueCard';
import { LoginPage } from './pages/LoginPage'; // Import the new login page
import * as apiClient from './apiClient';
import { LogOut } from 'lucide-react';
import { Button } from './components/uiPrimitives';

// Types
export type Item = { name: string; qty: number };
export type Customer = { name: string; note?: string };

/**
 * This component holds the main dashboard UI, which is shown after login.
 */
const DashboardPage: React.FC<{ onLogout: () => void }> = ({ onLogout }) => {
  const [items, setItems] = useState<Item[]>([]);
  const [queue, setQueue] = useState<Customer[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Callback to refresh all data
  const fetchData = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const [inventoryData, queueData] = await Promise.all([
        apiClient.getInventory(),
        apiClient.getQueue(),
      ]);
      setItems(inventoryData);
      setQueue(queueData);
    } catch (err) {
      setError('Failed to connect to the API. Please ensure the backend server is running.');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  // Initial data fetch for the dashboard
  useEffect(() => {
    fetchData();
  }, []);

  if (isLoading) {
    return (
      <div className="min-h-screen w-full flex items-center justify-center p-6 bg-gray-50 dark:bg-zinc-950">
        <p className="text-lg text-gray-500">Loading application data...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen w-full flex items-center justify-center p-6 bg-red-50 dark:bg-red-900/10">
        <div className="text-center">
          <h1 className="text-xl font-semibold text-red-700 dark:text-red-300">Connection Error</h1>
          <p className="text-red-600 dark:text-red-400 mt-2">{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen w-full p-6 md:p-10 bg-gradient-to-b from-gray-50 to-white dark:from-black dark:to-zinc-950 text-gray-900 dark:text-gray-100">
      <div className="max-w-6xl mx-auto grid gap-8">
        <header className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl md:text-3xl font-semibold tracking-tight">Farm Dashboard</h1>
            <p className="text-sm text-gray-500 dark:text-gray-400">Inventory & Customer Queue Management</p>
          </div>
          <Button onClick={onLogout} className="text-gray-600 dark:text-gray-300">
            <LogOut className="w-4 h-4" />
            <span>Logout</span>
          </Button>
        </header>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <InventoryCard
            initialItems={items}
            onDataChange={fetchData} // Pass the refresh function
          />
          <QueueCard
            initialQueue={queue}
            onDataChange={fetchData} // Pass the refresh function
          />
        </div>
      </div>
    </div>
  );
};

/**
 * This is the main App component. It decides whether to show
 * the Login page or the main Dashboard.
 */
export default function App() {
  // We use a simple boolean state to track auth.
  // In a real app, you might use localStorage or React Context.
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // This is the function we pass to the LoginPage
  const handleLoginSuccess = () => {
    setIsAuthenticated(true);
  };

  // This is the function we pass to the DashboardPage
  const handleLogout = () => {
    setIsAuthenticated(false);
  };

  // Render the correct page based on auth state
  if (!isAuthenticated) {
    return <LoginPage onLoginSuccess={handleLoginSuccess} />;
  }

  return <DashboardPage onLogout={handleLogout} />;
}

