package com.awesprojects.lmsclient.api.impl;

public class APIFactory {

    public interface IAPIFactory {
        IUsersAPI getUsersAPI();

        IDocumentsAPI getDocumentsAPI();

        IManageAPIDocuments getManageDocumentsAPI();

        IManageAPIUsers getManageUsersAPI();

        INotificationAPI getNotificationAPI();
    }

    private static IAPIFactory instance;

    public static IAPIFactory getDefault() {
        if (instance == null)
            instance = new APIFactoryImpl();
        return instance;
    }

    private static class APIFactoryImpl implements IAPIFactory {

        @Override
        public IUsersAPI getUsersAPI() {
            return wrap(new UsersAPIImpl(), IUsersAPI.class);
        }

        @Override
        public IDocumentsAPI getDocumentsAPI() {
            return wrap(new DocumentsAPIImpl(), IDocumentsAPI.class);
        }

        @Override
        public IManageAPIDocuments getManageDocumentsAPI() {
            return wrap(new ManageAPIDocumentsImpl(), IManageAPIDocuments.class);
        }

        @Override
        public IManageAPIUsers getManageUsersAPI() {
            return wrap(new ManageAPIUsersImpl(), IManageAPIUsers.class);
        }

        @Override
        public INotificationAPI getNotificationAPI() {
            return wrap(new NotificationAPIImpl(), INotificationAPI.class);
        }

        public static <T> T wrap(T apiImplementation, Class<? super T> interfaze) {
            return ApiCallHandler.createAndWrap(apiImplementation, interfaze);
        }
    }

}
