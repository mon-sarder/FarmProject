import React from 'react';

// This file contains small, reusable UI components used across the application.

export const Card: React.FC<React.HTMLAttributes<HTMLDivElement>> = ({ className = "", ...props }) => (
  <div className={`rounded-2xl shadow-lg border border-gray-200 dark:border-gray-800 bg-white dark:bg-zinc-900 ${className}`} {...props} />
);

export const SectionTitle: React.FC<{ icon: React.ReactNode; title: string; subtitle?: string }> = ({ icon, title, subtitle }) => (
  <div className="flex items-center gap-3 mb-6">
    <div className="p-2 rounded-xl bg-gray-100 dark:bg-zinc-800">{icon}</div>
    <div>
      <h2 className="text-xl font-semibold tracking-tight">{title}</h2>
      {subtitle && <p className="text-sm text-gray-500 dark:text-gray-400">{subtitle}</p>}
    </div>
  </div>
);

export const TextInput: React.FC<React.InputHTMLAttributes<HTMLInputElement>> = ({ className = "", ...props }) => (
  <input
    className={`w-full rounded-xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-zinc-900 px-3 py-2 outline-none focus:ring-2 focus:ring-indigo-500 ${className}`}
    {...props}
  />
);

export const NumberInput: React.FC<React.InputHTMLAttributes<HTMLInputElement>> = (props) => (
  <TextInput type="number" inputMode="numeric" {...props} />
);

export const Button: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement>> = ({ className = "", ...props }) => (
  <button
    className={`inline-flex items-center gap-2 rounded-xl px-3 py-2 shadow-sm border border-gray-200 dark:border-gray-800 hover:shadow transition active:scale-[.99] disabled:opacity-50 disabled:cursor-not-allowed ${className}`}
    {...props}
  />
);
