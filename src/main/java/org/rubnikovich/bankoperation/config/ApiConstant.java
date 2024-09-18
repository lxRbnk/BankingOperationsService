package org.rubnikovich.bankoperation.config;

public interface ApiConstant {
    String NOT_FOUND = "Not found ";
    String DELETED = "Deleted ";
    String ALREADY_EXISTS = "Already exists";
    String REGISTRATION_FAILED = "Registration failed ";
    String REGISTRATION_COMPLETED = "Registration is completed ";
    String LOGIN_FAILED = "Login is failed ";
    String LOGIN_COMPLETED = "Login is completed ";
    String INCORRECT = "Incorrect login or password ";
    String MESSAGE = "Message ";

    String JWT_TOKEN = "Jwt token";
    String INVALID_JWT_TOKEN = "Invalid JWT Token";
    String BEARER = "Bearer ";
    String AUTHORIZATION = "Authorization";

    String TRANSACTION_SUCCESSFULLY = "Transaction made successfully ";
    String TRANSACTION_FAILED = "Failed to make transaction ";
    String TRANSACTION_FAILED_BALANCE = "Failed to make transaction, insufficient funds for the transaction";

    String USER_NOT_FOUND = "User not found";
    String USER_DELETION_FAILED ="User deletion failed ";
    String FETCHING_ALL_USERS = "Fetching all users ";

    String EMAILS = "Emails ";
    String EMAIL_UPDATED = "Email updated ";
    String FAILED_UPDATED_EMAIL = "Failed to update email ";
    String EMAIL_ADDED = "Email added ";
    String FAILED_ADD_EMAIL = "Failed to add email ";
    String FAILED_DELETE_EMAIL = "Failed to delete email ";

    String PHONES = "Phones ";
    String PHONE_UPDATED = "Phone updated ";
    String FAILED_UPDATED_PHONE = "Failed to update phone ";
    String PHONE_ADDED = "Phone added ";
    String FAILED_ADD_PHONE = "Failed to add phone ";
    String FAILED_DELETE_PHONE = "Failed to delete phone ";

}
