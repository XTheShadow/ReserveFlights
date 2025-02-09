# ✈️ ReserveFlights

## 📖 Description  
ReserveFlights is a **Java-based** flight reservation system with a **graphical user interface (GUI)** and **database integration**. It allows users to **register, log in, and make flight reservations** efficiently.

---

##  📌 Features  
✔️ **User authentication** (Login & Registration)  
✔️ **Flight reservation system** with real-time booking  
✔️ **Intuitive GUI** built using **Java Swing**  
✔️ **Database integration** for storing users & flight details  
✔️ **Configurable settings** for database & system preferences  

---

## 🛠️ Installation  

### ✅ Prerequisites  
- ☕ **Java 11** or later  
- ⚙️ **Gradle** (included via Gradle Wrapper)  
- 🗄️ **MySQL** or a compatible database  

### 🚀 Setup & Installation  

1️⃣ **Clone the repository:**  
   ```sh
   git clone https://github.com/yourusername/ReserveFlights.git
   cd ReserveFlights
   ```

2️⃣ **Set up the database:**  
   - Create a MySQL database  
   - Import the schema file:  
     ```sh
     mysql -u your_user -p your_database < src/resources/db/schema.sql
     ```
   - *(Optional)* Import seed data for testing:  
     ```sh
     mysql -u your_user -p your_database < src/resources/db/seed.sql
     ```

3️⃣ **Configure the application:**  
   - Copy the config example:  
     ```sh
     cp src/resources/config.example.properties src/resources/config.properties
     ```
   - Edit `config.properties` to match your database credentials  

4️⃣ **Build and run the project:**  
   ```sh
   ./gradlew build
   java -jar build/libs/ReserveFlights.jar
   ```

---

## 🎮 Usage  
- Run the application and **log in / register** as a user.  
- Browse available flights and **make reservations**.  
- Admins can **manage flight data** through the database directly.  

---

## 🏗️ Technologies Used  
🔹 **Java** (Swing for GUI)  
🔹 **Gradle** (Build system)  
🔹 **MySQL** (Database)  

---

## 📜 License  
This project is licensed under the **MIT License**.  
