package hms.ewon.sample.m2websample;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ewon.Account;
import ewon.Ewon;
import ewon.Pool;
import ewon.Request;
import ewon.SessionResult;

/**
 * Created by jorda on 7/14/2017.
 */

public class MainDashboard extends Fragment {
    @BindView(R.id.txtAccountName) TextView txtAccount;
    @BindView(R.id.txtCompanyName) TextView txtCompany;
    @BindView(R.id.txtAccountType) TextView txtAccountType;
    @BindView(R.id.ewon_recycler) RecyclerView recyclerView;
    @BindView(R.id.chkOnlineOnly) CheckBox chkOnline;
    private EwonAdapter adapter;
    private Spinner poolsSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_dashboard_fragment, container, false);
        ButterKnife.bind(this, view);
        new retrieveInformation().execute();
        chkOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    Toast.makeText(getContext(), "CHECKED", Toast.LENGTH_LONG).show();
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        return view;
    }

    private class retrieveInformation extends AsyncTask<Void, Void, List<Object>>{
        private String account, username, password, developerId;
        private Request request;
        private SharedPreferences preferences;
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            this.preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            this.account = preferences.getString(getContext().getString(R.string.account_pref), "");
            this.username = preferences.getString(getContext().getString(R.string.username_pref), "");
            this.password = preferences.getString(getContext().getString(R.string.password_pref), "");
            this.developerId = preferences.getString(getContext().getString(R.string.developer_id_pref), "");
            this.progressDialog = new ProgressDialog(getContext());
            this.progressDialog.setMessage("Loading...");
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            this.request = new Request();
        }

        @Override
        protected List<Object> doInBackground(Void... voids) {
            List<Object> information = new ArrayList<>();
            Account account = request.getAccountInformation(preferences.getString("sessionid", ""),preferences.getString(getContext().getString(R.string.developer_id_pref), ""));
            List<Ewon> ewons = request.getAccountDevices(preferences.getString("sessionid", ""),preferences.getString(getContext().getString(R.string.developer_id_pref), ""));
            information.add(account);
            information.add(ewons);
            return information;
        }

        @Override
        protected void onPostExecute(List<Object> objects){
            Account currentAccount = null;
            if(objects.get(0) instanceof Account){
                currentAccount = (Account) objects.get(0);
                txtAccount.setText("Account Name: " + currentAccount.getAccountName());
                txtCompany.setText("Company Name: " + currentAccount.getCompany());
                txtAccountType.setText("Account Type: " +currentAccount.getAccountType());
            }

            List<Ewon> ewons = null;
            if(objects.get(1) instanceof List){
                ewons = (List<Ewon>) objects.get(1);
            }

            adapter = new EwonAdapter(ewons, getContext());
            RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            if(this.progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }
}
