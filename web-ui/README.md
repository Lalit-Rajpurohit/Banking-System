# Banking System Web UI

A modern React-based frontend for the Banking System, built with TypeScript, Tailwind CSS, and Vite.

## Features

- User registration and authentication
- Dashboard with account overview
- Create multiple account types (Savings, Checking, Business)
- Deposit and withdraw funds
- Transfer between accounts
- Transaction history

## Tech Stack

- **React 18** - UI library
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **Vite** - Build tool and dev server
- **React Router v6** - Client-side routing
- **React Hook Form** - Form handling
- **Axios** - HTTP client

## Prerequisites

- Node.js 18+ (LTS recommended)
- npm or yarn
- Backend server running on `http://localhost:8080`

## Getting Started

### 1. Install Dependencies

```bash
cd web-ui
npm install
```

### 2. Start Development Server

```bash
npm run dev
```

The app will open at `http://localhost:5173`

### 3. Build for Production

```bash
npm run build
```

Build output will be in the `dist/` directory.

## Configuration

### API Base URL

The API base URL is configured in `src/api/api.ts`. By default, it points to:

```
http://localhost:8080/api
```

For production, update this to your backend server URL.

### CORS

The backend must allow requests from:
- `http://localhost:5173` (Vite dev server)
- `http://localhost:3000` (alternative)

This is configured in the backend's `SecurityConfig.java`.

## Project Structure

```
web-ui/
├── src/
│   ├── api/              # API client and endpoint functions
│   │   ├── api.ts        # Axios instance with interceptors
│   │   ├── auth.ts       # Authentication endpoints
│   │   ├── accounts.ts   # Account endpoints
│   │   └── transactions.ts # Transaction endpoints
│   ├── components/       # Reusable UI components
│   │   ├── Header.tsx
│   │   ├── AccountCard.tsx
│   │   └── TransactionList.tsx
│   ├── contexts/         # React contexts
│   │   └── AuthContext.tsx
│   ├── pages/            # Page components
│   │   ├── Login.tsx
│   │   ├── Register.tsx
│   │   ├── Dashboard.tsx
│   │   ├── AccountDetail.tsx
│   │   └── Transfer.tsx
│   ├── App.tsx           # Main app with routing
│   ├── main.tsx          # Entry point
│   └── index.css         # Global styles with Tailwind
├── index.html
├── package.json
├── tailwind.config.cjs
├── postcss.config.cjs
├── tsconfig.json
└── vite.config.ts
```

## Available Scripts

| Script | Description |
|--------|-------------|
| `npm run dev` | Start development server |
| `npm run build` | Build for production |
| `npm run preview` | Preview production build |
| `npm run lint` | Run ESLint |

## Styling

The app uses a custom color scheme:
- **Primary (Teal)**: Navigation, buttons, accents
- **Accent (Orange)**: Call-to-action buttons

Custom utility classes are defined in `src/index.css`:
- `.btn-primary` - Orange accent button
- `.btn-secondary` - Teal button
- `.btn-outline` - Outlined button
- `.input-field` - Form input styling
- `.card` - Card container

## Authentication

JWT tokens are stored in `localStorage` and automatically attached to API requests via an Axios interceptor. On 401 responses, users are redirected to the login page.

## Development Notes

1. Ensure the backend is running before starting the frontend
2. The app uses proxy configuration in `vite.config.ts` for development
3. All forms use React Hook Form for validation and state management
4. TypeScript strict mode is enabled for better type safety
