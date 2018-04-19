package com.jetruby.nfc.example

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.jetruby.nfc.example.nfc.OutcomingNfcManager

class SenderActivity : AppCompatActivity(), OutcomingNfcManager.NfcActivity {

    private lateinit var tvOutcomingMessage: TextView
    private lateinit var etOutcomingMessage: EditText
    private lateinit var btnSetOutcomingMessage: Button

    private var nfcAdapter: NfcAdapter? = null

    private val isNfcSupported: Boolean =
        this.nfcAdapter != null

    private lateinit var outcomingNfcCallback: OutcomingNfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender)

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        if (!isNfcSupported) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (!nfcAdapter?.isEnabled!!) {
            Toast.makeText(
                this,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()
        }

        initViews()

        // encapsulate sending logic in a separate class
        this.outcomingNfcCallback = OutcomingNfcManager(this)
        this.nfcAdapter?.setOnNdefPushCompleteCallback(outcomingNfcCallback, this)
        this.nfcAdapter?.setNdefPushMessageCallback(outcomingNfcCallback, this)
    }

    private fun initViews() {
        this.tvOutcomingMessage = findViewById(R.id.tv_out_message)
        this.etOutcomingMessage = findViewById(R.id.et_message)
        this.btnSetOutcomingMessage = findViewById(R.id.btn_set_out_message)
        this.btnSetOutcomingMessage.setOnClickListener { setOutGoingMessage() }
    }

    override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }

    private fun setOutGoingMessage() {
        val outMessage = this.etOutcomingMessage.text.toString()
        this.tvOutcomingMessage.text = outMessage
    }

    override fun getOutcomingMessage(): String =
        this.tvOutcomingMessage.text.toString()


    override fun signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
        runOnUiThread {
            Toast.makeText(this, R.string.message_beaming_complete, Toast.LENGTH_SHORT).show()
        }
    }
}
