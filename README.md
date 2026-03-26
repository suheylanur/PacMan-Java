# PacMan-Java
Pac-Man game with a scary twist built using Java Swing.
# 🕹️ Pac-Man: The Classic Remake (Java Swing)

Hi! I'm a 2nd-year Computer Engineering student, and this is my remake of the legendary Pac-Man game. I built this project to deepen my understanding of **Object-Oriented Programming (OOP)**, handle real-time game loops, and experiment with Java's standard graphical libraries.

My focus was on recreating a clean and smooth classic experience.

## ✨ Core Features

- **Classic Mechanics:** Accurate smooth movement, pellet collecting, and real-time score tracking.
- **Custom Map Engine:** I designed the level using a `tileMap` array, making it super easy to change the maze layout or spawn points.
- **Ghost AI & Movement:** Four distinct ghost types with random and somewhat chasing movement patterns.
- **Power-Up Logic:** Reaching 1000 points triggers "Super Pellets." When eaten, the ghosts enter a "scared" state, allowing you to fight back.
- **Classic Game Over:** A clear "Game Over" screen when you lose all lives, focusing on performance.

## 🛠️ Tech Stack

- **Language:** Java
- **GUI & Graphics:** `javax.swing` (for windowing) and `java.awt` (for custom drawing).
- **Game Logic:** - Managed smooth 50ms frame updates using the `Timer` class.
  - Used `HashSet` for fast collision checks with walls and food to maintain performance.
  - Implemented pixel-perfect collision detection logic.

## 🎮 How to Play

1. **Movement:** Use your **Arrow Keys** (⬆️, ⬇️, ⬅️, ➡️) to navigate the maze.
2. **Goal:** Clear all the food on the map without getting caught!
3. **Survival:** Beware of the ghosts... unless you've eaten a blue Super Pellet!

## 🚀 Setup & Run

If you want to try it out yourself:
1. Clone this repo: `git clone https://github.com/suheylanur/PacMan-Java.git`
2. Open it in your favorite IDE (I used VS Code/IntelliJ).
3. Make sure all the image assets (`.png`) are in the same folder as the code.
4. Run `PacMan.java` and enjoy the classic vibe!

---
*Note: This project is part of my learning journey in software development. Feel free to check out the code or leave a star!*
