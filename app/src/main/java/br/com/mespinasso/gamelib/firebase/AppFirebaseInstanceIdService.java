package br.com.mespinasso.gamelib.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by MatheusEspinasso on 14/09/17.
 */

public class AppFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //TODO: Implement method
    }
}
