package com.upac.upacapp;

import android.view.View;
import android.widget.Button;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class RaffleEntryClickListener implements View.OnClickListener {
    private Button raffleButton;
    private static RaffleSQLiteHelper entry;
    private String eventId;

    public RaffleEntryClickListener(final Button b, final RaffleSQLiteHelper e, final String i) {
        raffleButton = b;
        entry = e;
        eventId = i;
    }

    @Override
    public void onClick(final View v) {
        final Date today = new Date();

        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(today.toString().getBytes());
            final byte[] digest = md5.digest();
            final BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            hashtext = hashtext.toUpperCase();

            hashtext = hashtext.substring(0, Math.min(hashtext.length(), 5));

            final App parse = new App();

            parse.addEntry(eventId, hashtext);
            entry.addEntry(eventId, hashtext);
            raffleButton.setText("Ticket number: #" + hashtext);
            raffleButton.setClickable(false);
        } catch (final NoSuchAlgorithmException e) {
            System.out.println("Error");
        }
    }
}