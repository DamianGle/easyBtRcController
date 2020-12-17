package com.example.easybtrccontroller

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView.OnMoveListener
import kotlinx.android.synthetic.main.activity_steering.*
import java.io.IOException
import java.util.*

class SteeringActivity : AppCompatActivity()
{
    companion object {  // Variables to set BT connections
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steering)
        m_address = intent.getStringExtra(BtControlActivity.EXTRA_ADDRESS)
        ConnectToDevice(this).execute()
        // Steering buttons and JoyStick
        turn_left.setOnClickListener { sendCommand("L") }
        turn_right.setOnClickListener { sendCommand("P") }
        go_up.setOnClickListener { sendCommand("U") }
        go_down.setOnClickListener { sendCommand("D") }
        stop.setOnClickListener { sendCommand("S") }

        control_led_disconnect.setOnClickListener { disconnect() }
        // Send Angle and Strength from JoyStick
        joyStick.setOnMoveListener(OnMoveListener { angle, strength ->
            sendCommand("A"+ angle.toString()+ "F" + strength.toString())
        })


    }
    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect() { // Disconnect BT connection
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>()
    {
        private var connectSuccess: Boolean = true
        private val context: Context = c
                                                                        // Connection processing
        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
}