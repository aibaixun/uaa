package com.aibaixun.uaa.service;

public interface IAuthPermissionService {

    boolean hasPermission();

    boolean hasPermission(String url);


    boolean hasPermission(String url,String uid,String method);
}
