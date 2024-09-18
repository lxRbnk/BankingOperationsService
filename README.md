# Banking Operations Service

This project implements a banking operations service where each client has exactly one bank account with an initial balance. Clients can transfer money between accounts, and interest is accrued on balances periodically.

## Features

### User Management
- Each user has one bank account, a phone number, and an email.
- Phone or email is mandatory.

### Initial Deposit
- Users start with an initial balance.

### Transaction Security
- User account balances cannot go negative.

### Profile Management
- Users can add/change their phone numbers or emails, provided they are not already in use by other users.
- Deleting the last phone or email is not allowed.

### Immutable Data
- Except for phones and emails, other user details cannot be modified.

### Search API
- Supports searching for users with filters based on:
  - Date of birth
  - Phone number (exact match)
  - Full name (using a like query)
  - Email (exact match)

### Authenticated Access
- API endpoints require JWT authentication, except for the public API for creating new users.

### Interest Calculation
- User account balances increase by 5% every minute but capped at 207% of the initial deposit.

### Transfer Functionality
- Authenticated users can transfer money between accounts.

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA

### Database
- PostgreSQL

### Documentation
- OpenAPI (Swagger)

### Authentication
- JWT (JSON Web Tokens)

### Logging
- Integrated logging for monitoring and debugging

### Testing
- Comprehensive unit and integration tests for transfer functionality
