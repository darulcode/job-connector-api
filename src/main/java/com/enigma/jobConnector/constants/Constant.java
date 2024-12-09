package com.enigma.jobConnector.constants;

public class Constant {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public static final String USER_CATEGORY_TABLE= "m_user_category";
    public static final String USER_TABLE = "m_user";
    public static final String TEST_TABLE = "t_test";
    public static final String TEST_DETAIL_TABLE = "t_test_detail";
    public static final String FILE_TEST_TABLE = "t_file_test";
    public static final String FILE_SUBMISSION_TABLE = "t_file_submission";
    public static final String CLIENT_TABLE = "m_client";
    public static final String FILE_TABLE = "t_file";


    public static final String AUTH_API = "/api/auth";

    public static final String USER_API = "/api/user";
    public static final String USER_CATEGORY_API = "/api/user/category";

    public static final String CLIENT_API = "/api/client";
    public static final String TEST_API = "/api/test";
    public static final String TEST_DETAIL_API = "/api/test/detail";
    public static final String FILE_API = "/api/file";



    public static final String OK = "OK";
    public static final String SUCCESS_LOGIN_MESSAGE="Successfully logged in";

    public static final String SUCCESS_CREATE_USER="Successfully create user";
    public static final String SUCCESS_UPDATE_USER="Successfully update user";
    public static final String SUCCESS_DELETE_USER="Successfully delete user";
    public static final String SUCCESS_FETCHING_USER="Successfully fetch user";
    public static final String SUCCESS_FETCHING_ALL_USER="Successfully fetch all user";
    public static final String SUCCESS_CREATE_USER_CATEGORY = "Successfully created user category";
    public static final String SUCCESS_UPDATE_USER_CATEGORY = "Successfully updated user category";
    public static final String SUCCESS_FETCHING_ALL_USER_CATEGORY = "Successfully fetching all user category";
    public static final String SUCCESS_FETCHING_USER_CATEGORY="Successfully fetch user category";
    public static final String SUCCESS_DELETE_USER_CATEGORY = "Successfully deleted user category";

    public static final String SUCCESS_CREATE_TEST_MESSAGE = "Successfully created test";
    public static final String SUCCESS_FETCH_ALL_CLIENT = "Successfully fetching all clients";
    public static final String SUCCESS_FETCH_CLIENT = "Successfully fetching client";
    public static final String SUCCESS_CREATED_CLIENT = "Successfully created client";
    public static final String SUCCESS_UPDATE_CLIENT = "Successfully updated client";
    public static final String SUCCESS_DELETE_CLIENT = "Successfully deleted client";
    public static final String SUCCESS_FETCHING_ALL_TEST = "Successfully fetching all test";
    public static final String SUCCESS_DELETE_TEST = "Successfully deleted test";
    public static final String SUCCESS_FETCHING_TEST_DETAILS = "Successfully fetching all test details";
    public static final String SUCCESS_CHANGE_PASSWORD = "Successfully change password";
    public static final String SUCCESS_SUBMITTED_SUBMISSION = "Successfully submitted submission";
    public static final String SUCCESS_CHANGE_STATUS_SUBMISSION = "Successfully change status submission";


    public static final String ERROR_CREATE_JWT="Error Creating JWT Token";
    public static final String TOKEN_INVALID="Token invalid";
    public static final String INVALID_REFRESH_TOKEN="Invalid refresh token";

    public static final String CATEGORY_NAME_ALREADY_EXIST="Category name already exist";
    public static final String CATEGORY_NOT_FOUND="Category not found";
    public static final String UNAUTHORIZED_MESSAGE="Unauthorized";
    public static final String USER_NOT_FOUND = "Invalid Credential";
    public static final String USERNAME_ALREADY_EXIST = "Username already exist";
    public static final String USERNAME_OR_EMAIL_ALREADY_EXIST = "Username or email already exist";
    public static final String EMAIL_ALREADY_EXIST = "Username or email already exist";
    public static final String USERNAME_NOT_FOUND = "Username not found";
    public static final String REFRESH_TOKEN_REQUIRED_MESSAGE="Refresh Token is required";
    public static final String CLIENT_ALREADY_EXIST = "Client already exist";
    public static final String CLIENT_NOT_FOUND = "Client not found";
    public static final String TEST_NOT_FOUND = "Test not found";
    public static final String PASSWORD_SAME = "Can use your latest password";
    public static final String STATUS_SUBMISSION_NOT_FOUND = "Submission Status Not Found";
    public static final String TEST_DETAIL_NOT_FOUND = "Test detail not found";
}
