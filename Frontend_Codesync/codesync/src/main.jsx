// src/main.jsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import { BrowserRouter } from 'react-router-dom'; // Import this

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    {/* Add the BrowserRouter here */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
);