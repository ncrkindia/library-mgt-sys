# Library Management System

Desktop library application for **JP Institute of Engineering & Technology** (Meerut). It uses **Java AWT** for the UI and **JDBC** against a **MySQL** database named `library`.

## Requirements to run

| Requirement | Notes |
|-------------|--------|
| **JDK** | **Java 8** (project targets `1.8` in NetBeans `project.properties`). Newer JDKs can compile/run if compatible. |
| **MySQL** | Server reachable at **`127.0.0.1:3306`** with database **`library`**. |
| **Database user** | Code uses user **`root`** with an **empty password** (`jdbc:mysql://127.0.0.1:3306/library`). Change this in every `Frame*.java` that opens a connection if your setup differs. |
| **MySQL JDBC driver** | Code loads **`org.gjt.mm.mysql.Driver`** (legacy Connector/J class name). Add a compatible **MySQL Connector/J** JAR to the project classpath (NetBeans: *Project Properties → Libraries*). For newer drivers you may need to switch to **`com.mysql.cj.jdbc.Driver`** and adjust the JDBC URL (e.g. `?serverTimezone=UTC`). |
| **Build / IDE** | **Apache NetBeans** (or Ant with `build.xml`). Main class: **`Main`**. |

### Compile and run (command line)

1. Add the MySQL connector JAR to the compile/runtime classpath.
2. From the project root:

```bash
javac -encoding UTF-8 -d build/classes -cp "path/to/mysql-connector-j.jar" src/*.java
java -cp "build/classes;path/to/mysql-connector-j.jar" Main
```

On Linux/macOS, use `:` instead of `;` in the classpath.

### NetBeans

Open the folder as a Java application project, set **Run → Main Class** to `Main`, add the MySQL JAR under **Libraries**, then **Run**.

---

## Project layout

| Path | Role |
|------|------|
| `src/Main.java` | Entry point: launches the main window. |
| `src/Frame4.java` | **Library Panel** — root menu with navigation to other screens. |
| `src/Frame1.java` | **Student entry** — validates student ID. |
| `src/Frame2.java` | **Student detail / book issue** — profile, issue limit, issue flow. |
| `src/Frame3.java` | **Book return** — return by book number. |
| `src/Frame5.java` | **Book details** — search and add catalog entries. |
| `build.xml`, `nbproject/` | Ant / NetBeans build configuration. |

---

## Code overview

### `Main.java`

- Declares `main` and shows **`Frame4`** (`new Frame4().setVisible(true)`).

### `Frame4` — Library Panel

- Window title: **"Library Panel"**; closing exits the app (`System.exit(0)`).
- Displays institute name and location labels.
- Three buttons:
  - **Student Detail / Issue** → opens `Frame1`.
  - **Return** → opens `Frame3`.
  - **Book Details** → opens `Frame5`.
- Child frames hide `Frame4` while open and restore it on close.

### `Frame1` — Student entry point

- JDBC: `Class.forName("org.gjt.mm.mysql.Driver")` and connection to `library`.
- **Student ID** text field and **Enter** button.
- Queries **`Studentdetail`** with `WHERE ID = ?`.
- If found: opens **`Frame2`** with that ID; if not: shows **"Sorry , Invalid Student ID ."**

### `Frame2` — Student detail and book issue

- Loads student row from **`Studentdetail`** for the given ID; shows **ID**, **Name**, **Father**.
- Counts rows in **`issue`** for that student → **Book Issued** label.
- **Issue limit:** if count **≥ 4**, the **Issue** button is disabled (`b.disable()`).
- **Issue** flow:
  1. Parses **Book No.** from a text field.
  2. Checks **`bookavailable`** for exactly one row with that `BookNo`.
  3. If available: deletes from **`bookavailable`**, inserts into **`issue`** (`ID`, `BookNo`, fixed `Date`/`Time` in SQL: `'2015-07-17'`, `'00:00:00'`).
  4. Refreshes issued count and **printed book list** (titles/authors for issued books).
- **`printBookDetails()`:** reads **`issue`** → **`BookNo`** → **`BookNo`** table for **`BookID`** → **`BookDetail`** for **BookName** / **Author**; fills up to four labels.

### `Frame3` — Book returning point

- Validates **`BookNo`** exists in **`BookNo`**.
- If the book appears in **`issue`**: deletes from **`issue`**, inserts into **`BookAvailable`** (physical copy back in stock).
- Messages: **Returned .**, **All-ready Returned .**, or **BookNo is InValid .**

> **Note:** `Frame2` uses the table name **`bookavailable`** (lowercase) while `Frame3` inserts into **`BookAvailable`**. MySQL on Windows often treats these as the same; on Linux with case-sensitive filesystem/table names, ensure table naming matches your schema.

### `Frame5` — Book detail: add / search

- Fields: **Book ID**, **Book Name**, **Author**, **Book No.**
- **Search Name** — `BookDetail` by `BookName`.
- **Search ID** — `BookDetail` by `BookID` (integer validation on errors).
- **Seach BookNo** — lookup **`BookNo`** then **`BookDetail`** by `BookID`.
- **Add to DataBase** — `INSERT INTO BookDetail` with `(BookID, BookName, Author)`.

---

## Database schema (inferred from SQL in code)

Create a database `library` and tables consistent with the queries. Example shape (adjust types/indexes as needed):

```sql
CREATE DATABASE IF NOT EXISTS library;
USE library;

CREATE TABLE Studentdetail (
  ID    VARCHAR(…) PRIMARY KEY,
  Name  VARCHAR(…),
  Father VARCHAR(…)
);

CREATE TABLE BookDetail (
  BookID   INT PRIMARY KEY,
  BookName VARCHAR(…),
  Author   VARCHAR(…)
);

CREATE TABLE BookNo (
  BookNo INT PRIMARY KEY,
  BookID INT,
  FOREIGN KEY (BookID) REFERENCES BookDetail(BookID)
);

CREATE TABLE bookavailable (
  BookNo INT PRIMARY KEY,
  FOREIGN KEY (BookNo) REFERENCES BookNo(BookNo)
);

CREATE TABLE issue (
  ID     VARCHAR(…),
  BookNo INT,
  Date   DATE,
  Time   TIME,
  PRIMARY KEY (ID, BookNo),
  FOREIGN KEY (BookNo) REFERENCES BookNo(BookNo)
);
```

Populate **`BookDetail`**, **`BookNo`**, and **`bookavailable`** so copies exist before issuing. **`Studentdetail`** must contain valid student IDs.

---

## Features summary

1. **Main menu (Library Panel)** — Institute branding; navigation to student/issue, return, and book catalog.
2. **Student login by ID** — Validates against **`Studentdetail`** before access to issue screen.
3. **Student profile view** — ID, name, father, and count of books currently issued.
4. **Book issue** — From available stock only; updates **`issue`** and **`bookavailable`**; **maximum 4 books per student**.
5. **Issued books list** — Shows title and author for each issued copy (up to four slots in the UI).
6. **Book return** — By book number; returns copy to availability if it was issued.
7. **Book catalog** — Search by name, by book ID, or by book number; **add new** books to **`BookDetail`**.

---

## Limitations and implementation notes

- **Hard-coded issue date/time** in `Frame2` (`2015-07-17`, `00:00:00`) — not the actual issue moment.
- **Credentials and URL** are embedded in source; not suitable for production without external configuration.
- Errors are often printed to **`System.out`**; the UI may stay quiet while the console shows exceptions.
- **Package-less** classes (`Main`, `Frame1`–`Frame5`) in default package — fine for a small app, uncommon for larger projects.

---

## License / attribution

Project metadata lists vendor **SHIVA** (`application.vendor` in NetBeans properties). Add a license file if you plan to distribute the project.
