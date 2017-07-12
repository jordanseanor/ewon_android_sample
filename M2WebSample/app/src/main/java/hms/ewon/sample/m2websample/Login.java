package hms.ewon.sample.m2websample;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

            Request request = new Request();
            SessionResult result = request.login(account, username, password, id);
            if(result.getSuccess()){
                writeToPreferences(id, account, username, password);
                return true;
            }else{
                return false;
            }
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
}
