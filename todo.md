# Project: User App Flow

## 1. Login
- **Responsibility**: **PP**
  - **Action**: User logs into the app using email and password or biometric authentication.
  - **Outcome**: On successful login, the user is directed to the Home screen.

## 2. Home (Main User Screen)
- **Responsibility**: **PP**
  - **QR Code**: Display QR code for user authentication and actions (e.g., scanning at checkout).

### PSQ 2.1 Wallet
- **2.1.1 Transaction List**: Display a list of all user transactions (e.g., payments).
  - **Responsibility**: **PSQ**
- **2.1.2 Add Credit**: Static functionality for adding credit (No dynamic transaction handling for now).
  - **Responsibility**: **PSQ**

### PSQ 2.2 Settings
- **2.2.1 Transaction History**: View the list of past transactions associated with the user’s account.
  - **Responsibility**: **PSQ**
- **2.2.2 User Information**: Display user details (name, email, etc.).
  - **Responsibility**: **PSQ**
- **2.2.3 Change Language**: Option to change the app's language.
  - **Responsibility**: **PSQ**
- **2.2.4 Logout**: Option to log the user out of the app.
  - **Responsibility**: **PP**
- **2.2.5 Use Biometric Authentication**: Option to enable or disable biometric authentication (e.g., fingerprint).
  - **Responsibility**: **PP**
- **2.2.6 Save Session**: Manage session (auto-login or session expiry). *Question: Is this feature necessary?*
  - **Responsibility**: **PP**

### PP 2.3 Menu
- **2.3.1 Meal List**: Display a scrollable list of meals for each day pulled from the database using ListView.
  - **Responsibility**: **PP**

### PP 2.4 Pickup (Completed)
- **2.4.1 Book Items**: Option to book food items such as Cestino (Box), Panino (Sandwich), or Insalata Poke.
  - **Responsibility**: **PP**
- **2.4.2 Choose Time**: Option for the user to select the time for food pickup.
  - **Responsibility**: **PP**
- **2.4.3 In-App Payment with Credit**: Pay for the food using the user’s wallet credit.
  - **Responsibility**: **PP**

## PP 2. Home for Employee (Worker)
### 2.1 Booking List (Completed)
- **2.1.1 Filter Option**: Consider adding a filter for booking records. *Needs to be confirmed if required.*
  - **Responsibility**: **PSQ**
  
### 2.2 Scan QR Code (Completed)
- **2.2.1 Price Selection, etc.**: Complete functionality for scanning QR code and choosing the price, among other details. *To be finished.*
  - **Responsibility**: **PP**

---

## Notes:
- The **flow** starts with the **Login** process, followed by the **Home screen** that includes wallet, settings, menu, and pickup options.
- **Biometric Authentication** could be integrated to allow quick and secure login.
- **Scholarship Feature**:
  - The system should display the user’s **scholarship tier** (1/2/3).
  - This should affect the **wallet balance** or **meal pricing** dynamically.
- **Payment mode**:
  - "order;product": prenotazione da app
  - "topup;cash": ricarca con contanti dalla mensa
  - "topup;online": ricarica online da app
  - "payment": pagamento alla mensa 
- **ON LOADING**

## Future Considerations:
- **QR Code Functionality**: Ensure smooth and efficient handling of QR codes for user actions.
- **Data Sync**: Keep data synchronized between the user's app and backend (e.g., meal lists, transactions).
- **UI/UX Improvements**: Make the navigation and user flow smooth, particularly for workers and employees.


