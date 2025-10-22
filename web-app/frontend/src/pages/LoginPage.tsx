import React, { useState } from 'react';
import { Button, Card, SectionTitle, TextInput } from '../components/uiPrimitives';
import { LogIn } from 'lucide-react';

interface LoginPageProps {
  // This function will be passed down from App.tsx
  // to tell the main app that the user has successfully logged in.
  onLoginSuccess: () => void;
}

export const LoginPage: React.FC<LoginPageProps> = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // --- MOCK LOGIN LOGIC ---
    // In a real app, you would call your apiClient here:
    // try {
    //   const userData = await apiClient.login(username, password);
    //   onLoginSuccess(); // Tell the parent App to change views
    // } catch (err) {
    //   setError('Invalid username or password.');
    // }

    // For this example, we'll just check for simple hardcoded values.
    if (username === 'admin' && password === 'password') {
      onLoginSuccess();
    } else {
      setError('Invalid username or password. (Hint: admin / password)');
    }
    // --- END MOCK LOGIC ---
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center p-6 bg-gray-50 dark:bg-zinc-950">
      <Card className="p-6 w-full max-w-sm">
        <SectionTitle icon={<LogIn className="w-5 h-5" />} title="Login" subtitle="Access your dashboard" />

        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label className="text-sm font-medium text-gray-700 dark:text-gray-300">Username</label>
            <TextInput
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="e.g., admin"
              className="mt-1"
              required
            />
          </div>

          <div>
            <label className="text-sm font-medium text-gray-700 dark:text-gray-300">Password</label>
            <TextInput
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="e.g., password"
              className="mt-1"
              required
            />
          </div>

          {error && (
            <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
          )}

          <Button type="submit" className="w-full justify-center bg-indigo-600 text-white hover:bg-indigo-700">
            Sign In
          </Button>
        </form>
      </Card>
    </div>
  );
};
