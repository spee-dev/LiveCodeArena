// src/pages/AdminPage.jsx
import React, { useState } from 'react';

const AdminPage = () => {
    const [generatedLink, setGeneratedLink] = useState('');
    const [isCopied, setIsCopied] = useState(false);

    const createSessionAndGetLink = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/sessions/create', {
                method: 'POST',
            });
            const data = await response.json(); // This will now only contain the sessionId
            
            // Construct the single, full, shareable URL
            const fullLink = `${window.location.origin}/session/${data.sessionId}`;
            setGeneratedLink(fullLink);
            setIsCopied(false); // Reset copy status
        } catch (error) {
            console.error('Failed to create session:', error);
            alert('Could not create a new session. Please ensure the backend is running correctly.');
        }
    };

    const copyLinkToClipboard = () => {
        if (!generatedLink) return;
        navigator.clipboard.writeText(generatedLink).then(() => {
            setIsCopied(true);
            // Revert the "Copied!" text after 2 seconds
            setTimeout(() => setIsCopied(false), 2000);
        });
    };

    return (
        <div className="admin-container">
            <div className="admin-card">
                <header className="admin-header">
                    <h1>Interview Session Manager</h1>
                    <p>Create a unique and secure link for your next technical interview.</p>
                </header>

                <div className="admin-action">
                    <button onClick={createSessionAndGetLink} className="admin-button">
                        Generate New Interview Link
                    </button>
                </div>

                {generatedLink && (
                    <div className="generated-link-section">
                        <h2>Your Link is Ready:</h2>
                        <div className="link-box">
                            <input type="text" readOnly value={generatedLink} className="generated-link-input" />
                            <button onClick={copyLinkToClipboard} className="copy-button">
                                {isCopied ? 'Copied!' : 'Copy'}
                            </button>
                        </div>
                        <p className="instructions">
                            Share this link with both the interviewer and the candidate.
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminPage;