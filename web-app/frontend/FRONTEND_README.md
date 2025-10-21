# React Frontend Component (CustomerQueueApp.tsx)

## Use with Vite
1. Copy `CustomerQueueApp.tsx` into `src/` of your Vite React project.
2. Install icons (optional): `npm i lucide-react`
3. Optionally enable Tailwind for best visuals (not required).
4. Set API base (or rely on default `http://localhost:8080`):
   ```
   echo "VITE_API_BASE=http://localhost:8080" > .env.local
   ```
5. Render it in `src/App.tsx`:
   ```tsx
   import CustomerQueueApp from './CustomerQueueApp'
   export default function App() { return <CustomerQueueApp /> }
   ```
