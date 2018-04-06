package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.impl.*;
import com.awesprojects.lmsclient.api.internal.*;
import java.util.logging.Logger;

public class ManageAPI {

    public static final String TAG = "ManageAPI";
    private static final Logger log = Logger.getLogger(TAG);

    public static class Users {
        private static IManageAPIUsers impl;

        static {
            impl = APIFactory.getDefault().getManageUsersAPI();
        }

        @ApiCall(
                path = "/manage/users/addUser",
                method = Method.POST
        )
        @ResponseType(returns = {
                User.class,
                Response.class
        })
        public static Responsable addUser(AccessToken accessToken, User user) {
            return impl.addUser(accessToken, user);
        }

        @ApiCall(
                path = "/manage/users/modifyUser",
                method = Method.POST
        )
        @ResponseType(returns = {
                User.class,
                Response.class
        })
        public static Responsable modifyUser(AccessToken accessToken, User user) {
            return impl.modifyUser(accessToken, user);
        }

        @ApiCall(
                path = "/manage/users/removeUser",
                method = Method.POST
        )
        @ResponseType(returns = {
                User.class,
                Response.class
        })
        public static Responsable removeUser(AccessToken accessToken, User user) {
            return impl.removeUser(accessToken, user);
        }

        @ApiCall(
                path = "/manage/users/generateNewPassword",
                method = Method.POST
        )
        @ResponseType(returns = {
                User.class,
                Response.class
        })
        public static Responsable generateNewPassword(AccessToken accessToken, int userID) {
            return impl.generateNewPassword(accessToken, userID);
        }

        @ApiCall(
                path = "/manage/users/getDocumentsUserCheckedOut",
                method = Method.GET
        )
        @ResponseType(returns = {
                ResponsableContainer.class, //ResponsableContainer<Documents[]>
                Response.class
        })
        public static Responsable getDocumentsUserCheckedOut(AccessToken accessToken, int userId) {
            return impl.getDocumentsUserCheckedOut(accessToken, userId);
        }

        @ApiCall(
                path = "/manage/users/getUser",
                method = Method.GET
        )
        @ResponseType(returns = {
                User.class,
                Response.class
        })
        public static Responsable getUser(AccessToken accessToken, int id) {
            return impl.getUser(accessToken, id);
        }

        @ApiCall(
                path = "/manage/users/getUsers",
                method = Method.GET
        )
        @ResponseType(returns = {
                ResponsableContainer.class, // ResponsableContainer<User[]>
                Response.class
        })
        public static Responsable getUsers(AccessToken accessToken) {
            return impl.getUsers(accessToken);
        }

    }

    public static class Documents {
        private static IManageAPIDocuments impl;

        static {
            impl = APIFactory.getDefault().getManageDocumentsAPI();
        }

        @ApiCall(
                path = "/manage/documents/addDocument",
                method = Method.POST
        )
        @ResponseType(returns = {
                Document.class,
                Response.class
        })
        public static Responsable addDocument(AccessToken accessToken, Document document) {
            return impl.addDocument(accessToken, document);
        }

        @ApiCall(
                path = "/manage/documents/modifyDocument",
                method = Method.POST
        )
        @ResponseType(returns = {
                Document.class,
                Response.class
        })
        public static Responsable modifyDocument(AccessToken accessToken, Document document) {
            return impl.modifyDocument(accessToken, document);
        }

        @ApiCall(
                path = "/manage/documents/removeDocument",
                method = Method.POST
        )
        @ResponseType(returns = {
                Document.class,
                Response.class
        })
        public static Responsable removeDocument(AccessToken accessToken, Document document) {
            return impl.removeDocument(accessToken, document);
        }

    }

}
