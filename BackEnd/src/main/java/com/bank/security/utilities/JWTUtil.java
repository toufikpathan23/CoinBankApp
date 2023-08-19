package com.bank.security.utilities;

public class JWTUtil {
    public static final String SECRET = "Xv2zSb74jazC@sjhdjhd78&23%ZQX&$)(##@)_+|";
    public static final String AUTH_HEADER = "Authorization";
    public static final long EXPIRE_ACCESS_TOKEN = 80 * 80 * 1000;
    public static final long EXPIRE_REFRESH_TOKEN = 500 * 80 * 1000;
    public static final String PREFIX = "Bearer ";
}
