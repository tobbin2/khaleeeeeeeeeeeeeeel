package com.example.test

import android.Manifest.permission.*
import android.R
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import java.io.IOException
import java.util.*
import java.util.logging.Handler


class Bluetooth3 ( activity: com.example.test.StartPageActivity) {
    var _activity = activity
    val bluetoothManager: BluetoothManager? = ContextCompat.getSystemService(_activity, BluetoothManager::class.java)
    var bluetoothAdapter: BluetoothAdapter? = null
    var bluetoothLeScanner: BluetoothLeScanner? = null

    private var mmSocket: BluetoothSocket? = null

    private var scanning = false


    init {
        if(bluetoothManager != null)
            bluetoothAdapter = bluetoothManager.getAdapter()
        if(bluetoothAdapter == null || bluetoothAdapter?.isEnabled == false) {
            if (checkSelfPermission(activity, BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(_activity, arrayOf(BLUETOOTH_CONNECT), Global.BLUETOOTH_CONNET_REQUEST_CODE)
            }

        }

        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        _activity.startActivityForResult(enableBtIntent, Global.ACTION_REQUEST_ENABLE_REQUEST_CODE)
    }


    fun getPairedDevices(hasAlreadyChecked: Boolean = false): Set<BluetoothDevice>?  {
        if(bluetoothAdapter?.isEnabled == false)
            return null;

        if(ContextCompat.checkSelfPermission(_activity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) { //BLUETOOTH_CONNECT

            if (ActivityCompat.shouldShowRequestPermissionRationale(_activity, ACCESS_COARSE_LOCATION)) {
                showExplanation("Permission Needed", "Bror du måste", ACCESS_COARSE_LOCATION, Global.ACCESS_COARSE_LOCATION_REQUEST_CODE); //Global.BLUETOOTH_CONNECT_REQUEST_CODE
            } else {
                requestPermission(ACCESS_COARSE_LOCATION, Global.ACCESS_COARSE_LOCATION_REQUEST_CODE); //BLUETOOTH_CONNECT, Global.BLUETOOTH_REQUEST_CODE
            }

            if(hasAlreadyChecked)
                return null;

            return getPairedDevices(true);
        }

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        return pairedDevices
    }


    fun getDiscoverableDevices(leScanCallback: ScanCallback, hasAlreadyChecked: Boolean = false ) {

        if(ContextCompat.checkSelfPermission(_activity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) { //BLUETOOTH_CONNECT

            if (ActivityCompat.shouldShowRequestPermissionRationale(_activity, ACCESS_COARSE_LOCATION)) {
                showExplanation("Permission Needed", "Bror du måste", ACCESS_COARSE_LOCATION, Global.ACCESS_COARSE_LOCATION_REQUEST_CODE); //Global.BLUETOOTH_SCAN_REQUEST_CODE
            } else {
                requestPermission(ACCESS_COARSE_LOCATION, Global.ACCESS_COARSE_LOCATION_REQUEST_CODE); //BLUETOOTH_SCAN, Global.BLUETOOTH_SCAN_REQUEST_CODE
            }

            if(hasAlreadyChecked)
                return;

            getDiscoverableDevices(leScanCallback, true);
        }

        if (!scanning) { // Stops scanning after a pre-defined scan period.

            scanning = true

            bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }


    /*public val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        fun onReceive(context: Context?, intent: Intent) {
            val action: String = intenet.gtAction()
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice
                showToast("Found device " + device.getName())
            }
        }
    }*/

    fun connectDevice(device: BluetoothDevice, hasAlreadyChecked: Boolean = false): BluetoothSocket? {

        if(ActivityCompat.checkSelfPermission(_activity, BLUETOOTH) != PackageManager.PERMISSION_GRANTED ) {
            _activity.requestPermissions(
                arrayOf(BLUETOOTH),
                Global.BLUETOOTH_REQUEST_CODE
            )

            if(hasAlreadyChecked)
                return null;

            return connectDevice(device, true);
        }


        val mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        mmSocket = device.createRfcommSocketToServiceRecord(mUUID);
        mmSocket?.let { socket ->
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            socket.connect()

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
        }
        return mmSocket;

    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            mmSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }

    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        val builder = AlertDialog.Builder(_activity)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                R.string.ok
            ) { dialog, id -> requestPermission(permission, permissionRequestCode) }
        builder.create().show()
    }

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        requestPermissions(_activity, arrayOf(permissionName), permissionRequestCode)
    }


}