package org.dada.info.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;

import org.dada.info.util.PreferencesSettings;


public class PinLockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock);
        showLockScreenFragment();
        //PFSecurityManager.getInstance().setPinCodeHelper(new TestPFPinCodeHelperImpl());
    }

    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener =
            new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
                @Override
                public void onCodeCreated(String encodedCode) {
                    Toast.makeText(PinLockActivity.this, "Code created", Toast.LENGTH_SHORT).show();
                    PreferencesSettings.saveToPref(PinLockActivity.this, encodedCode);
                }

                @Override
                public void onNewCodeValidationFailed() {
                    Toast.makeText(PinLockActivity.this, "Code validation error", Toast.LENGTH_SHORT).show();
                }
            };

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {

                @Override
                public void onCodeInputSuccessful() {
                    Toast.makeText(PinLockActivity.this, "Code successfull", Toast.LENGTH_SHORT).show();
                    showMainFragment();
                }

                @Override
                public void onFingerprintSuccessful() {
                    Toast.makeText(PinLockActivity.this, "Fingerprint successfull", Toast.LENGTH_SHORT).show();
                    showMainFragment();
                }

                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(PinLockActivity.this, "Pin failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFingerprintLoginFailed() {
                    Toast.makeText(PinLockActivity.this, "Fingerprint failed", Toast.LENGTH_SHORT).show();
                }
            };

    private void showLockScreenFragment() {
        new PFPinCodeViewModel().isPinCodeEncryptionKeyExist().observe(
                this,
                new Observer<PFResult<Boolean>>() {
                    @Override
                    public void onChanged(@Nullable PFResult<Boolean> result) {
                        if (result == null) {
                            return;
                        }
                        if (result.getError() != null) {
                            Toast.makeText(PinLockActivity.this, "Can not get pin code info", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showLockScreenFragment(result.getResult());
                    }
                }
        );
    }

    private void showLockScreenFragment(boolean isPinExist) {
        final PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setTitle(isPinExist ? "Unlock with your pin code or fingerprint" : "Create Code")
                .setCodeLength(6)
                .setLeftButton("Can't remeber")
                .setNewCodeValidation(true)
                .setNewCodeValidationTitle("Please input code again")
                .setUseFingerprint(true);
        final PFLockScreenFragment fragment = new PFLockScreenFragment();

        fragment.setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PinLockActivity.this, "Left button pressed", Toast.LENGTH_LONG).show();
            }
        });

        builder.setMode(isPinExist
                ? PFFLockScreenConfiguration.MODE_AUTH
                : PFFLockScreenConfiguration.MODE_CREATE);
        if (isPinExist) {
            fragment.setEncodedPinCode(PreferencesSettings.getCode(this));
            fragment.setLoginListener(mLoginListener);
        }

        fragment.setConfiguration(builder.build());
        fragment.setCodeCreateListener(mCodeCreateListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();

    }

    private void showMainFragment() {
        final MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }


}
