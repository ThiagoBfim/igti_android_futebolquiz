package br.com.igti.android.futebolquiz;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.UUID;


public class FutebolQuizActivity extends Activity {

    private static final String TAG = "FutebolQuizActivity";
    private static final String PREF_PRIMEIRA_VEZ = "primeiraVez";

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        boolean primeiraVez = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getBoolean(PREF_PRIMEIRA_VEZ, true);

        if (primeiraVez) {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putBoolean(PREF_PRIMEIRA_VEZ, false)
                    .commit();
            Toast.makeText(this, "Bem-vindo ao FutebolQuiz!", Toast.LENGTH_SHORT).show();
        }
    }

    private Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(FutebolQuizFragment.ARG_ID_FUTEBOL);
        return FutebolQuizFragment.newInstance(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, (fragment == null) ? createFragment() : fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_futebol_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
