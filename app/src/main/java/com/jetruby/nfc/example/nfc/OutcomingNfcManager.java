package com.jetruby.nfc.example.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

public class OutcomingNfcManager implements NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    private NfcActivity activity;

    public OutcomingNfcManager(NfcActivity activity) {
        this.activity = activity;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        // creating outcoming NFC message with a helper method
        // you could as well create it manually and will surely need, if Android version is too low
        String outString = activity.getOutcomingMessage();
        byte[] outBytes = outString.getBytes();
        NdefRecord outRecord = NdefRecord.createMime(MIME_TEXT_PLAIN, outBytes);

        return new NdefMessage(outRecord);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        // onNdefPushComplete() is called on the Binder thread, so remember to explicitly notify
        // your view on the UI thread
        activity.signalResult();
    }


    /*
    * Callback to be implemented by a Sender activity
    * */
    public interface NfcActivity {
        String getOutcomingMessage();

        void signalResult();
    }
}
