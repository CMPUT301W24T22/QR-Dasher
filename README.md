# QR Dasher


## Overview 📱
**QR Dasher** is an Android application designed for seamless event check-ins using **QR codes** and **geolocation-based attendance verification**. It provides features to create profiles, organize events, and ensure accurate attendance tracking. To enhance the user experience, the app includes **real-time notifications** and an **interactive leaderboard**.


## Features ✨
- **QR Code Check-ins**:
  - Scan QR codes for quick and secure event check-ins.
- **Geolocation-Based Verification**:
  - Ensure attendees are physically present at the event location.
- **Profile Creation**:
  - Allow users to create and manage their profiles.
- **Event Organization**:
  - Enable event hosts to create, manage, and track events.
- **Real-time Notifications**:
  - Notify attendees about event updates and check-in status.
- **Interactive Leaderboard**:
  - Foster engagement with a leaderboard displaying attendee activity.


## Technical Details 🛠️
- **Platform:** Android (Java/Kotlin).
- **Technologies Used:**
  - **Firebase**: For real-time notifications and database management.
  - **Google Maps API**: For geolocation-based attendance verification.
  - **ZXing Library**: For QR code scanning.
- **Architecture:** Follows the MVVM (Model-View-ViewModel) pattern for better code organization and scalability.


## How to Build and Run 🚀
1. **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/qr-dasher.git
    ```
2. **Set Up Firebase:**
   - Download the `google-services.json` file from your Firebase console and place it in the `app/` directory.
3. **Set Up API Keys:**
   - Add your **Google Maps API Key** in the `local.properties` file:
     ```
     MAPS_API_KEY=your_api_key
     ```
4. **Build and Run:**
   - Open the project in Android Studio.
   - Sync Gradle and build the project.
   - Run the app on an Android emulator or physical device.


## Usage 🎯
1. **Create a Profile**:
   - Sign up or log in to create and manage your user profile.
2. **Organize Events**:
   - Use the event management feature to set up events and generate QR codes for check-ins.
3. **Check-In**:
   - Scan QR codes at the event location. Geolocation will verify your presence.
4. **Notifications**:
   - Receive updates about events and check-in statuses.
5. **Leaderboard**:
   - Compete with others by earning points for event participation.



## Future Enhancements 💡
- Add support for NFC-based check-ins as an alternative to QR codes.
- Implement analytics for event organizers to track attendance trends.
- Introduce gamification elements to further boost user engagement.


## Contributors 👩‍💻
<div>  
<a href="https://github.com/Jastegh"><img src="https://images.weserv.nl/?url=https://github.com/Jastegh.png?v=4&h=50&w=50&fit=cover&mask=circle&maxage=7d"/></a>
<a href="https://github.com/jassidaksingh"><img src="https://images.weserv.nl/?url=https://github.com/jassidaksingh.png?v=4&h=50&w=50&fit=cover&mask=circle&maxage=7d"/></a>
<a href="https://github.com/jaz404"><img src="https://avatars.githubusercontent.com/u/115124290?v=4"/></a>
<a href="https://github.com/saipranav1512"><img src="https://avatars.githubusercontent.com/u/145928964?v=4"/></a>
<a href="https://github.com/rithwikkb"><img src="https://avatars.githubusercontent.com/u/111675689?v=4"/></a>
<a href="https://github.com/rishabh2727"><img src="https://avatars.githubusercontent.com/u/121647557?v=4"/></a>
</div>  

## 📜 License
This project is licensed under the [MIT License](LICENSE).

