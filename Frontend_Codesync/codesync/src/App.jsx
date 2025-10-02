// src/App.jsx
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import AdminPage from './pages/AdminPage'; // Renamed import
import EditorPage from './pages/editorPage';
import './App.css';

function App() {
  return (
    <Routes>
      {/* The main page can now just be a placeholder */}
      <Route path="/" element={
        <div style={{ padding: '450px', textAlign: 'center' }}>
          <h1>CodeSync Platform</h1>
        </div>
      } />
      
      {/* The new admin page */}
      <Route path="/admin" element={<AdminPage />} />

      <Route path="/session/:sessionId" element={<EditorPage />} />
    </Routes>
  );
}

export default App;
