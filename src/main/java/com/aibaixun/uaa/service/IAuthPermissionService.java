package com.aibaixun.uaa.service;

public interface IAuthPermissionService {

    boolean hasPermission();

    boolean hasPermission(String url);
}
