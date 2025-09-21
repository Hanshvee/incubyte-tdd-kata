# Sweet Shop Management System with TDD kata 🥋

A full-stack application designed using **Test-Driven Development (TDD)** principles to manage inventory, purchases, and administration of a Sweet Shop. Built with a modern Java + Spring Boot backend and React frontend.

---

## 🧪 Test-Driven Development (TDD)

This project strictly follows the TDD methodology:

> **Red ➜ Green ➜ Refactor ➜ Integrate**

- **Red**: Write failing tests before code.
- **Green**: Write just enough code to pass.
- **Refactor**: Clean up without breaking tests.
- **Integrate**: Commit changes with test validation.

### 🔺 TDD Pyramid

![TDD Pyramid](https://drive.google.com/uc?id=11S1ZE3lEyG9olij-snCpCfUIG1UhVKYL)

> **Developer Focus:**  
> Developers should prioritize:
>
> - ✅ Unit Tests (Fast, isolated tests for core logic using JUnit + Mockito)
> - ✅ Integration Tests (Spring Boot Tests for API and database interactions)

---

## ❌ Common Myth: "TDD Slows Development"

### ✅ The Reality: TDD accelerates development in the long run by:

- Catching bugs early.
- Promoting modular, maintainable code.
- Speeding up refactoring and onboarding.
- Reducing time spent on manual QA.
- Making integration seamless and confident.

---

## 🛠 Tech Stack

Backend : Java, Spring Boot  
 Testing : JUnit, Mockito, Spring Boot Test  
 Build : Maven  
 Frontend : React  
 Database : PostgreSQL  

---

# 🚀 How to Run This App Locally

Follow these steps to get the Sweet Shop Management System running locally:

## 1. Clone the Repository

```
git clone <your-repo-url>
cd sweet-shop-management-system
```

## 2. Start the Backend (Spring Boot)

```
cd Backend/sweetshopsystem/sweetshopsystem
./mvnw clean package   # or mvnw.cmd clean package on Windows
java -jar target/sweetshopsystem-0.0.1-SNAPSHOT.jar
```
The backend will start on [http://localhost:8080](http://localhost:8080).

## 3. Start the Frontend (React)

Open a new terminal and run:

```
cd Frontend/sugar-dash-react
npm install
npm run dev
```
The frontend will start on [http://localhost:5173](http://localhost:5173) (or the port shown in your terminal).

## 4. Access the App

Open your browser and go to [http://localhost:5173](http://localhost:5173).

---

**Note:**
- Make sure you have Java 17+ and Node.js 16+ installed.
- The backend uses a PostgreSQL database. Configure your DB connection in `application.properties` if needed.
- For production, see the Docker/Railway deployment instructions.

---

## My AI Usage

### **Which AI tools I used**

* **Cursor AI IDE**
* **TRAE AI IDE**
* **VS Code (GPT-4.1 integration)**
* **Gemini**
* **ChatGPT (GPT models)**
* **GitHub Copilot**
* **Lovable**
* **Claude**

### **How I used them**

* Used in-IDE AI assistants (**Claude, GPT, Gemini 2.5**) for:

  * Generating test cases
  * Setting up project logic
  * Debugging issues
* **GPT** : Optimized prompts before passing them to IDE AI tools
* **Lovable** : Helped in quickly creating UI components (was **impressively good**)
* **AI tools overall** : Helped with naming conventions, writing industry-standard commit messages, and structuring clean code/test cases
* **Pro tip 😅** : To keep using free AI credits, I shamelessly switched between different IDEs with different AI integrations.

### **Reflection**

AI tools significantly optimized my development workflow by:

* Saving time in generating solutions, boilerplate code, and UI designs
* Improving naming conventions and overall code readability
* Assisting in writing professional commit messages
* Producing high-quality test cases aligned with industry standards
* Adding a bit of fun to the process with all the AI juggling 🎭

---

## 🎥 Demo

- [Demo Video](https://drive.google.com/file/d/15fl6DO7fcn3tdwiiw-1ol8ulBkEIwEbe/preview" )

---

## 📚 Learning Resources

- [Incubyte Blog](https://blog.incubyte.co/blog/a-practical-approach-to-test-driven-development-beyond-red-green-refactor/)
- [Incubyte YouTube Channel](https://www.youtube.com/@incubyte_co)
