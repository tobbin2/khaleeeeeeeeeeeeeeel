package com.example.test

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.test.databinding.ActivityStartPageBinding

class StartPageActivity : AppCompatActivity() {
    var leDeviceListAdapter: ArrayAdapter<BluetoothDevice>? = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityStartPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start_page)

        val listView: ListView = findViewById<View>(R.id.bluetooth_device_lw) as ListView
        listView.setAdapter(leDeviceListAdapter)
        leDeviceListAdapter = ArrayAdapter<BluetoothDevice>(this, androidx.appcompat.R.layout.abc_activity_chooser_view_list_item);
        var bluetooth3 = Bluetooth3(this);
        //var list = bluetooth3.getPairedDevices();
        bluetooth3.getDiscoverableDevices(leScanCallback);

    }

    public val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.i("ald", "got device " + result.device)
            leDeviceListAdapter?.add(result.device)
            leDeviceListAdapter?.notifyDataSetChanged()
        }
    }

    public fun devicePicked() {

    }


    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("ald", "logged, " + requestCode + " " + resultCode + " " + data);

    }

}