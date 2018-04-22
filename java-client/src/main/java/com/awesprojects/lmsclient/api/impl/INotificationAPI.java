package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.NotificationAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;

@ApiImplementation(api = NotificationAPI.class)
public interface INotificationAPI {
    NotificationAPI.NotificationReader create(AccessToken token);
}
