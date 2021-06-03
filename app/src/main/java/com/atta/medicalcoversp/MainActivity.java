package com.atta.medicalcoversp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int userType;

    AppBarConfiguration appBarConfiguration;

    FirebaseFirestore db;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        userType = SessionManager.getInstance(this).getType();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_appointment, R.id.navigation_settings)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (userType){
            case 0:
            case 2:
            case 3:
            case 4:
                navView.getMenu().getItem(1).setVisible(false);
                break;
            case 1:
                navView.getMenu().getItem(0).setVisible(false);

                NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);

                navGraph.setStartDestination(R.id.navigation_appointment);
                navController.setGraph(navGraph);
                break;
        }
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        db = FirebaseFirestore.getInstance();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            // Get new FCM registration token
            String token = task.getResult();
            SessionManager.getInstance(MainActivity.this).saveToken(token);
            getTokens(token);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void getTokens(String token){
        String id = SessionManager.getInstance(this).getUserId();
        db.collection("Users")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user.getTokens() != null) {
                        if (!user.getTokens().contains(token)) {


                            ArrayList<String> tokens = user.getTokens();
                            if (tokens == null){
                                tokens = new ArrayList<>();
                            }
                            tokens.add(token);
                            addTokens(tokens);
                        }
                    }else {
                        ArrayList<String> tokens = new ArrayList<>();

                        tokens.add(token);
                        addTokens(tokens);
                    }
                });
    }

    public void addTokens(ArrayList<String> tokens){

        db.collection("Users")
                .document(SessionManager.getInstance(this).getUserId())
                .update("tokens", tokens);
    }

}