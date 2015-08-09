package com.upac.upacapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class EmailBoardMemberClickListener implements OnClickListener {
    private String email;
    private Context context;

    public EmailBoardMemberClickListener(final String e, final Context c) {
        email = e;
        context = c;
    }

    @Override
    public void onClick(final View v) {
        final Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (final ActivityNotFoundException e) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}