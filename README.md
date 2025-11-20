SafeSip: Android App to Help People Reduce Alcohol Consumption

**SafeSip** is an Android application designed to help individuals struggling with alcohol addiction track, control, and progressively reduce their drinking habits.  
It provides a simple and effective way to monitor daily intake, receive alerts when limits are exceeded, and maintain a clear record of progress.

---

## Features

### Easy Drink Logging
- Users can quickly record each drink they consume.
- Data is stored locally using **SharedPreferences**.

### Daily Tracking
- Displays the total amount consumed on the current day.
- Automatically resets daily consumption at midnight through `MidnightReceiver`.

### Excess Consumption Warning
- If the user exceeds the safety threshold, the app displays warnings according to the amount of acohol that was drank.

### Undo Last Drink
- Users can remove the last recorded drink.
- The internal vectors and daily totals update consistently.

### Complete History
- The application maintains arrays containing all past records, enabling future analysis or visualization.

## Technologies Used

- **Java** (Android)
- **SharedPreferences** for persistent local storage
- **BroadcastReceiver** for automatic midnight updates

## Social Purpose

The SafeSip app was created to support individuals aiming to:

- Track drinking habits

- Build healthier routines

- Reduce daily alcohol intake

- Avoid binge drinking

- Stay aware of their real consumption levels

No account or external tracking is required.
All data stays private on the device.

## Authors

Developed by the following students of CentraleSupélec for the Coding Weeks:

- Artur Bandeira Chan Jorge\[[LinkedIn](https://www.linkedin.com/in/artur-bandeira-chan-jorge-b7646327a/)\]\[[Contact](mailto:arturbchanj@gmail.com)\]
- Jared Bilstein\[[LinkedIn](https://www.linkedin.com/in/jared-bilstein-232b0b2a6/)\]\[[Contact](mailto:jared.bilstein@student-cs.fr)\]
- Pedro Lubaszewski Lima\[[LinkedIn](https://www.linkedin.com/in/pedro-lubaszewski/)\]\[[Contact](mailto:pedro.lubaszewski.lima@gmail.com)\]
- Sabrina Baloul\[[LinkedIn](https://www.linkedin.com/in/sabrina-baloul-1603a6310/)\]\[[Contact](mailto:sabrina.baloul@student-cs.fr)\]