# 📁 File Favorites Manager

## 📝 Description

**File Favorites Manager** is a Java-based desktop application with a graphical user interface (GUI) built using **JavaFX**. It helps users manage and organize their favorite files using descriptive tags. Users can mark files, add rich metadata, and perform advanced searches to quickly retrieve important files.

## 🎯 Objectives

The main goals of this application are to:
- Tag and describe important files
- Store metadata for easy retrieval
- Search and classify favorite files using keywords
- Generate file-related statistics and summaries

The application uses a **relational database** to store data persistently and supports a set of essential features listed below.

---

## 💡 Features

### 1. ✅ Mark Favorite Files
Users can select files from their machine and mark them as "favorites" by filling in metadata fields:
- **Author** *(optional)*
- **Title** *(required)*
- **Tags** *(required; keywords separated by commas or semicolons)*
- **Summary** *(optional)*
- **Comments** *(optional)*

> Example:
>
> - File: `C:\meslivres\livre2.pdf`  
> - Author: Steven Halim  
> - Title: Competitive Programming  
> - Tags: compétition, algorithmique, programmation  
> - Summary:  
> - Comments: Très important : à consulter pour la préparation du concours TCPC !

### 2. ✏️ Update or Remove Favorites
- Modify the metadata of an already marked file.
- Remove a file from the favorites list.

### 3. 📜 List Favorite Files
- Display all favorite files along with their metadata.
- Option to export the list to a `.txt` file.

### 4. 🔍 Search Favorite Files
Search by:
- Author
- Title
- Tag (keyword)

Results include:
- File title
- Full file path (location)

### 5. 📊 View Properties and Statistics
Visualize file usage data such as:
- Total number of favorite files
- List of authors
- List of tags used
- Number of files per tag (shows most frequently used topics)

---

## 🖼️ GUI & Technologies

- **JavaFX** (GUI framework)
- **JDBC** (for database connectivity)
- **SQLite** or **MySQL** (relational database)
- **JavaFX FileChooser** (for file selection)
- **`java.awt.Desktop`** (to open PDF and other files)

---


