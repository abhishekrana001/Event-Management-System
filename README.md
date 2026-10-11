# Event Management System 📅

A modern, native Android application built with **Kotlin**, **Room Database**, and **Material Design 3** that simplifies event management for both administrators and general users.

---

## 🌟 Features

### 👑 Admin Module
- **Create & Edit Events:** Easily add new events with title, description, date picker, time picker, and location. Edit or delete existing events anytime.
- **View All Events:** Manage active events with quick action buttons.
- **Statistics Dashboard:** Real-time statistics showing total events hosted, total event participants, and per-event participation counts.

### 👤 User Module
- **Browse & Join Events:** Discover active upcoming events and register with a single click.
- **Smart Time Conflict Detection:** Automatically prevents joining multiple events scheduled for the exact same date and time.
- **My Joined Events:** Track all your active event registrations with an option to cancel.
- **Auto-Archiving & History:** Automatically moves expired events to event history upon completion.

### 🔐 Authentication & Roles
- **Default Admin Account:** Pre-seeded default admin credentials for quick testing:
  - **Email:** `admin@gmail.com`
  - **Password:** `1234`
- **User Registration & Login:** Simple sign-up process for regular users with email uniqueness validation.

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin
- **Database:** Room Database (SQLite)
- **Asynchronous Processing:** Kotlin Coroutines & `lifecycleScope`
- **UI Components:** Material Design 3 (`MaterialCardView`, `MaterialToolbar`, `TextInputLayout`), ViewBinding
- **Architecture Pattern:** DAO (Data Access Object) Pattern with LiveData observables
- **Build System:** Gradle (Kotlin DSL `.gradle.kts`)
- **Min SDK:** 24 (Android 7.0)
- **Target SDK / Compile SDK:** 36

---

## 📁 Project Structure

```
com.example.eventmanagementsystem/
├── activity/
│   ├── MainActivity.kt           # Home landing screen (Login / Register options)
│   ├── LongInActivity.kt         # User & Admin authentication
│   ├── RegisterActivity.kt       # User registration screen
│   ├── AdminActivity.kt          # Admin dashboard
│   ├── UserActivity.kt           # User dashboard
│   ├── CreateEventActivity.kt    # Event creation & editing with Date/Time pickers
│   ├── ViewEventsActivity.kt     # Admin event management list
│   ├── AllEventsActivity.kt      # Browse & Join events for users
│   ├── MyJoinedEventsActivity.kt # User's registered events screen
│   ├── HistoryActivity.kt        # User's completed/expired events history
│   └── AdminStatsActivity.kt     # Event statistics & analytics screen
├── adapter/
│   ├── AdminEventAdapter.kt      # RecyclerView adapter for event statistics
│   ├── EventAdapter.kt           # RecyclerView adapter for admin event management
│   ├── HistoryAdapter.kt         # RecyclerView adapter for event history
│   └── UserEventAdapter.kt       # RecyclerView adapter for user event browsing/joining
├── dao/
│   ├── UserDao.kt                # Database operations for users
│   ├── EventDao.kt               # Database operations for events
│   ├── EventJoinDao.kt           # Database operations for event registrations
│   └── EventHistoryDao.kt        # Database operations for event history
├── data/
│   ├── User.kt                   # Room User entity
│   ├── Event.kt                  # Room Event entity
│   ├── EventJoin.kt              # Room EventJoin entity
│   ├── EventHistory.kt           # Room EventHistory entity
│   └── EventWithCount.kt         # Query model for event participant count
├── database/
│   └── EventDatabase.kt          # Room Database instance with destructive migration fallback
└── utils/
    └── DateUtils.kt              # Date parsing, formatting, and expiration utility
```

---

## 🚀 Getting Started

### Prerequisites
- [Android Studio Jellyfish / Ladybug or newer](https://developer.android.com/studio)
- JDK 11 or higher
- Android SDK 36 (or compatible)

### Installation & Run Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/EventManagementSystem.git
   ```
2. **Open in Android Studio:**
   - Launch Android Studio and click **Open**.
   - Select the `EventManagementSystem` project folder.
3. **Sync Project with Gradle Files:**
   - Allow Gradle to download dependencies automatically.
4. **Run the App:**
   - Connect an Android device or start an Emulator (API 24+).
   - Click the **Run** button (`Shift + F10`).

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
