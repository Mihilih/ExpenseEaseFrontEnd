## ExpenseEase
An expense tracking app that allows user to add, edit, and delete their income and expenses and track which categories of expenses occur the most.

## Description
This app was developed as a personal project after learning Android development during the fall semester and backend development during the winter break of 2023. As I was getting into personal finance I noticed that most expense-tracking apps are too complicated and have too many features. My intention was to develop an app that is simple and has the basic features but is also user-friendly.    

## Features

- User Registration and login can be completed seamlessly and user authentication is integrated with the backend using session tokens. The session tokens are stored in sharedPreferences.

![Screenshot_20240117_091055_Expense Ease](https://github.com/Mihilih/ExpenseEaseFrontEnd/assets/72967681/33e7fa94-7179-46ad-a98d-2fee3ac15ae8) 

- A spending summary page with a pie chart (integrated using the [Jitpack library](https://jitpack.io/)) to show an overview of the users' spending.
  
![Screenshot_20240117_084549_Expense Ease](https://github.com/Mihilih/ExpenseEaseFrontEnd/assets/72967681/52a0124f-0a94-4944-a292-7b311ff5f819)

- Expense and Income fragments that have nested recycler views. Each recycler view has the date and the expenses/incomes made on the specific date.

![Screenshot_20240117_084622_Expense Ease](https://github.com/Mihilih/ExpenseEaseFrontEnd/assets/72967681/c2ec4d2e-989e-44de-9f7e-530a64bb633a)

- Clicking on an individual income or an expense will bring the user to a page with expanded details of the income/expense and buttons to edit or delete the income/expense

![Screenshot_20240117_090839_Expense Ease](https://github.com/Mihilih/ExpenseEaseFrontEnd/assets/72967681/75f40d5b-940d-4194-a571-911a49ed62d6)

- Adding new expenses is done using a dialog fragment that pops up with the input fields.
  
![Screenshot_20240117_084602_Expense Ease](https://github.com/Mihilih/ExpenseEaseFrontEnd/assets/72967681/601d9506-93c7-4a43-b5a4-d6d876d4d314)

- A profile fragment that shows the user's details, current balance (which is updated according to the income and expenses added), and buttons to logout, edit, and delete the profile

![Screenshot_20240117_084556_Expense Ease](https://github.com/Mihilih/ExpenseEaseFrontEnd/assets/72967681/cc4257a6-ef9c-4dc6-a46a-54b42fac1d06)

- Users can also send email reports using a link in the overview page. The email report looks as follows:

![Screenshot_20240117_084706_Gmail](https://github.com/Mihilih/ExpenseEaseFrontEnd/assets/72967681/086f9250-7504-44a6-83ce-480b9738cc4b)
