package com.grs.helpdeskmodule.utils;

public class Endpoints {
    public static final String[] SUPERADMIN_PATHS = new String[]{
            "/api/auth/su/**",
    };
    public static final String[] ADMIN_PATHS = new String[]{
            "/api/user/**",
            "/api/issue_reply/**",
            "/api/auth/all",
            "/api/dashboard/**"
    };
    public static final String[] OFFICER_PATHS = new String[]{
            "/api/attachments/**",
            "/api/issue/user/**",
            "/api/issue/new"
    };
    public static final String[] PERMITALL_PATHS = new String[]{
            "/api/user/**",
            "/api/user/create",
            "/api/user/login",
            "/api/settings/**"
    };
}
