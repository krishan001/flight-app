package com.uni.flightfinder


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic
import com.google.android.gms.vision.barcode.Barcode
import com.google.gson.Gson
import com.uni.flightfinder.adaptors.FlightItem
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever
import java.lang.Exception


class ScanQR : AppCompatActivity(), BarcodeRetriever {

    /*
    Activity for scanning flight information QR code
    Scan valid QR code --> Flight information activity

    The class implements googles BarcodeCapture and uses
    BarcodeRetriever to set up the camera and fragment
     */
    private var barcodeCapture : BarcodeCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)

        // init the camera and scanner
        barcodeCapture = supportFragmentManager.findFragmentById(R.id.barcode) as BarcodeCapture?

        val instructions = Dialog(this)
        instructions.setContentView(R.layout.qr_instructions)

        val instructionsCancel = instructions.findViewById(R.id.barcodeInstructClose) as ImageButton

        instructionsCancel.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                instructions.dismiss()
            }})

        //display the instructions and start scanning for QR codes
        instructions.show()
        barcodeCapture!!.setRetrieval(this)

    }

    override fun onRetrieved(barcode: Barcode?) {

        /*
        Deserialise the barcode data
         - Get the data from the barcode capture
         - Use GSON to deserialise the data into a FlightItem
            -- If this fails then the barcode is not a Flight Finder barcode
         - Start the flight info activity with the new FlightItem object
         */

        val gson = Gson()

        try {
            val flightItem: FlightItem = gson.fromJson<FlightItem>(barcode?.displayValue, FlightItem::class.java)
            val img : TextView = findViewById(R.id.textView12)

            val context=img.context
            val intent = Intent(context, FlightInfo::class.java)
            intent.putExtra("FlightListAdaptor", flightItem)
            context.startActivity(intent)
        }
        catch (e: Exception) {
            runOnUiThread(Runnable { Toast.makeText(applicationContext, "Unrecognised QR code. Please Use a Flight Finder generated QR code.", Toast.LENGTH_LONG).show() })
        }
    }

    override fun onRetrievedMultiple(closetToClick: Barcode?, barcode: MutableList<BarcodeGraphic>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRetrievedFailed(reason: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionRequestDenied() {
        runOnUiThread(
            object : Runnable {
                override fun run() {
                    runOnUiThread(Runnable { Toast.makeText(applicationContext, "QR codes cannot be scanned without camera permission.", Toast.LENGTH_LONG).show() })
                }
            }
        )
    }
}
