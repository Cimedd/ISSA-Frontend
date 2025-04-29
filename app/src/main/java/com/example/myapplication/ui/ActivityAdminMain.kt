package com.example.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAdminMainBinding
import com.example.myapplication.network.Result
import com.example.myapplication.network.TransactionsItem
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.example.myapplication.ui.viewmodel.SettingVMF
import com.example.myapplication.ui.viewmodel.SettingViewModel
import com.example.myapplication.util.Utility
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ActivityAdminMain : AppCompatActivity() {
    private lateinit var binding : ActivityAdminMainBinding
    private val settingVM by viewModels<SettingViewModel>{
        SettingVMF.getInstance(this)
    }

    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }
    private var data : List<TransactionsItem> = emptyList()

    @RequiresApi(Build.VERSION_CODES.Q)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            createPdf(data)
        } else {
            Toast.makeText(this, "Permission denied. Cannot save PDF.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        homeVM.transaction.observe(this){
            when(it){
                is Result.Error -> {Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()}
                Result.Loading -> {}
                is Result.Success ->{
                    data = it.data as List<TransactionsItem>
                    checkPermissionAndSavePdf(data)
                }
            }
        }

        binding.btnReport.setOnClickListener {
            homeVM.getHistory()

        }
        binding.btnProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
        binding.btnProvider.setOnClickListener {
            val intent = Intent(this, AddProviderActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                homeVM.logout()
                settingVM.logout()
                val intent = Intent(this@ActivityAdminMain, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionAndSavePdf(data : List<TransactionsItem>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            createPdf(data)
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                createPdf(data)

            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createPdf(data : List<TransactionsItem>) {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint()
        val linePaint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        titlePaint.textSize = 18f
        paint.textSize = 12f
        linePaint.color = Color.GRAY
        linePaint.strokeWidth = 1f

        val startX = 40f
        var currentY = 50f

        // Header
        canvas.drawText("User ID", startX, currentY, titlePaint)
        canvas.drawText("Type", startX + 100, currentY, titlePaint)
        canvas.drawText("Amount", startX + 200, currentY, titlePaint)
        canvas.drawText("Transaction Time", startX + 300, currentY, titlePaint)
        canvas.drawText("Status", startX + 500, currentY, titlePaint)

        currentY += 30f

        // Draw a line after header
        canvas.drawLine(startX, currentY, pageInfo.pageWidth.toFloat() - startX, currentY, linePaint)
        currentY += 10f

        // List items
        data.forEach { item ->
            canvas.drawText(item.userId.toString(), startX, currentY, paint)
            canvas.drawText(item.type.toString(), startX + 100, currentY, paint)
            canvas.drawText(item.amount.toString(), startX + 200, currentY, paint)
            canvas.drawText( Utility.formatDateTime(item.createdAt ?: ""), startX + 300, currentY, paint)
            canvas.drawText(item.status.toString(), startX + 500, currentY, paint)

            currentY += 20f

            // Draw a line after each item
            canvas.drawLine(startX, currentY, pageInfo.pageWidth.toFloat() - startX, currentY, linePaint)
            currentY += 10f
        }

        pdfDocument.finishPage(page)

        // Save to Downloads
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        val file = File(downloadsPath, "Transactions.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF saved to Downloads!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error saving PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        pdfDocument.close()

//        Toast.makeText(this, "Starting to create file", Toast.LENGTH_SHORT).show()
//        binding.progressBar.isVisible = true
//        val pdfDocument = PdfDocument()
//        val paint = Paint()
//        val pageInfo = PdfDocument.PageInfo.Builder(600, 800, 1).create()
//        var page = pdfDocument.startPage(pageInfo)
//
//
//        var canvas: Canvas = page.canvas
//        paint.textSize = 16f
//
//        var startX = 10f
//        var startY = 30f
//        val lineHeight = 25f
//
//        // Draw table header
//        paint.isFakeBoldText = true
//        canvas.drawText("User_ID", startX, startY, paint)
//        canvas.drawText("Type", startX + 100f, startY, paint)
//        canvas.drawText("Amount", startX + 200f, startY, paint)
//        canvas.drawText("Status", startX + 300f, startY, paint)
//        canvas.drawText("Transaction Time", startX + 400f, startY, paint)
//        paint.isFakeBoldText = false
//
//        startY += 10f
//        canvas.drawLine(startX, startY, startX + 550f, startY, paint)
//        startY += lineHeight
//
//        for (item in data) {
//            canvas.drawText(item.userId.toString(), startX, startY, paint)
//            canvas.drawText(item.type ?: "-", startX + 100f, startY, paint)
//            canvas.drawText(item.amount.toString(), startX + 200f, startY, paint)
//            canvas.drawText(item.status ?: "-", startX + 300f, startY, paint)
//            canvas.drawText(Utility.formatDateTime(item.createdAt ?: "") ?: "-", startX + 400f, startY, paint)
//            startY += lineHeight
//            Log.d("Data", "Created line")
//
//            if (startY > 750f) {
//                pdfDocument.finishPage(page)
//
//                val newPageInfo = PageInfo.Builder(600, 800, pdfDocument.pages.size + 1).create()
//                page = pdfDocument.startPage(newPageInfo)  // <--- update page!
//                canvas = page.canvas                      // <--- update canvas!
//                startY = 30f
//
//                paint.isFakeBoldText = true
//                canvas.drawText("Continued...", startX, startY, paint)
//                paint.isFakeBoldText = false
//                startY += lineHeight
//            }
//        }
//        pdfDocument.finishPage(page)
//        Toast.makeText(this, "Done.", Toast.LENGTH_SHORT).show()
//        binding.progressBar.isVisible = false
//
//        val file = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "GFG.pdf"
//        )
//        val fileName = "transaction_report_${System.currentTimeMillis()}.pdf"
//
//        try {
//            val contentValues = android.content.ContentValues().apply {
//                put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
//                put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
//                put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
//            }
//
//            val resolver = contentResolver
//            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
//
//            if (uri != null) {
//                resolver.openOutputStream(uri)?.use { outputStream ->
//                    pdfDocument.writeTo(outputStream)
//                }
//                Toast.makeText(this, "PDF generated successfully.", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Failed to create file.", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(this, "Failed to generate PDF file.", Toast.LENGTH_SHORT).show()
//        } finally {
//            pdfDocument.close()
//        }


//        try {
//            // writing our PDF file to that location.
//            pdfDocument.writeTo(FileOutputStream(file))
//
//            // printing toast message on completion of PDF generation.
//            Toast.makeText(
//                this@ActivityAdminMain,
//                "PDF file generated successfully.",
//                Toast.LENGTH_SHORT
//            ).show()
//        } catch (e: IOException) {
//
//            e.printStackTrace()
//            Toast.makeText(this@ActivityAdminMain, "Failed to generate PDF file.", Toast.LENGTH_SHORT)
//                .show()
//        }
//
////
    }

    private fun generatePDF() {
        // creating an object variable for our PDF document.
        val pdfDocument = PdfDocument()

        // two variables for paint "paint" is used for drawing shapes and we will use "title" for adding text in our PDF file.
        val paint = Paint()
        val title = Paint()

        // adding page info to our PDF file in which we will be passing our pageWidth, pageHeight and number of pages and after that we are calling it to create our PDF.
        val mypageInfo = PageInfo.Builder(600, 10000, 1).create()

        // setting start page for our PDF file.
        val myPage = pdfDocument.startPage(mypageInfo)

        // creating a variable for canvas from our page of PDF.
        val canvas = myPage.canvas


        // adding typeface for our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

        // setting text size which we will be displaying in our PDF file.
        title.textSize = 15f

        // setting color of our text inside our PDF file.
        title.color = ContextCompat.getColor(this, com.example.myapplication.R.color.black)

        // drawing text in our PDF file.
        canvas.drawText("A portal for IT professionals.", 209f, 100f, title)
        canvas.drawText("Geeks for Geeks", 209f, 80f, title)

        // creating another text and in this we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.color = ContextCompat.getColor(this, com.example.myapplication.R.color.black)
        title.textSize = 15f

        // setting our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396f, 560f, title)

        // finishing our page.
        pdfDocument.finishPage(myPage)

        // setting the name of our PDF file and its path.
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "GFG.pdf"
        )

        try {
            // writing our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // printing toast message on completion of PDF generation.
            Toast.makeText(
                this@ActivityAdminMain,
                "PDF file generated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            // handling error
            e.printStackTrace()
            Toast.makeText(this@ActivityAdminMain, "Failed to generate PDF file.", Toast.LENGTH_SHORT)
                .show()
        }

        // closing our PDF file.
        pdfDocument.close()
    }

}