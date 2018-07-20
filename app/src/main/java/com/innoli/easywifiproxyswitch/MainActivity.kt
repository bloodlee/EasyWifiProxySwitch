package com.innoli.easywifiproxyswitch

import android.net.ProxyInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.widget.EditText
import android.widget.TextView
import com.google.common.base.Objects

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val wifiManager: WifiManager
        get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val pacEditText = findViewById<EditText>(R.id.pac_edit_text)

        val unblockBtn = findViewById<Button>(R.id.unblock_btn)
        unblockBtn.setOnClickListener {
            if (!wifiManager.isWifiEnabled) {
                Toast.makeText(this@MainActivity, "Wifi is disabled.", Toast.LENGTH_SHORT).show()
            } else {
                val connManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                if (connManager.activeNetworkInfo == null || !connManager.activeNetworkInfo.isConnected) {
                    Toast.makeText(this@MainActivity, "No connected network", Toast.LENGTH_SHORT).show()
                } else {
                    val wifiConfig = wifiManager.configuredNetworks.filter { Objects.equal(wifiManager.connectionInfo.networkId, it.networkId)}.first()
                    if (wifiConfig != null) {
                        wifiConfig.httpProxy = ProxyInfo.buildPacProxy(Uri.parse(pacEditText.text as String?))
                        wifiManager.updateNetwork(wifiConfig)
                    }
                    Toast.makeText(this@MainActivity, "Going to set the proxy setting.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val restoreBtn = findViewById<Button>(R.id.restore_btn)
        restoreBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Restore the proxy setting.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
