import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import Editor from '@monaco-editor/react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import '../App.css'; 

// A unique ID for this user's browser session to identify their cursor and messages
const CLIENT_ID = `user_${Math.random().toString(36).substr(2, 9)}`;

const EditorPage = () => {
    const { sessionId } = useParams();

    // --- STATE FOR EDITOR & LIVE FEATURES ---
    const [code, setCode] = useState(`// Welcome to session: ${sessionId}`);
    const [language, setLanguage] = useState('javascript');
    const [theme, setTheme] = useState('vs-dark');
    const [isConnected, setIsConnected] = useState(false);
    const [remoteCursors, setRemoteCursors] = useState({});
    
    // --- REFS ---
    const stompClientRef = useRef(null);
    const editorRef = useRef(null);
    const decorationIdsRef = useRef([]);
    const isRemoteChange = useRef({ code: false, language: false });

    useEffect(() => {
        if (!sessionId) return;

        const stompClient = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            reconnectDelay: 5000,
            onConnect: () => {
                console.log('Connected to WebSocket server!');
                setIsConnected(true);
                
                // Subscription for code updates
                stompClient.subscribe(`/topic/editor/${sessionId}`, (message) => {
                    const received = JSON.parse(message.body);
                    if (editorRef.current && editorRef.current.getValue() !== received.content) {
                        isRemoteChange.current.code = true;
                        editorRef.current.setValue(received.content);
                    }
                });

                // Subscription for cursor movements
                stompClient.subscribe(`/topic/cursor/${sessionId}`, (message) => {
                    const { clientId, lineNumber, column } = JSON.parse(message.body);
                    if (clientId !== CLIENT_ID) {
                        setRemoteCursors(prev => ({ ...prev, [clientId]: { lineNumber, column } }));
                    }
                });

                // Subscription for language changes
                stompClient.subscribe(`/topic/language/${sessionId}`, (message) => {
                    const { clientId, language: newLanguage } = JSON.parse(message.body);
                    if (clientId !== CLIENT_ID) {
                        isRemoteChange.current.language = true;
                        setLanguage(newLanguage);
                    }
                });
            },
            onDisconnect: () => setIsConnected(false),
            onStompError: (frame) => console.error('Broker error:', frame.headers['message']),
        });

        stompClient.activate();
        stompClientRef.current = stompClient;

        return () => {
            if (stompClientRef.current) stompClientRef.current.deactivate();
        };
    }, [sessionId]);

    // This useEffect renders the remote cursors when their positions change
    useEffect(() => {
        if (editorRef.current && window.monaco) {
            const decorations = Object.entries(remoteCursors).map(([clientId, pos]) => ({
                range: new window.monaco.Range(pos.lineNumber, pos.column, pos.lineNumber, pos.column),
                options: { className: 'remote-cursor', stickiness: 1 }
            }));
            decorationIdsRef.current = editorRef.current.deltaDecorations(decorationIdsRef.current, decorations);
        }
    }, [remoteCursors]);

    // --- HANDLER FUNCTIONS ---
    const handleEditorChange = (value) => {
        setCode(value);
        if (isRemoteChange.current.code) { isRemoteChange.current.code = false; return; }
        if (stompClientRef.current?.connected) {
            stompClientRef.current.publish({
                destination: `/app/editor.update/${sessionId}`,
                body: JSON.stringify({ clientId: CLIENT_ID, content: value }),
            });
        }
    };

    const handleLanguageChange = (e) => {
        const newLanguage = e.target.value;
        setLanguage(newLanguage);
        if (isRemoteChange.current.language) { isRemoteChange.current.language = false; return; }
        if (stompClientRef.current?.connected) {
            stompClientRef.current.publish({
                destination: `/app/language.change/${sessionId}`,
                body: JSON.stringify({ clientId: CLIENT_ID, language: newLanguage }),
            });
        }
    };
    
    function handleEditorDidMount(editor, monaco) {
        editorRef.current = editor;
        editor.onDidChangeCursorPosition(e => {
            if (stompClientRef.current?.connected) {
                stompClientRef.current.publish({
                    destination: `/app/cursor.move/${sessionId}`,
                    body: JSON.stringify({
                        clientId: CLIENT_ID,
                        lineNumber: e.position.lineNumber,
                        column: e.position.column,
                    }),
                });
            }
        });
    }

    const copyToClipboard = () => {
        navigator.clipboard.writeText(code).then(() => {
            alert('Code copied to clipboard!');
        }, (err) => {
            console.error('Failed to copy text: ', err);
        });
    };

    return (
        <div className="app-container">
            <aside className="sidebar">
                <div className="sidebar-header">
                    <h1>CodeSync</h1>
                    <div className={`connection-status ${isConnected ? 'connected' : 'disconnected'}`}>
                        {isConnected ? '● Connected' : '● Disconnected'}
                    </div>
                </div>
                <div className="sidebar-content">
                    <div className="toolbar">
                        <div className="toolbar-row">
                            <label htmlFor="language-select">Language:</label>
                            <select id="language-select" value={language} onChange={handleLanguageChange}>
                                <option value="javascript">JavaScript</option>
                                <option value="typescript">TypeScript</option>
                                <option value="python">Python</option>
                                <option value="java">Java</option>
                                <option value="html">HTML</option>
                                <option value="css">CSS</option>
                            </select>
                        </div>
                        <div className="toolbar-row">
                            <label htmlFor="theme-select">Theme:</label>
                            <select id="theme-select" value={theme} onChange={(e) => setTheme(e.target.value)}>
                                <option value="vs-dark">VS Dark</option>
                                <option value="light">Light</option>
                            </select>
                        </div>
                        <button onClick={copyToClipboard} className="toolbar-button">Copy Code</button>
                    </div>
                </div>
            </aside>
            <main className="editor-area">
                <div className="editor-container">
                    <Editor 
                        language={language}
                        theme={theme}
                        value={code}
                        onChange={handleEditorChange}
                        onMount={handleEditorDidMount}
                    />
                </div>
            </main>
        </div>
    );
};

export default EditorPage;