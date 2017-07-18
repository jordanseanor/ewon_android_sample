package hms.ewon.sample.m2websample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import ewon.Request;
import ewon.SessionResult;

public class Login extends AppCompatActivity {
    @BindView(R.id.txtDeveloperId) TextView txtDeveloperId;
    @BindView(R.id.txtAccount) TextView txtAccount;
    @BindView(R.id.txtUsername) TextView txtUsername;
    @BindView(R.id.txtPassword) TextView txtPassword;
    @BindView(R.id.btnLogin) Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public boolean loginToAccount(View v){
        boolean validEntries = true;
        if(!validateEntries(txtDeveloperId)){
            validEntries = false;
        }

        if(!validateEntries(txtAccount)){
            validEntries = false;
        }

        if(!validateEntries(txtUsername)){
            validEntries = false;
        }

        if(!validateEntries(txtPassword)){
            validEntries = false;
        }

        if(validEntries) {
            String id = txtDeveloperId.getText().toString();
            String account = txtAccount.getText().toString();
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            new login(account, username, password, id).execute();
        }
        return false;
    }

    private boolean validateEntries(TextView textView){
        if(textView.getId() == R.id.txtDeveloperId){
            if(textView.getText().length() == 36){
                return true;
            }else{
                textView.setError("Error. Developer ID must be 36 characters in length");
                return false;
            }
        }else{
            if(textView.getText().toString().length() != 0 && !textView.getText().toString().equals("")){
                return true;
            }else{
                textView.setError("Error. Entry cannot be blank.");
                return false;
            }
        }
    }

    private void writeToPreferences(String developerId, String account, String username, String password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences
                .edit()
                .putString(getString(R.string.developer_id_pref), developerId)
                .putString(getString(R.string.account_pref), account)
                .putString(getString(R.string.username_pref), username)
                .putString(getString(R.string.password_pref), password)
                .apply();
    }

    private class login extends AsyncTask<Void, Void, SessionResult>{
        private String account, username, password, developerId;
        private ProgressDialog loadingDialog;

        public login(String account, String username, String password, String developerId){
            this.account = account;
            this.username = username;
            this.password = password;
            this.developerId = developerId;
            loadingDialog = new ProgressDialog(Login.this);
        }

        @Override
        protected void onPreExecute(){
            loadingDialog.setMessage("Logging in...");
            loadingDialog.setCancelable(false);
            loadingDialog.setIndeterminate(true);
            loadingDialog.show();
        }

        @Override
        protected SessionResult doInBackground(Void... voids) {
            Request request = new Request();
            return request.login(account, username, password, developerId);
        }

        @Override
        protected void onPostExecute(SessionResult result){
            if(loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
            
            if(result.getSuccess()){
                writeToPreferences(developerId, account, username, password);
                startActivity(new Intent(Login.this, Dashboard.class));
            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                alertDialog.setTitle("Failure");
                alertDialog.setMessage("Unable to log into account. Please confirm your entries.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }
}
