package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.NotificationAPI;

@ApiImplementation(api = NotificationAPI.class)
public interface INotificationAPI {
    NotificationAPI.NotificationReader create();
}
