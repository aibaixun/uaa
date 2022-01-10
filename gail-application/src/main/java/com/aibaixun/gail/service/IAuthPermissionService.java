package com.aibaixun.gail.service;

public interface IAuthPermissionService {

    boolean hasPermission();

    boolean hasPermission(String url);
}
