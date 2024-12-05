package com.enigma.jobConnector.constants;

public class Constant {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";


    public static final String USER_TABLE = "m_user";
    public static final String TEST_TABLE = "t_test";
    public static final String TEST_DETAIL_TABLE = "t_test_detail";
    public static final String FILE_TEST_TABLE = "t_file_test";
    public static final String FILE_SUBMISSION_TABLE = "t_file_submission";
    public static final String CLIENT_TABLE = "m_client";
    public static final String FILE_TABLE = "t_file";


    public static final String AUTH_API = "/api/auth";
    public static final String CLIENT_API = "/api/client";


    public static final String OK = "OK";
    public static final String SUCCESS_LOGIN_MESSAGE="Successfully logged in";
    public static final String SUCCESS_FETCH_ALL_CLIENT = "Successfully fetching all clients";
    public static final String SUCCESS_FETCH_CLIENT = "Successfully fetching client";
    public static final String SUCCESS_CREATED_CLIENT = "Successfully created client";
    public static final String SUCCESS_UPDATE_CLIENT = "Successfully updated client";
    public static final String SUCCESS_DELETE_CLIENT = "Successfully deleted client";

    public static final String ERROR_CREATE_JWT="Error Creating JWT Token";
    public static final String TOKEN_INVALID="Token invalid";

    public static final String UNAUTHORIZED_MESSAGE="Unauthorized";
    public static final String USER_NOT_FOUND = "Invalid Credential";
    public static final String REFRESH_TOKEN_REQUIRED_MESSAGE="Refresh Token is required";
    public static final String CLIENT_ALREADY_EXIST = "Client already exist";
    public static final String CLIENT_NOT_FOUND = "Client not found";
}
