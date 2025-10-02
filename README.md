<div align="center">

# 🚀 CodeSync - Real-Time Collaborative Code Editor  
**Real-Time Collaborative Code Editor for Professional Technical Interviews**  

_A professional platform to conduct live coding interviews with secure, real-time collaboration and seamless interviewer-candidate workflows._  

</div>

---

<div align="center">

## 💡 Project Overview  

</div>

CodeSync is a **full-stack web application** designed to facilitate live, multi-user coding sessions in professional technical interviews.  
Unlike simple CRUD apps, it solves complex problems like:  
- **Concurrent user interactions**  
- **Real-time state synchronization**  
- **Role-based permissions**  

Built with **Spring Boot (Backend)** and **React (Frontend)**, it ensures a smooth, secure, and professional interview environment.  

---

<div align="center">

## ✨ Core Features  

</div>

- 🔒 **Secure, Role-Based Interview Links**  
  Unique tokenized links for Interviewers and Candidates, ensuring controlled and secure access.  

- 🤝 **Professional Interview Lobby**  
  Candidates wait in a controlled lobby until admitted by the interviewer.  

- ⚡ **Live Code Synchronization**  
  Edits appear instantly across all participants via WebSockets.  

- 🖱️ **Multi-User Cursor Tracking**  
  Visualize other users’ cursors in real time.  

- 🎨 **Synchronized Editor Settings**  
  Themes and languages stay consistent for everyone.  

- 🤫 **Isolated & Shareable Sessions**  
  Each session is private and accessible only via secure tokens.  

---

<div align="center">

## 🚀 Tech Stack & Architecture  

</div>

**Backend:**  
- Java 17+, Spring Boot 3  
- Spring WebSocket & STOMP  
- Maven (dependency management)  

**Frontend:**  
- React 18 + Vite  
- react-router-dom, STOMP.js, SockJS-Client  
- Monaco Editor (the editor behind VS Code)  

**Key Concepts:**  
- Event-Driven Design  
- Real-Time State Management  
- WebSocket vs REST  
- Handling Concurrency  
- Role-Based Permissions  

---

<div align="center">

## 🔧 How the Workflow Works  

</div>

1. **Generate Links** → Admin creates secure session links via `/admin`.  
2. **Distribute Links** → Interviewer and Candidate get unique role-based links.  
3. **Lobby System** → Candidate waits in a lobby while the interviewer sees "Waiting for candidate...".  
4. **Start Session** → Interviewer admits the candidate, triggering a `"SESSION_STARTED"` event.  

---

<div align="center">

## ⚙️ Local Setup & Installation  

</div>

### Backend (Spring Boot)  
### Backend (Spring Boot)  
cd codesync-backend
./mvnw spring-boot:run
Access backend at: [http://localhost:8080](http://localhost:8080/)  

### Frontend (React)  
cd codesync-frontend
npm install
npm run dev

The frontend dev server will boot on your local environment.  

---

<div align="center">
</div>
<div align="center">

 

