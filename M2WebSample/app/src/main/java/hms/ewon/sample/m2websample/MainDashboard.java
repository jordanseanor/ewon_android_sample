package hms.ewon.sample.m2websample;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ewon.Request;
import ewon.SessionResult;

/**
 * Created by jorda on 7/14/2017.
 */

public class MainDashboard extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_dashboard_fragment, container, false);
        return view;
    }

    private class retrieveInformation extends AsyncTask<Void, Void, Object[]>{
        private String account, username, password, developerId;
        private Request request;
        private SharedPreferences preferences;

        @Override
        protected void onPreExecute(){
            this.preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            this.account = preferences.getString(getContext().getString(R.string.account_pref), "");
            this.username = preferences.getString(getContext().getString(R.string.username_pref), "");
            this.password = preferences.getString(getContext().getString(R.string.password_pref), "");
            this.developerId = preferences.getString(getContext().getString(R.string.developer_id_pref), "");
            this.request = new Request();
        }

        @Override
        protected Object[] doInBackground(Void... voids) {
            SessionResult result = request.get
            return null;
        }
    }
}
