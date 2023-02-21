package com.example.barbershop.repositories;

import android.content.Context;

import com.example.barbershop.SharedPreferencesManager;
import com.example.barbershop.interfaces.ILoginCallback;
import com.example.barbershop.model.Constants;
import com.example.barbershop.model.User;
import com.example.barbershop.model.UserType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserRepo {

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private User mUser;
    private Context mContext;

    public UserRepo(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
    }

    /**
     * //TODO - write this functionality
     * @return
     */
    public boolean isUserLoggedIn() {
        User user = SharedPreferencesManager.getUser(mContext);

        if (user == null) {
            if (mFirebaseUser != null) {
                mAuth.signOut();
            }
            return false;
        }

        return true;
    }

    public User getUser() {
        return SharedPreferencesManager.getUser(mContext);
    }

    // TODO - Also delete the user from shared pref.
    public void signOut() {
        SharedPreferencesManager.clearUserData(mContext);
        mAuth.signOut();
    }

    /**
     * Checks if the email+password that was typed are matched with the admin credentials.
     * @param email - email of the login attempt
     * @param password - password of the login attempt
     * @return @true if it's an admin, @false if it's not an admin.
     */
    private boolean isAdminUser(String email, String password) {
        boolean isAdminEmail = Constants.ADMIN_USERNAME.equals(email);
        boolean isAdminPassword = Constants.ADMIN_PASSWORD.equals(password);

        return (isAdminEmail && isAdminPassword);
    }

    public void performSignIn(String email, String password, ILoginCallback listener) {

        if (isAdminUser(email, password)) {
            SharedPreferencesManager.saveLoggedUser(mContext, new User("admin", "admin", UserType.ADMIN));
            listener.onSuccess();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mFirebaseUser = mAuth.getCurrentUser();
                        SharedPreferencesManager.saveLoggedUser(mContext, new User(mFirebaseUser.getUid(), mFirebaseUser.getDisplayName(), UserType.CLIENT));
                        listener.onSuccess();
                    } else {
                        listener.onError();
                    }
                });
    }

    /**
     * Creates a new user on firebase for this new user.
     * After user is created, update his full name on firebase as well.
     * @param email
     * @param password
     * @param fullName
     * @param listener
     */
    public void performRegister(String email, String password, String fullName,
                                ILoginCallback listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    updateUserFullName(fullName, listener);
                } else {
                    listener.onError();
                }
        });
    }

    private void updateUserFullName(String fullName, ILoginCallback listener) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();

        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SharedPreferencesManager.saveLoggedUser(mContext, new User(mFirebaseUser.getUid(), fullName, UserType.CLIENT));
                listener.onSuccess();
            }
        });
    }
}
