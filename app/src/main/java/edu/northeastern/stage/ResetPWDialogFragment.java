package edu.northeastern.stage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPWDialogFragment extends DialogFragment {

    public static String TAG = "ResetPWDialog";
    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        context = requireContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Enter the email associated with your account to receive a password reset link.");
        builder.setTitle("Reset your password.");
        final EditText emailET = new EditText(context);
        emailET.setHint("Enter your email address.");
        builder.setView(emailET);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = emailET.getText().toString().trim();

                if(!TextUtils.isEmpty(userInput)) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(userInput)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        switchToSuccessDialogFragment();
                                    } else {
                                        Exception exception = task.getException();
                                        if (exception != null) {
                                            Log.e(TAG, "Failed: " + exception.getMessage());
                                            Toast.makeText(context, "Failed to reset password: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(context,"Please enter an email address.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // when cancel is clicked
            }
        });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null; // Clear the context reference when the fragment is detached
    }

    private void switchToSuccessDialogFragment() {
        ResetPWSuccessDialogFragment resetPWSuccessDialogFragment = new ResetPWSuccessDialogFragment();

        FragmentManager fragmentManager = getParentFragmentManager();
        resetPWSuccessDialogFragment.show(fragmentManager, ResetPWSuccessDialogFragment.TAG);
    }
}