# ⌚ Nursing Home Wear OS App

A Wear OS smartwatch application that allows nursing home residents to send emergency and non-emergency alerts to nurses, improving response time and overall care quality.

---

## ✨ Features

- 🆘 **Emergency Alerts:** Tap to request immediate help from nursing staff.
- 🚰 **Non-Emergency Requests:** Request water, bathroom assistance, or other support.
- 📲 **Simple Navigation UI:** Swipeable screens for intuitive use.
- 🔊 **Audio Integration:** Record or playback audio (if supported).
- 🌙 **Dark Mode Support:** Accessible interface for day or night use.
- 🔐 **Firebase Integration:** Sync alerts and user authentication with the backend.

---

## 🧭 Navigation Flow

```
MainActivity.kt
└── NavControl.kt
    ├── LoginScreen.kt
    ├── SignUpScreen.kt
    ├── HomeScreen.kt
    │   ├── EmergencyScreen.kt
    │   └── NonEmergencyScreen.kt
    ├── PhoneScreen.kt
    └── SettingScreen.kt
```

---

## 🔧 Technologies Used

- Kotlin for Wear OS
- Android Jetpack Compose
- Firebase Authentication & Firestore
- Android Studio Emulator (Wear OS Round)
- MVVM Pattern (ViewModel for patient data)

---

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/viyamira/user_interface-circular.git
   ```

2. **Open the project in Android Studio**
   - File > Open > Select the root `user_interface-circular` folder

3. **Build and Run**
   - Select a Wear OS emulator or connected smartwatch
   - Press ▶️ to build and deploy

---

## 📁 Directory Structure

```
app/
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── navbar/
                        └── presentation/
                            ├── EmergencyScreen.kt
                            ├── NonEmergencyScreen.kt
                            ├── HomeScreen.kt
                            ├── LoginScreen.kt
                            ├── SignUpScreen.kt
                            ├── SettingScreen.kt
                            ├── PhoneScreen.kt
                            ├── Patient Details.kt
                            ├── PatientViewModel.kt
                            └── NavControl.kt
```

---

## ✅ Note

> If you encounter Gradle sync issues, try:
> - File > Invalidate Caches / Restart
> - Check Gradle version compatibility
> - Ensure Firebase configuration file is present and valid

---

## 📜 License

This project is intended for educational and nonprofit use only.
