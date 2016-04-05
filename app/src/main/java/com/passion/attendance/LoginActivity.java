package com.passion.attendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserIdView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActionBar();
        // Set up the login form.
        mUserIdView = (AutoCompleteTextView) findViewById(R.id.user_id);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form_scrollview);
        mProgressView = findViewById(R.id.login_progress);

        if (BuildConfig.DEBUG) {
            startActivtyWithDummyCredentials();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserIdView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        // Show the Up button in the action bar.
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserIdView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userId = mUserIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userId address.
        if (TextUtils.isEmpty(userId)) {
            mUserIdView.setError(getString(R.string.error_field_required));
            focusView = mUserIdView;
            cancel = true;
        } else if (!isUserIdValid(userId)) {
            mUserIdView.setError(getString(R.string.error_invalid_email));
            focusView = mUserIdView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            loginUser(userId, password);
        }
    }

    private void loginUser(final String userId, final String password) {
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        if (PassionAttendance.isNetworkConnectionAvailable(this)) {
            // Send a POST request to the server to load data
            OkHttpClient httpClient = new OkHttpClient();
            String host = PassionAttendance.HOST;
            final MediaType mediaType = MediaType.parse("application/json");

            JSONObject j = new JSONObject();
            try {
                j.put(PassionAttendance.KEY_ID, userId);
                j.put(PassionAttendance.KEY_PASSWORD, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String body = j.toString();

            Request.Builder requestBuilder = new Request.Builder()
                    .post(RequestBody.create(mediaType, body))
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache");

            HttpUrl.Builder urlBuilder = new HttpUrl.Builder();

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(host)
                    .addPathSegment("login")
                    .build();

            Request request = requestBuilder.url(url)
                    .build();

            Object[] params = {httpClient, request};


            mAuthTask = new UserLoginTask(userId, password);
            mAuthTask.execute(params);
        } else {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle("No Network")
                    .setMessage("Network connectivty not available\n" +
                            "Please connect to a network and try again")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loginUser(userId, password);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(false);
                        }
                    })
                    .create();

            dialog.show();

        }
    }

    private boolean isUserIdValid(String email) {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
//        return password.length() > 4;
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserIdView.setAdapter(adapter);
    }

    private void showConnectionErrorMessage() {
        Dialog dialog = new AlertDialog.Builder(LoginActivity.this)
                .setTitle("No Network")
                .setMessage("Cannot connect to server right now")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(false);
                    }
                })
                .create();
        dialog.show();

        mUserIdView.requestFocus();
    }

    private void startActivtyWithDummyCredentials() {
        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);

        intent.putExtra(PassionAttendance.KEY_TOKEN, PassionAttendance.DUMMY_TOKEN);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(
                LoginActivity.this
        );

        sp.edit().putString(PassionAttendance.KEY_TOKEN, PassionAttendance.DUMMY_TOKEN).apply();

        startActivityForResult(intent, PassionAttendance.ACTIVTY_OVERVIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PassionAttendance.ACTIVTY_OVERVIEW:
                if (resultCode == PassionAttendance.ACTION_LOGOUT)
                    ;
                else
                    finish();
                break;
            default:
                break;
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public void loginWithToken() {
        final String token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PassionAttendance.KEY_TOKEN, "");

        showProgress(true);

        if (token.isEmpty()) {
            showProgress(false);
            return;
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPostExecute(Boolean loginStatus) {
                if (loginStatus){
                    registerGCM();
                } else {
                    showProgress(false);
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    OkHttpClient httpClient = new OkHttpClient();
                    httpClient.interceptors().add(new CurlLoggingInterceptor());

                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("https")
                            .host(PassionAttendance.HOST)
                            .addPathSegment("api")
                            .addPathSegment("login")
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "Token " + token)
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    JSONObject r = new JSONObject(response.body().string());

                    return r.getBoolean(PassionAttendance.KEY_STATUS);
                } catch (IOException | JSONException e) {
                    return false;
                }
            }
        }.execute();


    }

    public void registerGCM() {
        final String token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PassionAttendance.KEY_TOKEN, "");

        final String gcm_id = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PassionAttendance.GCM_ID, "");

        if (token.isEmpty() || gcm_id.isEmpty()) {
            finish();
            return;
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPostExecute(Boolean status) {
                Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                intent.putExtra(PassionAttendance.KEY_TOKEN, token);

                startActivityForResult(intent, PassionAttendance.ACTIVTY_OVERVIEW);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    OkHttpClient httpClient = new OkHttpClient();
                    httpClient.interceptors().add(new CurlLoggingInterceptor());


                    JSONObject body = new JSONObject();
                    body.put(PassionAttendance.KEY_GCM, gcm_id);

                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("https")
                            .host(PassionAttendance.HOST)
                            .addPathSegment("api")
                            .addPathSegment("register_gcm")
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "Token " + token)
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .post(RequestBody.create(
                                    MediaType.parse("application/json"),
                                    body.toString()
                            ))
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    JSONObject r = new JSONObject(response.body().string());

                    return r.getBoolean(PassionAttendance.KEY_STATUS);
                } catch (IOException | JSONException e) {
                    return false;
                }
            }
        }.execute();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Object, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        private String mResponse;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient httpClient = (OkHttpClient) params[0];
            Request request = (Request) params[1];

            try {
                Response response = httpClient.newCall(request).execute();
                mResponse = response.body().string();
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            View focusView = getCurrentFocus();
            if (focusView != null) focusView.clearFocus();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                try {
                    JSONObject response = new JSONObject(mResponse);

                    Boolean status = response.getBoolean("status");

                    if (status) {
                        String token = response.getString("token");

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(
                                LoginActivity.this
                        );

                        sp.edit().putString(PassionAttendance.KEY_TOKEN, token).apply();

                        registerGCM();
                    }
                } catch (JSONException e) {
                    // If connection cannot be established, login with dummy credentials
                    // in debug mode
                    if (BuildConfig.DEBUG)
                        startActivtyWithDummyCredentials();
                    else
                        showConnectionErrorMessage();
                }
            } else {
                // If connection cannot be established, login with dummy credentials
                // in debug mode
                if (BuildConfig.DEBUG)
                    startActivtyWithDummyCredentials();
                else
                    showConnectionErrorMessage();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

